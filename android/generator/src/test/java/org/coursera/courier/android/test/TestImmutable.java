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
import org.coursera.courier.android.adapters.CustomIntAdapter;
import org.coursera.courier.android.adapters.DateTimeAdapter;
import org.coursera.courier.android.customtypes.CustomInt;
import org.coursera.maps.immutable.WithComplexTypesMap;
import org.coursera.records.immutable.Simple;
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
    String json = "{ \"message\": \"simple message\"}";

    Simple deserialized = gson.fromJson(json, Simple.class);

    Assert.assertEquals(deserialized.message, "simple message");

    Simple roundTripped = gson.fromJson(gson.toJson(deserialized), Simple.class);

    Assert.assertEquals(roundTripped.message, "simple message");

  }

  @Test
  public void testWithComplexTypesMap() {
    String json = load("WithComplexTypesMap.json");

    WithComplexTypesMap deserialized = gson.fromJson(json, WithComplexTypesMap.class);

    // TODO deserialized assertions

    String serialized = gson.toJson(deserialized);
    WithComplexTypesMap roundTripped = gson.fromJson(gson.toJson(deserialized), WithComplexTypesMap.class);

    assertJsonEquals(json, serialized);

    // TODO: roundTripped assertions
  }

  @Test
  public void testWithCustomTypesArray() {
    String json = load("WithCustomTypesArray.json");

    WithCustomTypesArray deserialized = gson.fromJson(json, WithCustomTypesArray.class);

    // TODO deserialized assertions

    String serialized = gson.toJson(deserialized);
    WithCustomTypesArray roundTripped = gson.fromJson(gson.toJson(deserialized), WithCustomTypesArray.class);

    assertJsonEquals(json, serialized);

    // TODO: roundTripped assertions
  }
}
