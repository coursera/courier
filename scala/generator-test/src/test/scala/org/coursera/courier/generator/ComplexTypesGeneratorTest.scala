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
import org.coursera.courier.data.IntMap
import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.enums.Fruits
import org.coursera.records.JsonTest
import org.coursera.records.WithInclude
import org.coursera.records.{`class` => RecordsClass}
import org.coursera.records.test.Simple
import org.coursera.records.test.SimpleMap
import org.coursera.records.test.WithComplexTypeDefaults
import org.coursera.records.test.WithComplexTypes
import org.coursera.records.test.WithOptionalComplexTypeDefaults
import org.coursera.records.test.WithOptionalComplexTypes
import org.coursera.records.test.WithOptionalComplexTypesDefaultNone
import org.coursera.records.test.WithUnionWithInlineRecord
import org.coursera.typerefs.InlineRecord
import org.coursera.typerefs.UnionWithInlineRecord
import org.coursera.typerefs.UnionTyperef
import org.coursera.unions.WithEmptyUnion
import org.example.TyperefExample
import org.junit.Test

/**
 * Tests for complex record types with required/optional complex fields, nested unions,
 * and records with special names.
 */
class ComplexTypesGeneratorTest extends GeneratorTest with SchemaFixtures {

  // Helpers
  private val simpleRecord = Simple(Some("test"))
  private val intUnionMember = WithComplexTypes.Union.IntMember(42)

  // ---------------------------------------------------------------------------
  // org.coursera.records.`class` (keyword-named record)
  // ---------------------------------------------------------------------------

  @Test
  def testRecordsClass_construction(): Unit = {
    val r = RecordsClass(`private` = "hello")
    assert(r.`private` === "hello")
  }

