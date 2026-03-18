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

import org.coursera.courier.data.IntArray
import org.coursera.courier.generator.customtypes.CustomArrayTestId
import org.coursera.courier.generator.customtypes.CustomIntWrapper
import org.coursera.courier.generator.customtypes.CustomMapTestKeyId
import org.coursera.courier.generator.customtypes.CustomMapTestValueId
import org.coursera.courier.generator.customtypes.CustomRecord
import org.coursera.courier.generator.customtypes.CustomRecordTestId
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.customtypes.CustomArrayTestIdArray
import org.coursera.customtypes.CustomIntArray
import org.coursera.customtypes.CustomIntMap
import org.coursera.customtypes.CustomIntToStringMap
import org.coursera.customtypes.CustomMapTestKeyIdToCustomMapTestValueIdMap
import org.coursera.customtypes.CustomRecordArray
import org.coursera.customtypes.CustomRecordToCustomRecordMap
import org.coursera.customtypes.IntIdArray
import org.coursera.customtypes.IntIdMap
import org.coursera.customtypes.IntIdToStringMap
import org.coursera.enums.Fruits
import org.coursera.enums.FruitsArray
import org.coursera.enums.FruitsMap
import org.coursera.enums.FruitsToStringMap
import org.coursera.fixed.Fixed8
import org.coursera.fixed.Fixed8Array
import org.coursera.fixed.Fixed8Map
import org.coursera.fixed.Fixed8ToStringMap
import org.coursera.fixed.WithFixed8
import org.coursera.maps.WithCustomTypesMap
import org.coursera.records.CourierFile
import org.coursera.records.test.Empty
import org.coursera.records.test.EmptyArray
import org.coursera.records.test.EmptyMap
import org.coursera.records.test.InlineOptionalRecord
import org.coursera.records.test.Simple
import org.coursera.records.test.SimpleArray
import org.coursera.records.test.SimpleArrayArray
import org.coursera.records.test.SimpleArrayMap
import org.coursera.records.test.SimpleMap
import org.coursera.records.test.SimpleMapArray
import org.coursera.records.test.SimpleMapMap
import org.coursera.records.test.SimpleToStringMap
import org.coursera.arrays.WithCustomArrayTestId
import org.coursera.maps.WithCustomMapTestIds
import org.coursera.records.test.WithComplexTyperefs
import org.coursera.records.test.WithCourierFile
import org.coursera.records.test.WithCustomIntWrapper
import org.coursera.records.test.WithCustomRecord
import org.coursera.records.test.WithCustomRecordTestId
import org.coursera.records.test.WithInlineRecord
import org.coursera.records.test.WithOmitField
import org.coursera.records.test.WithPrimitiveCustomTypes
import org.coursera.records.test.WithPrimitives
import org.coursera.records.test.packaging.{Empty => PackagingEmpty}
import org.coursera.typerefs.UnionTyperef
import org.junit.Test

/**
 * Extended coverage tests for collection types (arrays, maps) DataBuilders
 * and record types that need copy/equality/productArity/unapply coverage.
 */
class CollectionAndRecordCoverageTest extends GeneratorTest with SchemaFixtures {

  private val customRecord = CustomRecord("title1", "body1")
  private val simpleRecord = Simple(Some("x"))
  private val emptyRecord = Empty()

