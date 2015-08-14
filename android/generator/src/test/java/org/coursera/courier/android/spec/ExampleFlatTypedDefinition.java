package org.coursera.courier.android.spec;

import com.google.gson.annotations.JsonAdapter;
import org.coursera.courier.android.runtime.TypedDefinitionAdapterFactory;

@JsonAdapter(ExampleFlatTypedDefinition.Adapter.class)
public interface ExampleFlatTypedDefinition {

  public final class NoteMember extends Note implements ExampleFlatTypedDefinition {
    private static final String TYPE_NAME = "note";
    public final String typeName = TYPE_NAME;
  }

  public final class MessageMember extends Message implements ExampleFlatTypedDefinition {
    private static final String TYPE_NAME = "message";
    public final String typeName = TYPE_NAME;
  }

  public final class $UnknownMember implements ExampleFlatTypedDefinition {
    private $UnknownMember() {}
  }

  final class Adapter extends TypedDefinitionAdapterFactory<ExampleFlatTypedDefinition> {
    Adapter() {
      super(ExampleFlatTypedDefinition.class, new Resolver<ExampleFlatTypedDefinition>() {
        @Override
        public Class<? extends ExampleFlatTypedDefinition> resolve(String typeName) {
          if (typeName.equals(NoteMember.TYPE_NAME)) return NoteMember.class;
          else if (typeName.equals(MessageMember.TYPE_NAME)) return MessageMember.class;
          else return $UnknownMember.class;
        }
      });
    }
  }
}
