/*
 Copyright 2015 Coursera Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.coursera.courier.generator

import org.coursera.courier.data.BooleanMap
import org.coursera.courier.data.BytesMap
import org.coursera.courier.data.DoubleMap
import org.coursera.courier.data.FloatMap
import org.coursera.courier.data.IntMap
import org.coursera.courier.data.LongMap
import org.coursera.courier.data.StringMap
import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.courier.generator.customtypes.CustomIntCoercer
import org.coursera.customtypes.CustomIntMap
import org.coursera.enums.Fruits
import org.coursera.enums.FruitsMap
import org.coursera.maps.WithComplexTypesMap
import org.coursera.maps.WithCustomTypesMap
import org.coursera.maps.WithPrimitivesMap
import org.coursera.records.test.Empty
import org.coursera.records.test.EmptyMap
import org.junit.BeforeClass
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

object MapGeneratorTest extends SchemaFixtures with GeneratorTest {

  @BeforeClass
  def setup(): Unit = {
    generateTestSchemas(Seq(
      Records.Empty.schema,
      Records.WithComplexTypesMap.schema,
      Records.WithPrimitivesMap.schema,
      Records.WithCustomTypesMap.schema))
  }
}

class MapGeneratorTest extends GeneratorTest with SchemaFixtures with AssertionsForJUnit {

  @Test
  def testWithComplexTypesMap(): Unit = {
    val original = WithComplexTypesMap(
      EmptyMap("a" -> Empty(), "b" -> Empty(), "c" -> Empty()),
      FruitsMap("a" -> Fruits.APPLE, "b" -> Fruits.BANANA, "c" -> Fruits.ORANGE))
    //println(mapToJson(original))
  }

  @Test
  def testWithPrimitivesMap(): Unit = {
    val original = WithPrimitivesMap(
      IntMap("a" -> 1, "b" -> 2, "c" -> 3),
      LongMap("a" -> 10, "b" -> 20, "c" -> 30),
      FloatMap("a" -> 1.1f, "b" -> 2.2f, "c" -> 3.3f),
      DoubleMap("a" -> 11.1d, "b" -> 22.2d, "c" -> 33.3d),
      BooleanMap("a" -> true, "b" -> false, "c" -> true),
      StringMap("a" -> "string1", "b" -> "string2", "c" -> "string3"),
      BytesMap("a" -> bytes1, "b" -> bytes2, "c" -> bytes3))
    //println(mapToJson(original))
  }

  @Test
  def testWithCustomTypesMap(): Unit = {
    CustomIntCoercer.registerCoercer() // TODO: fix
    val original = WithCustomTypesMap(
      CustomIntMap("a" -> CustomInt(1), "b" -> CustomInt(2), "c" -> CustomInt(3)))
    println(mapToJson(original))
  }
}