  // ---------------------------------------------------------------------------
  // SimpleArray — copy, equality, unapply, productArity
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleArray_equality(): Unit = {
    val a = SimpleArray(Simple(Some("a")), Simple(Some("b")))
    val b = SimpleArray(Simple(Some("a")), Simple(Some("b")))
    val c = SimpleArray(Simple(Some("x")))
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testSimpleArray_copy(): Unit = {
    val arr = SimpleArray(Simple(Some("a")))
    val copied = arr.copy()
    assert(copied === arr)
  }

  @Test
  def testSimpleArray_toString(): Unit = {
    val arr = SimpleArray(Simple(Some("a")))
    assert(arr.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // SimpleMap — copy, equality, operations
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleMap_equality(): Unit = {
    val a = SimpleMap("k" -> Simple(Some("v")))
    val b = SimpleMap("k" -> Simple(Some("v")))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testSimpleMap_operations(): Unit = {
    val m = SimpleMap("k1" -> Simple(Some("v1")), "k2" -> Simple(Some("v2")))
    assert(m.get("k1") === Some(Simple(Some("v1"))))
    assert(m.get("missing") === None)
    assert((m - "k1").size === 1)
    assert((m + ("k3" -> Simple(Some("v3")))).size === 3)
    assert(m.iterator.size === 2)
    assert(m.isEmpty === false)
  }

  @Test
  def testSimpleMap_toString(): Unit = {
    val m = SimpleMap("k" -> Simple(Some("v")))
    assert(m.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // EmptyArray DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testEmptyArray_equality(): Unit = {
    val a = EmptyArray(Empty())
    val b = EmptyArray(Empty())
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testEmptyArray_operations(): Unit = {
    val arr = EmptyArray(Empty(), Empty())
    assert(arr.length === 2)
    assert(arr.apply(0) === Empty())
    val arr2 = arr.copy()
    assert(arr2 === arr)
  }

  @Test
  def testEmptyArray_toString(): Unit = {
    val arr = EmptyArray(Empty())
    assert(arr.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // EmptyMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testEmptyMap_equality(): Unit = {
    val a = EmptyMap("k" -> Empty())
    val b = EmptyMap("k" -> Empty())
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testEmptyMap_operations(): Unit = {
    val m = EmptyMap("k1" -> Empty(), "k2" -> Empty())
    assert(m.get("k1") === Some(Empty()))
    assert((m - "k1").size === 1)
    assert(m.iterator.size === 2)
  }

  // ---------------------------------------------------------------------------
  // SimpleToStringMap
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleToStringMap_equality(): Unit = {
    val a = SimpleToStringMap(Simple(Some("a")) -> "v1")
    val b = SimpleToStringMap(Simple(Some("a")) -> "v1")
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testSimpleToStringMap_operations(): Unit = {
    val k1 = Simple(Some("k1"))
    val k2 = Simple(Some("k2"))
    val m = SimpleToStringMap(k1 -> "v1", k2 -> "v2")
    assert(m.get(k1) === Some("v1"))
    assert((m - k1).size === 1)
    assert(m.iterator.size === 2)
  }

  // ---------------------------------------------------------------------------
  // SimpleArrayArray / SimpleMapArray / SimpleMapMap / SimpleArrayMap
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleArrayArray_equality(): Unit = {
    val inner = SimpleArray(Simple(Some("a")))
    val a = SimpleArrayArray(inner)
    val b = SimpleArrayArray(inner)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testSimpleArrayArray_toString(): Unit = {
    assert(SimpleArrayArray(SimpleArray()).toString.nonEmpty)
  }

  @Test
  def testSimpleMapArray_equality(): Unit = {
    val inner = SimpleMap("k" -> Simple(Some("v")))
    val a = SimpleMapArray(inner)
    val b = SimpleMapArray(inner)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testSimpleMapMap_equality(): Unit = {
    val inner = SimpleMap("k" -> Simple(Some("v")))
    val a = SimpleMapMap("key" -> inner)
    val b = SimpleMapMap("key" -> inner)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testSimpleArrayMap_equality(): Unit = {
    val inner = SimpleArray(Simple(Some("x")))
    val a = SimpleArrayMap("k" -> inner)
    val b = SimpleArrayMap("k" -> inner)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  // ---------------------------------------------------------------------------
  // FruitsArray / FruitsMap / FruitsToStringMap
  // ---------------------------------------------------------------------------

  @Test
  def testFruitsArray_equality(): Unit = {
    val a = FruitsArray(Fruits.APPLE, Fruits.PINEAPPLE)
    val b = FruitsArray(Fruits.APPLE, Fruits.PINEAPPLE)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testFruitsArray_operations(): Unit = {
    val arr = FruitsArray(Fruits.APPLE, Fruits.PINEAPPLE)
    assert(arr.length === 2)
    assert(arr(0) === Fruits.APPLE)
    val arr2 = arr.copy()
    assert(arr2 === arr)
  }

  @Test
  def testFruitsArray_toString(): Unit = {
    assert(FruitsArray(Fruits.APPLE).toString.nonEmpty)
  }

  @Test
  def testFruitsMap_equality(): Unit = {
    val a = FruitsMap("k" -> Fruits.APPLE)
    val b = FruitsMap("k" -> Fruits.APPLE)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testFruitsMap_operations(): Unit = {
    val m = FruitsMap("k1" -> Fruits.APPLE, "k2" -> Fruits.PINEAPPLE)
    assert(m.get("k1") === Some(Fruits.APPLE))
    assert((m - "k1").size === 1)
    assert(m.iterator.size === 2)
  }

  @Test
  def testFruitsToStringMap_equality(): Unit = {
    val a = FruitsToStringMap(Fruits.APPLE -> "v")
    val b = FruitsToStringMap(Fruits.APPLE -> "v")
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testFruitsToStringMap_operations(): Unit = {
    val m = FruitsToStringMap(Fruits.APPLE -> "a", Fruits.PINEAPPLE -> "p")
    assert(m.get(Fruits.APPLE) === Some("a"))
    assert((m - Fruits.APPLE).size === 1)
    assert(m.iterator.size === 2)
  }

  // ---------------------------------------------------------------------------
  // Fixed8 / Fixed8Array / Fixed8Map / Fixed8ToStringMap / WithFixed8
  // ---------------------------------------------------------------------------

  @Test
  def testFixed8_construction(): Unit = {
    val f = Fixed8(bytesFixed8)
    assert(f !== null)
  }

  @Test
  def testFixed8_equality(): Unit = {
    val a = Fixed8(bytesFixed8)
    val b = Fixed8(bytesFixed8)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testFixed8_toString(): Unit = {
    val f = Fixed8(bytesFixed8)
    assert(f.toString.nonEmpty)
  }

  @Test
  def testFixed8Array_equality(): Unit = {
    val a = Fixed8Array(Fixed8(bytesFixed8))
    val b = Fixed8Array(Fixed8(bytesFixed8))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testFixed8Array_operations(): Unit = {
    val arr = Fixed8Array(Fixed8(bytesFixed8))
    assert(arr.length === 1)
    val arr2 = arr.copy()
    assert(arr2 === arr)
  }

  @Test
  def testFixed8Array_toString(): Unit = {
    assert(Fixed8Array(Fixed8(bytesFixed8)).toString.nonEmpty)
  }

  @Test
  def testFixed8Map_equality(): Unit = {
    val a = Fixed8Map("k" -> Fixed8(bytesFixed8))
    val b = Fixed8Map("k" -> Fixed8(bytesFixed8))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testFixed8Map_operations(): Unit = {
    val m = Fixed8Map("k1" -> Fixed8(bytesFixed8))
    assert(m.get("k1").isDefined)
    assert((m - "k1").size === 0)
  }

  @Test
  def testFixed8ToStringMap_equality(): Unit = {
    val a = Fixed8ToStringMap(Fixed8(bytesFixed8) -> "v")
    val b = Fixed8ToStringMap(Fixed8(bytesFixed8) -> "v")
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testFixed8ToStringMap_operations(): Unit = {
    val m = Fixed8ToStringMap(Fixed8(bytesFixed8) -> "v")
    assert(m.get(Fixed8(bytesFixed8)) === Some("v"))
    assert(m.iterator.size === 1)
  }

  @Test
  def testWithFixed8_equality(): Unit = {
    val a = WithFixed8(Fixed8(bytesFixed8))
    val b = WithFixed8(Fixed8(bytesFixed8))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithFixed8_copy(): Unit = {
    val w = WithFixed8(Fixed8(bytesFixed8))
    val copied = w.copy(fixed = Fixed8(bytesFixed8))
    assert(copied === w)
  }

  @Test
  def testWithFixed8_unapply(): Unit = {
    val w = WithFixed8(Fixed8(bytesFixed8))
    val WithFixed8(f) = w
    assert(f === Fixed8(bytesFixed8))
  }

  @Test
  def testWithFixed8_roundTrip(): Unit = {
    val original = WithFixed8(Fixed8(bytesFixed8))
    val roundTripped = WithFixed8.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithFixed8_toString(): Unit = {
    assert(WithFixed8(Fixed8(bytesFixed8)).toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // CustomIntArray / CustomIntMap / CustomIntToStringMap DataBuilders
  // ---------------------------------------------------------------------------

  @Test
  def testCustomIntArray_equality(): Unit = {
    val a = CustomIntArray(org.coursera.courier.generator.customtypes.CustomInt(1))
    val b = CustomIntArray(org.coursera.courier.generator.customtypes.CustomInt(1))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testCustomIntArray_operations(): Unit = {
    val ci = org.coursera.courier.generator.customtypes.CustomInt(5)
    val arr = CustomIntArray(ci, ci)
    assert(arr.length === 2)
    val arr2 = arr.copy()
    assert(arr2 === arr)
  }

  @Test
  def testCustomIntMap_equality(): Unit = {
    val ci = org.coursera.courier.generator.customtypes.CustomInt(1)
    val a = CustomIntMap("k" -> ci)
    val b = CustomIntMap("k" -> ci)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testCustomIntMap_operations(): Unit = {
    val ci = org.coursera.courier.generator.customtypes.CustomInt(1)
    val m = CustomIntMap("k1" -> ci, "k2" -> ci)
    assert(m.get("k1") === Some(ci))
    assert((m - "k1").size === 1)
  }

  @Test
  def testCustomIntToStringMap_equality(): Unit = {
    val ci = org.coursera.courier.generator.customtypes.CustomInt(1)
    val a = CustomIntToStringMap(ci -> "v")
    val b = CustomIntToStringMap(ci -> "v")
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testCustomIntToStringMap_operations(): Unit = {
    val ci = org.coursera.courier.generator.customtypes.CustomInt(1)
    val m = CustomIntToStringMap(ci -> "v")
    assert(m.get(ci) === Some("v"))
    assert(m.iterator.size === 1)
  }

  // ---------------------------------------------------------------------------
  // CustomRecordArray / CustomRecordToCustomRecordMap DataBuilders
  // ---------------------------------------------------------------------------

  @Test
  def testCustomRecordArray_equality(): Unit = {
    val a = CustomRecordArray(customRecord)
    val b = CustomRecordArray(customRecord)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testCustomRecordArray_operations(): Unit = {
    val arr = CustomRecordArray(customRecord, customRecord)
    assert(arr.length === 2)
    val arr2 = arr.copy()
    assert(arr2 === arr)
  }

  @Test
  def testCustomRecordToCustomRecordMap_equality(): Unit = {
    val a = CustomRecordToCustomRecordMap(customRecord -> customRecord)
    val b = CustomRecordToCustomRecordMap(customRecord -> customRecord)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testCustomRecordToCustomRecordMap_operations(): Unit = {
    val m = CustomRecordToCustomRecordMap(customRecord -> customRecord)
    assert(m.get(customRecord).isDefined)
    assert(m.iterator.size === 1)
  }

  // ---------------------------------------------------------------------------
  // CustomArrayTestIdArray / CustomMapTestKeyIdToCustomMapTestValueIdMap
  // ---------------------------------------------------------------------------

  @Test
  def testCustomArrayTestIdArray_equality(): Unit = {
    val id = CustomArrayTestId(1)
    val a = CustomArrayTestIdArray(id)
    val b = CustomArrayTestIdArray(id)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testCustomArrayTestIdArray_operations(): Unit = {
    val id = CustomArrayTestId(1)
    val arr = CustomArrayTestIdArray(id, id)
    assert(arr.length === 2)
    val arr2 = arr.copy()
    assert(arr2 === arr)
  }

  @Test
  def testCustomMapTestKeyIdToCustomMapTestValueIdMap_equality(): Unit = {
    val k = CustomMapTestKeyId(1)
    val v = CustomMapTestValueId(2)
    val a = CustomMapTestKeyIdToCustomMapTestValueIdMap(k -> v)
    val b = CustomMapTestKeyIdToCustomMapTestValueIdMap(k -> v)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testCustomMapTestKeyIdToCustomMapTestValueIdMap_operations(): Unit = {
    val k = CustomMapTestKeyId(1)
    val v = CustomMapTestValueId(2)
    val m = CustomMapTestKeyIdToCustomMapTestValueIdMap(k -> v)
    assert(m.get(k) === Some(v))
    assert(m.iterator.size === 1)
  }

  // ---------------------------------------------------------------------------
  // IntIdArray / IntIdMap / IntIdToStringMap
  // ---------------------------------------------------------------------------

  @Test
  def testIntIdArray_equality(): Unit = {
    val id = org.coursera.courier.generator.customtypes.IntId(1)
    val a = IntIdArray(id)
    val b = IntIdArray(id)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testIntIdArray_operations(): Unit = {
    val id = org.coursera.courier.generator.customtypes.IntId(1)
    val arr = IntIdArray(id, id)
    assert(arr.length === 2)
  }

  @Test
  def testIntIdMap_equality(): Unit = {
    val id = org.coursera.courier.generator.customtypes.IntId(1)
    val a = IntIdMap("k" -> id)
    val b = IntIdMap("k" -> id)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testIntIdMap_operations(): Unit = {
    val id = org.coursera.courier.generator.customtypes.IntId(1)
    val m = IntIdMap("k" -> id)
    assert(m.get("k") === Some(id))
  }

  @Test
  def testIntIdToStringMap_equality(): Unit = {
    val id = org.coursera.courier.generator.customtypes.IntId(1)
    val a = IntIdToStringMap(id -> "v")
    val b = IntIdToStringMap(id -> "v")
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  // ---------------------------------------------------------------------------
  // Record types: WithCourierFile, InlineOptionalRecord, WithComplexTyperefs
  //               WithCustomRecordTestId, WithCustomArrayTestId, WithCustomMapTestIds
  //               WithCustomRecord, WithCustomIntWrapper
  // ---------------------------------------------------------------------------

  @Test
  def testWithCourierFile_equality(): Unit = {
    val cf = CourierFile("test.courier")
    val a = WithCourierFile(courierFile = cf)
    val b = WithCourierFile(courierFile = cf)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithCourierFile_copy(): Unit = {
    val cf1 = CourierFile("file1.courier")
    val cf2 = CourierFile("file2.courier")
    val w = WithCourierFile(cf1)
    val copied = w.copy(courierFile = cf2)
    assert(copied.courierFile === cf2)
  }

  @Test
  def testWithCourierFile_unapply(): Unit = {
    val cf = CourierFile("test.courier")
    val w = WithCourierFile(cf)
    val WithCourierFile(f) = w
    assert(f === cf)
  }

  @Test
  def testWithCourierFile_toString(): Unit = {
    assert(WithCourierFile(CourierFile("test")).toString.nonEmpty)
  }

  @Test
  def testCourierFile_equality(): Unit = {
    val a = CourierFile("file.courier")
    val b = CourierFile("file.courier")
    val c = CourierFile("other.courier")
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testCourierFile_copy(): Unit = {
    val f = CourierFile("original.courier")
    val copied = f.copy(find = "updated.courier")
    assert(copied.find === "updated.courier")
  }

  @Test
  def testCourierFile_unapply(): Unit = {
    val f = CourierFile("test.courier")
    val CourierFile(name) = f
    assert(name === "test.courier")
  }

  @Test
  def testCourierFile_toString(): Unit = {
    assert(CourierFile("test.courier").toString.nonEmpty)
  }

  @Test
  def testInlineOptionalRecord_equality(): Unit = {
    val a = InlineOptionalRecord("x")
    val b = InlineOptionalRecord("x")
    val c = InlineOptionalRecord("y")
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testInlineOptionalRecord_copy(): Unit = {
    val r = InlineOptionalRecord("original")
    val copied = r.copy(value = "updated")
    assert(copied.value === "updated")
  }

  @Test
  def testInlineOptionalRecord_unapply(): Unit = {
    val r = InlineOptionalRecord("abc")
    val InlineOptionalRecord(v) = r
    assert(v === "abc")
  }

  @Test
  def testInlineOptionalRecord_roundTrip(): Unit = {
    val original = InlineOptionalRecord("test")
    val roundTripped =
      InlineOptionalRecord.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testInlineOptionalRecord_toString(): Unit = {
    assert(InlineOptionalRecord("x").toString.nonEmpty)
  }

  @Test
  def testWithComplexTyperefs_equality(): Unit = {
    val union = UnionTyperef.StringMember("hello")
    val a = WithComplexTyperefs(
      enum = Fruits.APPLE,
      record = Empty(),
      map = EmptyMap(),
      array = EmptyArray(),
      union = union
    )
    val b = WithComplexTyperefs(Fruits.APPLE, Empty(), EmptyMap(), EmptyArray(), union)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithComplexTyperefs_copy(): Unit = {
    val union = UnionTyperef.IntMember(1)
    val r = WithComplexTyperefs(Fruits.APPLE, Empty(), EmptyMap(), EmptyArray(), union)
    val copied = r.copy(enum = Fruits.PINEAPPLE)
    assert(copied.enum === Fruits.PINEAPPLE)
  }

  @Test
  def testWithComplexTyperefs_unapply(): Unit = {
    val union = UnionTyperef.IntMember(5)
    val r = WithComplexTyperefs(Fruits.APPLE, Empty(), EmptyMap(), EmptyArray(), union)
    val WithComplexTyperefs(e, rec, m, arr, u) = r
    assert(e === Fruits.APPLE)
  }

  @Test
  def testWithComplexTyperefs_roundTrip(): Unit = {
    val union = UnionTyperef.StringMember("test")
    val original = WithComplexTyperefs(Fruits.APPLE, Empty(), EmptyMap(), EmptyArray(), union)
    val roundTripped =
      WithComplexTyperefs.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithComplexTyperefs_toString(): Unit = {
    val union = UnionTyperef.IntMember(1)
    assert(WithComplexTyperefs(Fruits.APPLE, Empty(), EmptyMap(), EmptyArray(), union).toString.nonEmpty)
  }

  @Test
  def testWithCustomRecordTestId_equality(): Unit = {
    val id = CustomRecordTestId(1)
    val a = WithCustomRecordTestId(id)
    val b = WithCustomRecordTestId(id)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithCustomRecordTestId_copy(): Unit = {
    val r = WithCustomRecordTestId(CustomRecordTestId(1))
    val copied = r.copy(id = CustomRecordTestId(99))
    assert(copied.id === CustomRecordTestId(99))
  }

  @Test
  def testWithCustomRecordTestId_unapply(): Unit = {
    val r = WithCustomRecordTestId(CustomRecordTestId(5))
    val WithCustomRecordTestId(id) = r
    assert(id === CustomRecordTestId(5))
  }

  @Test
  def testWithCustomRecordTestId_roundTrip(): Unit = {
    val original = WithCustomRecordTestId(CustomRecordTestId(42))
    val roundTripped =
      WithCustomRecordTestId.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithCustomRecordTestId_toString(): Unit = {
    assert(WithCustomRecordTestId(CustomRecordTestId(1)).toString.nonEmpty)
  }

  @Test
  def testWithCustomArrayTestId_equality(): Unit = {
    val arr = CustomArrayTestIdArray(CustomArrayTestId(1))
    val a = WithCustomArrayTestId(arr)
    val b = WithCustomArrayTestId(arr)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithCustomArrayTestId_copy(): Unit = {
    val arr1 = CustomArrayTestIdArray(CustomArrayTestId(1))
    val arr2 = CustomArrayTestIdArray(CustomArrayTestId(2))
    val w = WithCustomArrayTestId(arr1)
    val copied = w.copy(array = arr2)
    assert(copied.array === arr2)
  }

  @Test
  def testWithCustomArrayTestId_unapply(): Unit = {
    val arr = CustomArrayTestIdArray(CustomArrayTestId(1))
    val w = WithCustomArrayTestId(arr)
    val WithCustomArrayTestId(a) = w
    assert(a === arr)
  }

  @Test
  def testWithCustomArrayTestId_roundTrip(): Unit = {
    val arr = CustomArrayTestIdArray(CustomArrayTestId(3))
    val original = WithCustomArrayTestId(arr)
    val roundTripped =
      WithCustomArrayTestId.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithCustomArrayTestId_toString(): Unit = {
    assert(WithCustomArrayTestId(CustomArrayTestIdArray()).toString.nonEmpty)
  }

  @Test
  def testWithCustomMapTestIds_equality(): Unit = {
    val m = CustomMapTestKeyIdToCustomMapTestValueIdMap(
      CustomMapTestKeyId(1) -> CustomMapTestValueId(2))
    val a = WithCustomMapTestIds(m)
    val b = WithCustomMapTestIds(m)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithCustomMapTestIds_copy(): Unit = {
    val m1 = CustomMapTestKeyIdToCustomMapTestValueIdMap()
    val m2 = CustomMapTestKeyIdToCustomMapTestValueIdMap(
      CustomMapTestKeyId(1) -> CustomMapTestValueId(2))
    val w = WithCustomMapTestIds(m1)
    val copied = w.copy(map = m2)
    assert(copied.map === m2)
  }

  @Test
  def testWithCustomMapTestIds_roundTrip(): Unit = {
    val m = CustomMapTestKeyIdToCustomMapTestValueIdMap()
    val original = WithCustomMapTestIds(m)
    val roundTripped =
      WithCustomMapTestIds.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithCustomMapTestIds_toString(): Unit = {
    assert(WithCustomMapTestIds(CustomMapTestKeyIdToCustomMapTestValueIdMap()).toString.nonEmpty)
  }

  @Test
  def testWithCustomRecord_equality(): Unit = {
    val a = WithCustomRecord()
    val b = WithCustomRecord()
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithCustomRecord_copy(): Unit = {
    val w = WithCustomRecord()
    val cr = CustomRecord("title2", "body2")
    val copied = w.copy(custom = cr)
    assert(copied.custom === cr)
  }

  @Test
  def testWithCustomRecord_unapply(): Unit = {
    val w = WithCustomRecord()
    val WithCustomRecord(c, ca, cm) = w
    assert(c === w.custom)
  }

  @Test
  def testWithCustomRecord_roundTrip(): Unit = {
    val original = WithCustomRecord()
    val roundTripped =
      WithCustomRecord.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithCustomRecord_toString(): Unit = {
    assert(WithCustomRecord().toString.nonEmpty)
  }

  @Test
  def testWithCustomTypesMap_equality(): Unit = {
    val ci = org.coursera.courier.generator.customtypes.CustomInt(1)
    val m = CustomIntMap("k" -> ci)
    val a = WithCustomTypesMap(m)
    val b = WithCustomTypesMap(m)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithCustomTypesMap_copy(): Unit = {
    val ci = org.coursera.courier.generator.customtypes.CustomInt(1)
    val m1 = CustomIntMap("k1" -> ci)
    val m2 = CustomIntMap("k2" -> ci)
    val w = WithCustomTypesMap(m1)
    val copied = w.copy(ints = m2)
    assert(copied.ints === m2)
  }

  @Test
  def testWithCustomTypesMap_unapply(): Unit = {
    val m = CustomIntMap()
    val w = WithCustomTypesMap(m)
    val WithCustomTypesMap(ints) = w
    assert(ints === m)
  }

  @Test
  def testWithCustomTypesMap_roundTrip(): Unit = {
    val ci = org.coursera.courier.generator.customtypes.CustomInt(5)
    val m = CustomIntMap("k" -> ci)
    val original = WithCustomTypesMap(m)
    val roundTripped =
      WithCustomTypesMap.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  // ---------------------------------------------------------------------------
  // WithOmitField additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testWithOmitField_equality(): Unit = {
    val ci = org.coursera.courier.generator.customtypes.CustomInt(1)
    val a = WithOmitField(1, ci)
    val b = WithOmitField(1, ci)
    val c = WithOmitField(2, ci)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithOmitField_unapply(): Unit = {
    val ci = org.coursera.courier.generator.customtypes.CustomInt(5)
    val w = WithOmitField(3, ci)
    val WithOmitField(k, kc) = w
    assert(k === 3)
    assert(kc === ci)
  }

  @Test
  def testWithOmitField_roundTrip(): Unit = {
    val ci = org.coursera.courier.generator.customtypes.CustomInt(1)
    val original = WithOmitField(7, ci)
    val roundTripped =
      WithOmitField.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithOmitField_toString(): Unit = {
    assert(WithOmitField(1, org.coursera.courier.generator.customtypes.CustomInt(1)).toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithPrimitiveCustomTypes additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testWithPrimitiveCustomTypes_equality(): Unit = {
    val ci = org.coursera.courier.generator.customtypes.CustomInt(1)
    val a = WithPrimitiveCustomTypes(intField = ci)
    val b = WithPrimitiveCustomTypes(intField = ci)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithPrimitiveCustomTypes_copy(): Unit = {
    val ci1 = org.coursera.courier.generator.customtypes.CustomInt(1)
    val ci2 = org.coursera.courier.generator.customtypes.CustomInt(99)
    val w = WithPrimitiveCustomTypes(ci1)
    val copied = w.copy(intField = ci2)
    assert(copied.intField === ci2)
  }

  @Test
  def testWithPrimitiveCustomTypes_unapply(): Unit = {
    val ci = org.coursera.courier.generator.customtypes.CustomInt(7)
    val w = WithPrimitiveCustomTypes(ci)
    val WithPrimitiveCustomTypes(c) = w
    assert(c === ci)
  }

  @Test
  def testWithPrimitiveCustomTypes_toString(): Unit = {
    assert(WithPrimitiveCustomTypes(
      org.coursera.courier.generator.customtypes.CustomInt(1)).toString.nonEmpty)
  }
}
