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
import org.coursera.courier.data.IntArray
import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.courier.generator.customtypes.CustomRecord
import org.coursera.courier.generator.customtypes.CustomArrayTestId
import org.coursera.courier.generator.customtypes.CustomMapTestKeyId
import org.coursera.courier.generator.customtypes.CustomMapTestValueId
import org.coursera.courier.generator.customtypes.IntId
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.customtypes.CustomArrayTestIdArray
import org.coursera.customtypes.CustomIntArray
import org.coursera.customtypes.CustomMapTestKeyIdToCustomMapTestValueIdMap
import org.coursera.customtypes.CustomRecordArray
import org.coursera.customtypes.CustomRecordToCustomRecordMap
import org.coursera.customtypes.IntIdArray
import org.coursera.customtypes.IntIdMap
import org.coursera.customtypes.IntIdToStringMap
import org.coursera.arrays.WithCustomTypesArrayUnion
import org.coursera.arrays.WithCustomTypesArrayUnionArray
import org.coursera.arrays.WithRecordArray
import org.coursera.enums.Fruits
import org.coursera.enums.FruitsArray
import org.coursera.enums.FruitsMap
import org.coursera.enums.FruitsToStringMap
import org.coursera.fixed.Fixed8
import org.coursera.fixed.Fixed8Array
import org.coursera.fixed.Fixed8Map
import org.coursera.fixed.Fixed8ToStringMap
import org.coursera.maps.WithComplexTypesMapUnion
import org.coursera.maps.WithComplexTypesMapUnionMap
import org.coursera.records.Message
import org.coursera.records.Note
import org.coursera.records.WithFlatTypedDefinition
import org.coursera.records.WithTypedDefinition
import org.coursera.records.WithUnion
import org.coursera.records.test.{InlineRecord => TestInlineRecord}
import org.coursera.records.test.{Message => TestMessage}
import org.coursera.records.test.Simple
import org.coursera.records.test.SimpleArrayMap
import org.coursera.records.test.SimpleMap
import org.coursera.records.test.WithComplexTypeDefaults
import org.coursera.records.test.WithComplexTypes
import org.coursera.records.test.WithCustomIntWrapper
import org.coursera.records.test.WithOptionalComplexTypeDefaults
import org.coursera.records.test.WithOptionalComplexTypes
import org.coursera.records.test.WithOptionalComplexTypesDefaultNone
import org.coursera.typerefs.FlatTypedDefinition
import org.coursera.typerefs.{InlineRecord => TyperefsInlineRecord}
import org.coursera.typerefs.InlineRecord2
import org.coursera.typerefs.TypedDefinition
import org.coursera.typerefs.UnionWithInlineRecord
import org.coursera.unions.WithEmptyUnion
import org.coursera.unions.WithPrimitiveCustomTypesUnion
import org.coursera.unions.WithPrimitivesUnion
import org.coursera.unions.WithRecordCustomTypeUnion
import org.junit.Test

/**
 * Final coverage tests targeting remaining uncovered code paths.
 */
class RemainingCoverageTest extends GeneratorTest with SchemaFixtures {

  private val simpleRecord = Simple(Some("test"))
  private val customRecord = CustomRecord("title", "body")

  // ---------------------------------------------------------------------------
  // records.Message (org.coursera.records.Message) — 35% coverage
  // ---------------------------------------------------------------------------

