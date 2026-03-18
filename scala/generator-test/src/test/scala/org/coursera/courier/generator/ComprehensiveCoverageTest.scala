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
import org.coursera.arrays.WithCustomTypesArray
import org.coursera.arrays.WithPrimitivesArray
import org.coursera.arrays.WithRecordArray
import org.coursera.courier.data.BooleanArray
import org.coursera.courier.data.BooleanMap
import org.coursera.courier.data.ByteStringToStringMap
import org.coursera.courier.data.BytesArray
import org.coursera.courier.data.BytesMap
import org.coursera.courier.data.DoubleArray
import org.coursera.courier.data.DoubleMap
import org.coursera.courier.data.DoubleToStringMap
import org.coursera.courier.data.FloatArray
import org.coursera.courier.data.FloatMap
import org.coursera.courier.data.FloatToStringMap
import org.coursera.courier.data.IntArray
import org.coursera.courier.data.IntArrayToStringMap
import org.coursera.courier.data.IntMap
import org.coursera.courier.data.IntToStringMap
import org.coursera.courier.data.LongArray
import org.coursera.courier.data.LongMap
import org.coursera.courier.data.LongToStringMap
import org.coursera.courier.data.StringArray
import org.coursera.courier.data.StringMap
import org.coursera.courier.generator.customtypes.BooleanId
import org.coursera.courier.generator.customtypes.BoxedIntId
import org.coursera.courier.generator.customtypes.ByteId
import org.coursera.courier.generator.customtypes.CaseClassCustomIntWrapper
import org.coursera.courier.generator.customtypes.CharId
import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.courier.generator.customtypes.CustomRecord
import org.coursera.courier.generator.customtypes.DateTimeCoercer
import org.coursera.courier.generator.customtypes.DoubleId
import org.coursera.courier.generator.customtypes.FloatId
import org.coursera.courier.generator.customtypes.IntId
import org.coursera.courier.generator.customtypes.LongId
import org.coursera.courier.generator.customtypes.ShortId
import org.coursera.courier.generator.customtypes.StringId
import org.coursera.courier.generator.customtypes.StringIdWrapper
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.customtypes.CustomArrayTestIdArray
import org.coursera.customtypes.CustomIntArray
import org.coursera.customtypes.CustomIntMap
import org.coursera.customtypes.CustomIntToStringMap
import org.coursera.customtypes.CustomRecordArray
import org.coursera.customtypes.CustomRecordToCustomRecordMap
import org.coursera.customtypes.IntIdArray
import org.coursera.customtypes.IntIdMap
import org.coursera.customtypes.IntIdToStringMap
import org.coursera.deprecated.DeprecatedRecord
import org.coursera.enums.EmptyEnum
import org.coursera.enums.EnumProperties
import org.coursera.enums.Fruits
import org.coursera.enums.FruitsArray
import org.coursera.enums.FruitsMap
import org.coursera.enums.FruitsToStringMap
import org.coursera.escaping.DefaultLiteralEscaping
import org.coursera.escaping.ReservedClassFieldEscaping
import org.coursera.fixed.Fixed8
import org.coursera.fixed.Fixed8Array
import org.coursera.fixed.Fixed8Map
import org.coursera.fixed.Fixed8ToStringMap
import org.coursera.maps.Toggle
import org.coursera.maps.ToggleToStringMap
import org.coursera.maps.WithComplexTypesMap
import org.coursera.maps.WithComplexTypesMapUnion
import org.coursera.maps.WithComplexTypesMapUnionMap
import org.coursera.maps.WithCustomTypesMap
import org.coursera.maps.WithPrimitivesMap
import org.coursera.maps.WithTypedKeyMap
import org.coursera.records.CourierFile
import org.coursera.records.Message
import org.coursera.records.Note
import org.coursera.records.WithFlatTypedDefinition
import org.coursera.records.WithInclude
import org.coursera.records.WithTypedDefinition
import org.coursera.records.WithUnion
import org.coursera.records.test.{InlineRecord => TestInlineRecord}
import org.coursera.records.test.{Message => TestMessage}
import org.coursera.records.test.Empty
import org.coursera.records.test.EmptyArray
import org.coursera.records.test.EmptyMap
import org.coursera.records.test.InlineOptionalRecord
import org.coursera.records.test.NumericDefaults
import org.coursera.records.test.RecursivelyDefinedRecord
import org.coursera.records.test.Simple
import org.coursera.records.test.SimpleArray
import org.coursera.records.test.SimpleArrayArray
import org.coursera.records.test.SimpleArrayMap
import org.coursera.records.test.SimpleMap
import org.coursera.records.test.SimpleMapArray
import org.coursera.records.test.SimpleMapMap
import org.coursera.records.test.SimpleToStringMap
import org.coursera.records.test.WithCaseClassCustomType
import org.coursera.records.test.WithComplexTypeDefaults
import org.coursera.records.test.WithComplexTypes
import org.coursera.records.test.WithComplexTyperefs
import org.coursera.records.test.WithCourierFile
import org.coursera.records.test.WithCustomIntWrapper
import org.coursera.records.test.WithCustomRecord
import org.coursera.records.test.WithCustomRecordTestId
import org.coursera.records.test.{WithInclude => TestWithInclude}
import org.coursera.records.test.WithInlineRecord
import org.coursera.records.test.WithOmitField
import org.coursera.records.test.WithOptionalComplexTypeDefaults
import org.coursera.records.test.WithOptionalComplexTypes
import org.coursera.records.test.WithOptionalComplexTypesDefaultNone
import org.coursera.records.test.WithOptionalPrimitiveCustomTypes
import org.coursera.records.test.WithOptionalPrimitiveDefaultNone
import org.coursera.records.test.WithOptionalPrimitiveDefaults
import org.coursera.records.test.WithOptionalPrimitiveTyperefs
import org.coursera.records.test.WithOptionalPrimitives
import org.coursera.records.test.WithPrimitiveCustomTypes
import org.coursera.records.test.WithPrimitiveDefaults
import org.coursera.records.test.WithPrimitiveTyperefs
import org.coursera.records.test.WithPrimitives
import org.coursera.records.test.WithUnionWithInlineRecord
import org.coursera.records.primitivestyle.{Simple => PrimSimple}
import org.coursera.records.primitivestyle.{WithComplexTypes => PrimWithComplexTypes}
import org.coursera.records.primitivestyle.{WithPrimitives => PrimWithPrimitives}
import org.coursera.typerefs.FlatTypedDefinition
import org.coursera.typerefs.{InlineRecord => TyperefsInlineRecord}
import org.coursera.typerefs.InlineRecord2
import org.coursera.typerefs.TypedDefinition
import org.coursera.typerefs.Union
import org.coursera.typerefs.UnionTyperef
import org.coursera.typerefs.UnionWithInlineRecord
import org.coursera.unions.WithComplexTypesUnion
import org.coursera.unions.WithCustomUnionTestId
import org.coursera.unions.WithEmptyUnion
import org.coursera.unions.WithPrimitiveCustomTypesUnion
import org.coursera.unions.WithPrimitivesUnion
import org.coursera.unions.WithPrimitiveTyperefsUnion
import org.coursera.unions.WithRecordCustomTypeUnion
import org.example.Apostrophe
import org.example.FortuneCookie
import org.example.FortuneTelling
import org.example.MagicEightBall
import org.example.MagicEightBallAnswer
import org.example.TyperefExample
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Test

/**
 * Comprehensive coverage tests for all remaining uncovered code paths across generated classes.
 * Each test exercises: equality, copy, toString, productArity/productElement, classMixinDef,
 * companionMixinDef, and other specific patterns needed per type.
 */
class ComprehensiveCoverageTest extends GeneratorTest with SchemaFixtures {

  // Shared fixtures
  private val simpleRecord = Simple(Some("test"))
  private val customRecord = CustomRecord("title", "body")
  private val testDateTime = new DateTime(2024, 1, 1, 0, 0, DateTimeZone.UTC)
  private val testDateTime2 = new DateTime(2024, 6, 15, 12, 0, DateTimeZone.UTC)

  // ---------------------------------------------------------------------------
  // WithDateTime (org.coursera.records.test) — 44 uncovered statements
  // ---------------------------------------------------------------------------

  @Test
  def testWithDateTimeTest_construction(): Unit = {
    DateTimeCoercer.registerCoercer()
    val r = org.coursera.records.test.WithDateTime(testDateTime)
    assert(r.createdAt === testDateTime)
  }

