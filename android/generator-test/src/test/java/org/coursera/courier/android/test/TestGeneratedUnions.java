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

import com.google.gson.Gson;
import org.coursera.records.WithFlatTypedDefinition;
import org.coursera.records.WithTypedDefinition;
import org.coursera.records.WithUnion;
import org.coursera.typerefs.FlatTypedDefinition;
import org.coursera.typerefs.Union;
import org.junit.Assert;
import org.coursera.records.Note;
import org.coursera.typerefs.TypedDefinition;
import org.junit.Test;

public class TestGeneratedUnions extends JsonTest {
  @Test
  public void testTypedDefinition() {
    Gson gson = new Gson();
    String json = load("WithTypedDefinition.json");
    WithTypedDefinition record = gson.fromJson(json, WithTypedDefinition.class);
    TypedDefinition TypedDefinition = record.value;
    TypedDefinition.MessageMember member = (TypedDefinition.MessageMember) TypedDefinition;
    Assert.assertEquals(member.definition.title, "title");
    Assert.assertEquals(member.definition.body, "Hello, Courier.");

    TypedDefinition.NoteMember noteMember = new TypedDefinition.NoteMember(new Note("text"));

    String serialized = gson.toJson(noteMember);
    TypedDefinition roundTripped = gson.fromJson(serialized, TypedDefinition.class);
    TypedDefinition.NoteMember roundTrippedMember = (TypedDefinition.NoteMember)roundTripped;
    Assert.assertEquals(roundTrippedMember.definition.text, "text");
  }

  @Test
  public void testFlatTypedDefinition() {
    Gson gson = new Gson();
    String json = load("WithFlatTypedDefinition.json");
    WithFlatTypedDefinition record = gson.fromJson(json, WithFlatTypedDefinition.class);
    FlatTypedDefinition TypedDefinition = record.value;
    FlatTypedDefinition.MessageMember member = (FlatTypedDefinition.MessageMember) TypedDefinition;
    Assert.assertEquals(member.title, "title");
    Assert.assertEquals(member.body, "Hello, Courier.");

    FlatTypedDefinition.NoteMember noteMember = new FlatTypedDefinition.NoteMember("text");
    String serialized = gson.toJson(noteMember);
    FlatTypedDefinition roundTripped = gson.fromJson(serialized, FlatTypedDefinition.class);
    FlatTypedDefinition.NoteMember roundTrippedMember = (FlatTypedDefinition.NoteMember)roundTripped;
    Assert.assertEquals(roundTrippedMember.text, "text");
  }

  @Test
  public void testUnion() {
    Gson gson = new Gson();
    String json = load("WithUnion.json");
    WithUnion record = gson.fromJson(json, WithUnion.class);
    Union Union = record.value;
    Union.MessageMember member = (Union.MessageMember) Union;
    Assert.assertEquals(member.member.title, "title");
    Assert.assertEquals(member.member.body, "Hello, Courier.");


    Union.NoteMember Member1 = new Union.NoteMember(new Note("text"));

    String serialized = gson.toJson(Member1);
    Union roundTripped = gson.fromJson(serialized, Union.class);
    Union.NoteMember roundTrippedMember = (Union.NoteMember)roundTripped;
    Assert.assertEquals(roundTrippedMember.member.text, "text");
  }
}
