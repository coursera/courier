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
import com.google.gson.GsonBuilder;
import org.coursera.arrays.immutable.WithCustomTypesArray;
import org.coursera.arrays.immutable.WithPrimitivesArray;
import org.coursera.arrays.immutable.WithRecordArray;
import org.coursera.courier.android.adapters.CustomIntAdapter;
import org.coursera.courier.android.adapters.DateTimeAdapter;
import org.coursera.courier.android.customtypes.CustomInt;
import org.coursera.maps.immutable.WithCustomTypesMap;
import org.coursera.maps.immutable.WithPrimitivesMap;
import org.coursera.maps.immutable.WithComplexTypesMap;
import org.coursera.maps.immutable.WithTypedKeyMap;
import org.coursera.records.immutable.Simple;
import org.coursera.records.immutable.WithPrimitives;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

public class TestImmutable extends JsonTest {
  Gson gson;
  public TestImmutable() {
    GsonBuilder builder = new GsonBuilder();

    // TODO: we should not need to register the adapter here
    builder.registerTypeAdapter(CustomInt.class, new CustomIntAdapter());
    builder.registerTypeAdapter(DateTime.class, new DateTimeAdapter());
    gson = builder.create();
  }

  @Test
  public void testSimple() {
    String json = load("Simple.json");

    Simple deserialized = gson.fromJson(json, Simple.class);

    Assert.assertEquals(deserialized.message, "simple message");

    String serialized = gson.toJson(deserialized);

    assertJsonEquals(json, serialized);
  }

  @Test
  public void testWithComplexTypesMap() {
    String json = load("WithComplexTypesMap.json");

    WithComplexTypesMap deserialized = gson.fromJson(json, WithComplexTypesMap.class);

    // TODO deserialized assertions

    String serialized = gson.toJson(deserialized);
    assertJsonEquals(json, serialized);
  }

  @Test
  public void testWithCustomTypesArray() {
    String json = load("WithCustomTypesArray.json");

    WithCustomTypesArray deserialized = gson.fromJson(json, WithCustomTypesArray.class);

    // TODO deserialized assertions

    String serialized = gson.toJson(deserialized);
    assertJsonEquals(json, serialized);
  }

  @Test
  public void testWithCustomTypesMap() {
    String json = load("WithCustomTypesMap.json");

    WithCustomTypesMap deserialized = gson.fromJson(json, WithCustomTypesMap.class);

    // TODO deserialized assertions

    String serialized = gson.toJson(deserialized);
    assertJsonEquals(json, serialized);
  }

  @Test
  public void testWithPrimitives() {
    String json = load("WithPrimitives.json");

    WithPrimitives deserialized = gson.fromJson(json, WithPrimitives.class);

    // TODO deserialized assertions

    String serialized = gson.toJson(deserialized);
    assertJsonEquals(json, serialized);
  }

  @Test
  public void testWithPrimitivesArray() {
    String json = load("WithPrimitivesArray.json");

    WithPrimitivesArray deserialized = gson.fromJson(json, WithPrimitivesArray.class);

    // TODO deserialized assertions

    String serialized = gson.toJson(deserialized);
    assertJsonEquals(json, serialized);
  }

  @Test
  public void testWithPrimitivesMap() {
    String json = load("WithPrimitivesMap.json");

    WithPrimitivesMap deserialized = gson.fromJson(json, WithPrimitivesMap.class);

    // TODO deserialized assertions

    String serialized = gson.toJson(deserialized);
    assertJsonEquals(json, serialized);
  }

  @Test
  public void testWithRecordArray() {
    String json = load("WithRecordArray.json");

    WithRecordArray deserialized = gson.fromJson(json, WithRecordArray.class);

    // TODO deserialized assertions

    String serialized = gson.toJson(deserialized);
    assertJsonEquals(json, serialized);
  }

  @Test
  public void testWithTypedKeyMap() {
    String json = load("WithTypedKeyMap.json");

    WithTypedKeyMap deserialized = gson.fromJson(json, WithTypedKeyMap.class);

    // TODO deserialized assertions

    String serialized = gson.toJson(deserialized);
    assertJsonEquals(json, serialized);
  }
}
