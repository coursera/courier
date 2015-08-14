/*
 * Copyright 2015 Coursera Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.coursera.courier.android.test;

public class TestGsonSpec {
  /*WithPrimitives withPrimitives = new WithPrimitives();
    WithPrimitives.SimpleMember member = new WithPrimitives.SimpleMember();
    Simple simple = new Simple();
    simple.message = "Hello World!";
    member.member = simple;
    withPrimitives.union = member;
    Gson gson = new Gson();
    System.err.println(gson.toJson(withPrimitives));*/

  public static class Record {
    public String string;
    public Integer integer;
  }
}
