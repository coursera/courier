package org.coursera.courier.android.spec;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import org.coursera.courier.android.runtime.UnionAdapterFactory;

@JsonAdapter(ExampleUnion.Adapter.class)
public interface ExampleUnion {

  public final class NoteMember implements ExampleUnion {
    private static final String MEMBER_KEY = "org.coursera.courier.android.test.GsonTestFixtures.Note";

    @SerializedName(MEMBER_KEY)
    public Note member;
  }

  public final class MessageMember implements ExampleUnion {
    private static final String MEMBER_KEY = "org.coursera.courier.android.test.GsonTestFixtures.Message";

    @SerializedName(MEMBER_KEY)
    public Message member;
  }

  public final class $UnknownMember implements ExampleUnion {
    private $UnknownMember() { }
  }

  final class Adapter extends UnionAdapterFactory<ExampleUnion> {
    Adapter() {
      super(ExampleUnion.class, new Resolver<ExampleUnion>() {
        @Override
        public Class<? extends ExampleUnion> resolve(String memberKey) {
          switch (memberKey) {
            case NoteMember.MEMBER_KEY: return NoteMember.class;
            case MessageMember.MEMBER_KEY: return MessageMember.class;
            default: return $UnknownMember.class;
          }
        }
      });
    }
  }
}
