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

import org.coursera.arrays.WithCustomTypesArray
import org.coursera.arrays.WithPrimitivesArray
import org.coursera.arrays.WithRecordArray
import org.coursera.courier.data.BooleanArray
import org.coursera.courier.data.BytesArray
import org.coursera.courier.data.DataTemplates.DataConversion
import org.coursera.courier.data.DoubleArray
import org.coursera.courier.data.FloatArray
import org.coursera.courier.data.IntArray
import org.coursera.courier.data.LongArray
import org.coursera.courier.data.StringArray
import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.customtypes.CustomIntArray
import org.coursera.enums.Fruits
import org.coursera.enums.FruitsArray
import org.coursera.records.test.Empty
import org.coursera.records.test.EmptyArray
import org.junit.BeforeClass
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

object ArrayGeneratorTest extends SchemaFixtures with GeneratorTest {

  @BeforeClass
  def setup(): Unit = {
    generateTestSchemas(Seq(
      Records.Empty,
      Arrays.WithRecordArray,
      Arrays.WithPrimitivesArray,
      Arrays.WithCustomTypesArray))
  }
}

class ArrayGeneratorTest extends GeneratorTest with SchemaFixtures with AssertionsForJUnit {

  @Test
  def testWithRecordArray(): Unit = {
    val original = WithRecordArray(
      EmptyArray(Empty(), Empty(), Empty()),
      FruitsArray(Fruits.APPLE, Fruits.BANANA, Fruits.ORANGE))
    assert(mapToJson(original) ===
      """{
         |  "empties" : [ { }, { }, { } ],
         |  "fruits" : [ "APPLE", "BANANA", "ORANGE" ]
         |}""".stripMargin)

    val roundTripped = WithRecordArray(roundTrip(original.data()), DataConversion.SetReadOnly)

    assert(original === roundTripped)
  }

  @Test
  def testWithPrimitivesArray(): Unit = {
    val original = WithPrimitivesArray(
      IntArray(1, 2, 3),
      LongArray(10L, 20L, 30L),
      FloatArray(1.1f, 2.2f, 3.3f),
      DoubleArray(11.1d, 22.2d, 33.3d),
      BooleanArray(false, true),
      StringArray("a", "b", "c"),
      BytesArray(bytes1, bytes2))
    val roundTripped = WithPrimitivesArray(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(mapToJson(original) ===
      s"""{
    |  "bytes" : [ "${'\\'}u0000${'\\'}u0001${'\\'}u0002", "${'\\'}u0003${'\\'}u0004${'\\'}u0005" ],
    |  "longs" : [ 10, 20, 30 ],
    |  "strings" : [ "a", "b", "c" ],
    |  "doubles" : [ 11.1, 22.2, 33.3 ],
    |  "booleans" : [ false, true ],
    |  "floats" : [ 1.1, 2.2, 3.3 ],
    |  "ints" : [ 1, 2, 3 ]
    |}""".stripMargin)
    assert(original === roundTripped)
  }

  @Test
  def testWithCustomTypesArray(): Unit = {
    val original = WithCustomTypesArray(CustomIntArray(CustomInt(1), CustomInt(2), CustomInt(3)))
    val roundTripped = WithCustomTypesArray(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(mapToJson(original) ===
      """{
        |  "ints" : [ 1, 2, 3 ]
        |}""".stripMargin)
    assert(original === roundTripped)
  }
}
