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
 * GSON {@link com.google.gson.TypeAdapterFactory} for "typedDefinitions" and "flatTypedDefinitions".
 * @param <T>
 */
public class TypedDefinitionAdapterFactory<T> implements TypeAdapterFactory {

  public interface Resolver<T> {
    public Class<? extends T> resolve(String typeName);
  }

  private Class<T> _unionClass;
  private Resolver<T> _resolver;

  public TypedDefinitionAdapterFactory(Class<T> unionClass, Resolver<T> resolver) {
    _unionClass = unionClass;
    _resolver = resolver;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (type.getType().equals(_unionClass)) {
      return new TypedDefinitionAdapter<T>(gson, (Resolver<T>) _resolver);
    } else {
      throw new IllegalArgumentException(
          "ExampleUnion.JsonAdapter may only be used with " + _unionClass.getName() + ", but was used " +
              "with: " + type.getRawType().getClass().getName());
    }
  }

  private static class TypedDefinitionAdapter<T> extends TypeAdapter<T> {
    private Gson _gson;
    private Resolver<T> _resolver;
    public TypedDefinitionAdapter(Gson gson, Resolver<T> resolver) {
      _gson = gson;
      _resolver = resolver;
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
      // Since union types are abstract, they can never be directly written, and the
      // member types are already deserializable.
      throw new UnsupportedOperationException();
    }

    @Override
    public T read(JsonReader reader) throws IOException {
      // Ideally we would stream the data from the reader here, but we need to inspect the
      // 'typeName' field, which may appear after the 'definition' field.
      JsonElement element = new JsonParser().parse(reader);
      JsonObject obj = element.getAsJsonObject();
      String typeName = obj.get("typeName").getAsString();
      Class<? extends T> clazz = _resolver.resolve(typeName);
      return _gson.getAdapter(clazz).fromJsonTree(element);
    }
  }
}