  @Test
  def testMessage_equality(): Unit = {
    val a = Message(Some("title"), Some("body"))
    val b = Message(Some("title"), Some("body"))
    val c = Message(None, None)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testMessage_copy(): Unit = {
    val m = Message(Some("title"), Some("body"))
    val copied = m.copy(title = None)
    assert(copied.title === None)
    assert(copied.body === Some("body"))
  }

  @Test
  def testMessage_unapply(): Unit = {
    val m = Message(Some("title"), Some("body"))
    val Message(t, b) = m
    assert(t === Some("title"))
    assert(b === Some("body"))
  }

  @Test
  def testMessage_productArity(): Unit = {
    val m = Message(Some("t"), Some("b"))
    assert(m.productArity === 2)
    assert(m.productElement(0) === Some("t"))
    assert(m.productElement(1) === Some("b"))
  }

  @Test
  def testMessage_productElement_outOfBounds(): Unit = {
    val m = Message(None, None)
    intercept[IndexOutOfBoundsException] {
      m.productElement(2)
    }
  }

  @Test
  def testMessage_toString(): Unit = {
    assert(Message(None, None).toString.nonEmpty)
  }

  @Test
  def testMessage_roundTrip(): Unit = {
    val original = Message(Some("title"), Some("body"))
    val roundTripped = Message.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  // ---------------------------------------------------------------------------
  // WithEmptyUnion — 14% coverage (needs copy/equality/unapply/productArity)
  // ---------------------------------------------------------------------------

  @Test
  def testWithEmptyUnion_equality(): Unit = {
    val unionMap = new DataMap()
    unionMap.put("unknownKey", "val")
    unionMap.makeReadOnly()
    val union = WithEmptyUnion.Union.build(unionMap, DataConversion.SetReadOnly)
    val outerMap = new DataMap()
    outerMap.put("union", unionMap)
    outerMap.makeReadOnly()
    val a = WithEmptyUnion.build(outerMap, DataConversion.SetReadOnly)
    val b = WithEmptyUnion.build(outerMap, DataConversion.SetReadOnly)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithEmptyUnion_unapply(): Unit = {
    val unionMap = new DataMap()
    unionMap.put("unknownKey", "val")
    unionMap.makeReadOnly()
    val outerMap = new DataMap()
    outerMap.put("union", unionMap)
    outerMap.makeReadOnly()
    val w = WithEmptyUnion.build(outerMap, DataConversion.SetReadOnly)
    val WithEmptyUnion(u) = w
    assert(u.isInstanceOf[WithEmptyUnion.Union.$UnknownMember])
  }

  @Test
  def testWithEmptyUnion_copy(): Unit = {
    val unionMap1 = new DataMap()
    unionMap1.put("key1", "val1")
    unionMap1.makeReadOnly()
    val unionMap2 = new DataMap()
    unionMap2.put("key2", "val2")
    unionMap2.makeReadOnly()
    val outerMap = new DataMap()
    outerMap.put("union", unionMap1)
    outerMap.makeReadOnly()
    val w = WithEmptyUnion.build(outerMap, DataConversion.SetReadOnly)
    val union2 = WithEmptyUnion.Union.build(unionMap2, DataConversion.SetReadOnly)
    val copied = w.copy(union = union2)
    assert(copied.union === union2)
  }

  @Test
  def testWithEmptyUnion_productArity(): Unit = {
    val unionMap = new DataMap()
    unionMap.put("unknownKey", "val")
    unionMap.makeReadOnly()
    val outerMap = new DataMap()
    outerMap.put("union", unionMap)
    outerMap.makeReadOnly()
    val w = WithEmptyUnion.build(outerMap, DataConversion.SetReadOnly)
    assert(w.productArity === 1)
    assert(w.productElement(0).isInstanceOf[WithEmptyUnion.Union.$UnknownMember])
  }

  @Test
  def testWithEmptyUnion_productElement_outOfBounds(): Unit = {
    val unionMap = new DataMap()
    unionMap.put("unknownKey", "val")
    unionMap.makeReadOnly()
    val outerMap = new DataMap()
    outerMap.put("union", unionMap)
    outerMap.makeReadOnly()
    val w = WithEmptyUnion.build(outerMap, DataConversion.SetReadOnly)
    intercept[IndexOutOfBoundsException] {
      w.productElement(1)
    }
  }

  @Test
  def testWithEmptyUnion_toString(): Unit = {
    val unionMap = new DataMap()
    unionMap.put("unknownKey", "val")
    unionMap.makeReadOnly()
    val outerMap = new DataMap()
    outerMap.put("union", unionMap)
    outerMap.makeReadOnly()
    val w = WithEmptyUnion.build(outerMap, DataConversion.SetReadOnly)
    assert(w.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // Note / records.Message additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testNote_equality(): Unit = {
    val a = Note("hello")
    val b = Note("hello")
    val c = Note("world")
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testNote_copy(): Unit = {
    val n = Note("original")
    val copied = n.copy(text = "updated")
    assert(copied.text === "updated")
  }

  @Test
  def testNote_unapply(): Unit = {
    val n = Note("abc")
    val Note(t) = n
    assert(t === "abc")
  }

  @Test
  def testNote_productArity(): Unit = {
    val n = Note("x")
    assert(n.productArity === 1)
    assert(n.productElement(0) === "x")
  }

  @Test
  def testNote_productElement_outOfBounds(): Unit = {
    val n = Note("x")
    intercept[IndexOutOfBoundsException] {
      n.productElement(1)
    }
  }

  @Test
  def testNote_toString(): Unit = {
    assert(Note("test").toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // records.test.Simple additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testSimple_equality(): Unit = {
    val a = Simple(Some("x"))
    val b = Simple(Some("x"))
    val c = Simple(None)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testSimple_copy(): Unit = {
    val s = Simple(Some("original"))
    val copied = s.copy(message = Some("updated"))
    assert(copied.message === Some("updated"))
  }

  @Test
  def testSimple_unapply(): Unit = {
    val s = Simple(Some("test"))
    val Simple(m) = s
    assert(m === Some("test"))
  }

  @Test
  def testSimple_productArity(): Unit = {
    val s = Simple(Some("x"))
    assert(s.productArity === 1)
    assert(s.productElement(0) === Some("x"))
  }

  @Test
  def testSimple_productElement_outOfBounds(): Unit = {
    val s = Simple(None)
    intercept[IndexOutOfBoundsException] {
      s.productElement(1)
    }
  }

  @Test
  def testSimple_toString(): Unit = {
    assert(Simple(Some("x")).toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // Array types — productElement (out of bounds) coverage
  // ---------------------------------------------------------------------------

  @Test
  def testFruitsArray_productElement_outOfBounds(): Unit = {
    val arr = FruitsArray(Fruits.APPLE)
    intercept[IndexOutOfBoundsException] {
      arr.productElement(1)
    }
  }

  @Test
  def testCustomIntArray_productElement_outOfBounds(): Unit = {
    val arr = CustomIntArray(CustomInt(1))
    intercept[IndexOutOfBoundsException] {
      arr.productElement(1)
    }
  }

  @Test
  def testIntIdArray_productElement_outOfBounds(): Unit = {
    val arr = IntIdArray(IntId(1))
    intercept[IndexOutOfBoundsException] {
      arr.productElement(1)
    }
  }

  @Test
  def testCustomRecordArray_productElement_outOfBounds(): Unit = {
    val arr = CustomRecordArray(customRecord)
    intercept[IndexOutOfBoundsException] {
      arr.productElement(1)
    }
  }

  @Test
  def testCustomArrayTestIdArray_productElement_outOfBounds(): Unit = {
    val arr = CustomArrayTestIdArray(CustomArrayTestId(1))
    intercept[IndexOutOfBoundsException] {
      arr.productElement(1)
    }
  }

  @Test
  def testFixed8Array_productElement_outOfBounds(): Unit = {
    val arr = Fixed8Array(Fixed8(bytesFixed8))
    intercept[IndexOutOfBoundsException] {
      arr.productElement(1)
    }
  }

  @Test
  def testWithCustomTypesArrayUnionArray_productElement_outOfBounds(): Unit = {
    val arr = WithCustomTypesArrayUnionArray(WithCustomTypesArrayUnion.IntMember(1))
    intercept[IndexOutOfBoundsException] {
      arr.productElement(1)
    }
  }

  // ---------------------------------------------------------------------------
  // Map types — additional operations
  // ---------------------------------------------------------------------------

  @Test
  def testIntIdMap_operations(): Unit = {
    val id = IntId(1)
    val m = IntIdMap("k1" -> id, "k2" -> id)
    assert(m.get("k1") === Some(id))
    assert((m - "k1").size === 1)
    assert((m + ("k3" -> IntId(3))).size === 3)
    assert(m.iterator.size === 2)
    assert(m.isEmpty === false)
  }

  @Test
  def testIntIdToStringMap_operations(): Unit = {
    val id = IntId(1)
    val m = IntIdToStringMap(id -> "v1", IntId(2) -> "v2")
    assert(m.get(id) === Some("v1"))
    assert((m - id).size === 1)
    assert(m.iterator.size === 2)
  }

  @Test
  def testCustomRecordToCustomRecordMap_operations(): Unit = {
    val cr2 = CustomRecord("t2", "b2")
    val m = CustomRecordToCustomRecordMap(customRecord -> cr2)
    assert(m.get(customRecord) === Some(cr2))
    assert(m.iterator.size === 1)
  }

  @Test
  def testCustomMapTestKeyIdToCustomMapTestValueIdMap_operations(): Unit = {
    val k = CustomMapTestKeyId(1)
    val v = CustomMapTestValueId(2)
    val m = CustomMapTestKeyIdToCustomMapTestValueIdMap(k -> v, CustomMapTestKeyId(2) -> CustomMapTestValueId(3))
    assert(m.get(k) === Some(v))
    assert((m - k).size === 1)
    assert(m.iterator.size === 2)
  }

  @Test
  def testFixed8ToStringMap_operations(): Unit = {
    val f = Fixed8(bytesFixed8)
    val m = Fixed8ToStringMap(f -> "v1")
    assert(m.get(f) === Some("v1"))
    assert((m - f).size === 0)
    assert(m.iterator.size === 1)
  }

  // ---------------------------------------------------------------------------
  // WithRecordArray additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testWithRecordArray_equality(): Unit = {
    val empties = org.coursera.records.test.EmptyArray()
    val fruits = FruitsArray()
    val a = WithRecordArray(empties, fruits)
    val b = WithRecordArray(empties, fruits)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithRecordArray_productElement_outOfBounds(): Unit = {
    val arr = WithRecordArray(org.coursera.records.test.EmptyArray(), FruitsArray())
    intercept[IndexOutOfBoundsException] {
      arr.productElement(2)
    }
  }

  @Test
  def testWithRecordArray_toString(): Unit = {
    assert(WithRecordArray(org.coursera.records.test.EmptyArray(), FruitsArray()).toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithPrimitivesUnion additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testWithPrimitivesUnion_equality(): Unit = {
    val a = WithPrimitivesUnion(WithPrimitivesUnion.Union.IntMember(1))
    val b = WithPrimitivesUnion(WithPrimitivesUnion.Union.IntMember(1))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithPrimitivesUnion_copy(): Unit = {
    val original = WithPrimitivesUnion(WithPrimitivesUnion.Union.IntMember(1))
    val copied = original.copy(union = WithPrimitivesUnion.Union.StringMember("hello"))
    assert(copied.union.isInstanceOf[WithPrimitivesUnion.Union.StringMember])
  }

  @Test
  def testWithPrimitivesUnion_unapply(): Unit = {
    val w = WithPrimitivesUnion(WithPrimitivesUnion.Union.IntMember(5))
    val WithPrimitivesUnion(u) = w
    assert(u.isInstanceOf[WithPrimitivesUnion.Union.IntMember])
  }

  @Test
  def testWithPrimitivesUnion_productArity(): Unit = {
    val w = WithPrimitivesUnion(WithPrimitivesUnion.Union.IntMember(1))
    assert(w.productArity === 1)
    assert(w.productElement(0).isInstanceOf[WithPrimitivesUnion.Union.IntMember])
  }

  @Test
  def testWithPrimitivesUnion_productElement_outOfBounds(): Unit = {
    val w = WithPrimitivesUnion(WithPrimitivesUnion.Union.IntMember(1))
    intercept[IndexOutOfBoundsException] {
      w.productElement(1)
    }
  }

  @Test
  def testWithPrimitivesUnion_toString(): Unit = {
    assert(WithPrimitivesUnion(WithPrimitivesUnion.Union.IntMember(1)).toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithPrimitiveCustomTypesUnion additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testWithPrimitiveCustomTypesUnion_copy(): Unit = {
    val ci1 = CustomInt(1)
    val ci2 = CustomInt(99)
    val original = WithPrimitiveCustomTypesUnion(
      WithPrimitiveCustomTypesUnion.Union.CustomIntMember(ci1))
    val copied = original.copy(union =
      WithPrimitiveCustomTypesUnion.Union.CustomIntMember(ci2))
    assert(copied.union.asInstanceOf[
      WithPrimitiveCustomTypesUnion.Union.CustomIntMember].value === ci2)
  }

  @Test
  def testWithPrimitiveCustomTypesUnion_unapply(): Unit = {
    val w = WithPrimitiveCustomTypesUnion(
      WithPrimitiveCustomTypesUnion.Union.CustomIntMember(CustomInt(7)))
    val WithPrimitiveCustomTypesUnion(u) = w
    assert(u.isInstanceOf[WithPrimitiveCustomTypesUnion.Union.CustomIntMember])
  }

  @Test
  def testWithPrimitiveCustomTypesUnion_productArity(): Unit = {
    val w = WithPrimitiveCustomTypesUnion(
      WithPrimitiveCustomTypesUnion.Union.CustomIntMember(CustomInt(1)))
    assert(w.productArity === 1)
    assert(w.productElement(0).isInstanceOf[
      WithPrimitiveCustomTypesUnion.Union.CustomIntMember])
  }

  @Test
  def testWithPrimitiveCustomTypesUnion_productElement_outOfBounds(): Unit = {
    val w = WithPrimitiveCustomTypesUnion(
      WithPrimitiveCustomTypesUnion.Union.CustomIntMember(CustomInt(1)))
    intercept[IndexOutOfBoundsException] {
      w.productElement(1)
    }
  }

  @Test
  def testWithPrimitiveCustomTypesUnion_toString(): Unit = {
    val w = WithPrimitiveCustomTypesUnion(
      WithPrimitiveCustomTypesUnion.Union.CustomIntMember(CustomInt(1)))
    assert(w.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithRecordCustomTypeUnion additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testWithRecordCustomTypeUnion_copy(): Unit = {
    val cr1 = CustomRecord("t1", "b1")
    val cr2 = CustomRecord("t2", "b2")
    val original = WithRecordCustomTypeUnion(
      WithRecordCustomTypeUnion.Union.CustomRecordMember(cr1))
    val copied = original.copy(union =
      WithRecordCustomTypeUnion.Union.CustomRecordMember(cr2))
    assert(copied.union.asInstanceOf[
      WithRecordCustomTypeUnion.Union.CustomRecordMember].value === cr2)
  }

  @Test
  def testWithRecordCustomTypeUnion_unapply(): Unit = {
    val w = WithRecordCustomTypeUnion(
      WithRecordCustomTypeUnion.Union.CustomRecordMember(customRecord))
    val WithRecordCustomTypeUnion(u) = w
    assert(u.isInstanceOf[WithRecordCustomTypeUnion.Union.CustomRecordMember])
  }

  @Test
  def testWithRecordCustomTypeUnion_productArity(): Unit = {
    val w = WithRecordCustomTypeUnion(
      WithRecordCustomTypeUnion.Union.CustomRecordMember(customRecord))
    assert(w.productArity === 1)
  }

  @Test
  def testWithRecordCustomTypeUnion_productElement_outOfBounds(): Unit = {
    val w = WithRecordCustomTypeUnion(
      WithRecordCustomTypeUnion.Union.CustomRecordMember(customRecord))
    intercept[IndexOutOfBoundsException] {
      w.productElement(1)
    }
  }

  @Test
  def testWithRecordCustomTypeUnion_roundTrip(): Unit = {
    val original = WithRecordCustomTypeUnion(
      WithRecordCustomTypeUnion.Union.CustomRecordMember(customRecord))
    val roundTripped =
      WithRecordCustomTypeUnion.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithRecordCustomTypeUnion_toString(): Unit = {
    val w = WithRecordCustomTypeUnion(
      WithRecordCustomTypeUnion.Union.CustomRecordMember(customRecord))
    assert(w.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithFlatTypedDefinition / WithTypedDefinition / WithUnion additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testWithFlatTypedDefinition_equality(): Unit = {
    val a = WithFlatTypedDefinition(FlatTypedDefinition.NoteMember(Note("x")))
    val b = WithFlatTypedDefinition(FlatTypedDefinition.NoteMember(Note("x")))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithFlatTypedDefinition_copy(): Unit = {
    val original = WithFlatTypedDefinition(FlatTypedDefinition.NoteMember(Note("x")))
    val copied = original.copy(value = FlatTypedDefinition.NoteMember(Note("y")))
    assert(copied.value.asInstanceOf[FlatTypedDefinition.NoteMember].value.text === "y")
  }

  @Test
  def testWithFlatTypedDefinition_unapply(): Unit = {
    val w = WithFlatTypedDefinition(FlatTypedDefinition.NoteMember(Note("test")))
    val WithFlatTypedDefinition(v) = w
    assert(v.isInstanceOf[FlatTypedDefinition.NoteMember])
  }

  @Test
  def testWithFlatTypedDefinition_productArity(): Unit = {
    val w = WithFlatTypedDefinition(FlatTypedDefinition.NoteMember(Note("x")))
    assert(w.productArity === 1)
    assert(w.productElement(0).isInstanceOf[FlatTypedDefinition.NoteMember])
  }

  @Test
  def testWithFlatTypedDefinition_productElement_outOfBounds(): Unit = {
    val w = WithFlatTypedDefinition(FlatTypedDefinition.NoteMember(Note("x")))
    intercept[IndexOutOfBoundsException] {
      w.productElement(1)
    }
  }

  @Test
  def testWithFlatTypedDefinition_toString(): Unit = {
    val w = WithFlatTypedDefinition(FlatTypedDefinition.NoteMember(Note("x")))
    assert(w.toString.nonEmpty)
  }

  @Test
  def testWithTypedDefinition_equality(): Unit = {
    val a = WithTypedDefinition(TypedDefinition.NoteMember(Note("x")))
    val b = WithTypedDefinition(TypedDefinition.NoteMember(Note("x")))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithTypedDefinition_copy(): Unit = {
    val original = WithTypedDefinition(TypedDefinition.NoteMember(Note("x")))
    val copied = original.copy(value = TypedDefinition.NoteMember(Note("y")))
    assert(copied.value.asInstanceOf[TypedDefinition.NoteMember].value.text === "y")
  }

  @Test
  def testWithTypedDefinition_unapply(): Unit = {
    val w = WithTypedDefinition(TypedDefinition.NoteMember(Note("test")))
    val WithTypedDefinition(v) = w
    assert(v.isInstanceOf[TypedDefinition.NoteMember])
  }

  @Test
  def testWithTypedDefinition_productArity(): Unit = {
    val w = WithTypedDefinition(TypedDefinition.NoteMember(Note("x")))
    assert(w.productArity === 1)
  }

  @Test
  def testWithTypedDefinition_productElement_outOfBounds(): Unit = {
    val w = WithTypedDefinition(TypedDefinition.NoteMember(Note("x")))
    intercept[IndexOutOfBoundsException] {
      w.productElement(1)
    }
  }

  @Test
  def testWithTypedDefinition_toString(): Unit = {
    val w = WithTypedDefinition(TypedDefinition.NoteMember(Note("x")))
    assert(w.toString.nonEmpty)
  }

  @Test
  def testWithUnion_equality(): Unit = {
    val n = Note("x")
    val a = WithUnion(org.coursera.typerefs.Union.NoteMember(n))
    val b = WithUnion(org.coursera.typerefs.Union.NoteMember(n))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithUnion_unapply(): Unit = {
    val w = WithUnion(org.coursera.typerefs.Union.NoteMember(Note("x")))
    val WithUnion(v) = w
    assert(v.isInstanceOf[org.coursera.typerefs.Union.NoteMember])
  }

  @Test
  def testWithUnion_productArity(): Unit = {
    val w = WithUnion(org.coursera.typerefs.Union.NoteMember(Note("x")))
    assert(w.productArity === 1)
  }

  @Test
  def testWithUnion_productElement_outOfBounds(): Unit = {
    val w = WithUnion(org.coursera.typerefs.Union.NoteMember(Note("x")))
    intercept[IndexOutOfBoundsException] {
      w.productElement(1)
    }
  }

  @Test
  def testWithUnion_toString(): Unit = {
    val w = WithUnion(org.coursera.typerefs.Union.NoteMember(Note("x")))
    assert(w.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // typerefs.InlineRecord / InlineRecord2 additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testTyperefsInlineRecord_equality(): Unit = {
    val a = TyperefsInlineRecord(Some(1))
    val b = TyperefsInlineRecord(Some(1))
    val c = TyperefsInlineRecord(None)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testTyperefsInlineRecord_productArity(): Unit = {
    val r = TyperefsInlineRecord(Some(5))
    assert(r.productArity === 1)
    assert(r.productElement(0) === Some(5))
  }

  @Test
  def testTyperefsInlineRecord_productElement_outOfBounds(): Unit = {
    val r = TyperefsInlineRecord(None)
    intercept[IndexOutOfBoundsException] {
      r.productElement(1)
    }
  }

  @Test
  def testTyperefsInlineRecord_toString(): Unit = {
    assert(TyperefsInlineRecord(Some(1)).toString.nonEmpty)
  }

  @Test
  def testInlineRecord2_equality(): Unit = {
    val a = InlineRecord2()
    val b = InlineRecord2()
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testInlineRecord2_unapply(): Unit = {
    val r = InlineRecord2()
    assert(InlineRecord2.unapply(r) === true)
  }

  @Test
  def testInlineRecord2_productArity(): Unit = {
    val r = InlineRecord2()
    assert(r.productArity === 0)
  }

  @Test
  def testInlineRecord2_productElement_outOfBounds(): Unit = {
    val r = InlineRecord2()
    intercept[IndexOutOfBoundsException] {
      r.productElement(0)
    }
  }

  @Test
  def testInlineRecord2_toString(): Unit = {
    assert(InlineRecord2().toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithOptionalComplexTypeDefaults.Union.StringMember additional
  // ---------------------------------------------------------------------------

  @Test
  def testWithOptionalComplexTypeDefaults_union_stringMember(): Unit = {
    val member = WithOptionalComplexTypeDefaults.Union.StringMember("hello")
    assert(member.value === "hello")
    assert(member._1 === "hello")
  }

  @Test
  def testWithOptionalComplexTypeDefaults_union_stringMember_equality(): Unit = {
    val a = WithOptionalComplexTypeDefaults.Union.StringMember("x")
    val b = WithOptionalComplexTypeDefaults.Union.StringMember("x")
    val c = WithOptionalComplexTypeDefaults.Union.StringMember("y")
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithOptionalComplexTypeDefaults_union_stringMember_unapply(): Unit = {
    val m = WithOptionalComplexTypeDefaults.Union.StringMember("test")
    val WithOptionalComplexTypeDefaults.Union.StringMember(v) = m
    assert(v === "test")
  }

  @Test
  def testWithOptionalComplexTypeDefaults_union_stringMember_toString(): Unit = {
    assert(WithOptionalComplexTypeDefaults.Union.StringMember("x").toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithComplexTypeDefaults.Union.StringMember / SimpleMember additional
  // ---------------------------------------------------------------------------

  @Test
  def testWithComplexTypeDefaults_union_stringMember_equality(): Unit = {
    val a = WithComplexTypeDefaults.Union.StringMember("x")
    val b = WithComplexTypeDefaults.Union.StringMember("x")
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithComplexTypeDefaults_union_stringMember_unapply(): Unit = {
    val m = WithComplexTypeDefaults.Union.StringMember("test")
    val WithComplexTypeDefaults.Union.StringMember(v) = m
    assert(v === "test")
  }

  @Test
  def testWithComplexTypeDefaults_union_simpleMember_equality(): Unit = {
    val a = WithComplexTypeDefaults.Union.SimpleMember(simpleRecord)
    val b = WithComplexTypeDefaults.Union.SimpleMember(simpleRecord)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithComplexTypes_union_stringMember_equality(): Unit = {
    val a = WithComplexTypes.Union.StringMember("x")
    val b = WithComplexTypes.Union.StringMember("x")
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithComplexTypes_union_simpleMember_equality(): Unit = {
    val a = WithComplexTypes.Union.SimpleMember(simpleRecord)
    val b = WithComplexTypes.Union.SimpleMember(simpleRecord)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  // ---------------------------------------------------------------------------
  // WithOptionalComplexTypes.Union additional
  // ---------------------------------------------------------------------------

  @Test
  def testWithOptionalComplexTypes_union_stringMember_equality(): Unit = {
    val a = WithOptionalComplexTypes.Union.StringMember("x")
    val b = WithOptionalComplexTypes.Union.StringMember("x")
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithOptionalComplexTypes_union_simpleMember_equality(): Unit = {
    val a = WithOptionalComplexTypes.Union.SimpleMember(simpleRecord)
    val b = WithOptionalComplexTypes.Union.SimpleMember(simpleRecord)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_union_stringMember_equality(): Unit = {
    val a = WithOptionalComplexTypesDefaultNone.Union.StringMember("x")
    val b = WithOptionalComplexTypesDefaultNone.Union.StringMember("x")
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_union_simpleMember_equality(): Unit = {
    val a = WithOptionalComplexTypesDefaultNone.Union.SimpleMember(simpleRecord)
    val b = WithOptionalComplexTypesDefaultNone.Union.SimpleMember(simpleRecord)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_union_intMember_equality(): Unit = {
    val a = WithOptionalComplexTypesDefaultNone.Union.IntMember(1)
    val b = WithOptionalComplexTypesDefaultNone.Union.IntMember(1)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  // ---------------------------------------------------------------------------
  // WithCustomIntWrapper additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testWithCustomIntWrapper_equality(): Unit = {
    val wrapper = org.coursera.courier.generator.customtypes.CustomIntWrapper(
      org.coursera.courier.generator.customtypes.CustomInt(1))
    val a = WithCustomIntWrapper(wrapper)
    val b = WithCustomIntWrapper(wrapper)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithCustomIntWrapper_copy(): Unit = {
    val w1 = org.coursera.courier.generator.customtypes.CustomIntWrapper(CustomInt(1))
    val w2 = org.coursera.courier.generator.customtypes.CustomIntWrapper(CustomInt(2))
    val r = WithCustomIntWrapper(w1)
    val copied = r.copy(wrapper = w2)
    assert(copied.wrapper === w2)
  }

  @Test
  def testWithCustomIntWrapper_unapply(): Unit = {
    val wrapper = org.coursera.courier.generator.customtypes.CustomIntWrapper(CustomInt(5))
    val r = WithCustomIntWrapper(wrapper)
    val WithCustomIntWrapper(w) = r
    assert(w === wrapper)
  }

  @Test
  def testWithCustomIntWrapper_roundTrip(): Unit = {
    val wrapper = org.coursera.courier.generator.customtypes.CustomIntWrapper(CustomInt(3))
    val original = WithCustomIntWrapper(wrapper)
    val roundTripped =
      WithCustomIntWrapper.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithCustomIntWrapper_toString(): Unit = {
    val wrapper = org.coursera.courier.generator.customtypes.CustomIntWrapper(CustomInt(1))
    assert(WithCustomIntWrapper(wrapper).toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // SimpleArrayMap additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleArrayMap_operations(): Unit = {
    val arr = org.coursera.records.test.SimpleArray(Simple(Some("x")))
    val m = SimpleArrayMap("k1" -> arr, "k2" -> arr)
    assert(m.get("k1") === Some(arr))
    assert((m - "k1").size === 1)
    assert(m.iterator.size === 2)
  }

  @Test
  def testSimpleArrayMap_toString(): Unit = {
    val arr = org.coursera.records.test.SimpleArray(Simple(Some("x")))
    assert(SimpleArrayMap("k" -> arr).toString.nonEmpty)
  }
}
