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


import org.coursera.arrays.WithCustomTypesArray;
import org.coursera.courier.android.customtypes.CustomInt;
import org.coursera.records.Message;
import org.coursera.records.test.Empty;
import org.coursera.records.test.Simple;
import org.coursera.records.test.WithComplexTypes;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class TestGeneratedRecords {

  @Test
  public void testEmptyEqualsHashCode() {
    Empty record1 = new Empty();
    Empty record2 = new Empty();

    assertEqualsHashCode(record1, record2);
  }

  @Test
  public void testFieldEqualsHashCode() {
    Message title = new Message();
    title.title = "title";

    Message sameAsTitle = new Message();
    sameAsTitle.title = "title";
    assertEqualsHashCode(title, sameAsTitle);

    Message titleBody = new Message();
    titleBody.title = "title";
    titleBody.body = "body";

    Message sameAsTitleBody = new Message();
    sameAsTitleBody.title = "title";
    sameAsTitleBody.body = "body";
    assertEqualsHashCode(titleBody, sameAsTitleBody);

    Message empty = new Message();
    Message alsoEmpty = new Message();
    assertEqualsHashCode(empty, alsoEmpty);
  }

  private void assertEqualsHashCode(Object one, Object two) {
    Assert.assertTrue(one.equals(two));
    Assert.assertEquals(one.hashCode(), two.hashCode());
  }

  @Test
  public void testFieldEqualsHashCodeNotMatching() {
    Message title = new Message();
    title.title = "title";

    Message title2 = new Message();
    title2.title = "title2";

    Message body = new Message();
    body.body = "body";

    Message body2 = new Message();
    body2.body = "body2";

    Message titleBody = new Message();
    titleBody.title = "title";
    titleBody.body = "body";

    Message title2Body = new Message();
    title2Body.title = "title2";
    title2Body.body = "body";

    Message titleBody2 = new Message();
    titleBody2.title = "title";
    titleBody2.body = "body2";

    Message empty = new Message();

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
    WithComplexTypes record = new WithComplexTypes();
    record.array = new int[] { 1,2,3 };
    record.map = new HashMap<>(1);
    record.map.put("one", 1);
    WithComplexTypes.Union.SimpleMember member = new WithComplexTypes.Union.SimpleMember();
    Simple simple = new Simple();
    simple.message = "message";
    member.member = simple;
    record.union = member;
    return record;
  }

  @Test
  public void testCustomTypesEqualsHashCode() {
    WithCustomTypesArray record1 = createCustomTypeArrays();
    WithCustomTypesArray record2 = createCustomTypeArrays();

    assertEqualsHashCode(record1, record2);
  }

  private WithCustomTypesArray createCustomTypeArrays() {
    WithCustomTypesArray record = new WithCustomTypesArray();
    Simple[][] arrays = new Simple[][] { new Simple[2], new Simple[2] };
    arrays[0][0] = new Simple();
    arrays[0][0].message = "0-0";
    arrays[0][1] = new Simple();
    arrays[0][1].message = "0-1";
    arrays[1][0] = new Simple();
    arrays[1][0].message = "1-0";
    arrays[1][1] = new Simple();
    arrays[1][1].message = "1-1";
    record.arrays = arrays;
    record.ints = new CustomInt[] {new CustomInt(1), new CustomInt(2), new CustomInt(3)};
    return record;
  }
}
