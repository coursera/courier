package org.coursera.courier.android.runtime;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * GSON {@link com.google.gson.TypeAdapterFactory} for Pegasus style unions.
 *
 * For example, consider a union "AnswerFormat" with the union member options of "TextEntry" and
 * "MultipleChoice".
 *
 * Example JSON:
 *
 * <code>
 *   {
 *     "org.example.TextEntry": { "textEntryField1": ... }
 *   }
 * </code>
 *
 * Example Usage with a GSON Java class:
 *
 * <code>
 * {@literal}JsonAdapter(AnswerFormat.Adapter.class)
 * interface AnswerFormat {
 *   public final class TextEntryMember implements AnswerFormat {
 *     private static final String MEMBER_KEY = "org.example.TextEntry";
 *
 *      {@literal}SerializedName(MEMBER_KEY)
 *      public TextEntry member;
 *   }
 *
 *   public final class MultipleChoiceMember implements AnswerFormat {
 *     // ...
 *   }
 *
 *   final class Adapter extends UnionAdapterFactory&lt;AnswerFormat&gt; {
 *     Adapter() {
 *       super(AnswerFormat.class, new Resolver&lt;AnswerFormat&gt;() {
 *         public Class&lt;? extends AnswerFormat&gt; resolve(String memberKey) {
 *           switch(memberKey) {
 *             case AnswerFormat.MEMBER_KEY: return TextEntryMember.class;
 *             case MultipleChoice.MEMBER_KEY: return MultipleChoice.class;
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
public class UnionAdapterFactory<T> implements TypeAdapterFactory {

  /**
   * Provides a mapping of typedDefinition "typeName"s (which are the union tags) to their
   * corresponding Java classes.
   *
   * @param <T> provides the marker interface that identifies the union.
   */
  public interface Resolver<T> {
    public Class<? extends T> resolve(String memberKey);
  }

  private Class<T> unionClass;
  private Resolver<T> resolver;

  public UnionAdapterFactory(Class<T> unionClass, Resolver<T> resolver) {
    this.unionClass = unionClass;
    this.resolver = resolver;
  }

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (unionClass.equals(type.getType())) {
      return new UnionAdapter<T>(gson);
    } else {
      return null;
    }
  }

  private class UnionAdapter<T> extends TypeAdapter<T> {
    private Gson gson;

    public UnionAdapter(Gson gson) {
      this.gson = gson;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(JsonWriter out, T value) throws IOException {
      if (!unionClass.equals(value.getClass())) {
        TypeToken<T> actualType = TypeToken.get((Class<T>)value.getClass());
        gson.getDelegateAdapter(UnionAdapterFactory.this, actualType).write(out, value);
      }
      // else GSON is actually trying to serialize an abstract class, which would be a GSON bug
    }

    @Override
    @SuppressWarnings("unchecked")
    public T read(JsonReader reader) throws IOException {
      // Ideally we would stream the data from the reader here, but we need to inspect the
      // 'typeName' field, which may appear after the 'definition' field.

      JsonElement element = new JsonParser().parse(reader);
      Set<Map.Entry<String, JsonElement>> entries = element.getAsJsonObject().entrySet();
      if (entries.size() != 1) {
        StringBuilder keys = new StringBuilder();
        Iterator<Map.Entry<String, JsonElement>> iter = entries.iterator();
        while (iter.hasNext()) {
          keys.append("'").append(iter.next().getKey()).append("'");
          if (iter.hasNext()) keys.append(", ");
        }
        throw new IOException(
            "JSON object for '" + unionClass.getName() + "' union must contain exactly one " +
            "'memberKey' field but contains " +
                (keys.length() > 0 ? keys.toString() : "no fields") +  " at path " + reader.getPath());
      }
      Map.Entry<String, JsonElement> member = entries.iterator().next();
      Class<? extends T> clazz = (Class<? extends T>) resolver.resolve(member.getKey());
      return gson.getAdapter(clazz).fromJsonTree(element);
    }
  }
}
