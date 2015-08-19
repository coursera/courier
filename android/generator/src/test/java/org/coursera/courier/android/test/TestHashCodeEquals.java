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


import org.coursera.arrays.immutable.WithCustomTypesArray;
import org.coursera.courier.android.customtypes.CustomInt;
import org.coursera.records.immutable.Message;
import org.coursera.records.immutable.Empty;
import org.coursera.records.immutable.Simple;
import org.coursera.records.immutable.WithComplexTypes;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class TestHashCodeEquals {

  @Test
  public void testEmptyEqualsHashCode() {
    Empty record1 = new Empty();
    Empty record2 = new Empty();

    assertEqualsHashCode(record1, record2);
  }

  @Test
  public void testFieldEqualsHashCode() {
    Message title = new Message("title", null);
    Message sameAsTitle = new Message("title", null);
    assertEqualsHashCode(title, sameAsTitle);

    Message titleBody = new Message("title", "body");

    Message sameAsTitleBody = new Message("title", "body");
    assertEqualsHashCode(titleBody, sameAsTitleBody);

    Message empty = new Message.Builder().build();
    Message alsoEmpty = new Message.Builder().build();
    assertEqualsHashCode(empty, alsoEmpty);
  }

  private void assertEqualsHashCode(Object one, Object two) {
    Assert.assertTrue(one.equals(two));
    Assert.assertEquals(one.hashCode(), two.hashCode());
  }

  @Test
  public void testFieldEqualsHashCodeNotMatching() {
    Message title = new Message("title", null);
    Message title2 = new Message("title2", null);
    Message body = new Message("body", null);
    Message body2 = new Message("body2", null);
    Message titleBody = new Message("title", "body");
    Message title2Body = new Message("title1", "body");
    Message titleBody2 = new Message("title", "body2");
    Message empty = new Message(null, null);

    // Usually the hash code should be different, but that's not guaranteed.
    Assert.assertFalse(title.equals(title2));
    Assert.assertFalse(body.equals(body2));
    Assert.assertFalse(titleBody.equals(title2Body));
    Assert.assertFalse(titleBody.equals(titleBody2));
    Assert.assertFalse(title2Body.equals(titleBody2));
    Assert.assertFalse(title.equals(empty));
    Assert.assertFalse(empty.equals(titleBody));
  }

  @Test
  public void testComplexTypesEqualsHashCode() {
    WithComplexTypes record1 = createComplexType();
    WithComplexTypes record2 = createComplexType();

    assertEqualsHashCode(record1, record2);
  }

  private WithComplexTypes createComplexType() {
    WithComplexTypes.Builder record = new WithComplexTypes.Builder();
    record.array = new ArrayList<Integer>();
    record.array.add(1);
    record.array.add(2);
    record.array.add(3);
    record.map = new HashMap<>(1);
    record.map.put("one", 1);

    Simple.Builder simple = new Simple.Builder();
    simple.message = "message";
    WithComplexTypes.Union.SimpleMember member =
        new WithComplexTypes.Union.SimpleMember(simple.build());
    record.union = member;
    return record.build();
  }

  @Test
  public void testCustomTypesEqualsHashCode() {
    WithCustomTypesArray record1 = createCustomTypeArrays();
    WithCustomTypesArray record2 = createCustomTypeArrays();

    assertEqualsHashCode(record1, record2);
  }

  private WithCustomTypesArray createCustomTypeArrays() {
    WithCustomTypesArray.Builder record = new WithCustomTypesArray.Builder();
    List<List<Simple>> arrays = new ArrayList<List<Simple>>();
    List<Simple> o1 = new ArrayList<Simple>();
    o1.add(new Simple("0-0"));
    o1.add(new Simple("0-1"));

    List<Simple> o2 = new ArrayList<Simple>();
    o2.add(new Simple("1-0"));
    o2.add(new Simple("1-1"));

    record.arrays = arrays;
    record.ints = new ArrayList<CustomInt>();
    record.ints.add(new CustomInt(1));
    record.ints.add(new CustomInt(2));
    record.ints.add(new CustomInt(3));
    return record.build();
  }
}