  @Test
  def testWithDateTimeTest_equality(): Unit = {
    val a = org.coursera.records.test.WithDateTime(testDateTime)
    val b = org.coursera.records.test.WithDateTime(testDateTime)
    val c = org.coursera.records.test.WithDateTime(testDateTime2)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithDateTimeTest_copy(): Unit = {
    val r = org.coursera.records.test.WithDateTime(testDateTime)
    val copied = r.copy(createdAt = testDateTime2)
    assert(copied.createdAt === testDateTime2)
  }

  @Test
  def testWithDateTimeTest_toString(): Unit = {
    assert(org.coursera.records.test.WithDateTime(testDateTime).toString.nonEmpty)
  }

  @Test
  def testWithDateTimeTest_productArity(): Unit = {
    val r = org.coursera.records.test.WithDateTime(testDateTime)
    assert(r.productArity === 1)
    assert(r.productElement(0) === testDateTime)
  }

  @Test
  def testWithDateTimeTest_productElement_outOfBounds(): Unit = {
    val r = org.coursera.records.test.WithDateTime(testDateTime)
    intercept[IndexOutOfBoundsException] { r.productElement(1) }
  }

  @Test
  def testWithDateTimeTest_unapply(): Unit = {
    val r = org.coursera.records.test.WithDateTime(testDateTime)
    val org.coursera.records.test.WithDateTime(dt) = r
    assert(dt === testDateTime)
  }

  @Test
  def testWithDateTimeTest_classMixinDef(): Unit = {
    val r = org.coursera.records.test.WithDateTime(testDateTime)
    assert(r.classMixinDef === None)
  }

  @Test
  def testWithDateTimeTest_companionMixinDef(): Unit = {
    assert(org.coursera.records.test.WithDateTime.companionMixinDef === None)
  }

  @Test
  def testWithDateTimeTest_roundTrip(): Unit = {
    val original = org.coursera.records.test.WithDateTime(testDateTime)
    val rebuilt = org.coursera.records.test.WithDateTime.build(
      roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original.createdAt.getMillis === rebuilt.createdAt.getMillis)
  }

  // ---------------------------------------------------------------------------
  // WithDateTime (org.coursera.records) — 44 uncovered statements
  // ---------------------------------------------------------------------------

  @Test
  def testWithDateTimeRecords_equality(): Unit = {
    val a = org.coursera.records.WithDateTime(testDateTime)
    val b = org.coursera.records.WithDateTime(testDateTime)
    val c = org.coursera.records.WithDateTime(testDateTime2)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithDateTimeRecords_copy(): Unit = {
    val r = org.coursera.records.WithDateTime(testDateTime)
    val copied = r.copy(time = testDateTime2)
    assert(copied.time === testDateTime2)
  }

  @Test
  def testWithDateTimeRecords_toString(): Unit = {
    assert(org.coursera.records.WithDateTime(testDateTime).toString.nonEmpty)
  }

  @Test
  def testWithDateTimeRecords_productArity(): Unit = {
    val r = org.coursera.records.WithDateTime(testDateTime)
    assert(r.productArity === 1)
    assert(r.productElement(0) === testDateTime)
  }

  @Test
  def testWithDateTimeRecords_productElement_outOfBounds(): Unit = {
    val r = org.coursera.records.WithDateTime(testDateTime)
    intercept[IndexOutOfBoundsException] { r.productElement(1) }
  }

  @Test
  def testWithDateTimeRecords_unapply(): Unit = {
    val r = org.coursera.records.WithDateTime(testDateTime)
    val org.coursera.records.WithDateTime(dt) = r
    assert(dt === testDateTime)
  }

  @Test
  def testWithDateTimeRecords_classMixinDef(): Unit = {
    val r = org.coursera.records.WithDateTime(testDateTime)
    assert(r.classMixinDef === None)
  }

  @Test
  def testWithDateTimeRecords_companionMixinDef(): Unit = {
    assert(org.coursera.records.WithDateTime.companionMixinDef === None)
  }

  // ---------------------------------------------------------------------------
  // Fortune (org.example) — 49 uncovered statements
  // ---------------------------------------------------------------------------

  @Test
  def testFortune_equality(): Unit = {
    val telling = FortuneTelling.StringMember("lucky")
    val a = org.example.Fortune(telling, testDateTime)
    val b = org.example.Fortune(telling, testDateTime)
    val c = org.example.Fortune(telling, testDateTime2)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testFortune_copy(): Unit = {
    val telling = FortuneTelling.StringMember("test")
    val r = org.example.Fortune(telling, testDateTime)
    val copied = r.copy(createdAt = testDateTime2)
    assert(copied.createdAt === testDateTime2)
    assert(copied.telling === telling)
  }

  @Test
  def testFortune_toString(): Unit = {
    val telling = FortuneTelling.StringMember("x")
    assert(org.example.Fortune(telling, testDateTime).toString.nonEmpty)
  }

  @Test
  def testFortune_productArity(): Unit = {
    val telling = FortuneTelling.StringMember("x")
    val r = org.example.Fortune(telling, testDateTime)
    assert(r.productArity === 2)
    assert(r.productElement(0) === telling)
    assert(r.productElement(1) === testDateTime)
  }

  @Test
  def testFortune_productElement_outOfBounds(): Unit = {
    val telling = FortuneTelling.StringMember("x")
    val r = org.example.Fortune(telling, testDateTime)
    intercept[IndexOutOfBoundsException] { r.productElement(2) }
  }

  @Test
  def testFortune_unapply(): Unit = {
    val telling = FortuneTelling.StringMember("test")
    val r = org.example.Fortune(telling, testDateTime)
    val org.example.Fortune(t, dt) = r
    assert(t === telling)
    assert(dt === testDateTime)
  }

  @Test
  def testFortune_classMixinDef(): Unit = {
    val r = org.example.Fortune(FortuneTelling.StringMember("x"), testDateTime)
    assert(r.classMixinDef === None)
  }

  @Test
  def testFortune_companionMixinDef(): Unit = {
    assert(org.example.Fortune.companionMixinDef === None)
  }

  @Test
  def testFortune_roundTrip(): Unit = {
    val telling = FortuneTelling.StringMember("fortune")
    val original = org.example.Fortune(telling, testDateTime)
    val rebuilt = org.example.Fortune.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original.telling === rebuilt.telling)
    assert(original.createdAt.getMillis === rebuilt.createdAt.getMillis)
  }

  // ---------------------------------------------------------------------------
  // records.test.Message (required title/body) — 30 uncovered statements
  // ---------------------------------------------------------------------------

  @Test
  def testTestMessage_equality(): Unit = {
    val a = TestMessage("Hello", "World")
    val b = TestMessage("Hello", "World")
    val c = TestMessage("Hi", "World")
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testTestMessage_canEqual(): Unit = {
    val m = TestMessage("t", "b")
    assert(m.canEqual(m))
    assert(!m.canEqual("other"))
  }

  @Test
  def testTestMessage_equals_differentProductArity(): Unit = {
    val m = TestMessage("t", "b")
    assert(!(m == Tuple2("t", "b")))
  }

  @Test
  def testTestMessage_copy(): Unit = {
    val m = TestMessage("original", "body")
    val copied = m.copy(title = "updated")
    assert(copied.title === "updated")
    assert(copied.body === "body")
  }

  @Test
  def testTestMessage_unapply(): Unit = {
    val m = TestMessage("title", "body")
    val TestMessage(t, b) = m
    assert(t === "title")
    assert(b === "body")
  }

  @Test
  def testTestMessage_productArity(): Unit = {
    val m = TestMessage("t", "b")
    assert(m.productArity === 2)
    assert(m.productElement(0) === "t")
    assert(m.productElement(1) === "b")
  }

  @Test
  def testTestMessage_productElement_outOfBounds(): Unit = {
    val m = TestMessage("t", "b")
    intercept[IndexOutOfBoundsException] { m.productElement(2) }
  }

  @Test
  def testTestMessage_toString(): Unit = {
    assert(TestMessage("t", "b").toString.nonEmpty)
  }

  @Test
  def testTestMessage_classMixinDef(): Unit = {
    assert(TestMessage("t", "b").classMixinDef === None)
  }

  @Test
  def testTestMessage_companionMixinDef(): Unit = {
    assert(TestMessage.companionMixinDef === None)
  }

  @Test
  def testTestMessage_roundTrip(): Unit = {
    val original = TestMessage("title", "body")
    val rebuilt = TestMessage.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === rebuilt)
  }

  // ---------------------------------------------------------------------------
  // WithOptionalPrimitiveDefaultNone — 29 uncovered (mostly setFields + classMixinDef)
  // ---------------------------------------------------------------------------

  @Test
  def testWithOptionalPrimitiveDefaultNone_classMixinDef(): Unit = {
    assert(WithOptionalPrimitiveDefaultNone().classMixinDef === None)
  }

  @Test
  def testWithOptionalPrimitiveDefaultNone_companionMixinDef(): Unit = {
    assert(WithOptionalPrimitiveDefaultNone.companionMixinDef === None)
  }

  @Test
  def testWithOptionalPrimitiveDefaultNone_equals_differentProductArity(): Unit = {
    val r = WithOptionalPrimitiveDefaultNone()
    assert(!(r == Tuple1(None)))
  }

  @Test
  def testWithOptionalPrimitiveDefaultNone_copy_allFields(): Unit = {
    // Calls setFields with multiple non-None values to cover the putDirect branches
    val r = WithOptionalPrimitiveDefaultNone(
      intWithDefault = Some(1),
      longWithDefault = Some(2L),
      floatWithDefault = Some(3.0f),
      doubleWithDefault = Some(4.0d),
      booleanWithDefault = Some(true),
      stringWithDefault = Some("s"),
      bytesWithDefault = Some(bytes1),
      enumWithDefault = Some(Fruits.APPLE)
    )
    assert(r.intWithDefault === Some(1))
    assert(r.enumWithDefault === Some(Fruits.APPLE))
    val copied = r.copy(intWithDefault = Some(99))
    assert(copied.intWithDefault === Some(99))
    assert(copied.stringWithDefault === Some("s"))
  }

  // ---------------------------------------------------------------------------
  // SimpleMapMap — 21 uncovered (+, removed, updated, schema, companionMixinDef)
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleMapMap_plus_and_operations(): Unit = {
    val inner1 = SimpleMap("k" -> Simple(Some("v1")))
    val inner2 = SimpleMap("k2" -> Simple(Some("v2")))
    val m = SimpleMapMap("a" -> inner1)
    val m2 = m + ("b" -> inner2)
    assert(m2.size === 2)
    // plus with supertype
    val m3: Map[String, Any] = m + ("c" -> "notAMap")
    assert(m3.size === 2)
    // removed
    val m4 = m.removed("a")
    assert(m4.size === 0)
    // updated
    val m5 = m.updated("a", inner2)
    assert(m5.get("a") === Some(inner2))
    // schema
    assert(m.schema() != null)
  }

  @Test
  def testSimpleMapMap_classMixinDef(): Unit = {
    val m = SimpleMapMap("k" -> SimpleMap("x" -> Simple(Some("v"))))
    assert(m.classMixinDef === None)
  }

  @Test
  def testSimpleMapMap_companionMixinDef(): Unit = {
    assert(SimpleMapMap.companionMixinDef === None)
  }

  @Test
  def testSimpleMapMap_build(): Unit = {
    val m = SimpleMapMap("k" -> SimpleMap("x" -> Simple(Some("v"))))
    val rebuilt = SimpleMapMap.build(roundTrip(m.data()), DataConversion.SetReadOnly)
    assert(rebuilt.size === 1)
  }

  // ---------------------------------------------------------------------------
  // SimpleToStringMap — 21 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleToStringMap_plus_and_operations(): Unit = {
    val k1 = Simple(Some("k1"))
    val k2 = Simple(Some("k2"))
    val m = SimpleToStringMap(k1 -> "v1")
    val m2 = m + (k2 -> "v2")
    assert(m2.size === 2)
    // plus supertype
    val m3: Map[Simple, Any] = m + (k2 -> 42)
    assert(m3.size === 2)
    // removed
    val m4 = m.removed(k1)
    assert(m4.size === 0)
    // updated
    val m5 = m.updated(k1, "updated")
    assert(m5.get(k1) === Some("updated"))
    // schema
    assert(m.schema() != null)
  }

  @Test
  def testSimpleToStringMap_classMixinDef(): Unit = {
    val m = SimpleToStringMap(Simple(Some("x")) -> "v")
    assert(m.classMixinDef === None)
  }

  @Test
  def testSimpleToStringMap_companionMixinDef(): Unit = {
    assert(SimpleToStringMap.companionMixinDef === None)
  }

  @Test
  def testSimpleToStringMap_build(): Unit = {
    val k = Simple(Some("x"))
    val m = SimpleToStringMap(k -> "v")
    val rebuilt = SimpleToStringMap.build(roundTrip(m.data()), DataConversion.SetReadOnly)
    assert(rebuilt.size === 1)
  }

  @Test
  def testSimpleToStringMap_wrap(): Unit = {
    val inner: Map[Simple, String] = Map(Simple(Some("a")) -> "val")
    val m: SimpleToStringMap = inner
    assert(m.size === 1)
  }

  // ---------------------------------------------------------------------------
  // EmptyMap — 21 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testEmptyMap_plus_and_operations(): Unit = {
    val m = EmptyMap("k" -> Empty())
    val m2 = m + ("k2" -> Empty())
    assert(m2.size === 2)
    // plus supertype
    val m3: Map[String, Any] = m + ("k3" -> "notEmpty")
    assert(m3.size === 2)
    // removed
    val m4 = m.removed("k")
    assert(m4.size === 0)
    // updated
    val m5 = m.updated("k", Empty())
    assert(m5.get("k") === Some(Empty()))
    // schema
    assert(m.schema() != null)
  }

  @Test
  def testEmptyMap_classMixinDef(): Unit = {
    assert(EmptyMap("k" -> Empty()).classMixinDef === None)
  }

  @Test
  def testEmptyMap_companionMixinDef(): Unit = {
    assert(EmptyMap.companionMixinDef === None)
  }

  @Test
  def testEmptyMap_build(): Unit = {
    val m = EmptyMap("k" -> Empty())
    val rebuilt = EmptyMap.build(roundTrip(m.data()), DataConversion.SetReadOnly)
    assert(rebuilt.size === 1)
  }

