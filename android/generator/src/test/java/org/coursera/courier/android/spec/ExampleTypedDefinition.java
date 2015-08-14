package org.coursera.courier.android.spec;

import com.google.gson.annotations.JsonAdapter;
import org.coursera.courier.android.runtime.TypedDefinitionAdapterFactory;

@JsonAdapter(ExampleTypedDefinition.Adapter.class)
public interface ExampleTypedDefinition {

  public final class NoteMember implements ExampleTypedDefinition {
    private static final String TYPE_NAME = "note";
    public final String typeName = TYPE_NAME;
    public Note definition;
  }

  public final class MessageMember implements ExampleTypedDefinition {
    private static final String TYPE_NAME = "message";
    public final String typeName = TYPE_NAME;
    public Message definition;
  }

  public final class $UnknownMember implements ExampleTypedDefinition {
    private $UnknownMember() {}
  }

  final class Adapter extends TypedDefinitionAdapterFactory<ExampleTypedDefinition> {
    Adapter() {
      super(ExampleTypedDefinition.class, new Resolver<ExampleTypedDefinition>() {
        @Override
        public Class<? extends ExampleTypedDefinition> resolve(String typeName) {
          if (typeName.equals(NoteMember.TYPE_NAME)) return NoteMember.class;
          else if (typeName.equals(MessageMember.TYPE_NAME)) return MessageMember.class;
          else return $UnknownMember.class;
        }
      });
    }
  }
}
