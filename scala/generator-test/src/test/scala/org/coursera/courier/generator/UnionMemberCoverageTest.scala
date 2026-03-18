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

import com.linkedin.data.DataMap
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.arrays.WithCustomTypesArrayUnion
import org.coursera.arrays.WithCustomTypesArrayUnionArray
import org.coursera.maps.WithComplexTypesMapUnion
import org.coursera.maps.WithComplexTypesMapUnionMap
import org.coursera.records.test.Simple
import org.coursera.unions.WithComplexTypesUnion
import org.coursera.unions.WithCustomUnionTestId
import org.coursera.unions.WithPrimitiveCustomTypesUnion
import org.coursera.unions.WithPrimitivesUnion
import org.coursera.unions.WithRecordCustomTypeUnion
import org.junit.Test

/**
 * Tests to cover union member classes used in arrays/maps, and additional
 * union member coverage for WithPrimitivesUnion, WithComplexTypesUnion, etc.
 */
class UnionMemberCoverageTest extends GeneratorTest with SchemaFixtures {

  private val simpleRecord = Simple(Some("test"))

  // ---------------------------------------------------------------------------
  // WithComplexTypesMapUnion member classes
  // ---------------------------------------------------------------------------

  @Test
  def testWithComplexTypesMapUnion_intMember(): Unit = {
    val member = WithComplexTypesMapUnion.IntMember(42)
    assert(member.value === 42)
    assert(member._1 === 42)
  }

  @Test
  def testWithComplexTypesMapUnion_stringMember(): Unit = {
    val member = WithComplexTypesMapUnion.StringMember("hello")
    assert(member.value === "hello")
  }

  @Test
  def testWithComplexTypesMapUnion_simpleMember(): Unit = {
    val member = WithComplexTypesMapUnion.SimpleMember(simpleRecord)
    assert(member.value === simpleRecord)
  }

