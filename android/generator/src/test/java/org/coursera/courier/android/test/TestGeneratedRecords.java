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


import org.coursera.records.Message;
import org.junit.Assert;
import org.junit.Test;

public class TestGeneratedRecords {
  @Test
  public void testEqualsHashCode() {
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
    Assert.assertTrue(one.equals(two) && one.hashCode() == two.hashCode());
  }

  @Test
  public void testEqualsHashCodeNotMatching() {
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
}
