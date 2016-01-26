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
import org.coursera.courier.data.BooleanToStringMap
import org.coursera.courier.data.ByteStringToStringMap
import org.coursera.courier.data.BytesMap
import org.coursera.courier.data.DoubleMap
import org.coursera.courier.data.DoubleToStringMap
import org.coursera.courier.data.FloatMap
import org.coursera.courier.data.FloatToStringMap
import org.coursera.courier.data.IntArray
import org.coursera.courier.data.IntArrayToStringMap
import org.coursera.courier.data.IntMap
import org.coursera.courier.data.IntToStringMap
import org.coursera.courier.data.LongMap
import org.coursera.courier.data.LongToStringMap
import org.coursera.courier.data.StringMap
import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.courier.templates.DataTemplates
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.customtypes.CustomIntMap
import org.coursera.customtypes.CustomIntToStringMap
import org.coursera.enums.Fruits
import org.coursera.enums.FruitsMap
import org.coursera.enums.FruitsToStringMap
import org.coursera.fixed.Fixed8
import org.coursera.fixed.Fixed8Map
import org.coursera.fixed.Fixed8ToStringMap
import org.coursera.maps.Toggle
import org.coursera.maps.ToggleToStringMap

//import org.coursera.maps.RecordKey
//import org.coursera.maps.RecordKeyToStringMap
import org.coursera.maps.WithComplexTypesMap
import org.coursera.maps.WithComplexTypesMapUnion
import org.coursera.maps.WithComplexTypesMapUnionMap
import org.coursera.maps.WithCustomTypesMap
import org.coursera.maps.WithPrimitivesMap
import org.coursera.maps.WithTypedKeyMap
import org.coursera.records.test.Empty
import org.coursera.records.test.EmptyMap
import org.coursera.records.test.Simple
import org.coursera.records.test.SimpleArray
import org.coursera.records.test.SimpleArrayMap
import org.coursera.records.test.SimpleMap
import org.coursera.records.test.SimpleMapArray
import org.coursera.records.test.SimpleMapMap
import org.coursera.records.test.SimpleToStringMap
import org.junit.Test

class MapGeneratorTest extends GeneratorTest with SchemaFixtures {

  @Test
  def testWithComplexTypesMap(): Unit = {
    val json = load("WithComplexTypesMap.json")
    val original = WithComplexTypesMap(
      EmptyMap("a" -> Empty(), "b" -> Empty(), "c" -> Empty()),
      FruitsMap("a" -> Fruits.APPLE, "b" -> Fruits.BANANA, "c" -> Fruits.ORANGE),
      SimpleArrayMap("a" -> SimpleArray(Simple(Some("v1")), Simple(Some("v2")))),
      SimpleMapMap("o1" -> SimpleMap("i1" -> Simple(Some("o1i1")), "i2" -> Simple(Some("o1i2")))),
      WithComplexTypesMapUnionMap(
        "a" -> WithComplexTypesMapUnion.IntMember(1),
        "b" -> WithComplexTypesMapUnion.StringMember("u1")),
      Fixed8Map("a" -> Fixed8(bytesFixed8)))
    val roundTripped = WithComplexTypesMap(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)

    Seq(original, roundTripped).foreach { record =>
      assertJson(record, json)
    }
  }

  @Test
  def
  testWithPrimitivesMap(): Unit = {
    val json = load("WithPrimitivesMap.json")
    val original = WithPrimitivesMap(
      IntMap("a" -> 1, "b" -> 2, "c" -> 3),
      LongMap("a" -> 10L, "b" -> 20L, "c" -> 30L),
      FloatMap("a" -> 1.1f, "b" -> 2.2f, "c" -> 3.3f),
      DoubleMap("a" -> 11.1d, "b" -> 22.2d, "c" -> 33.3d),
      BooleanMap("a" -> true, "b" -> false, "c" -> true),
      StringMap("a" -> "string1", "b" -> "string2", "c" -> "string3"),
      BytesMap("a" -> bytes1, "b" -> bytes2, "c" -> bytes3))
    val roundTripped = WithPrimitivesMap(roundTrip(original.data()), DataConversion.SetReadOnly)

    assert(original === roundTripped)

    Seq(original, roundTripped).foreach { record =>
      assertJson(original, json)
    }
  }

  @Test
  def testWithCustomTypesMap(): Unit = {
    val json = load("WithCustomTypesMap.json")
    val original = WithCustomTypesMap(
      CustomIntMap("a" -> CustomInt(1), "b" -> CustomInt(2), "c" ->CustomInt(3)))
    val roundTripped = WithCustomTypesMap(roundTrip(original.data()), DataConversion.SetReadOnly)

    assert(original === roundTripped)

    Seq(original, roundTripped).foreach { record =>
      assertJson(original, json)
    }
  }

  @Test
  def testWithTypedKeyMap(): Unit = {
    val json = load("WithTypedKeyMap.json")
    val original = WithTypedKeyMap(
      IntToStringMap(1 -> "int"),
      LongToStringMap(2L -> "long"),
      FloatToStringMap(3.14f -> "float"),
      DoubleToStringMap(2.71d -> "double"),
      BooleanToStringMap(true -> "boolean"),
      StringMap("key" -> "string"),
      ByteStringToStringMap(bytesFixed8 -> "bytes"),
      SimpleToStringMap(Simple(Some("key")) -> "record"),
      IntArrayToStringMap(IntArray(1, 2) -> "array"),
      FruitsToStringMap(Fruits.APPLE -> "enum"),
      CustomIntToStringMap(CustomInt(100) -> "custom"),
      Fixed8ToStringMap(Fixed8(bytesFixed8) -> "fixed"))
      /*RecordKeyToStringMap(RecordKey(100, false) -> "inlineRecord")*/ // TODO: fix

    val roundTripped = WithTypedKeyMap(roundTrip(original.data()), DataConversion.SetReadOnly)

    assert(original === roundTripped)

    Seq(original, roundTripped).foreach { record =>
      assertJson(original, json)
    }
  }

  @Test
  def testTypedMapOperations(): Unit = {
    val customMap = CustomIntToStringMap(CustomInt(100) -> "custom")
    assert(customMap.get(CustomInt(100)) === Some("custom"))
    assert((customMap - CustomInt(100)).size === 0)
    assert((customMap + (CustomInt(101) -> "custom2")).size === 2)
    assert(customMap.iterator.toSeq == Seq(CustomInt(100) -> "custom"))
  }

  @Test
  def testWrapImplicitsArrayMap(): Unit = {
    val original: SimpleArrayMap = Map(
      "k1" -> Seq(Simple(Some("a"))),
      "k2" -> Seq(Simple(Some("b"))))
    val roundTripped = SimpleArrayMap(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWrapImplicitsMapMap(): Unit = {
    val original: SimpleMapMap = Map(
      "k1" -> Map("kk1" -> Simple(Some("a"))),
      "k2" -> Map("kk2" -> Simple(Some("b"))))
    val roundTripped = SimpleMapMap(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }
}
