/*
 * Copyright 2015 Coursera Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.coursera.courier.android.test;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import org.coursera.courier.android.runtime.TypedDefinitionAdapterFactory;
import org.junit.Test;

//import org.coursera.records.Simple;
//import org.coursera.records.WithPrimitives;

public class TestGson {

  @Test
  public void testRecord() {
    /*WithPrimitives withPrimitives = new WithPrimitives();
    WithPrimitives.SimpleMember member = new WithPrimitives.SimpleMember();
    Simple simple = new Simple();
    simple.message = "Hello World!";
    member.member = simple;
    withPrimitives.union = member;
    Gson gson = new Gson();
    System.err.println(gson.toJson(withPrimitives));*/
  }

  public static class ExampleRecord {
    public ExampleUnion exampleUnion;
  }

  public static class Record2 {
    public String field1;
  }

  @JsonAdapter(ExampleUnion.Adapter.class)
  public static abstract class ExampleUnion {

    public static final class ExampleMember1 extends ExampleUnion {
      static final String TYPE_NAME = "exampleMember1";
      private String typeName = TYPE_NAME;

      public Record2 definition;
    }

    public static final class ExampleMember2 extends ExampleUnion {
      static final String TYPE_NAME = "exampleMember2";
      private String typeName = TYPE_NAME;

      public Record2 definition;
    }

    public static final class $UnknownMember extends ExampleUnion {
      $UnknownMember() {} // package protected
    }

    public static final class Adapter extends TypedDefinitionAdapterFactory<ExampleUnion> {
      Adapter() {
        super(ExampleUnion.class, new Resolver<ExampleUnion>() {
          @Override
          public Class<? extends ExampleUnion> resolve(String typeName) {
            if (typeName.equals(ExampleMember1.TYPE_NAME)) return ExampleMember1.class;
            if (typeName.equals(ExampleMember2.TYPE_NAME)) return ExampleMember2.class;
            return $UnknownMember.class;
          }
        });
      }
    }
  }

  @Test
  public void testUnions() {
    Gson gson = new Gson();
    String json =
        "{" +
        "  \"exampleUnion\": { " +
        "  \"typeName\": \"exampleMember2\", " +
        "  \"definition\": { " +
        "    \"field1\": \"Hello, GSON.\"" +
            "}" +
        "  }" +
        "}";
    ExampleRecord exampleRecord = gson.fromJson(json, ExampleRecord.class);
    ExampleUnion exampleUnion = exampleRecord.exampleUnion;
    System.err.println(exampleUnion.getClass());
    ExampleUnion.ExampleMember2 member = (ExampleUnion.ExampleMember2)exampleUnion;
    System.err.println("definition: " + member.definition.field1);

    ExampleUnion.ExampleMember1 exampleMember1 = new ExampleUnion.ExampleMember1();
    Record2 record2 = new Record2();
    record2.field1 = "Write test";
    exampleMember1.definition = record2;

    String serialized = gson.toJson(exampleMember1);
    System.err.println(serialized);
    ExampleUnion roundTripped = gson.fromJson(serialized, ExampleUnion.class);
    ExampleUnion.ExampleMember1 roundTrippedMember = (ExampleUnion.ExampleMember1)roundTripped;
    System.err.println("roundTripped: " + roundTrippedMember.definition.field1);
  }
}