  // ---------------------------------------------------------------------------
  // EmptyArray — 15 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testEmptyArray_classMixinDef(): Unit = {
    assert(EmptyArray(Empty()).classMixinDef === None)
  }

  @Test
  def testEmptyArray_companionMixinDef(): Unit = {
    assert(EmptyArray.companionMixinDef === None)
  }

  @Test
  def testEmptyArray_productArity(): Unit = {
    val arr = EmptyArray(Empty(), Empty())
    assert(arr.productArity === 2)
    assert(arr.productElement(0) != null)
  }

  @Test
  def testEmptyArray_wrapImplicit(): Unit = {
    val items: Iterable[Empty] = List(Empty(), Empty())
    val arr: EmptyArray = items
    assert(arr.length === 2)
  }

  // ---------------------------------------------------------------------------
  // WithOptionalPrimitives — 14 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithOptionalPrimitives_classMixinDef(): Unit = {
    assert(WithOptionalPrimitives().classMixinDef === None)
  }

  @Test
  def testWithOptionalPrimitives_companionMixinDef(): Unit = {
    assert(WithOptionalPrimitives.companionMixinDef === None)
  }

  @Test
  def testWithOptionalPrimitives_copy_allFields(): Unit = {
    val r = WithOptionalPrimitives(
      intField = Some(1), longField = Some(2L), floatField = Some(3.0f),
      doubleField = Some(4.0d), booleanField = Some(true),
      stringField = Some("s"), bytesField = Some(bytes1))
    assert(r.intField === Some(1))
    val copied = r.copy(intField = Some(99))
    assert(copied.intField === Some(99))
    assert(copied.stringField === Some("s"))
  }

  @Test
  def testWithOptionalPrimitives_equals_differentProductArity(): Unit = {
    val r = WithOptionalPrimitives()
    assert(!(r == Tuple1(None)))
  }

  // ---------------------------------------------------------------------------
  // WithOptionalPrimitiveTyperefs — 14 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithOptionalPrimitiveTyperefs_classMixinDef(): Unit = {
    assert(WithOptionalPrimitiveTyperefs().classMixinDef === None)
  }

  @Test
  def testWithOptionalPrimitiveTyperefs_companionMixinDef(): Unit = {
    assert(WithOptionalPrimitiveTyperefs.companionMixinDef === None)
  }

  @Test
  def testWithOptionalPrimitiveTyperefs_copy_allFields(): Unit = {
    val r = WithOptionalPrimitiveTyperefs(
      intField = Some(1), longField = Some(2L), floatField = Some(3.0f),
      doubleField = Some(4.0d), booleanField = Some(true),
      stringField = Some("s"), bytesField = Some(bytes1))
    val copied = r.copy(intField = Some(99))
    assert(copied.intField === Some(99))
  }

  // ---------------------------------------------------------------------------
  // WithOptionalPrimitiveCustomTypes — 14 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithOptionalPrimitiveCustomTypes_classMixinDef(): Unit = {
    assert(WithOptionalPrimitiveCustomTypes().classMixinDef === None)
  }

  @Test
  def testWithOptionalPrimitiveCustomTypes_companionMixinDef(): Unit = {
    assert(WithOptionalPrimitiveCustomTypes.companionMixinDef === None)
  }

  @Test
  def testWithOptionalPrimitiveCustomTypes_equals_different(): Unit = {
    val a = WithOptionalPrimitiveCustomTypes(Some(CustomInt(1)))
    val b = WithOptionalPrimitiveCustomTypes(Some(CustomInt(2)))
    assert(a !== b)
  }

  @Test
  def testWithOptionalPrimitiveCustomTypes_copy_withValue(): Unit = {
    val r = WithOptionalPrimitiveCustomTypes(intField = Some(CustomInt(42)))
    assert(r.intField === Some(CustomInt(42)))
    val copied = r.copy(intField = Some(CustomInt(99)))
    assert(copied.intField === Some(CustomInt(99)))
  }

  // ---------------------------------------------------------------------------
  // WithInlineRecord — 14 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithInlineRecord_classMixinDef(): Unit = {
    val r = WithInlineRecord(TestInlineRecord(1))
    assert(r.classMixinDef === None)
  }

  @Test
  def testWithInlineRecord_companionMixinDef(): Unit = {
    assert(WithInlineRecord.companionMixinDef === None)
  }

  @Test
  def testWithInlineRecord_equality(): Unit = {
    val a = WithInlineRecord(TestInlineRecord(1))
    val b = WithInlineRecord(TestInlineRecord(1))
    val c = WithInlineRecord(TestInlineRecord(2))
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithInlineRecord_copy(): Unit = {
    val r = WithInlineRecord(TestInlineRecord(1))
    val copied = r.copy(inline = TestInlineRecord(99), inlineOptional = Some(InlineOptionalRecord("x")))
    assert(copied.inline === TestInlineRecord(99))
    assert(copied.inlineOptional === Some(InlineOptionalRecord("x")))
  }

  @Test
  def testWithInlineRecord_productArity(): Unit = {
    val r = WithInlineRecord(TestInlineRecord(1))
    assert(r.productArity === 2)
    assert(r.productElement(0) === TestInlineRecord(1))
  }

  @Test
  def testWithInlineRecord_toString(): Unit = {
    assert(WithInlineRecord(TestInlineRecord(1)).toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithUnionWithInlineRecord — 12 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithUnionWithInlineRecord_classMixinDef(): Unit = {
    val member = UnionWithInlineRecord.InlineRecord2Member(InlineRecord2())
    val r = WithUnionWithInlineRecord(member)
    assert(r.classMixinDef === None)
  }

  @Test
  def testWithUnionWithInlineRecord_companionMixinDef(): Unit = {
    assert(WithUnionWithInlineRecord.companionMixinDef === None)
  }

  @Test
  def testWithUnionWithInlineRecord_equality(): Unit = {
    val member = UnionWithInlineRecord.InlineRecord2Member(InlineRecord2())
    val a = WithUnionWithInlineRecord(member)
    val b = WithUnionWithInlineRecord(member)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithUnionWithInlineRecord_copy(): Unit = {
    val member1 = UnionWithInlineRecord.InlineRecord2Member(InlineRecord2())
    val member2 = UnionWithInlineRecord.InlineRecordMember(TyperefsInlineRecord(Some(5)))
    val r = WithUnionWithInlineRecord(member1)
    val copied = r.copy(value = member2)
    assert(copied.value.isInstanceOf[UnionWithInlineRecord.InlineRecordMember])
  }

  @Test
  def testWithUnionWithInlineRecord_productArity(): Unit = {
    val member = UnionWithInlineRecord.InlineRecord2Member(InlineRecord2())
    val r = WithUnionWithInlineRecord(member)
    assert(r.productArity === 1)
    assert(r.productElement(0) === member)
  }

  @Test
  def testWithUnionWithInlineRecord_toString(): Unit = {
    val member = UnionWithInlineRecord.InlineRecord2Member(InlineRecord2())
    assert(WithUnionWithInlineRecord(member).toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // TestWithInclude (records.test.WithInclude) — 14 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testTestWithInclude_classMixinDef(): Unit = {
    assert(TestWithInclude(direct = 1).classMixinDef === None)
  }

  @Test
  def testTestWithInclude_companionMixinDef(): Unit = {
    assert(TestWithInclude.companionMixinDef === None)
  }

  @Test
  def testTestWithInclude_equality(): Unit = {
    val a = TestWithInclude(Some("msg"), 1)
    val b = TestWithInclude(Some("msg"), 1)
    val c = TestWithInclude(None, 2)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testTestWithInclude_copy(): Unit = {
    val r = TestWithInclude(Some("msg"), 1)
    val copied = r.copy(direct = 99)
    assert(copied.direct === 99)
    assert(copied.message === Some("msg"))
  }

  @Test
  def testTestWithInclude_productArity(): Unit = {
    val r = TestWithInclude(Some("x"), 5)
    assert(r.productArity === 2)
    assert(r.productElement(0) === Some("x"))
    assert(r.productElement(1) === 5)
  }

  @Test
  def testTestWithInclude_toString(): Unit = {
    assert(TestWithInclude(direct = 1).toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithOptionalComplexTypeDefaults — 20 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithOptionalComplexTypeDefaults_classMixinDef(): Unit = {
    assert(WithOptionalComplexTypeDefaults().classMixinDef === None)
  }

  @Test
  def testWithOptionalComplexTypeDefaults_companionMixinDef(): Unit = {
    assert(WithOptionalComplexTypeDefaults.companionMixinDef === None)
  }

  @Test
  def testWithOptionalComplexTypeDefaults_equals_different(): Unit = {
    val a = WithOptionalComplexTypeDefaults(record = Some(Simple(Some("x"))))
    val b = WithOptionalComplexTypeDefaults(record = Some(Simple(Some("y"))))
    assert(a !== b)
  }

  @Test
  def testWithOptionalComplexTypeDefaults_copy_allFields(): Unit = {
    val union = WithOptionalComplexTypeDefaults.Union.IntMember(1)
    val r = WithOptionalComplexTypeDefaults(
      record = Some(Simple(Some("x"))),
      enum = Some(Fruits.APPLE),
      union = Some(union),
      array = Some(org.coursera.courier.data.IntArray(1, 2)),
      map = Some(org.coursera.courier.data.IntMap("a" -> 1)),
      custom = Some(CustomInt(5))
    )
    assert(r.record === Some(Simple(Some("x"))))
    val copied = r.copy(enum = Some(Fruits.PINEAPPLE))
    assert(copied.enum === Some(Fruits.PINEAPPLE))
    assert(copied.record === Some(Simple(Some("x"))))
  }

  // ---------------------------------------------------------------------------
  // WithComplexTypeDefaults — 20 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithComplexTypeDefaults_classMixinDef(): Unit = {
    assert(WithComplexTypeDefaults().classMixinDef === None)
  }

  @Test
  def testWithComplexTypeDefaults_companionMixinDef(): Unit = {
    assert(WithComplexTypeDefaults.companionMixinDef === None)
  }

  @Test
  def testWithComplexTypeDefaults_equals_different(): Unit = {
    val a = WithComplexTypeDefaults(record = Simple(Some("x")))
    val b = WithComplexTypeDefaults(record = Simple(Some("y")))
    assert(a !== b)
  }

  @Test
  def testWithComplexTypeDefaults_copy_allFields(): Unit = {
    val union = WithComplexTypeDefaults.Union.IntMember(1)
    val r = WithComplexTypeDefaults(
      record = Simple(Some("x")),
      enum = Fruits.APPLE,
      union = union,
      array = org.coursera.courier.data.IntArray(1),
      map = org.coursera.courier.data.IntMap("k" -> 1),
      custom = CustomInt(1)
    )
    val copied = r.copy(enum = Fruits.PINEAPPLE)
    assert(copied.enum === Fruits.PINEAPPLE)
  }

  // ---------------------------------------------------------------------------
  // WithOptionalComplexTypesDefaultNone — 19 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithOptionalComplexTypesDefaultNone_classMixinDef(): Unit = {
    assert(WithOptionalComplexTypesDefaultNone().classMixinDef === None)
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_companionMixinDef(): Unit = {
    assert(WithOptionalComplexTypesDefaultNone.companionMixinDef === None)
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_copy_withFields(): Unit = {
    val union = WithOptionalComplexTypesDefaultNone.Union.IntMember(1)
    val r = WithOptionalComplexTypesDefaultNone(
      record = Some(Simple(Some("x"))),
      enum = Some(Fruits.APPLE),
      union = Some(union),
      array = Some(org.coursera.courier.data.IntArray(1)),
      map = Some(org.coursera.courier.data.IntMap("k" -> 1)),
      complexMap = Some(SimpleMap("k" -> Simple(Some("v")))),
      custom = Some(CustomInt(1))
    )
    val copied = r.copy(enum = Some(Fruits.PINEAPPLE))
    assert(copied.enum === Some(Fruits.PINEAPPLE))
  }

  // ---------------------------------------------------------------------------
  // WithOptionalComplexTypes — 17 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithOptionalComplexTypes_classMixinDef(): Unit = {
    assert(WithOptionalComplexTypes().classMixinDef === None)
  }

  @Test
  def testWithOptionalComplexTypes_companionMixinDef(): Unit = {
    assert(WithOptionalComplexTypes.companionMixinDef === None)
  }

  @Test
  def testWithOptionalComplexTypes_copy_withFields(): Unit = {
    val r = WithOptionalComplexTypes(
      record = Some(Simple(Some("x"))),
      enum = Some(Fruits.APPLE),
      union = Some(WithOptionalComplexTypes.Union.IntMember(1)),
      custom = Some(CustomInt(5))
    )
    val copied = r.copy(enum = Some(Fruits.PINEAPPLE))
    assert(copied.enum === Some(Fruits.PINEAPPLE))
  }

  // ---------------------------------------------------------------------------
  // SimpleArrayMap — 18 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleArrayMap_plus_and_operations(): Unit = {
    val arr1 = SimpleArray(Simple(Some("a")))
    val arr2 = SimpleArray(Simple(Some("b")))
    val m = SimpleArrayMap("k1" -> arr1)
    val m2 = m + ("k2" -> arr2)
    assert(m2.size === 2)
    // plus supertype
    val m3: Map[String, Any] = m + ("k3" -> "notAnArray")
    assert(m3.size === 2)
    // removed
    val m4 = m.removed("k1")
    assert(m4.size === 0)
    // updated
    val m5 = m.updated("k1", arr2)
    assert(m5.get("k1") === Some(arr2))
    // schema
    assert(m.schema() != null)
  }

  @Test
  def testSimpleArrayMap_classMixinDef(): Unit = {
    assert(SimpleArrayMap("k" -> SimpleArray()).classMixinDef === None)
  }

  @Test
  def testSimpleArrayMap_companionMixinDef(): Unit = {
    assert(SimpleArrayMap.companionMixinDef === None)
  }

  @Test
  def testSimpleArrayMap_build(): Unit = {
    val m = SimpleArrayMap("k" -> SimpleArray(Simple(Some("v"))))
    val rebuilt = SimpleArrayMap.build(roundTrip(m.data()), DataConversion.SetReadOnly)
    assert(rebuilt.size === 1)
  }

  // ---------------------------------------------------------------------------
  // RecursivelyDefinedRecord — 13 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testRecursivelyDefinedRecord_classMixinDef(): Unit = {
    assert(RecursivelyDefinedRecord().classMixinDef === None)
  }

  @Test
  def testRecursivelyDefinedRecord_companionMixinDef(): Unit = {
    assert(RecursivelyDefinedRecord.companionMixinDef === None)
  }

  @Test
  def testRecursivelyDefinedRecord_equality(): Unit = {
    val a = RecursivelyDefinedRecord(Some(RecursivelyDefinedRecord()))
    val b = RecursivelyDefinedRecord(Some(RecursivelyDefinedRecord()))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testRecursivelyDefinedRecord_copy(): Unit = {
    val r = RecursivelyDefinedRecord()
    val copied = r.copy(self = Some(RecursivelyDefinedRecord()))
    assert(copied.self.isDefined)
  }

  @Test
  def testRecursivelyDefinedRecord_productArity(): Unit = {
    val r = RecursivelyDefinedRecord()
    assert(r.productArity === 1)
    assert(r.productElement(0) === None)
  }

  @Test
  def testRecursivelyDefinedRecord_toString(): Unit = {
    assert(RecursivelyDefinedRecord().toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // NumericDefaults — 11 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testNumericDefaults_classMixinDef(): Unit = {
    assert(NumericDefaults().classMixinDef === None)
  }

  @Test
  def testNumericDefaults_companionMixinDef(): Unit = {
    assert(NumericDefaults.companionMixinDef === None)
  }

  @Test
  def testNumericDefaults_equals_different(): Unit = {
    val a = NumericDefaults(i = 1)
    val b = NumericDefaults(i = 2)
    assert(a !== b)
  }

  @Test
  def testNumericDefaults_copy(): Unit = {
    val r = NumericDefaults()
    val copied = r.copy(i = 42, l = 100L)
    assert(copied.i === 42)
    assert(copied.l === 100L)
  }

  // ---------------------------------------------------------------------------
  // WithPrimitivesArray — 21 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithPrimitivesArray_classMixinDef(): Unit = {
    val r = WithPrimitivesArray(
      IntArray(1), LongArray(2L), FloatArray(3.0f), DoubleArray(4.0d),
      BooleanArray(true), StringArray("s"), BytesArray(bytes1))
    assert(r.classMixinDef === None)
  }

  @Test
  def testWithPrimitivesArray_companionMixinDef(): Unit = {
    assert(WithPrimitivesArray.companionMixinDef === None)
  }

  @Test
  def testWithPrimitivesArray_equality(): Unit = {
    val a = WithPrimitivesArray(
      IntArray(1), LongArray(2L), FloatArray(3.0f), DoubleArray(4.0d),
      BooleanArray(true), StringArray("s"), BytesArray(bytes1))
    val b = WithPrimitivesArray(
      IntArray(1), LongArray(2L), FloatArray(3.0f), DoubleArray(4.0d),
      BooleanArray(true), StringArray("s"), BytesArray(bytes1))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithPrimitivesArray_copy(): Unit = {
    val r = WithPrimitivesArray(
      IntArray(1), LongArray(2L), FloatArray(3.0f), DoubleArray(4.0d),
      BooleanArray(true), StringArray("s"), BytesArray(bytes1))
    val copied = r.copy(ints = IntArray(99))
    assert(copied.ints === IntArray(99))
  }

  @Test
  def testWithPrimitivesArray_productArity(): Unit = {
    val r = WithPrimitivesArray(
      IntArray(1), LongArray(2L), FloatArray(3.0f), DoubleArray(4.0d),
      BooleanArray(true), StringArray("s"), BytesArray(bytes1))
    assert(r.productArity === 7)
    assert(r.productElement(0) === IntArray(1))
  }

  @Test
  def testWithPrimitivesArray_toString(): Unit = {
    val r = WithPrimitivesArray(
      IntArray(1), LongArray(2L), FloatArray(3.0f), DoubleArray(4.0d),
      BooleanArray(true), StringArray("s"), BytesArray(bytes1))
    assert(r.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithPrimitivesMap — 21 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithPrimitivesMap_classMixinDef(): Unit = {
    val r = WithPrimitivesMap(
      org.coursera.courier.data.IntMap("k" -> 1),
      org.coursera.courier.data.LongMap("k" -> 2L),
      org.coursera.courier.data.FloatMap("k" -> 3.0f),
      org.coursera.courier.data.DoubleMap("k" -> 4.0d),
      org.coursera.courier.data.BooleanMap("k" -> true),
      org.coursera.courier.data.StringMap("k" -> "v"),
      org.coursera.courier.data.BytesMap("k" -> bytes1))
    assert(r.classMixinDef === None)
  }

  @Test
  def testWithPrimitivesMap_companionMixinDef(): Unit = {
    assert(WithPrimitivesMap.companionMixinDef === None)
  }

  @Test
  def testWithPrimitivesMap_equality(): Unit = {
    val mkr = WithPrimitivesMap(
      org.coursera.courier.data.IntMap("k" -> 1),
      org.coursera.courier.data.LongMap("k" -> 2L),
      org.coursera.courier.data.FloatMap("k" -> 3.0f),
      org.coursera.courier.data.DoubleMap("k" -> 4.0d),
      org.coursera.courier.data.BooleanMap("k" -> true),
      org.coursera.courier.data.StringMap("k" -> "v"),
      org.coursera.courier.data.BytesMap("k" -> bytes1))
    val mkr2 = WithPrimitivesMap(
      org.coursera.courier.data.IntMap("k" -> 1),
      org.coursera.courier.data.LongMap("k" -> 2L),
      org.coursera.courier.data.FloatMap("k" -> 3.0f),
      org.coursera.courier.data.DoubleMap("k" -> 4.0d),
      org.coursera.courier.data.BooleanMap("k" -> true),
      org.coursera.courier.data.StringMap("k" -> "v"),
      org.coursera.courier.data.BytesMap("k" -> bytes1))
    assert(mkr === mkr2)
    assert(mkr.hashCode === mkr2.hashCode)
  }

  @Test
  def testWithPrimitivesMap_copy(): Unit = {
    val r = WithPrimitivesMap(
      org.coursera.courier.data.IntMap("k" -> 1),
      org.coursera.courier.data.LongMap("k" -> 2L),
      org.coursera.courier.data.FloatMap("k" -> 3.0f),
      org.coursera.courier.data.DoubleMap("k" -> 4.0d),
      org.coursera.courier.data.BooleanMap("k" -> true),
      org.coursera.courier.data.StringMap("k" -> "v"),
      org.coursera.courier.data.BytesMap("k" -> bytes1))
    val copied = r.copy(ints = org.coursera.courier.data.IntMap("k" -> 99))
    assert(copied.ints.get("k") === Some(99))
  }

  @Test
  def testWithPrimitivesMap_toString(): Unit = {
    val r = WithPrimitivesMap(
      org.coursera.courier.data.IntMap("k" -> 1),
      org.coursera.courier.data.LongMap("k" -> 2L),
      org.coursera.courier.data.FloatMap("k" -> 3.0f),
      org.coursera.courier.data.DoubleMap("k" -> 4.0d),
      org.coursera.courier.data.BooleanMap("k" -> true),
      org.coursera.courier.data.StringMap("k" -> "v"),
      org.coursera.courier.data.BytesMap("k" -> bytes1))
    assert(r.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithCustomTypesArray — 21 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithCustomTypesArray_classMixinDef(): Unit = {
    val r = WithCustomTypesArray(
      CustomIntArray(CustomInt(1)),
      SimpleArrayArray(SimpleArray()),
      SimpleMapArray(SimpleMap()),
      org.coursera.arrays.WithCustomTypesArrayUnionArray(),
      Fixed8Array(Fixed8(bytesFixed8)))
    assert(r.classMixinDef === None)
  }

  @Test
  def testWithCustomTypesArray_companionMixinDef(): Unit = {
    assert(WithCustomTypesArray.companionMixinDef === None)
  }

  @Test
  def testWithCustomTypesArray_equality(): Unit = {
    val arr = Fixed8Array(Fixed8(bytesFixed8))
    val a = WithCustomTypesArray(
      CustomIntArray(CustomInt(1)), SimpleArrayArray(), SimpleMapArray(),
      org.coursera.arrays.WithCustomTypesArrayUnionArray(), arr)
    val b = WithCustomTypesArray(
      CustomIntArray(CustomInt(1)), SimpleArrayArray(), SimpleMapArray(),
      org.coursera.arrays.WithCustomTypesArrayUnionArray(), arr)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithCustomTypesArray_copy(): Unit = {
    val r = WithCustomTypesArray(
      CustomIntArray(CustomInt(1)), SimpleArrayArray(), SimpleMapArray(),
      org.coursera.arrays.WithCustomTypesArrayUnionArray(), Fixed8Array(Fixed8(bytesFixed8)))
    val copied = r.copy(ints = CustomIntArray(CustomInt(99)))
    assert(copied.ints(0) === CustomInt(99))
  }

  @Test
  def testWithCustomTypesArray_toString(): Unit = {
    val r = WithCustomTypesArray(
      CustomIntArray(CustomInt(1)), SimpleArrayArray(), SimpleMapArray(),
      org.coursera.arrays.WithCustomTypesArrayUnionArray(), Fixed8Array(Fixed8(bytesFixed8)))
    assert(r.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithComplexTypesMap — 21 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithComplexTypesMap_classMixinDef(): Unit = {
    val r = WithComplexTypesMap(
      EmptyMap(), FruitsMap(), SimpleArrayMap(), SimpleMapMap(),
      WithComplexTypesMapUnionMap(), Fixed8Map())
    assert(r.classMixinDef === None)
  }

  @Test
  def testWithComplexTypesMap_companionMixinDef(): Unit = {
    assert(WithComplexTypesMap.companionMixinDef === None)
  }

  @Test
  def testWithComplexTypesMap_equality(): Unit = {
    val a = WithComplexTypesMap(EmptyMap(), FruitsMap(), SimpleArrayMap(), SimpleMapMap(), WithComplexTypesMapUnionMap(), Fixed8Map())
    val b = WithComplexTypesMap(EmptyMap(), FruitsMap(), SimpleArrayMap(), SimpleMapMap(), WithComplexTypesMapUnionMap(), Fixed8Map())
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithComplexTypesMap_copy(): Unit = {
    val r = WithComplexTypesMap(EmptyMap(), FruitsMap(), SimpleArrayMap(), SimpleMapMap(), WithComplexTypesMapUnionMap(), Fixed8Map())
    val f8m = Fixed8Map("k" -> Fixed8(bytesFixed8))
    val copied = r.copy(fixed = f8m)
    assert(copied.fixed.size === 1)
  }

  @Test
  def testWithComplexTypesMap_toString(): Unit = {
    val r = WithComplexTypesMap(EmptyMap(), FruitsMap(), SimpleArrayMap(), SimpleMapMap(), WithComplexTypesMapUnionMap(), Fixed8Map())
    assert(r.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // FruitsMap — 21 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testFruitsMap_plus_and_operations(): Unit = {
    val m = FruitsMap("k1" -> Fruits.APPLE)
    val m2 = m + ("k2" -> Fruits.PINEAPPLE)
    assert(m2.size === 2)
    val m3: Map[String, Any] = m + ("k3" -> "notFruit")
    assert(m3.size === 2)
    val m4 = m.removed("k1")
    assert(m4.size === 0)
    val m5 = m.updated("k1", Fruits.PINEAPPLE)
    assert(m5.get("k1") === Some(Fruits.PINEAPPLE))
    assert(m.schema() != null)
  }

  @Test
  def testFruitsMap_classMixinDef(): Unit = {
    assert(FruitsMap("k" -> Fruits.APPLE).classMixinDef === None)
  }

  @Test
  def testFruitsMap_companionMixinDef(): Unit = {
    assert(FruitsMap.companionMixinDef === None)
  }

  @Test
  def testFruitsMap_build(): Unit = {
    val m = FruitsMap("k" -> Fruits.APPLE)
    val rebuilt = FruitsMap.build(roundTrip(m.data()), DataConversion.SetReadOnly)
    assert(rebuilt.get("k") === Some(Fruits.APPLE))
  }

  @Test
  def testFruitsMap_wrap(): Unit = {
    val plain: Map[String, Fruits] = Map("k" -> Fruits.APPLE)
    val m: FruitsMap = plain
    assert(m.size === 1)
  }

  // ---------------------------------------------------------------------------
  // FruitsToStringMap — 21 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testFruitsToStringMap_plus_and_operations(): Unit = {
    val m = FruitsToStringMap(Fruits.APPLE -> "v1")
    val m2 = m + (Fruits.PINEAPPLE -> "v2")
    assert(m2.size === 2)
    val m3: Map[Fruits, Any] = m + (Fruits.PINEAPPLE -> 42)
    assert(m3.size === 2)
    val m4 = m.removed(Fruits.APPLE)
    assert(m4.size === 0)
    val m5 = m.updated(Fruits.APPLE, "updated")
    assert(m5.get(Fruits.APPLE) === Some("updated"))
    assert(m.schema() != null)
  }

  @Test
  def testFruitsToStringMap_classMixinDef(): Unit = {
    assert(FruitsToStringMap(Fruits.APPLE -> "v").classMixinDef === None)
  }

  @Test
  def testFruitsToStringMap_companionMixinDef(): Unit = {
    assert(FruitsToStringMap.companionMixinDef === None)
  }

  @Test
  def testFruitsToStringMap_build(): Unit = {
    val m = FruitsToStringMap(Fruits.APPLE -> "v")
    val rebuilt = FruitsToStringMap.build(roundTrip(m.data()), DataConversion.SetReadOnly)
    assert(rebuilt.get(Fruits.APPLE) === Some("v"))
  }

  // ---------------------------------------------------------------------------
  // Fixed8Map — 21 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testFixed8Map_plus_and_operations(): Unit = {
    val f = Fixed8(bytesFixed8)
    val m = Fixed8Map("k1" -> f)
    val m2 = m + ("k2" -> f)
    assert(m2.size === 2)
    val m3: Map[String, Any] = m + ("k3" -> "notFixed8")
    assert(m3.size === 2)
    val m4 = m.removed("k1")
    assert(m4.size === 0)
    val m5 = m.updated("k1", f)
    assert(m5.get("k1") === Some(f))
    assert(m.schema() != null)
  }

  @Test
  def testFixed8Map_classMixinDef(): Unit = {
    assert(Fixed8Map("k" -> Fixed8(bytesFixed8)).classMixinDef === None)
  }

  @Test
  def testFixed8Map_companionMixinDef(): Unit = {
    assert(Fixed8Map.companionMixinDef === None)
  }

  @Test
  def testFixed8Map_build(): Unit = {
    val f = Fixed8(bytesFixed8)
    val m = Fixed8Map("k" -> f)
    val rebuilt = Fixed8Map.build(roundTrip(m.data()), DataConversion.SetReadOnly)
    assert(rebuilt.get("k").isDefined)
  }

  // ---------------------------------------------------------------------------
  // Fixed8ToStringMap — 21 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testFixed8ToStringMap_plus_and_operations(): Unit = {
    val f = Fixed8(bytesFixed8)
    val m = Fixed8ToStringMap(f -> "v1")
    val m2 = m + (f -> "v2")
    assert(m2.size === 1) // same key, overwrites
    val m3: Map[Fixed8, Any] = m + (f -> 42)
    assert(m3.size === 1)
    val m4 = m.removed(f)
    assert(m4.size === 0)
    val m5 = m.updated(f, "updated")
    assert(m5.get(f) === Some("updated"))
    assert(m.schema() != null)
  }

  @Test
  def testFixed8ToStringMap_classMixinDef(): Unit = {
    assert(Fixed8ToStringMap(Fixed8(bytesFixed8) -> "v").classMixinDef === None)
  }

  @Test
  def testFixed8ToStringMap_companionMixinDef(): Unit = {
    assert(Fixed8ToStringMap.companionMixinDef === None)
  }

  @Test
  def testFixed8ToStringMap_build(): Unit = {
    val f = Fixed8(bytesFixed8)
    val m = Fixed8ToStringMap(f -> "v")
    val rebuilt = Fixed8ToStringMap.build(roundTrip(m.data()), DataConversion.SetReadOnly)
    assert(rebuilt.get(f) === Some("v"))
  }

  // ---------------------------------------------------------------------------
  // CustomIntMap — 21 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testCustomIntMap_plus_and_operations(): Unit = {
    val ci = CustomInt(1)
    val m = CustomIntMap("k1" -> ci)
    val m2 = m + ("k2" -> CustomInt(2))
    assert(m2.size === 2)
    val m3: Map[String, Any] = m + ("k3" -> "notInt")
    assert(m3.size === 2)
    val m4 = m.removed("k1")
    assert(m4.size === 0)
    val m5 = m.updated("k1", CustomInt(99))
    assert(m5.get("k1") === Some(CustomInt(99)))
    assert(m.schema() != null)
  }

  @Test
  def testCustomIntMap_classMixinDef(): Unit = {
    assert(CustomIntMap("k" -> CustomInt(1)).classMixinDef === None)
  }

  @Test
  def testCustomIntMap_companionMixinDef(): Unit = {
    assert(CustomIntMap.companionMixinDef === None)
  }

  @Test
  def testCustomIntMap_build(): Unit = {
    val m = CustomIntMap("k" -> CustomInt(5))
    val rebuilt = CustomIntMap.build(roundTrip(m.data()), DataConversion.SetReadOnly)
    assert(rebuilt.get("k") === Some(CustomInt(5)))
  }

  // ---------------------------------------------------------------------------
  // CustomIntToStringMap — 12 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testCustomIntToStringMap_plus_and_operations(): Unit = {
    val ci = CustomInt(1)
    val m = CustomIntToStringMap(ci -> "v1")
    val m2 = m + (CustomInt(2) -> "v2")
    assert(m2.size === 2)
    val m3 = m.removed(ci)
    assert(m3.size === 0)
    val m4 = m.updated(ci, "updated")
    assert(m4.get(ci) === Some("updated"))
    assert(m.schema() != null)
  }

  @Test
  def testCustomIntToStringMap_classMixinDef(): Unit = {
    assert(CustomIntToStringMap(CustomInt(1) -> "v").classMixinDef === None)
  }

  @Test
  def testCustomIntToStringMap_companionMixinDef(): Unit = {
    assert(CustomIntToStringMap.companionMixinDef === None)
  }

  // ---------------------------------------------------------------------------
  // CustomIntArray — 14 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testCustomIntArray_classMixinDef(): Unit = {
    assert(CustomIntArray(CustomInt(1)).classMixinDef === None)
  }

  @Test
  def testCustomIntArray_companionMixinDef(): Unit = {
    assert(CustomIntArray.companionMixinDef === None)
  }

  @Test
  def testCustomIntArray_productArity(): Unit = {
    val arr = CustomIntArray(CustomInt(1), CustomInt(2))
    assert(arr.productArity === 2)
    assert(arr.productElement(0) != null)
  }

  @Test
  def testCustomIntArray_wrapImplicit(): Unit = {
    val items: Iterable[CustomInt] = List(CustomInt(1), CustomInt(2))
    val arr: CustomIntArray = items
    assert(arr.length === 2)
  }

  // ---------------------------------------------------------------------------
  // IntIdArray — 14 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testIntIdArray_classMixinDef(): Unit = {
    assert(IntIdArray(org.coursera.courier.generator.customtypes.IntId(1)).classMixinDef === None)
  }

  @Test
  def testIntIdArray_companionMixinDef(): Unit = {
    assert(IntIdArray.companionMixinDef === None)
  }

  @Test
  def testIntIdArray_productArity(): Unit = {
    val arr = IntIdArray(
      org.coursera.courier.generator.customtypes.IntId(1),
      org.coursera.courier.generator.customtypes.IntId(2))
    assert(arr.productArity === 2)
  }

  // ---------------------------------------------------------------------------
  // IntIdToStringMap — 21 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testIntIdToStringMap_plus_and_operations(): Unit = {
    val id1 = org.coursera.courier.generator.customtypes.IntId(1)
    val id2 = org.coursera.courier.generator.customtypes.IntId(2)
    val m = IntIdToStringMap(id1 -> "v1")
    val m2 = m + (id2 -> "v2")
    assert(m2.size === 2)
    val m3 = m.removed(id1)
    assert(m3.size === 0)
    val m4 = m.updated(id1, "updated")
    assert(m4.get(id1) === Some("updated"))
    assert(m.schema() != null)
  }

  @Test
  def testIntIdToStringMap_classMixinDef(): Unit = {
    val id = org.coursera.courier.generator.customtypes.IntId(1)
    assert(IntIdToStringMap(id -> "v").classMixinDef === None)
  }

  @Test
  def testIntIdToStringMap_companionMixinDef(): Unit = {
    assert(IntIdToStringMap.companionMixinDef === None)
  }

  @Test
  def testIntIdToStringMap_build(): Unit = {
    val id = org.coursera.courier.generator.customtypes.IntId(1)
    val m = IntIdToStringMap(id -> "v")
    val rebuilt = IntIdToStringMap.build(roundTrip(m.data()), DataConversion.SetReadOnly)
    assert(rebuilt.size === 1)
  }

  // ---------------------------------------------------------------------------
  // IntIdMap — 12 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testIntIdMap_plus_and_operations2(): Unit = {
    val id1 = org.coursera.courier.generator.customtypes.IntId(1)
    val id2 = org.coursera.courier.generator.customtypes.IntId(2)
    val m = IntIdMap("k1" -> id1)
    val m2 = m + ("k2" -> id2)
    assert(m2.size === 2)
    val m3 = m.updated("k1", id2)
    assert(m3.get("k1") === Some(id2))
    assert(m.schema() != null)
  }

  @Test
  def testIntIdMap_classMixinDef(): Unit = {
    val id = org.coursera.courier.generator.customtypes.IntId(1)
    assert(IntIdMap("k" -> id).classMixinDef === None)
  }

  @Test
  def testIntIdMap_companionMixinDef(): Unit = {
    assert(IntIdMap.companionMixinDef === None)
  }

  // ---------------------------------------------------------------------------
  // CustomRecordArray — 12 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testCustomRecordArray_classMixinDef(): Unit = {
    assert(CustomRecordArray(customRecord).classMixinDef === None)
  }

  @Test
  def testCustomRecordArray_companionMixinDef(): Unit = {
    assert(CustomRecordArray.companionMixinDef === None)
  }

  @Test
  def testCustomRecordArray_productArity(): Unit = {
    val arr = CustomRecordArray(customRecord, customRecord)
    assert(arr.productArity === 2)
    assert(arr.productElement(0) != null)
  }

  // ---------------------------------------------------------------------------
  // CustomRecordToCustomRecordMap — 24 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testCustomRecordToCustomRecordMap_plus_and_operations(): Unit = {
    val cr2 = CustomRecord("t2", "b2")
    val m = CustomRecordToCustomRecordMap(customRecord -> cr2)
    val cr3 = CustomRecord("t3", "b3")
    val m2 = m + (cr3 -> cr2)
    assert(m2.size === 2)
    val m3 = m.removed(customRecord)
    assert(m3.size === 0)
    val m4 = m.updated(customRecord, cr3)
    assert(m4.get(customRecord) === Some(cr3))
    assert(m.schema() != null)
  }

  @Test
  def testCustomRecordToCustomRecordMap_classMixinDef(): Unit = {
    val cr2 = CustomRecord("t2", "b2")
    assert(CustomRecordToCustomRecordMap(customRecord -> cr2).classMixinDef === None)
  }

  @Test
  def testCustomRecordToCustomRecordMap_companionMixinDef(): Unit = {
    assert(CustomRecordToCustomRecordMap.companionMixinDef === None)
  }

  @Test
  def testCustomRecordToCustomRecordMap_build(): Unit = {
    val cr2 = CustomRecord("t2", "b2")
    val m = CustomRecordToCustomRecordMap(customRecord -> cr2)
    val rebuilt = CustomRecordToCustomRecordMap.build(roundTrip(m.data()), DataConversion.SetReadOnly)
    assert(rebuilt.size === 1)
  }

  // ---------------------------------------------------------------------------
  // CustomArrayTestIdArray — 14 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testCustomArrayTestIdArray_classMixinDef(): Unit = {
    val id = org.coursera.courier.generator.customtypes.CustomArrayTestId(1)
    assert(CustomArrayTestIdArray(id).classMixinDef === None)
  }

  @Test
  def testCustomArrayTestIdArray_companionMixinDef(): Unit = {
    assert(CustomArrayTestIdArray.companionMixinDef === None)
  }

  @Test
  def testCustomArrayTestIdArray_productArity(): Unit = {
    val id = org.coursera.courier.generator.customtypes.CustomArrayTestId(1)
    val arr = CustomArrayTestIdArray(id, id)
    assert(arr.productArity === 2)
  }

  // ---------------------------------------------------------------------------
  // FruitsArray — 14 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testFruitsArray_classMixinDef(): Unit = {
    assert(FruitsArray(Fruits.APPLE).classMixinDef === None)
  }

  @Test
  def testFruitsArray_companionMixinDef(): Unit = {
    assert(FruitsArray.companionMixinDef === None)
  }

  @Test
  def testFruitsArray_productArity(): Unit = {
    val arr = FruitsArray(Fruits.APPLE, Fruits.PINEAPPLE)
    assert(arr.productArity === 2)
    assert(arr.productElement(0) != null)
  }

  @Test
  def testFruitsArray_wrapImplicit(): Unit = {
    val items: Iterable[Fruits] = List(Fruits.APPLE)
    val arr: FruitsArray = items
    assert(arr.length === 1)
  }

  // ---------------------------------------------------------------------------
  // Fixed8Array — 14 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testFixed8Array_classMixinDef(): Unit = {
    assert(Fixed8Array(Fixed8(bytesFixed8)).classMixinDef === None)
  }

  @Test
  def testFixed8Array_companionMixinDef(): Unit = {
    assert(Fixed8Array.companionMixinDef === None)
  }

  @Test
  def testFixed8Array_productArity(): Unit = {
    val arr = Fixed8Array(Fixed8(bytesFixed8), Fixed8(bytesFixed8))
    assert(arr.productArity === 2)
    assert(arr.productElement(0) != null)
  }

  @Test
  def testFixed8Array_wrapImplicit(): Unit = {
    val items: Iterable[Fixed8] = List(Fixed8(bytesFixed8))
    val arr: Fixed8Array = items
    assert(arr.length === 1)
  }

  // ---------------------------------------------------------------------------
  // DefaultLiteralEscaping — 14 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testDefaultLiteralEscaping_classMixinDef(): Unit = {
    assert(DefaultLiteralEscaping("x").classMixinDef === None)
  }

  @Test
  def testDefaultLiteralEscaping_companionMixinDef(): Unit = {
    assert(DefaultLiteralEscaping.companionMixinDef === None)
  }

  @Test
  def testDefaultLiteralEscaping_equality(): Unit = {
    val a = DefaultLiteralEscaping("x")
    val b = DefaultLiteralEscaping("x")
    val c = DefaultLiteralEscaping("y")
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testDefaultLiteralEscaping_copy(): Unit = {
    val r = DefaultLiteralEscaping("x")
    val copied = r.copy(stringField = "updated")
    assert(copied.stringField === "updated")
  }

  @Test
  def testDefaultLiteralEscaping_productArity(): Unit = {
    val r = DefaultLiteralEscaping("x")
    assert(r.productArity === 1)
    assert(r.productElement(0) === "x")
  }

  @Test
  def testDefaultLiteralEscaping_toString(): Unit = {
    assert(DefaultLiteralEscaping("x").toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // ReservedClassFieldEscaping — 13 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testReservedClassFieldEscaping_classMixinDef(): Unit = {
    assert(ReservedClassFieldEscaping("d", "s", "c", "cl").classMixinDef === None)
  }

  @Test
  def testReservedClassFieldEscaping_companionMixinDef(): Unit = {
    assert(ReservedClassFieldEscaping.companionMixinDef === None)
  }

  @Test
  def testReservedClassFieldEscaping_equality(): Unit = {
    val a = ReservedClassFieldEscaping("x", "s", "c", "cl")
    val b = ReservedClassFieldEscaping("x", "s", "c", "cl")
    val c = ReservedClassFieldEscaping("y", "s", "c", "cl")
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testReservedClassFieldEscaping_copy(): Unit = {
    val r = ReservedClassFieldEscaping("original", "s", "c", "cl")
    val copied = r.copy(`data$` = "updated")
    assert(copied.`data$` === "updated")
  }

  @Test
  def testReservedClassFieldEscaping_productArity(): Unit = {
    val r = ReservedClassFieldEscaping("x", "s", "c", "cl")
    assert(r.productArity === 4)
    assert(r.productElement(0) === "x")
  }

  @Test
  def testReservedClassFieldEscaping_toString(): Unit = {
    assert(ReservedClassFieldEscaping("x", "s", "c", "cl").toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithFixed8 — 12 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithFixed8_classMixinDef(): Unit = {
    assert(org.coursera.fixed.WithFixed8(Fixed8(bytesFixed8)).classMixinDef === None)
  }

  @Test
  def testWithFixed8_companionMixinDef(): Unit = {
    assert(org.coursera.fixed.WithFixed8.companionMixinDef === None)
  }

  @Test
  def testWithFixed8_productElement(): Unit = {
    val r = org.coursera.fixed.WithFixed8(Fixed8(bytesFixed8))
    assert(r.productArity === 1)
    assert(r.productElement(0) === Fixed8(bytesFixed8))
  }

  @Test
  def testWithFixed8_toString(): Unit = {
    assert(org.coursera.fixed.WithFixed8(Fixed8(bytesFixed8)).toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // DeprecatedRecord — 10 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testDeprecatedRecord_classMixinDef(): Unit = {
    assert(DeprecatedRecord("x", "y").classMixinDef === None)
  }

  @Test
  def testDeprecatedRecord_companionMixinDef(): Unit = {
    assert(DeprecatedRecord.companionMixinDef === None)
  }

  @Test
  def testDeprecatedRecord_equality(): Unit = {
    val a = DeprecatedRecord("x", "y")
    val b = DeprecatedRecord("x", "y")
    val c = DeprecatedRecord("z", "y")
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testDeprecatedRecord_copy(): Unit = {
    val r = DeprecatedRecord("original", "f2")
    val copied = r.copy(field1 = "updated")
    assert(copied.field1 === "updated")
    assert(copied.field2 === "f2")
  }

  @Test
  def testDeprecatedRecord_productArity(): Unit = {
    val r = DeprecatedRecord("x", "y")
    assert(r.productArity === 2)
    assert(r.productElement(0) === "x")
  }

  @Test
  def testDeprecatedRecord_toString(): Unit = {
    assert(DeprecatedRecord("x", "y").toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // Toggle / ToggleToStringMap — 2+10 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testToggle_classMixinDef(): Unit = {
    assert(Toggle.UP.classMixinDef === None)
  }

  @Test
  def testToggle_companionMixinDef(): Unit = {
    assert(Toggle.companionMixinDef === None)
  }

  @Test
  def testToggleToStringMap_plus_and_operations(): Unit = {
    val m = ToggleToStringMap(Toggle.UP -> "v1")
    val m2 = m + (Toggle.DOWN -> "v2")
    assert(m2.size === 2)
    val m3 = m.removed(Toggle.UP)
    assert(m3.size === 0)
    val m4 = m.updated(Toggle.UP, "updated")
    assert(m4.get(Toggle.UP) === Some("updated"))
    assert(m.schema() != null)
  }

  @Test
  def testToggleToStringMap_classMixinDef(): Unit = {
    assert(ToggleToStringMap(Toggle.UP -> "v").classMixinDef === None)
  }

  @Test
  def testToggleToStringMap_companionMixinDef(): Unit = {
    assert(ToggleToStringMap.companionMixinDef === None)
  }

  @Test
  def testToggleToStringMap_build(): Unit = {
    val m = ToggleToStringMap(Toggle.UP -> "v")
    val rebuilt = ToggleToStringMap.build(roundTrip(m.data()), DataConversion.SetReadOnly)
    assert(rebuilt.size === 1)
  }

  // ---------------------------------------------------------------------------
  // EnumProperties / EmptyEnum — 1+2 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testEmptyEnum_classMixinDef(): Unit = {
    assert(EmptyEnum.$UNKNOWN.classMixinDef === None)
  }

  @Test
  def testEmptyEnum_companionMixinDef(): Unit = {
    assert(EmptyEnum.companionMixinDef === None)
  }

  @Test
  def testEnumProperties_classMixinDef(): Unit = {
    assert(EnumProperties.APPLE.classMixinDef === None)
  }

  // ---------------------------------------------------------------------------
  // WithCustomTypesMapUnionArray uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithCustomTypesArrayUnionArray_classMixinDef(): Unit = {
    assert(org.coursera.arrays.WithCustomTypesArrayUnionArray().classMixinDef === None)
  }

  @Test
  def testWithCustomTypesArrayUnionArray_companionMixinDef(): Unit = {
    assert(org.coursera.arrays.WithCustomTypesArrayUnionArray.companionMixinDef === None)
  }

  @Test
  def testWithCustomTypesArrayUnionArray_productArity(): Unit = {
    val arr = org.coursera.arrays.WithCustomTypesArrayUnionArray(
      org.coursera.arrays.WithCustomTypesArrayUnion.IntMember(1),
      org.coursera.arrays.WithCustomTypesArrayUnion.StringMember("s"))
    assert(arr.productArity === 2)
    assert(arr.productElement(0) != null)
  }

  // ---------------------------------------------------------------------------
  // WithComplexTypesMapUnionMap — 10 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithComplexTypesMapUnionMap_classMixinDef(): Unit = {
    assert(WithComplexTypesMapUnionMap().classMixinDef === None)
  }

  @Test
  def testWithComplexTypesMapUnionMap_companionMixinDef(): Unit = {
    assert(WithComplexTypesMapUnionMap.companionMixinDef === None)
  }

  @Test
  def testWithComplexTypesMapUnionMap_plus_and_operations(): Unit = {
    val m = WithComplexTypesMapUnionMap("k1" -> WithComplexTypesMapUnion.IntMember(1))
    val m2 = m + ("k2" -> WithComplexTypesMapUnion.StringMember("s"))
    assert(m2.size === 2)
    val m3: Map[String, Any] = m + ("k3" -> "notUnion")
    assert(m3.size === 2)
    val m4 = m.removed("k1")
    assert(m4.size === 0)
    val m5 = m.updated("k1", WithComplexTypesMapUnion.StringMember("updated"))
    assert(m5.get("k1").exists(_.isInstanceOf[WithComplexTypesMapUnion.StringMember]))
    assert(m.schema() != null)
  }

  // ---------------------------------------------------------------------------
  // WithPrimitiveTyperefsUnion and WithPrimitiveTyperefsUnion.Union — 14+9 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithPrimitiveTyperefsUnion_classMixinDef(): Unit = {
    assert(WithPrimitiveTyperefsUnion(WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(1))).classMixinDef === None)
  }

  @Test
  def testWithPrimitiveTyperefsUnion_companionMixinDef(): Unit = {
    assert(WithPrimitiveTyperefsUnion.companionMixinDef === None)
  }

  @Test
  def testWithPrimitiveTyperefsUnion_equality(): Unit = {
    val ci = CustomInt(1)
    val a = WithPrimitiveTyperefsUnion(WithPrimitiveTyperefsUnion.Union.CustomIntMember(ci))
    val b = WithPrimitiveTyperefsUnion(WithPrimitiveTyperefsUnion.Union.CustomIntMember(ci))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithPrimitiveTyperefsUnion_copy(): Unit = {
    val r = WithPrimitiveTyperefsUnion(WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(1)))
    val copied = r.copy(union = WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(99)))
    assert(copied.union.asInstanceOf[WithPrimitiveTyperefsUnion.Union.CustomIntMember].value === CustomInt(99))
  }

  @Test
  def testWithPrimitiveTyperefsUnion_productArity(): Unit = {
    val r = WithPrimitiveTyperefsUnion(WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(1)))
    assert(r.productArity === 1)
  }

  @Test
  def testWithPrimitiveTyperefsUnion_toString(): Unit = {
    val r = WithPrimitiveTyperefsUnion(WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(1)))
    assert(r.toString.nonEmpty)
  }

  @Test
  def testWithPrimitiveTyperefsUnion_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknown", "val")
    dataMap.makeReadOnly()
    val built = WithPrimitiveTyperefsUnion.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithPrimitiveTyperefsUnion.Union.$UnknownMember])
  }

  @Test
  def testWithPrimitiveTyperefsUnion_union_classMixinDef(): Unit = {
    assert(WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(1)).classMixinDef === None)
  }

  @Test
  def testWithPrimitiveTyperefsUnion_union_equality(): Unit = {
    val a = WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(1))
    val b = WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(1))
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithPrimitiveTyperefsUnion_union_unionCompanion(): Unit = {
    assert(WithPrimitiveTyperefsUnion.Union.CustomIntMember.unionCompanion eq WithPrimitiveTyperefsUnion.Union)
  }

  @Test
  def testWithPrimitiveTyperefsUnion_union_companionMixinDef(): Unit = {
    assert(WithPrimitiveTyperefsUnion.Union.companionMixinDef === None)
  }

  @Test
  def testWithPrimitiveTyperefsUnion_unapply(): Unit = {
    val r = WithPrimitiveTyperefsUnion(WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(5)))
    val WithPrimitiveTyperefsUnion(union) = r
    assert(union.isInstanceOf[WithPrimitiveTyperefsUnion.Union.CustomIntMember])
  }

  // ---------------------------------------------------------------------------
  // Union members in WithPrimitivesUnion.Union — 13 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithPrimitivesUnion_union_byteStringMember(): Unit = {
    val m = WithPrimitivesUnion.Union.ByteStringMember(bytes1)
    assert(m.value === bytes1)
    assert(m._1 === bytes1)
    assert(m.declaringTyperefSchema === None)
    assert(WithPrimitivesUnion.Union.ByteStringMember.unionCompanion eq WithPrimitivesUnion.Union)
  }

  @Test
  def testWithPrimitivesUnion_union_classMixinDef(): Unit = {
    assert(WithPrimitivesUnion.Union.IntMember(1).classMixinDef === None)
  }

  @Test
  def testWithPrimitivesUnion_union_companionMixinDef(): Unit = {
    assert(WithPrimitivesUnion.Union.companionMixinDef === None)
  }

  @Test
  def testWithPrimitivesUnion_union_booleanMember_declaringTyperefSchema(): Unit = {
    assert(WithPrimitivesUnion.Union.BooleanMember(true).declaringTyperefSchema === None)
    assert(WithPrimitivesUnion.Union.BooleanMember.unionCompanion eq WithPrimitivesUnion.Union)
  }

  @Test
  def testWithPrimitivesUnion_union_intMember_meta(): Unit = {
    val m = WithPrimitivesUnion.Union.IntMember(5)
    assert(m._1 === 5)
    assert(m.declaringTyperefSchema === None)
    assert(WithPrimitivesUnion.Union.IntMember.unionCompanion eq WithPrimitivesUnion.Union)
  }

  @Test
  def testWithPrimitivesUnion_union_longMember_meta(): Unit = {
    val m = WithPrimitivesUnion.Union.LongMember(100L)
    assert(m._1 === 100L)
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testWithPrimitivesUnion_union_floatMember_meta(): Unit = {
    val m = WithPrimitivesUnion.Union.FloatMember(1.5f)
    assert(m._1 === 1.5f)
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testWithPrimitivesUnion_union_doubleMember_meta(): Unit = {
    val m = WithPrimitivesUnion.Union.DoubleMember(2.5d)
    assert(m._1 === 2.5d)
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testWithPrimitivesUnion_union_stringMember_meta(): Unit = {
    val m = WithPrimitivesUnion.Union.StringMember("hello")
    assert(m._1 === "hello")
    assert(m.declaringTyperefSchema === None)
  }

  // ---------------------------------------------------------------------------
  // WithRecordCustomTypeUnion.Union — 8 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithRecordCustomTypeUnion_union_classMixinDef(): Unit = {
    assert(WithRecordCustomTypeUnion.Union.CustomRecordMember(customRecord).classMixinDef === None)
  }

  @Test
  def testWithRecordCustomTypeUnion_union_companionMixinDef(): Unit = {
    assert(WithRecordCustomTypeUnion.Union.companionMixinDef === None)
  }

  @Test
  def testWithRecordCustomTypeUnion_union_member_meta(): Unit = {
    val m = WithRecordCustomTypeUnion.Union.CustomRecordMember(customRecord)
    assert(m._1 === customRecord)
    assert(m.declaringTyperefSchema === None)
    assert(WithRecordCustomTypeUnion.Union.CustomRecordMember.unionCompanion eq WithRecordCustomTypeUnion.Union)
  }

  @Test
  def testWithRecordCustomTypeUnion_union_equality(): Unit = {
    val a = WithRecordCustomTypeUnion.Union.CustomRecordMember(customRecord)
    val b = WithRecordCustomTypeUnion.Union.CustomRecordMember(customRecord)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  // ---------------------------------------------------------------------------
  // WithComplexTypesUnion.Union — 8 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithComplexTypesUnion_union_classMixinDef(): Unit = {
    assert(WithComplexTypesUnion.Union.EmptyMember(Empty()).classMixinDef === None)
  }

  @Test
  def testWithComplexTypesUnion_union_companionMixinDef(): Unit = {
    assert(WithComplexTypesUnion.Union.companionMixinDef === None)
  }

  @Test
  def testWithComplexTypesUnion_union_member_meta(): Unit = {
    val m = WithComplexTypesUnion.Union.EmptyMember(Empty())
    assert(m._1 === Empty())
    assert(m.declaringTyperefSchema === None)
    assert(WithComplexTypesUnion.Union.EmptyMember.unionCompanion eq WithComplexTypesUnion.Union)
  }

  @Test
  def testWithComplexTypesUnion_union_fruits_member_meta(): Unit = {
    val m = WithComplexTypesUnion.Union.FruitsMember(Fruits.APPLE)
    assert(m._1 === Fruits.APPLE)
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testWithComplexTypesUnion_union_simpleArray_meta(): Unit = {
    val m = WithComplexTypesUnion.Union.SimpleArrayMember(SimpleArray())
    assert(m._1 === SimpleArray())
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testWithComplexTypesUnion_union_simpleMap_meta(): Unit = {
    val m = WithComplexTypesUnion.Union.SimpleMapMember(SimpleMap())
    assert(m._1 === SimpleMap())
    assert(m.declaringTyperefSchema === None)
  }

  // ---------------------------------------------------------------------------
  // WithPrimitiveCustomTypesUnion.Union — 7 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithPrimitiveCustomTypesUnion_union_classMixinDef(): Unit = {
    assert(WithPrimitiveCustomTypesUnion.Union.CustomIntMember(CustomInt(1)).classMixinDef === None)
  }

  @Test
  def testWithPrimitiveCustomTypesUnion_union_companionMixinDef(): Unit = {
    assert(WithPrimitiveCustomTypesUnion.Union.companionMixinDef === None)
  }

  @Test
  def testWithPrimitiveCustomTypesUnion_union_member_meta(): Unit = {
    val m = WithPrimitiveCustomTypesUnion.Union.CustomIntMember(CustomInt(42))
    assert(m._1 === CustomInt(42))
    assert(m.declaringTyperefSchema === None)
    assert(WithPrimitiveCustomTypesUnion.Union.CustomIntMember.unionCompanion eq WithPrimitiveCustomTypesUnion.Union)
  }

  // ---------------------------------------------------------------------------
  // WithCustomUnionTestId.Union — 7 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithCustomUnionTestId_union_classMixinDef(): Unit = {
    val id = org.coursera.courier.generator.customtypes.CustomUnionTestId(1)
    assert(WithCustomUnionTestId.Union.CustomUnionTestIdMember(id).classMixinDef === None)
  }

  @Test
  def testWithCustomUnionTestId_union_companionMixinDef(): Unit = {
    assert(WithCustomUnionTestId.Union.companionMixinDef === None)
  }

  @Test
  def testWithCustomUnionTestId_union_member_meta(): Unit = {
    val id = org.coursera.courier.generator.customtypes.CustomUnionTestId(42)
    val m = WithCustomUnionTestId.Union.CustomUnionTestIdMember(id)
    assert(m._1 === id)
    assert(m.declaringTyperefSchema === None)
  }

  // ---------------------------------------------------------------------------
  // WithEmptyUnion.Union — 6 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithEmptyUnion_union_classMixinDef(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("x", "y")
    dataMap.makeReadOnly()
    val unknown = WithEmptyUnion.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(unknown.classMixinDef === None)
  }

  @Test
  def testWithEmptyUnion_union_companionMixinDef(): Unit = {
    assert(WithEmptyUnion.Union.companionMixinDef === None)
  }

  @Test
  def testWithEmptyUnion_union_equality(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("x", "y")
    dataMap.makeReadOnly()
    val a = WithEmptyUnion.Union.build(dataMap, DataConversion.SetReadOnly)
    val b = WithEmptyUnion.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  // ---------------------------------------------------------------------------
  // WithCustomUnionTestId / WithPrimitivesUnion classMixinDef
  // ---------------------------------------------------------------------------

  @Test
  def testWithCustomUnionTestId_classMixinDef(): Unit = {
    val id = org.coursera.courier.generator.customtypes.CustomUnionTestId(1)
    assert(WithCustomUnionTestId(WithCustomUnionTestId.Union.CustomUnionTestIdMember(id)).classMixinDef === None)
  }

  @Test
  def testWithCustomUnionTestId_companionMixinDef(): Unit = {
    assert(WithCustomUnionTestId.companionMixinDef === None)
  }

  @Test
  def testWithPrimitivesUnion_classMixinDef(): Unit = {
    assert(WithPrimitivesUnion(WithPrimitivesUnion.Union.IntMember(1)).classMixinDef === None)
  }

  @Test
  def testWithPrimitivesUnion_companionMixinDef(): Unit = {
    assert(WithPrimitivesUnion.companionMixinDef === None)
  }

  @Test
  def testWithPrimitiveCustomTypesUnion_classMixinDef(): Unit = {
    assert(WithPrimitiveCustomTypesUnion(WithPrimitiveCustomTypesUnion.Union.CustomIntMember(CustomInt(1))).classMixinDef === None)
  }

  @Test
  def testWithPrimitiveCustomTypesUnion_companionMixinDef(): Unit = {
    assert(WithPrimitiveCustomTypesUnion.companionMixinDef === None)
  }

  @Test
  def testWithComplexTypesUnion_classMixinDef(): Unit = {
    assert(WithComplexTypesUnion(WithComplexTypesUnion.Union.EmptyMember(Empty())).classMixinDef === None)
  }

  @Test
  def testWithComplexTypesUnion_companionMixinDef(): Unit = {
    assert(WithComplexTypesUnion.companionMixinDef === None)
  }

  @Test
  def testWithRecordCustomTypeUnion_classMixinDef(): Unit = {
    assert(WithRecordCustomTypeUnion(WithRecordCustomTypeUnion.Union.CustomRecordMember(customRecord)).classMixinDef === None)
  }

  @Test
  def testWithRecordCustomTypeUnion_companionMixinDef(): Unit = {
    assert(WithRecordCustomTypeUnion.companionMixinDef === None)
  }

  // ---------------------------------------------------------------------------
  // Typeref union members — FlatTypedDefinition/TypedDefinition MessageMember
  // ---------------------------------------------------------------------------

  @Test
  def testFlatTypedDefinition_messageMember_meta(): Unit = {
    val msg = Message(Some("title"), Some("body"))
    val m = FlatTypedDefinition.MessageMember(msg)
    assert(m.value === msg)
    assert(m._1 === msg)
    assert(m.declaringTyperefSchema.isDefined)
    assert(FlatTypedDefinition.MessageMember.unionCompanion eq FlatTypedDefinition)
  }

  @Test
  def testFlatTypedDefinition_messageMember_equality(): Unit = {
    val msg = Message(Some("t"), Some("b"))
    val a = FlatTypedDefinition.MessageMember(msg)
    val b = FlatTypedDefinition.MessageMember(msg)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testFlatTypedDefinition_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknown", "val")
    dataMap.makeReadOnly()
    val built = FlatTypedDefinition.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[FlatTypedDefinition.$UnknownMember])
    assert(built.asInstanceOf[FlatTypedDefinition.$UnknownMember].declaringTyperefSchema.isDefined)
  }

  @Test
  def testFlatTypedDefinition_classMixinDef(): Unit = {
    val msg = Message(Some("t"), Some("b"))
    assert(FlatTypedDefinition.MessageMember(msg).classMixinDef === None)
  }

  @Test
  def testFlatTypedDefinition_companionMixinDef(): Unit = {
    assert(FlatTypedDefinition.companionMixinDef === None)
  }

  @Test
  def testTypedDefinition_messageMember_meta(): Unit = {
    val msg = Message(Some("title"), Some("body"))
    val m = TypedDefinition.MessageMember(msg)
    assert(m.value === msg)
    assert(m._1 === msg)
    assert(m.declaringTyperefSchema.isDefined)
    assert(TypedDefinition.MessageMember.unionCompanion eq TypedDefinition)
  }

  @Test
  def testTypedDefinition_messageMember_equality(): Unit = {
    val msg = Message(Some("t"), Some("b"))
    val a = TypedDefinition.MessageMember(msg)
    val b = TypedDefinition.MessageMember(msg)
    assert(a === b)
  }

  @Test
  def testTypedDefinition_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknown", "val")
    dataMap.makeReadOnly()
    val built = TypedDefinition.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[TypedDefinition.$UnknownMember])
    assert(built.asInstanceOf[TypedDefinition.$UnknownMember].declaringTyperefSchema.isDefined)
  }

  @Test
  def testTypedDefinition_classMixinDef(): Unit = {
    val msg = Message(Some("t"), Some("b"))
    assert(TypedDefinition.MessageMember(msg).classMixinDef === None)
  }

  @Test
  def testTypedDefinition_companionMixinDef(): Unit = {
    assert(TypedDefinition.companionMixinDef === None)
  }

  // ---------------------------------------------------------------------------
  // Union / UnionTyperef / UnionWithInlineRecord — typeref union types
  // ---------------------------------------------------------------------------

  @Test
  def testUnion_classMixinDef(): Unit = {
    assert(Union.NoteMember(Note("x")).classMixinDef === None)
  }

  @Test
  def testUnion_companionMixinDef(): Unit = {
    assert(Union.companionMixinDef === None)
  }

  @Test
  def testUnion_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknown", "val")
    dataMap.makeReadOnly()
    val built = Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[Union.$UnknownMember])
    assert(built.asInstanceOf[Union.$UnknownMember].declaringTyperefSchema.isDefined)
  }

  @Test
  def testUnionTyperef_classMixinDef(): Unit = {
    assert(UnionTyperef.StringMember("x").classMixinDef === None)
  }

  @Test
  def testUnionTyperef_companionMixinDef(): Unit = {
    assert(UnionTyperef.companionMixinDef === None)
  }

  @Test
  def testUnionTyperef_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknown", "val")
    dataMap.makeReadOnly()
    val built = UnionTyperef.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[UnionTyperef.$UnknownMember])
  }

  @Test
  def testUnionWithInlineRecord_classMixinDef(): Unit = {
    assert(UnionWithInlineRecord.InlineRecord2Member(InlineRecord2()).classMixinDef === None)
  }

  @Test
  def testUnionWithInlineRecord_companionMixinDef(): Unit = {
    assert(UnionWithInlineRecord.companionMixinDef === None)
  }

  @Test
  def testUnionWithInlineRecord_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknown", "val")
    dataMap.makeReadOnly()
    val built = UnionWithInlineRecord.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[UnionWithInlineRecord.$UnknownMember])
    assert(built.asInstanceOf[UnionWithInlineRecord.$UnknownMember].declaringTyperefSchema.isDefined)
  }

  @Test
  def testUnionWithInlineRecord_inlineRecordMember_meta(): Unit = {
    val ir = TyperefsInlineRecord(Some(5))
    val m = UnionWithInlineRecord.InlineRecordMember(ir)
    assert(m._1 === ir)
    assert(m.declaringTyperefSchema.isDefined)
    assert(UnionWithInlineRecord.InlineRecordMember.unionCompanion eq UnionWithInlineRecord)
  }

  @Test
  def testUnionWithInlineRecord_inlineRecord2Member_meta(): Unit = {
    val m = UnionWithInlineRecord.InlineRecord2Member(InlineRecord2())
    assert(m._1 === InlineRecord2())
    assert(m.declaringTyperefSchema.isDefined)
    assert(UnionWithInlineRecord.InlineRecord2Member.unionCompanion eq UnionWithInlineRecord)
  }

  @Test
  def testUnionTyperef_intMember_meta(): Unit = {
    val m = UnionTyperef.IntMember(5)
    assert(m._1 === 5)
    assert(m.declaringTyperefSchema.isDefined)
    assert(UnionTyperef.IntMember.unionCompanion eq UnionTyperef)
  }

  @Test
  def testUnionTyperef_stringMember_meta(): Unit = {
    val m = UnionTyperef.StringMember("x")
    assert(m._1 === "x")
    assert(m.declaringTyperefSchema.isDefined)
    assert(UnionTyperef.StringMember.unionCompanion eq UnionTyperef)
  }

  // ---------------------------------------------------------------------------
  // org.example — Apostrophe, TyperefExample, record, MagicEightBall classMixinDef
  // ---------------------------------------------------------------------------

  @Test
  def testApostrophe_classMixinDef(): Unit = {
    assert(Apostrophe(1).classMixinDef === None)
  }

  @Test
  def testApostrophe_companionMixinDef(): Unit = {
    assert(Apostrophe.companionMixinDef === None)
  }

  @Test
  def testApostrophe_equality(): Unit = {
    val a = Apostrophe(1)
    val b = Apostrophe(1)
    val c = Apostrophe(2)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testApostrophe_copy(): Unit = {
    val r = Apostrophe(1)
    val copied = r.copy(field = 99)
    assert(copied.field === 99)
  }

  @Test
  def testApostrophe_productArity(): Unit = {
    val r = Apostrophe(1)
    assert(r.productArity === 1)
    assert(r.productElement(0) === 1)
  }

  @Test
  def testApostrophe_toString(): Unit = {
    assert(Apostrophe(1).toString.nonEmpty)
  }

  @Test
  def testTyperefExample_classMixinDef(): Unit = {
    assert(TyperefExample(1000L).classMixinDef === None)
  }

  @Test
  def testTyperefExample_companionMixinDef(): Unit = {
    assert(TyperefExample.companionMixinDef === None)
  }

  @Test
  def testTyperefExample_equality(): Unit = {
    val a = TyperefExample(1000L)
    val b = TyperefExample(1000L)
    val c = TyperefExample(2000L)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testTyperefExample_copy(): Unit = {
    val r = TyperefExample(1000L)
    val copied = r.copy(time = 9999L)
    assert(copied.time === 9999L)
  }

  @Test
  def testTyperefExample_productArity(): Unit = {
    val r = TyperefExample(1000L)
    assert(r.productArity === 1)
    assert(r.productElement(0) === 1000L)
  }

  @Test
  def testTyperefExample_toString(): Unit = {
    assert(TyperefExample(1000L).toString.nonEmpty)
  }

  @Test
  def testMagicEightBall_classMixinDef(): Unit = {
    assert(MagicEightBall("Q?", MagicEightBallAnswer.IT_IS_CERTAIN).classMixinDef === None)
  }

  @Test
  def testMagicEightBall_companionMixinDef(): Unit = {
    assert(MagicEightBall.companionMixinDef === None)
  }

  @Test
  def testMagicEightBall_productArity(): Unit = {
    val r = MagicEightBall("Q?", MagicEightBallAnswer.IT_IS_CERTAIN)
    assert(r.productArity === 2)
    assert(r.productElement(0) === "Q?")
  }

  @Test
  def testMagicEightBall_toString(): Unit = {
    assert(MagicEightBall("Q", MagicEightBallAnswer.IT_IS_CERTAIN).toString.nonEmpty)
  }

  @Test
  def testFortuneCookie_classMixinDef(): Unit = {
    assert(FortuneCookie("msg", luckyNumbers = IntArray(1)).classMixinDef === None)
  }

  @Test
  def testFortuneCookie_companionMixinDef(): Unit = {
    assert(FortuneCookie.companionMixinDef === None)
  }

  @Test
  def testFortuneCookie_productArity(): Unit = {
    val fc = FortuneCookie("msg", Some(0.5f), IntArray(1))
    assert(fc.productArity === 3)
    assert(fc.productElement(0) === "msg")
  }

  @Test
  def testFortuneCookie_toString(): Unit = {
    assert(FortuneCookie("msg", luckyNumbers = IntArray(1)).toString.nonEmpty)
  }

  @Test
  def testFortuneTelling_classMixinDef(): Unit = {
    assert(FortuneTelling.StringMember("x").classMixinDef === None)
  }

  @Test
  def testFortuneTelling_companionMixinDef(): Unit = {
    assert(FortuneTelling.companionMixinDef === None)
  }

  @Test
  def testFortuneTelling_memberMeta(): Unit = {
    val cookie = FortuneCookie("msg", luckyNumbers = IntArray(1))
    val m = FortuneTelling.FortuneCookieMember(cookie)
    assert(m.declaringTyperefSchema !== null) // typeref, so schema is present
    val s = FortuneTelling.StringMember("x")
    assert(s._1 === "x")
    assert(s.declaringTyperefSchema !== null)
  }

  @Test
  def testFortuneTelling_unknownMember_declaringTyperefSchema(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownType", "someValue")
    dataMap.makeReadOnly()
    val built = FortuneTelling.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[FortuneTelling.$UnknownMember])
    // $UnknownMember.declaringTyperefSchema
    assert(built.asInstanceOf[FortuneTelling.$UnknownMember].declaringTyperefSchema !== null)
  }

  // WithoutNamespace is in default package and cannot be imported from a named package - skipped

  // ---------------------------------------------------------------------------
  // primitivestyle records
  // ---------------------------------------------------------------------------

  @Test
  def testPrimSimple_classMixinDef(): Unit = {
    assert(PrimSimple(Some("hello")).classMixinDef === None)
  }

  @Test
  def testPrimSimple_companionMixinDef(): Unit = {
    assert(PrimSimple.companionMixinDef === None)
  }

  @Test
  def testPrimSimple_equality(): Unit = {
    val a = PrimSimple(Some("hello"))
    val b = PrimSimple(Some("hello"))
    val c = PrimSimple(Some("world"))
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testPrimSimple_copy(): Unit = {
    val r = PrimSimple(Some("hello"))
    val copied = r.copy(message = Some("updated"))
    assert(copied.message === Some("updated"))
  }

  @Test
  def testPrimSimple_productArity(): Unit = {
    val r = PrimSimple(Some("hello"))
    assert(r.productArity === 1)
    assert(r.productElement(0) === Some("hello"))
  }

  @Test
  def testPrimSimple_toString(): Unit = {
    assert(PrimSimple(Some("hello")).toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithComplexTypesMapUnion.SimpleMember — 5 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testWithComplexTypesMapUnion_simpleMember_meta(): Unit = {
    val m = WithComplexTypesMapUnion.SimpleMember(simpleRecord)
    assert(m._1 === simpleRecord)
    assert(m.declaringTyperefSchema.isDefined)
    assert(WithComplexTypesMapUnion.SimpleMember.unionCompanion eq WithComplexTypesMapUnion)
  }

  @Test
  def testWithComplexTypesMapUnion_simpleMember_equality(): Unit = {
    val a = WithComplexTypesMapUnion.SimpleMember(simpleRecord)
    val b = WithComplexTypesMapUnion.SimpleMember(simpleRecord)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithComplexTypesMapUnion_classMixinDef(): Unit = {
    assert(WithComplexTypesMapUnion.IntMember(1).classMixinDef === None)
  }

  @Test
  def testWithComplexTypesMapUnion_companionMixinDef(): Unit = {
    assert(WithComplexTypesMapUnion.companionMixinDef === None)
  }

  // ---------------------------------------------------------------------------
  // records.test union member meta — WithComplexTypes/WithOptionalComplexTypes etc.
  // ---------------------------------------------------------------------------

  @Test
  def testWithComplexTypes_union_intMember_meta(): Unit = {
    val m = WithComplexTypes.Union.IntMember(5)
    assert(m._1 === 5)
    assert(m.declaringTyperefSchema === None)
    assert(WithComplexTypes.Union.IntMember.unionCompanion eq WithComplexTypes.Union)
  }

  @Test
  def testWithComplexTypes_union_stringMember_meta(): Unit = {
    val m = WithComplexTypes.Union.StringMember("x")
    assert(m._1 === "x")
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testWithComplexTypes_union_simpleMember_meta(): Unit = {
    val m = WithComplexTypes.Union.SimpleMember(simpleRecord)
    assert(m._1 === simpleRecord)
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testWithComplexTypes_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknown", "val")
    dataMap.makeReadOnly()
    val built = WithComplexTypes.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithComplexTypes.Union.$UnknownMember])
    assert(built.asInstanceOf[WithComplexTypes.Union.$UnknownMember].declaringTyperefSchema === None)
  }

  @Test
  def testWithComplexTypes_union_classMixinDef(): Unit = {
    assert(WithComplexTypes.Union.IntMember(1).classMixinDef === None)
  }

  @Test
  def testWithComplexTypes_union_companionMixinDef(): Unit = {
    assert(WithComplexTypes.Union.companionMixinDef === None)
  }

  @Test
  def testWithOptionalComplexTypes_union_intMember_meta(): Unit = {
    val m = WithOptionalComplexTypes.Union.IntMember(5)
    assert(m._1 === 5)
    assert(m.declaringTyperefSchema === None)
    assert(WithOptionalComplexTypes.Union.IntMember.unionCompanion eq WithOptionalComplexTypes.Union)
  }

  @Test
  def testWithOptionalComplexTypes_union_simpleMember_meta(): Unit = {
    val m = WithOptionalComplexTypes.Union.SimpleMember(simpleRecord)
    assert(m._1 === simpleRecord)
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testWithOptionalComplexTypes_union_stringMember_meta(): Unit = {
    val m = WithOptionalComplexTypes.Union.StringMember("x")
    assert(m._1 === "x")
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testWithOptionalComplexTypes_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknown", "val")
    dataMap.makeReadOnly()
    val built = WithOptionalComplexTypes.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithOptionalComplexTypes.Union.$UnknownMember])
    assert(built.asInstanceOf[WithOptionalComplexTypes.Union.$UnknownMember].declaringTyperefSchema === None)
  }

  @Test
  def testWithOptionalComplexTypes_union_classMixinDef(): Unit = {
    assert(WithOptionalComplexTypes.Union.IntMember(1).classMixinDef === None)
  }

  @Test
  def testWithOptionalComplexTypes_union_companionMixinDef(): Unit = {
    assert(WithOptionalComplexTypes.Union.companionMixinDef === None)
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_union_simpleMember_meta(): Unit = {
    val m = WithOptionalComplexTypesDefaultNone.Union.SimpleMember(simpleRecord)
    assert(m._1 === simpleRecord)
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_union_stringMember_meta(): Unit = {
    val m = WithOptionalComplexTypesDefaultNone.Union.StringMember("x")
    assert(m._1 === "x")
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_union_intMember_meta(): Unit = {
    val m = WithOptionalComplexTypesDefaultNone.Union.IntMember(5)
    assert(m._1 === 5)
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknown", "val")
    dataMap.makeReadOnly()
    val built = WithOptionalComplexTypesDefaultNone.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithOptionalComplexTypesDefaultNone.Union.$UnknownMember])
    assert(built.asInstanceOf[WithOptionalComplexTypesDefaultNone.Union.$UnknownMember].declaringTyperefSchema === None)
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_union_classMixinDef(): Unit = {
    assert(WithOptionalComplexTypesDefaultNone.Union.IntMember(1).classMixinDef === None)
  }

  @Test
  def testWithOptionalComplexTypesDefaultNone_union_companionMixinDef(): Unit = {
    assert(WithOptionalComplexTypesDefaultNone.Union.companionMixinDef === None)
  }

  @Test
  def testWithComplexTypeDefaults_union_intMember_meta(): Unit = {
    val m = WithComplexTypeDefaults.Union.IntMember(5)
    assert(m._1 === 5)
    assert(m.declaringTyperefSchema === None)
    assert(WithComplexTypeDefaults.Union.IntMember.unionCompanion eq WithComplexTypeDefaults.Union)
  }

  @Test
  def testWithComplexTypeDefaults_union_simpleMember_meta(): Unit = {
    val m = WithComplexTypeDefaults.Union.SimpleMember(simpleRecord)
    assert(m._1 === simpleRecord)
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testWithComplexTypeDefaults_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknown", "val")
    dataMap.makeReadOnly()
    val built = WithComplexTypeDefaults.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithComplexTypeDefaults.Union.$UnknownMember])
    assert(built.asInstanceOf[WithComplexTypeDefaults.Union.$UnknownMember].declaringTyperefSchema === None)
  }

  @Test
  def testWithComplexTypeDefaults_union_classMixinDef(): Unit = {
    assert(WithComplexTypeDefaults.Union.IntMember(1).classMixinDef === None)
  }

  @Test
  def testWithComplexTypeDefaults_union_companionMixinDef(): Unit = {
    assert(WithComplexTypeDefaults.Union.companionMixinDef === None)
  }

  @Test
  def testWithOptionalComplexTypeDefaults_union_intMember_meta(): Unit = {
    val m = WithOptionalComplexTypeDefaults.Union.IntMember(5)
    assert(m._1 === 5)
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testWithOptionalComplexTypeDefaults_union_simpleMember_meta(): Unit = {
    val m = WithOptionalComplexTypeDefaults.Union.SimpleMember(simpleRecord)
    assert(m._1 === simpleRecord)
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testWithOptionalComplexTypeDefaults_union_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknown", "val")
    dataMap.makeReadOnly()
    val built = WithOptionalComplexTypeDefaults.Union.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithOptionalComplexTypeDefaults.Union.$UnknownMember])
    assert(built.asInstanceOf[WithOptionalComplexTypeDefaults.Union.$UnknownMember].declaringTyperefSchema === None)
  }

  @Test
  def testWithOptionalComplexTypeDefaults_union_classMixinDef(): Unit = {
    assert(WithOptionalComplexTypeDefaults.Union.IntMember(1).classMixinDef === None)
  }

  @Test
  def testWithOptionalComplexTypeDefaults_union_companionMixinDef(): Unit = {
    assert(WithOptionalComplexTypeDefaults.Union.companionMixinDef === None)
  }

  // ---------------------------------------------------------------------------
  // DoubleToStringMap / IntArrayToStringMap / FloatToStringMap / ByteStringToStringMap
  // — courier.data package, 6 each
  // ---------------------------------------------------------------------------

  @Test
  def testDoubleToStringMap_plus_schema(): Unit = {
    val m = DoubleToStringMap(1.0d -> "v")
    val m2 = m + (2.0d -> "v2")
    assert(m2.size === 2)
    assert(m.schema() != null)
    val m3 = m.removed(1.0d)
    assert(m3.size === 0)
    assert(m.classMixinDef === None)
    assert(DoubleToStringMap.companionMixinDef === None)
  }

  @Test
  def testIntArrayToStringMap_plus_schema(): Unit = {
    val arr = IntArray(1, 2)
    val m = IntArrayToStringMap(arr -> "v")
    assert(m.schema() != null)
    assert(m.classMixinDef === None)
    assert(IntArrayToStringMap.companionMixinDef === None)
  }

  @Test
  def testFloatToStringMap_plus_schema(): Unit = {
    val m = FloatToStringMap(1.0f -> "v")
    val m2 = m + (2.0f -> "v2")
    assert(m2.size === 2)
    assert(m.schema() != null)
    val m3 = m.removed(1.0f)
    assert(m3.size === 0)
    assert(m.classMixinDef === None)
    assert(FloatToStringMap.companionMixinDef === None)
  }

  @Test
  def testByteStringToStringMap_plus_schema(): Unit = {
    val m = ByteStringToStringMap(bytes1 -> "v")
    val m2 = m + (bytes1 -> "v2")
    assert(m2.size === 1) // same key
    assert(m.schema() != null)
    val m3 = m.removed(bytes1)
    assert(m3.size === 0)
    assert(m.classMixinDef === None)
    assert(ByteStringToStringMap.companionMixinDef === None)
  }

  // ---------------------------------------------------------------------------
  // records.test.packaging.Empty — 9 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testPackagingEmpty_classMixinDef(): Unit = {
    assert(org.coursera.records.test.packaging.Empty().classMixinDef === None)
  }

  @Test
  def testPackagingEmpty_companionMixinDef(): Unit = {
    assert(org.coursera.records.test.packaging.Empty.companionMixinDef === None)
  }

  @Test
  def testPackagingEmpty_equality(): Unit = {
    val a = org.coursera.records.test.packaging.Empty()
    val b = org.coursera.records.test.packaging.Empty()
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testPackagingEmpty_toString(): Unit = {
    assert(org.coursera.records.test.packaging.Empty().toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // Empty2 — 8 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testEmpty2_classMixinDef(): Unit = {
    assert(org.coursera.records.test.Empty2().classMixinDef === None)
  }

  @Test
  def testEmpty2_companionMixinDef(): Unit = {
    assert(org.coursera.records.test.Empty2.companionMixinDef === None)
  }

  @Test
  def testEmpty2_equality2(): Unit = {
    val a = org.coursera.records.test.Empty2()
    val b = org.coursera.records.test.Empty2()
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  // ---------------------------------------------------------------------------
  // SimpleArray / SimpleArrayArray / SimpleMapArray — 7 each
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleArray_classMixinDef(): Unit = {
    assert(SimpleArray(Simple(Some("x"))).classMixinDef === None)
  }

  @Test
  def testSimpleArray_companionMixinDef(): Unit = {
    assert(SimpleArray.companionMixinDef === None)
  }

  @Test
  def testSimpleArray_productArity(): Unit = {
    val arr = SimpleArray(Simple(Some("a")), Simple(Some("b")))
    assert(arr.productArity === 2)
    assert(arr.productElement(0) != null)
  }

  @Test
  def testSimpleArrayArray_classMixinDef(): Unit = {
    assert(SimpleArrayArray(SimpleArray()).classMixinDef === None)
  }

  @Test
  def testSimpleArrayArray_companionMixinDef(): Unit = {
    assert(SimpleArrayArray.companionMixinDef === None)
  }

  @Test
  def testSimpleArrayArray_productArity(): Unit = {
    val arr = SimpleArrayArray(SimpleArray(), SimpleArray())
    assert(arr.productArity === 2)
    assert(arr.productElement(0) != null)
  }

  @Test
  def testSimpleMapArray_classMixinDef(): Unit = {
    assert(SimpleMapArray(SimpleMap()).classMixinDef === None)
  }

  @Test
  def testSimpleMapArray_companionMixinDef(): Unit = {
    assert(SimpleMapArray.companionMixinDef === None)
  }

  @Test
  def testSimpleMapArray_productArity(): Unit = {
    val arr = SimpleMapArray(SimpleMap(), SimpleMap())
    assert(arr.productArity === 2)
    assert(arr.productElement(0) != null)
  }

  // ---------------------------------------------------------------------------
  // SimpleMap — 9 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleMap_plus_and_operations(): Unit = {
    val m = SimpleMap("k1" -> Simple(Some("v1")))
    val m2 = m + ("k2" -> Simple(Some("v2")))
    assert(m2.size === 2)
    val m3: Map[String, Any] = m + ("k3" -> "notSimple")
    assert(m3.size === 2)
    val m4 = m.removed("k1")
    assert(m4.size === 0)
    assert(m.schema() != null)
  }

  @Test
  def testSimpleMap_classMixinDef(): Unit = {
    assert(SimpleMap("k" -> Simple(Some("v"))).classMixinDef === None)
  }

  @Test
  def testSimpleMap_companionMixinDef(): Unit = {
    assert(SimpleMap.companionMixinDef === None)
  }

  // ---------------------------------------------------------------------------
  // InlineRecord (records.test) — 20 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testTestInlineRecord_classMixinDef(): Unit = {
    assert(TestInlineRecord(1).classMixinDef === None)
  }

  @Test
  def testTestInlineRecord_companionMixinDef(): Unit = {
    assert(TestInlineRecord.companionMixinDef === None)
  }

  @Test
  def testTestInlineRecord_equality(): Unit = {
    val a = TestInlineRecord(1)
    val b = TestInlineRecord(1)
    val c = TestInlineRecord(2)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testTestInlineRecord_copy(): Unit = {
    val r = TestInlineRecord(1)
    val copied = r.copy(value = 99)
    assert(copied.value === 99)
  }

  @Test
  def testTestInlineRecord_productArity(): Unit = {
    val r = TestInlineRecord(5)
    assert(r.productArity === 1)
    assert(r.productElement(0) === 5)
  }

  @Test
  def testTestInlineRecord_toString(): Unit = {
    assert(TestInlineRecord(1).toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // records.WithInclude — 12 uncovered
  // ---------------------------------------------------------------------------

  @Test
  def testRecordsWithInclude_classMixinDef(): Unit = {
    assert(WithInclude("x", 1).classMixinDef === None)
  }

  @Test
  def testRecordsWithInclude_companionMixinDef(): Unit = {
    assert(WithInclude.companionMixinDef === None)
  }

  @Test
  def testRecordsWithInclude_equality(): Unit = {
    val a = WithInclude("x", 1)
    val b = WithInclude("x", 1)
    val c = WithInclude("y", 2)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testRecordsWithInclude_copy(): Unit = {
    val r = WithInclude("original", 1)
    val copied = r.copy(find = "updated")
    assert(copied.find === "updated")
    assert(copied.direct === 1)
  }

  @Test
  def testRecordsWithInclude_toString(): Unit = {
    assert(WithInclude("x", 1).toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // records.primitivestyle.WithPrimitives / WithComplexTypes — 12 each
  // ---------------------------------------------------------------------------

  @Test
  def testPrimWithPrimitives_classMixinDef(): Unit = {
    assert(PrimWithPrimitives(1, 2L, 3.0f, 4.0d, true, "s", bytes1).classMixinDef === None)
  }

  @Test
  def testPrimWithPrimitives_companionMixinDef(): Unit = {
    assert(PrimWithPrimitives.companionMixinDef === None)
  }

  @Test
  def testPrimWithPrimitives_equality(): Unit = {
    val a = PrimWithPrimitives(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    val b = PrimWithPrimitives(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testPrimWithPrimitives_copy(): Unit = {
    val r = PrimWithPrimitives(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    val copied = r.copy(intField = 99)
    assert(copied.intField === 99)
  }

  @Test
  def testPrimWithPrimitives_toString(): Unit = {
    assert(PrimWithPrimitives(1, 2L, 3.0f, 4.0d, true, "s", bytes1).toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithCaseClassCustomType — 21 uncovered
  // ---------------------------------------------------------------------------

  private def makeWithCaseClassCustomType =
    WithCaseClassCustomType(
      ShortId(1), ByteId(0x01), CharId('a'), IntId(2), LongId(3L),
      FloatId(1.0f), DoubleId(2.0d), StringId("s"), BooleanId(true), BoxedIntId(4),
      IntIdMap("k" -> IntId(5)), IntIdToStringMap(IntId(6) -> "v"),
      IntIdArray(IntId(7)), StringIdWrapper(StringId("w")), CaseClassCustomIntWrapper(CustomInt(8)))

  @Test
  def testWithCaseClassCustomType_classMixinDef(): Unit = {
    assert(makeWithCaseClassCustomType.classMixinDef === None)
  }

  @Test
  def testWithCaseClassCustomType_companionMixinDef(): Unit = {
    assert(WithCaseClassCustomType.companionMixinDef === None)
  }

  @Test
  def testWithCaseClassCustomType_equality(): Unit = {
    val a = makeWithCaseClassCustomType
    val b = makeWithCaseClassCustomType
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithCaseClassCustomType_copy(): Unit = {
    val r = makeWithCaseClassCustomType
    val copied = r.copy(int = IntId(99))
    assert(copied.int === IntId(99))
  }

  @Test
  def testWithCaseClassCustomType_toString(): Unit = {
    assert(makeWithCaseClassCustomType.toString.nonEmpty)
  }
}