  @Test
  def testWithComplexTypesMapUnion_build_intMember(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("int", 7)
    dataMap.makeReadOnly()
    val built = WithComplexTypesMapUnion.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithComplexTypesMapUnion.IntMember])
    assert(built.asInstanceOf[WithComplexTypesMapUnion.IntMember].value === 7)
  }

  @Test
  def testWithComplexTypesMapUnion_build_stringMember(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("string", "test")
    dataMap.makeReadOnly()
    val built = WithComplexTypesMapUnion.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithComplexTypesMapUnion.StringMember])
  }

  @Test
  def testWithComplexTypesMapUnion_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "val")
    dataMap.makeReadOnly()
    val built = WithComplexTypesMapUnion.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithComplexTypesMapUnion.$UnknownMember])
  }

  @Test
  def testWithComplexTypesMapUnion_equality(): Unit = {
    val a = WithComplexTypesMapUnion.IntMember(1)
    val b = WithComplexTypesMapUnion.IntMember(1)
    val c = WithComplexTypesMapUnion.IntMember(2)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithComplexTypesMapUnion_toString(): Unit = {
    val m = WithComplexTypesMapUnion.IntMember(1)
    assert(m.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithComplexTypesMapUnionMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testWithComplexTypesMapUnionMap_dataBuilder(): Unit = {
    val builder = WithComplexTypesMapUnionMap.newBuilder
    builder += ("k1" -> WithComplexTypesMapUnion.IntMember(1))
    builder += ("k2" -> WithComplexTypesMapUnion.StringMember("v2"))
    val result = builder.result()
    assert(result.size === 2)
    assert(result.get("k1").exists(_.isInstanceOf[WithComplexTypesMapUnion.IntMember]))
  }

  @Test
  def testWithComplexTypesMapUnionMap_dataBuilder_clear(): Unit = {
    val builder = WithComplexTypesMapUnionMap.newBuilder
    builder += ("k1" -> WithComplexTypesMapUnion.IntMember(1))
    builder.clear()
    assert(builder.result().size === 0)
  }

  @Test
  def testWithComplexTypesMapUnionMap_operations(): Unit = {
    val m = WithComplexTypesMapUnionMap(
      "k1" -> WithComplexTypesMapUnion.IntMember(1),
      "k2" -> WithComplexTypesMapUnion.StringMember("v")
    )
    assert(m.get("k1").exists(_.isInstanceOf[WithComplexTypesMapUnion.IntMember]))
    assert(m.get("missing") === None)
    assert((m - "k1").size === 1)
    assert((m + ("k3" -> WithComplexTypesMapUnion.IntMember(3))).size === 3)
    assert(m.iterator.size === 2)
  }

  @Test
  def testWithComplexTypesMapUnionMap_build_roundTrip(): Unit = {
    val original = WithComplexTypesMapUnionMap(
      "key" -> WithComplexTypesMapUnion.IntMember(5)
    )
    val rebuilt =
      WithComplexTypesMapUnionMap.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === rebuilt)
  }

  // ---------------------------------------------------------------------------
  // WithCustomTypesArrayUnion member classes
  // ---------------------------------------------------------------------------

  @Test
  def testWithCustomTypesArrayUnion_intMember(): Unit = {
    val member = WithCustomTypesArrayUnion.IntMember(10)
    assert(member.value === 10)
    assert(member._1 === 10)
  }

  @Test
  def testWithCustomTypesArrayUnion_stringMember(): Unit = {
    val member = WithCustomTypesArrayUnion.StringMember("s")
    assert(member.value === "s")
  }

  @Test
  def testWithCustomTypesArrayUnion_simpleMember(): Unit = {
    val member = WithCustomTypesArrayUnion.SimpleMember(simpleRecord)
    assert(member.value === simpleRecord)
  }

  @Test
  def testWithCustomTypesArrayUnion_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "val")
    dataMap.makeReadOnly()
    val built = WithCustomTypesArrayUnion.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithCustomTypesArrayUnion.$UnknownMember])
  }

  @Test
  def testWithCustomTypesArrayUnion_equality(): Unit = {
    val a = WithCustomTypesArrayUnion.IntMember(1)
    val b = WithCustomTypesArrayUnion.IntMember(1)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithCustomTypesArrayUnion_toString(): Unit = {
    val m = WithCustomTypesArrayUnion.IntMember(1)
    assert(m.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithCustomTypesArrayUnionArray DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testWithCustomTypesArrayUnionArray_dataBuilder(): Unit = {
    val builder = WithCustomTypesArrayUnionArray.newBuilder
    builder += (WithCustomTypesArrayUnion.IntMember(1))
    builder += (WithCustomTypesArrayUnion.StringMember("s"))
    val result = builder.result()
    assert(result.length === 2)
  }

  @Test
  def testWithCustomTypesArrayUnionArray_dataBuilder_clear(): Unit = {
    val builder = WithCustomTypesArrayUnionArray.newBuilder
    builder += (WithCustomTypesArrayUnion.IntMember(1))
    builder.clear()
    assert(builder.result().length === 0)
  }

  // ---------------------------------------------------------------------------
  // WithPrimitivesUnion additional member coverage
  // ---------------------------------------------------------------------------

  @Test
  def testWithPrimitivesUnion_booleanMember(): Unit = {
    val member = WithPrimitivesUnion.Union.BooleanMember(true)
    assert(member.value === true)
  }

  @Test
  def testWithPrimitivesUnion_intMember(): Unit = {
    val member = WithPrimitivesUnion.Union.IntMember(5)
    assert(member.value === 5)
  }

  @Test
  def testWithPrimitivesUnion_longMember(): Unit = {
    val member = WithPrimitivesUnion.Union.LongMember(100L)
    assert(member.value === 100L)
  }

  @Test
  def testWithPrimitivesUnion_floatMember(): Unit = {
    val member = WithPrimitivesUnion.Union.FloatMember(1.5f)
    assert(member.value === 1.5f)
  }

  @Test
  def testWithPrimitivesUnion_doubleMember(): Unit = {
    val member = WithPrimitivesUnion.Union.DoubleMember(2.5d)
    assert(member.value === 2.5d)
  }

  @Test
  def testWithPrimitivesUnion_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "val")
    dataMap.makeReadOnly()
    val built = WithPrimitivesUnion.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithPrimitivesUnion.Union.$UnknownMember])
  }

  @Test
  def testWithPrimitivesUnion_union_equality(): Unit = {
    val a = WithPrimitivesUnion.Union.IntMember(1)
    val b = WithPrimitivesUnion.Union.IntMember(1)
    val c = WithPrimitivesUnion.Union.IntMember(2)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithPrimitivesUnion_union_toString(): Unit = {
    val m = WithPrimitivesUnion.Union.IntMember(1)
    assert(m.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithComplexTypesUnion additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testWithComplexTypesUnion_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "val")
    dataMap.makeReadOnly()
    val built = WithComplexTypesUnion.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithComplexTypesUnion.Union.$UnknownMember])
  }

  @Test
  def testWithComplexTypesUnion_equality(): Unit = {
    val a = WithComplexTypesUnion(WithComplexTypesUnion.Union.EmptyMember(
      org.coursera.records.test.Empty()))
    val b = WithComplexTypesUnion(WithComplexTypesUnion.Union.EmptyMember(
      org.coursera.records.test.Empty()))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithComplexTypesUnion_copy(): Unit = {
    val m1 = WithComplexTypesUnion.Union.EmptyMember(org.coursera.records.test.Empty())
    val m2 = WithComplexTypesUnion.Union.FruitsMember(org.coursera.enums.Fruits.APPLE)
    val original = WithComplexTypesUnion(m1)
    val copied = original.copy(union = m2)
    assert(copied.union.isInstanceOf[WithComplexTypesUnion.Union.FruitsMember])
  }

  @Test
  def testWithComplexTypesUnion_unapply(): Unit = {
    val m = WithComplexTypesUnion.Union.EmptyMember(org.coursera.records.test.Empty())
    val w = WithComplexTypesUnion(m)
    val WithComplexTypesUnion(union) = w
    assert(union.isInstanceOf[WithComplexTypesUnion.Union.EmptyMember])
  }

  @Test
  def testWithComplexTypesUnion_toString(): Unit = {
    val w = WithComplexTypesUnion(WithComplexTypesUnion.Union.EmptyMember(
      org.coursera.records.test.Empty()))
    assert(w.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithPrimitiveCustomTypesUnion additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testWithPrimitiveCustomTypesUnion_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "val")
    dataMap.makeReadOnly()
    val built = WithPrimitiveCustomTypesUnion.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithPrimitiveCustomTypesUnion.Union.$UnknownMember])
  }

  @Test
  def testWithPrimitiveCustomTypesUnion_equality(): Unit = {
    val a = WithPrimitiveCustomTypesUnion(
      WithPrimitiveCustomTypesUnion.Union.CustomIntMember(
        org.coursera.courier.generator.customtypes.CustomInt(1)))
    val b = WithPrimitiveCustomTypesUnion(
      WithPrimitiveCustomTypesUnion.Union.CustomIntMember(
        org.coursera.courier.generator.customtypes.CustomInt(1)))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithPrimitiveCustomTypesUnion_union_equality(): Unit = {
    val a = WithPrimitiveCustomTypesUnion.Union.CustomIntMember(
      org.coursera.courier.generator.customtypes.CustomInt(1))
    val b = WithPrimitiveCustomTypesUnion.Union.CustomIntMember(
      org.coursera.courier.generator.customtypes.CustomInt(1))
    val c = WithPrimitiveCustomTypesUnion.Union.CustomIntMember(
      org.coursera.courier.generator.customtypes.CustomInt(2))
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  // ---------------------------------------------------------------------------
  // WithRecordCustomTypeUnion additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testWithRecordCustomTypeUnion_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "val")
    dataMap.makeReadOnly()
    val built = WithRecordCustomTypeUnion.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithRecordCustomTypeUnion.Union.$UnknownMember])
  }

  @Test
  def testWithRecordCustomTypeUnion_equality(): Unit = {
    val customRecord = org.coursera.courier.generator.customtypes.CustomRecord("name1", "body1")
    val a = WithRecordCustomTypeUnion(
      WithRecordCustomTypeUnion.Union.CustomRecordMember(customRecord))
    val b = WithRecordCustomTypeUnion(
      WithRecordCustomTypeUnion.Union.CustomRecordMember(customRecord))
    assert(a === b)
  }

  // ---------------------------------------------------------------------------
  // WithCustomUnionTestId additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testWithCustomUnionTestId_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "val")
    dataMap.makeReadOnly()
    val built = WithCustomUnionTestId.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithCustomUnionTestId.Union.$UnknownMember])
  }

  @Test
  def testWithCustomUnionTestId_equality(): Unit = {
    val id = org.coursera.courier.generator.customtypes.CustomUnionTestId(1)
    val a = WithCustomUnionTestId(
      WithCustomUnionTestId.Union.CustomUnionTestIdMember(id))
    val b = WithCustomUnionTestId(
      WithCustomUnionTestId.Union.CustomUnionTestIdMember(id))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithCustomUnionTestId_union_member(): Unit = {
    val id = org.coursera.courier.generator.customtypes.CustomUnionTestId(42)
    val member = WithCustomUnionTestId.Union.CustomUnionTestIdMember(id)
    assert(member.value === id)
  }

  @Test
  def testWithCustomUnionTestId_roundTrip(): Unit = {
    val id = org.coursera.courier.generator.customtypes.CustomUnionTestId(5)
    val original = WithCustomUnionTestId(
      WithCustomUnionTestId.Union.CustomUnionTestIdMember(id))
    val roundTripped =
      WithCustomUnionTestId.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithCustomUnionTestId_copy(): Unit = {
    val id1 = org.coursera.courier.generator.customtypes.CustomUnionTestId(1)
    val id2 = org.coursera.courier.generator.customtypes.CustomUnionTestId(99)
    val original = WithCustomUnionTestId(WithCustomUnionTestId.Union.CustomUnionTestIdMember(id1))
    val copied = original.copy(union = WithCustomUnionTestId.Union.CustomUnionTestIdMember(id2))
    assert(copied.union.asInstanceOf[WithCustomUnionTestId.Union.CustomUnionTestIdMember].value === id2)
  }

  @Test
  def testWithCustomUnionTestId_unapply(): Unit = {
    val id = org.coursera.courier.generator.customtypes.CustomUnionTestId(7)
    val w = WithCustomUnionTestId(WithCustomUnionTestId.Union.CustomUnionTestIdMember(id))
    val WithCustomUnionTestId(union) = w
    assert(union.isInstanceOf[WithCustomUnionTestId.Union.CustomUnionTestIdMember])
  }

  @Test
  def testWithCustomUnionTestId_toString(): Unit = {
    val id = org.coursera.courier.generator.customtypes.CustomUnionTestId(1)
    val w = WithCustomUnionTestId(WithCustomUnionTestId.Union.CustomUnionTestIdMember(id))
    assert(w.toString.nonEmpty)
  }
}
