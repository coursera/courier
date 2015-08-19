package org.coursera.courier.android.runtime;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * GSON {@link com.google.gson.TypeAdapterFactory} for "typedDefinition" and
 * "flatTypedDefinition".
 *
 * Both are tagged union formats.
 *
 * For example, consider a union "AnswerFormat" with the union member options of "TextEntry" and
 * "MultipleChoice".
 *
 * <h1>"typedDefinitions"</h1>
 *
 * Example JSON:
 *
 * <code>
 *   {
 *     "typeName": "textEntry",
 *     "definition": { "textEntryField1": ... }
 *   }
 * </code>
 *
 * Example Usage with a GSON Java class:
 *
 * <code>
 * {@literal}JsonAdapter(AnswerFormat.Adapter.class)
 * interface AnswerFormat {
 *   public static final class TextEntryMember implements AnswerFormat {
 *     private static final String TYPE_NAME = "textEntry";
 *      public final String typeName = TYPE_NAME;
 *      public TextEntry definition;
 *   }
 *
 *   public final class MultipleChoiceMember implements AnswerFormat {
 *     // ...
 *   }
 *
 *   final class Adapter extends TypedDefinitionAdapterFactory&lt;AnswerFormat&gt; {
 *     Adapter() {
 *       super(AnswerFormat.class, new Resolver&lt;AnswerFormat&gt;() {
 *         public Class&lt;? extends AnswerFormat&gt; resolve(String typeName) {
 *           switch(typeName) {
 *             case AnswerFormat.TYPE_NAME: return TextEntryMember.class;
 *             case MultipleChoice.TYPE_NAME: return MultipleChoice.class;
 *             // ...
 *           }
 *         }
 *       });
 *     }
 *   }
 * }
 * </code>
 *
 * <h1>"flatTypedDefinition"</h1>
 *
 * Example JSON:
 *
 * <code>
 *   {
 *     "typeName": "textEntry",
 *     "textEntryField1": ...
 *   }
 * </code>
 *
 * Example Usage with a GSON Java class:
 *
 * <code>
 * {@literal}JsonAdapter(AnswerFormat.Adapter.class)
 * interface AnswerFormat {
 *   public static final class TextEntryMember extends TextEntry implements AnswerFormat {
 *     private static final String TYPE_NAME = "textEntry";
 *      public final String typeName = TYPE_NAME;
 *   }
 *
 *   public final class MultipleChoiceMember implements AnswerFormat {
 *     // ...
 *   }
 *
 *   final class Adapter extends TypedDefinitionAdapterFactory&lt;AnswerFormat&gt; {
 *     Adapter() {
 *       super(AnswerFormat.class, new Resolver&lt;AnswerFormat&gt;() {
 *         public Class&lt;? extends AnswerFormat&gt; resolve(String typeName) {
 *           switch(typeName) {
 *             case AnswerFormat.TYPE_NAME: return TextEntryMember.class;
 *             case MultipleChoice.TYPE_NAME: return MultipleChoice.class;
 *             // ...
 *           }
 *         }
 *       });
 *     }
 *   }
 * }
 * </code>
 *
 * @param <T> provides the marker interface that identifies the union. All union members wrappers
 *           must implement this interface.
 */
public class TypedDefinitionAdapterFactory<T> implements TypeAdapterFactory {

  /**
   * Provides a mapping of typedDefinition "typeName"s (which are the union tags) to their
   * corresponding Java classes.
   *
   * @param <T> provides the marker interface that identifies the union.
   */
  public interface Resolver<T> {
    public Class<? extends T> resolve(String typeName);
  }

  private Class<T> unionClass;
  private Resolver<T> resolver;

  public TypedDefinitionAdapterFactory(Class<T> unionClass, Resolver<T> resolver) {
    this.unionClass = unionClass;
    this.resolver = resolver;
  }

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (type.getType().equals(unionClass)) {
      return new TypedDefinitionAdapter<T>(gson);
    } else {
      return null;
    }
  }

  private  class TypedDefinitionAdapter<T> extends TypeAdapter<T> {
    private Gson gson;
    public TypedDefinitionAdapter(Gson gson) {
      this.gson = gson;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(JsonWriter out, T value) throws IOException {
      if (!unionClass.equals(value.getClass())) {
        TypeToken<T> actualType = TypeToken.get((Class<T>)value.getClass());
        gson.getDelegateAdapter(TypedDefinitionAdapterFactory.this, actualType).write(out, value);
      }
      // else GSON is actually trying to serialize an abstract class, which would be a GSON bug
    }

    @Override
    @SuppressWarnings("unchecked")
    public T read(JsonReader reader) throws IOException {
      // Ideally we would stream the data from the reader here, but we need to inspect the
      // 'typeName' field, which may appear after the 'definition' field.
      JsonElement element = new JsonParser().parse(reader);
      JsonObject obj = element.getAsJsonObject();
      JsonElement typeNameElement = obj.get("typeName");
      if (typeNameElement == null) {
        throw new IOException(
            "'typeName' expected but not found when reading '" + unionClass.getName() + "'" +
            " at path " + reader.getPath());
      }
      String typeName = typeNameElement.getAsString();
      Class<? extends T> clazz = (Class<? extends T>) resolver.resolve(typeName);
      return gson.getAdapter(clazz).fromJsonTree(element);
    }
  }
}