  @Test
  def testRecordsClass_roundTrip(): Unit = {
    val original = RecordsClass("test")
    val roundTripped = RecordsClass.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testRecordsClass_equality(): Unit = {
    val a = RecordsClass("x")
    val b = RecordsClass("x")
    val c = RecordsClass("y")
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testRecordsClass_copy(): Unit = {
    val r = RecordsClass("original")
    val copied = r.copy(`private` = "updated")
    assert(copied.`private` === "updated")
  }

  @Test
  def testRecordsClass_unapply(): Unit = {
    val r = RecordsClass("abc")
    val RecordsClass(p) = r
    assert(p === "abc")
  }

  @Test
  def testRecordsClass_productArity(): Unit = {
    val r = RecordsClass("val")
    assert(r.productArity === 1)
    assert(r.productElement(0) === "val")
  }

  @Test
  def testRecordsClass_toString(): Unit = {
    val r = RecordsClass("str")
    assert(r.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // JsonTest (empty record)
  // ---------------------------------------------------------------------------

  @Test
  def testJsonTest_construction(): Unit = {
    val r = JsonTest()
    assert(r.productArity === 0)
  }

  @Test
  def testJsonTest_roundTrip(): Unit = {
    val original = JsonTest()
    val roundTripped = JsonTest.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testJsonTest_equality(): Unit = {
    val a = JsonTest()
    val b = JsonTest()
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testJsonTest_unapply(): Unit = {
    val r = JsonTest()
    assert(JsonTest.unapply(r) === true)
  }

  @Test
  def testJsonTest_toString(): Unit = {
    val r = JsonTest()
    assert(r.toString.nonEmpty)
  }

  @Test
  def testJsonTest_productElement_outOfBounds(): Unit = {
    val r = JsonTest()
    intercept[IndexOutOfBoundsException] {
      r.productElement(0)
    }
  }

  // ---------------------------------------------------------------------------
  // WithInclude
  // ---------------------------------------------------------------------------

  @Test
  def testWithInclude_construction(): Unit = {
    val r = WithInclude(find = "hello", direct = 42)
    assert(r.find === "hello")
    assert(r.direct === 42)
  }

  @Test
  def testWithInclude_roundTrip(): Unit = {
    val original = WithInclude("msg", 10)
    val roundTripped = WithInclude.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithInclude_equality(): Unit = {
    val a = WithInclude("x", 1)
    val b = WithInclude("x", 1)
    val c = WithInclude("y", 1)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithInclude_copy(): Unit = {
    val r = WithInclude("original", 5)
    val copied = r.copy(find = "updated")
    assert(copied.find === "updated")
    assert(copied.direct === 5)
  }

  @Test
  def testWithInclude_unapply(): Unit = {
    val r = WithInclude("abc", 7)
    val WithInclude(f, d) = r
    assert(f === "abc")
    assert(d === 7)
  }

  @Test
  def testWithInclude_toString(): Unit = {
    val r = WithInclude("hello", 1)
    assert(r.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithComplexTypes
  // ---------------------------------------------------------------------------

  @Test
  def testWithComplexTypes_construction(): Unit = {
    val w = WithComplexTypes(
      record = simpleRecord,
      enum = Fruits.APPLE,
      union = intUnionMember,
      array = IntArray(1, 2),
      map = IntMap("a" -> 1),
      complexMap = SimpleMap("k" -> simpleRecord),
      custom = CustomInt(5)
    )
    assert(w.record === simpleRecord)
    assert(w.enum === Fruits.APPLE)
    assert(w.custom === CustomInt(5))
  }

  @Test
  def testWithComplexTypes_roundTrip(): Unit = {
    val original = WithComplexTypes(
      simpleRecord, Fruits.APPLE, intUnionMember,
      IntArray(1), IntMap("a" -> 1), SimpleMap("k" -> simpleRecord), CustomInt(5)
    )
    val roundTripped =
      WithComplexTypes.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithComplexTypes_equality(): Unit = {
    val a = WithComplexTypes(
      simpleRecord, Fruits.APPLE, intUnionMember,
      IntArray(1), IntMap("a" -> 1), SimpleMap("k" -> simpleRecord), CustomInt(5)
    )
    val b = WithComplexTypes(
      simpleRecord, Fruits.APPLE, intUnionMember,
      IntArray(1), IntMap("a" -> 1), SimpleMap("k" -> simpleRecord), CustomInt(5)
    )
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithComplexTypes_union_intMember(): Unit = {
    val member = WithComplexTypes.Union.IntMember(99)
    assert(member.value === 99)
    assert(member._1 === 99)
  }

  @Test
  def testWithComplexTypes_union_stringMember(): Unit = {
    val member = WithComplexTypes.Union.StringMember("hello")
    assert(member.value === "hello")
  }

  @Test
  def testWithComplexTypes_union_simpleMember(): Unit = {
    val member = WithComplexTypes.Union.SimpleMember(simpleRecord)
    assert(member.value === simpleRecord)
  }

  @Test
  def testWithComplexTypes_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "val")
    dataMap.makeReadOnly()
    val built = WithComplexTypes.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithComplexTypes.Union.$UnknownMember])
  }

  // ---------------------------------------------------------------------------
  // WithComplexTypeDefaults (has default values, can construct with no args)
  // ---------------------------------------------------------------------------

  @Test
  def testWithComplexTypeDefaults_defaults(): Unit = {
    val w = WithComplexTypeDefaults()
    assert(w.enum === Fruits.APPLE)
    assert(w.custom === CustomInt(1))
    assert(w.array === IntArray(1))
  }

  @Test
  def testWithComplexTypeDefaults_roundTrip(): Unit = {
    val original = WithComplexTypeDefaults()
    val roundTripped =
      WithComplexTypeDefaults.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithComplexTypeDefaults_equality(): Unit = {
    val a = WithComplexTypeDefaults()
    val b = WithComplexTypeDefaults()
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithComplexTypeDefaults_union_intMember(): Unit = {
    val member = WithComplexTypeDefaults.Union.IntMember(7)
    assert(member.value === 7)
  }

  @Test
  def testWithComplexTypeDefaults_union_stringMember(): Unit = {
    val member = WithComplexTypeDefaults.Union.StringMember("s")
    assert(member.value === "s")
  }

  @Test
  def testWithComplexTypeDefaults_union_simpleMember(): Unit = {
    val member = WithComplexTypeDefaults.Union.SimpleMember(simpleRecord)
    assert(member.value === simpleRecord)
  }

  @Test
  def testWithComplexTypeDefaults_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "val")
    dataMap.makeReadOnly()
    val built = WithComplexTypeDefaults.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithComplexTypeDefaults.Union.$UnknownMember])
  }

  // ---------------------------------------------------------------------------
  // WithOptionalComplexTypes (all optional fields)
  // ---------------------------------------------------------------------------

  @Test
  def testWithOptionalComplexTypes_empty(): Unit = {
    val w = WithOptionalComplexTypes()
    assert(w.record === None)
    assert(w.enum === None)
    assert(w.union === None)
    assert(w.array === None)
    assert(w.map === None)
    assert(w.complexMap === None)
    assert(w.custom === None)
  }

  @Test
  def testWithOptionalComplexTypes_withValues(): Unit = {
    val w = WithOptionalComplexTypes(
      record = Some(simpleRecord),
      enum = Some(Fruits.APPLE),
      union = Some(WithOptionalComplexTypes.Union.IntMember(1)),
      array = Some(IntArray(1)),
      map = Some(IntMap("a" -> 1)),
      complexMap = Some(SimpleMap("k" -> simpleRecord)),
      custom = Some(CustomInt(5))
    )
    assert(w.record === Some(simpleRecord))
    assert(w.enum === Some(Fruits.APPLE))
    assert(w.custom === Some(CustomInt(5)))
  }

  @Test
  def testWithOptionalComplexTypes_roundTrip_withValues(): Unit = {
    val original = WithOptionalComplexTypes(
      record = Some(simpleRecord),
      enum = Some(Fruits.APPLE),
      union = Some(WithOptionalComplexTypes.Union.IntMember(1)),
      array = Some(IntArray(1)),
      map = Some(IntMap("a" -> 1)),
      complexMap = Some(SimpleMap("k" -> simpleRecord)),
      custom = Some(CustomInt(5))
    )
    val roundTripped =
      WithOptionalComplexTypes.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithOptionalComplexTypes_roundTrip_empty(): Unit = {
    val original = WithOptionalComplexTypes()
    val roundTripped =
      WithOptionalComplexTypes.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithOptionalComplexTypes_union_intMember(): Unit = {
    val member = WithOptionalComplexTypes.Union.IntMember(42)
    assert(member.value === 42)
  }

  @Test
  def testWithOptionalComplexTypes_union_stringMember(): Unit = {
    val member = WithOptionalComplexTypes.Union.StringMember("s")
    assert(member.value === "s")
  }

  @Test
  def testWithOptionalComplexTypes_union_simpleMember(): Unit = {
    val member = WithOptionalComplexTypes.Union.SimpleMember(simpleRecord)
    assert(member.value === simpleRecord)
  }

  @Test
  def testWithOptionalComplexTypes_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "val")
    dataMap.makeReadOnly()
    val built = WithOptionalComplexTypes.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithOptionalComplexTypes.Union.$UnknownMember])
  }

  @Test
  def testWithOptionalComplexTypes_equality(): Unit = {
    val a = WithOptionalComplexTypes(record = Some(simpleRecord))
    val b = WithOptionalComplexTypes(record = Some(simpleRecord))
    val c = WithOptionalComplexTypes()
    assert(a === b)
    assert(a !== c)
  }

  @Test
  def testWithOptionalComplexTypes_copy(): Unit = {
    val w = WithOptionalComplexTypes()
    val copied = w.copy(record = Some(simpleRecord))
    assert(copied.record === Some(simpleRecord))
  }

  // ---------------------------------------------------------------------------
  // WithOptionalComplexTypeDefaults
  // ---------------------------------------------------------------------------

  @Test
  def testWithOptionalComplexTypeDefaults_defaults(): Unit = {
    val w = WithOptionalComplexTypeDefaults()
    assert(w.enum === Some(Fruits.APPLE))
    assert(w.custom === Some(CustomInt(1)))
  }

  @Test
  def testWithOptionalComplexTypeDefaults_roundTrip(): Unit = {
    val original = WithOptionalComplexTypeDefaults()
    val roundTripped =
      WithOptionalComplexTypeDefaults.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithOptionalComplexTypeDefaults_equality(): Unit = {
    val a = WithOptionalComplexTypeDefaults()
    val b = WithOptionalComplexTypeDefaults()
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithOptionalComplexTypeDefaults_union_intMember(): Unit = {
    val member = WithOptionalComplexTypeDefaults.Union.IntMember(3)
    assert(member.value === 3)
  }

  @Test
  def testWithOptionalComplexTypeDefaults_union_simpleMember(): Unit = {
    val member = WithOptionalComplexTypeDefaults.Union.SimpleMember(simpleRecord)
    assert(member.value === simpleRecord)
  }

  @Test
  def testWithOptionalComplexTypeDefaults_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "val")
    dataMap.makeReadOnly()
    val built = WithOptionalComplexTypeDefaults.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithOptionalComplexTypeDefaults.Union.$UnknownMember])
  }

  // ---------------------------------------------------------------------------
  // WithOptionalComplexTypesDefaultNone
  // ---------------------------------------------------------------------------

  @Test
  def testWithOptionalComplexTypesDefaultNone_empty(): Unit = {
    val w = WithOptionalComplexTypesDefaultNone()
    assert(w.record === None)
    assert(w.enum === None)
    assert(w.union === None)
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_withValues(): Unit = {
    val w = WithOptionalComplexTypesDefaultNone(
      record = Some(simpleRecord),
      enum = Some(Fruits.APPLE),
      union = Some(WithOptionalComplexTypesDefaultNone.Union.IntMember(1)),
      array = Some(IntArray(1)),
      map = Some(IntMap("a" -> 1)),
      complexMap = Some(SimpleMap("k" -> simpleRecord)),
      custom = Some(CustomInt(5))
    )
    assert(w.record === Some(simpleRecord))
    assert(w.enum === Some(Fruits.APPLE))
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_roundTrip(): Unit = {
    val original = WithOptionalComplexTypesDefaultNone(
      record = Some(simpleRecord),
      enum = Some(Fruits.APPLE),
      union = Some(WithOptionalComplexTypesDefaultNone.Union.StringMember("hello")),
      array = Some(IntArray(1)),
      map = Some(IntMap("a" -> 1)),
      complexMap = Some(SimpleMap("k" -> simpleRecord)),
      custom = Some(CustomInt(5))
    )
    val roundTripped =
      WithOptionalComplexTypesDefaultNone.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_union_intMember(): Unit = {
    val member = WithOptionalComplexTypesDefaultNone.Union.IntMember(10)
    assert(member.value === 10)
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_union_stringMember(): Unit = {
    val member = WithOptionalComplexTypesDefaultNone.Union.StringMember("str")
    assert(member.value === "str")
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_union_simpleMember(): Unit = {
    val member = WithOptionalComplexTypesDefaultNone.Union.SimpleMember(simpleRecord)
    assert(member.value === simpleRecord)
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "val")
    dataMap.makeReadOnly()
    val built = WithOptionalComplexTypesDefaultNone.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithOptionalComplexTypesDefaultNone.Union.$UnknownMember])
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_equality(): Unit = {
    val a = WithOptionalComplexTypesDefaultNone()
    val b = WithOptionalComplexTypesDefaultNone()
    assert(a === b)
  }

  // ---------------------------------------------------------------------------
  // WithUnionWithInlineRecord
  // ---------------------------------------------------------------------------

  @Test
  def testWithUnionWithInlineRecord_construction(): Unit = {
    val ir = InlineRecord(Some(5))
    val union = UnionWithInlineRecord.InlineRecordMember(ir)
    val w = WithUnionWithInlineRecord(value = union)
    assert(w.value.isInstanceOf[UnionWithInlineRecord.InlineRecordMember])
  }

  @Test
  def testWithUnionWithInlineRecord_roundTrip(): Unit = {
    val ir = InlineRecord(Some(7))
    val union = UnionWithInlineRecord.InlineRecordMember(ir)
    val original = WithUnionWithInlineRecord(union)
    val roundTripped =
      WithUnionWithInlineRecord.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithUnionWithInlineRecord_equality(): Unit = {
    val ir = InlineRecord(Some(3))
    val a = WithUnionWithInlineRecord(UnionWithInlineRecord.InlineRecordMember(ir))
    val b = WithUnionWithInlineRecord(UnionWithInlineRecord.InlineRecordMember(ir))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithUnionWithInlineRecord_copy(): Unit = {
    val original = WithUnionWithInlineRecord(UnionWithInlineRecord.InlineRecordMember(InlineRecord(Some(1))))
    val copied = original.copy(value = UnionWithInlineRecord.InlineRecordMember(InlineRecord(Some(99))))
    assert(copied.value.asInstanceOf[UnionWithInlineRecord.InlineRecordMember].value.value === Some(99))
  }

  @Test
  def testWithUnionWithInlineRecord_unapply(): Unit = {
    val ir = InlineRecord(Some(10))
    val w = WithUnionWithInlineRecord(UnionWithInlineRecord.InlineRecordMember(ir))
    val WithUnionWithInlineRecord(v) = w
    assert(v.isInstanceOf[UnionWithInlineRecord.InlineRecordMember])
  }

  @Test
  def testWithUnionWithInlineRecord_toString(): Unit = {
    val w = WithUnionWithInlineRecord(UnionWithInlineRecord.InlineRecordMember(InlineRecord(Some(1))))
    assert(w.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithEmptyUnion (union with only $UnknownMember)
  // ---------------------------------------------------------------------------

  @Test
  def testWithEmptyUnion_unknownMember(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("someKey", "someVal")
    dataMap.makeReadOnly()
    val union = WithEmptyUnion.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(union.isInstanceOf[WithEmptyUnion.Union.$UnknownMember])
  }

  @Test
  def testWithEmptyUnion_build_withUnknown(): Unit = {
    val unionMap = new DataMap()
    unionMap.put("unknownKey", "val")
    unionMap.makeReadOnly()
    val union = WithEmptyUnion.Union.build(unionMap, DataConversion.SetReadOnly)
    val outer = new DataMap()
    outer.put("union", unionMap)
    outer.makeReadOnly()
    val w = WithEmptyUnion.build(outer, DataConversion.SetReadOnly)
    assert(w.union.isInstanceOf[WithEmptyUnion.Union.$UnknownMember])
  }

  // ---------------------------------------------------------------------------
  // TyperefExample
  // ---------------------------------------------------------------------------

  @Test
  def testTyperefExample_construction(): Unit = {
    val t = TyperefExample(time = 1000L)
    assert(t.time === 1000L)
  }

  @Test
  def testTyperefExample_defaultValue(): Unit = {
    val t = TyperefExample()
    assert(t.time === 1430849546000L)
  }

  @Test
  def testTyperefExample_roundTrip(): Unit = {
    val original = TyperefExample(2000L)
    val roundTripped = TyperefExample.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testTyperefExample_equality(): Unit = {
    val a = TyperefExample(100L)
    val b = TyperefExample(100L)
    val c = TyperefExample(200L)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testTyperefExample_copy(): Unit = {
    val t = TyperefExample(100L)
    val copied = t.copy(time = 200L)
    assert(copied.time === 200L)
  }

  @Test
  def testTyperefExample_unapply(): Unit = {
    val t = TyperefExample(999L)
    val TyperefExample(time) = t
    assert(time === 999L)
  }

  @Test
  def testTyperefExample_toString(): Unit = {
    val t = TyperefExample(1L)
    assert(t.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // UnionTyperef
  // ---------------------------------------------------------------------------

  @Test
  def testUnionTyperef_stringMember(): Unit = {
    val member = UnionTyperef.StringMember("hello")
    assert(member.value === "hello")
    assert(member._1 === "hello")
  }

  @Test
  def testUnionTyperef_intMember(): Unit = {
    val member = UnionTyperef.IntMember(42)
    assert(member.value === 42)
  }

  @Test
  def testUnionTyperef_build_stringMember(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("string", "testval")
    dataMap.makeReadOnly()
    val built = UnionTyperef.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[UnionTyperef.StringMember])
    assert(built.asInstanceOf[UnionTyperef.StringMember].value === "testval")
  }

  @Test
  def testUnionTyperef_build_intMember(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("int", 99)
    dataMap.makeReadOnly()
    val built = UnionTyperef.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[UnionTyperef.IntMember])
    assert(built.asInstanceOf[UnionTyperef.IntMember].value === 99)
  }

  @Test
  def testUnionTyperef_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownKey", "val")
    dataMap.makeReadOnly()
    val built = UnionTyperef.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[UnionTyperef.$UnknownMember])
  }

  @Test
  def testUnionTyperef_equality(): Unit = {
    val a = UnionTyperef.StringMember("x")
    val b = UnionTyperef.StringMember("x")
    val c = UnionTyperef.IntMember(1)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testUnionTyperef_toString(): Unit = {
    val m = UnionTyperef.StringMember("test")
    assert(m.toString.nonEmpty)
  }
}
