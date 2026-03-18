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

import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.deprecated.DeprecatedRecord
import org.coursera.enums.EmptyEnum
import org.coursera.escaping.DefaultLiteralEscaping
import org.coursera.escaping.ReservedClassFieldEscaping
import org.coursera.maps.Toggle
import org.coursera.maps.ToggleToStringMap
import org.coursera.records.CourierFile
import org.coursera.records.Message
import org.coursera.records.Note
import org.coursera.records.test.NumericDefaults
import org.coursera.records.test.RecursivelyDefinedRecord
import org.coursera.records.test.With22Fields
import org.coursera.records.test.With23Fields
import org.coursera.records.test.WithCourierFile
import org.coursera.records.test.WithOmitField
import org.junit.Test

class MiscRecordGeneratorTest extends GeneratorTest with SchemaFixtures {

  // ---------------------------------------------------------------------------
  // DeprecatedRecord
  // ---------------------------------------------------------------------------

  @Test
  def testDeprecatedRecord_construction(): Unit = {
    val original = DeprecatedRecord("f1", "f2")
    assert(original.field1 === "f1")
    assert(original.field2 === "f2")
  }

  @Test
  def testDeprecatedRecord_roundTrip(): Unit = {
    val original = DeprecatedRecord("hello", "world")
    val roundTripped =
      DeprecatedRecord.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testDeprecatedRecord_equality(): Unit = {
    val a = DeprecatedRecord("x", "y")
    val b = DeprecatedRecord("x", "y")
    val c = DeprecatedRecord("x", "z")
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testDeprecatedRecord_toString(): Unit = {
    val r = DeprecatedRecord("a", "b")
    assert(r.toString.nonEmpty)
  }

  @Test
  def testDeprecatedRecord_copy(): Unit = {
    val original = DeprecatedRecord("a", "b")
    val copied = original.copy(field2 = "c")
    assert(copied.field1 === "a")
    assert(copied.field2 === "c")
  }

  @Test
  def testDeprecatedRecord_unapply(): Unit = {
    val r = DeprecatedRecord("p", "q")
    val DeprecatedRecord(f1, f2) = r
    assert(f1 === "p")
    assert(f2 === "q")
  }

  @Test
  def testDeprecatedRecord_productArity(): Unit = {
    val r = DeprecatedRecord("a", "b")
    assert(r.productArity === 2)
    assert(r.productElement(0) === "a")
    assert(r.productElement(1) === "b")
  }

  @Test
  def testDeprecatedRecord_productElement_outOfBounds(): Unit = {
    val r = DeprecatedRecord("a", "b")
    intercept[IndexOutOfBoundsException] {
      r.productElement(5)
    }
  }

  // ---------------------------------------------------------------------------
  // EmptyEnum
  // ---------------------------------------------------------------------------

  @Test
  def testEmptyEnum_withName_unknown(): Unit = {
    val result = EmptyEnum.withName("NONEXISTENT")
    assert(result === EmptyEnum.$UNKNOWN)
  }

  @Test
  def testEmptyEnum_symbols(): Unit = {
    // EmptyEnum has no defined symbols (only $UNKNOWN)
    assert(EmptyEnum.symbols.contains(EmptyEnum.$UNKNOWN))
  }

  // ---------------------------------------------------------------------------
  // Toggle enum
  // ---------------------------------------------------------------------------

  @Test
  def testToggle_values(): Unit = {
    assert(Toggle.withName("UP") === Toggle.UP)
    assert(Toggle.withName("DOWN") === Toggle.DOWN)
    assert(Toggle.withName("UNKNOWN_VAL") === Toggle.$UNKNOWN)
  }

  @Test
  def testToggle_symbols(): Unit = {
    assert(Toggle.symbols.contains(Toggle.UP))
    assert(Toggle.symbols.contains(Toggle.DOWN))
  }

  @Test
  def testToggle_toString(): Unit = {
    assert(Toggle.UP.toString === "UP")
    assert(Toggle.DOWN.toString === "DOWN")
  }

  // ---------------------------------------------------------------------------
  // ToggleToStringMap
  // ---------------------------------------------------------------------------

  @Test
  def testToggleToStringMap_operations(): Unit = {
    val m = ToggleToStringMap(Toggle.UP -> "up", Toggle.DOWN -> "down")
    assert(m.get(Toggle.UP) === Some("up"))
    assert(m.get(Toggle.DOWN) === Some("down"))
    assert((m - Toggle.UP).size === 1)
    assert((m + (Toggle.$UNKNOWN -> "x")).size === 3)
    assert(m.iterator.toSeq.size === 2)
  }

  @Test
  def testToggleToStringMap_empty(): Unit = {
    val m = ToggleToStringMap.empty
    assert(m.isEmpty)
  }

  @Test
  def testToggleToStringMap_dataBuilder(): Unit = {
    val builder = ToggleToStringMap.newBuilder
    builder += (Toggle.UP -> "up")
    builder += (Toggle.DOWN -> "dn")
    val result = builder.result()
    assert(result.size === 2)
    assert(result.get(Toggle.UP) === Some("up"))
  }

  @Test
  def testToggleToStringMap_dataBuilder_clear(): Unit = {
    val builder = ToggleToStringMap.newBuilder
    builder += (Toggle.UP -> "up")
    builder.clear()
    val result = builder.result()
    assert(result.isEmpty)
  }

  @Test
  def testToggleToStringMap_build_roundTrip(): Unit = {
    val original = ToggleToStringMap(Toggle.UP -> "up")
    val rebuilt = ToggleToStringMap.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === rebuilt)
  }

  // ---------------------------------------------------------------------------
  // NumericDefaults
  // ---------------------------------------------------------------------------

  @Test
  def testNumericDefaults_defaults(): Unit = {
    val r = NumericDefaults()
    assert(r.i === 2147483647)
    assert(r.l === 9223372036854775807L)
    assert(r.f === 3.4028233E38f)
    assert(r.d === 1.7976931348623157E308d)
  }

  @Test
  def testNumericDefaults_explicit(): Unit = {
    val r = NumericDefaults(i = 1, l = 2L, f = 3.0f, d = 4.0d)
    assert(r.i === 1)
    assert(r.l === 2L)
    assert(r.f === 3.0f)
    assert(r.d === 4.0d)
  }

  @Test
  def testNumericDefaults_roundTrip(): Unit = {
    val original = NumericDefaults(i = 42, l = 100L, f = 1.5f, d = 2.5d)
    val roundTripped =
      NumericDefaults.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testNumericDefaults_equality(): Unit = {
    val a = NumericDefaults(1, 2L, 3.0f, 4.0d)
    val b = NumericDefaults(1, 2L, 3.0f, 4.0d)
    val c = NumericDefaults(2, 2L, 3.0f, 4.0d)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testNumericDefaults_copy(): Unit = {
    val original = NumericDefaults(1, 2L, 3.0f, 4.0d)
    val copied = original.copy(i = 99)
    assert(copied.i === 99)
    assert(copied.l === 2L)
  }

  @Test
  def testNumericDefaults_unapply(): Unit = {
    val r = NumericDefaults(5, 6L, 7.0f, 8.0d)
    val NumericDefaults(i, l, f, d) = r
    assert(i === 5)
    assert(l === 6L)
  }

  @Test
  def testNumericDefaults_productArity(): Unit = {
    val r = NumericDefaults(1, 2L, 3.0f, 4.0d)
    assert(r.productArity === 4)
    assert(r.productElement(0) === 1)
    assert(r.productElement(1) === 2L)
    assert(r.productElement(2) === 3.0f)
    assert(r.productElement(3) === 4.0d)
  }

  @Test
  def testNumericDefaults_productElement_outOfBounds(): Unit = {
    val r = NumericDefaults()
    intercept[IndexOutOfBoundsException] {
      r.productElement(10)
    }
  }

  // ---------------------------------------------------------------------------
  // With22Fields
  // ---------------------------------------------------------------------------

  @Test
  def testWith22Fields_construction(): Unit = {
    val r = With22Fields(
      1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
      11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
    assert(r.field1 === 1)
    assert(r.field22 === 22)
  }

  @Test
  def testWith22Fields_roundTrip(): Unit = {
    val original = With22Fields(
      1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
      11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
    val roundTripped =
      With22Fields.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWith22Fields_unapply(): Unit = {
    val r = With22Fields(
      1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
      11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
    val With22Fields(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10,
                    f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22) = r
    assert(f1 === 1)
    assert(f22 === 22)
  }

  @Test
  def testWith22Fields_productArity(): Unit = {
    val r = With22Fields(
      1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
      11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
    assert(r.productArity === 22)
    for (i <- 0 until 22) {
      assert(r.productElement(i) === (i + 1))
    }
  }

  @Test
  def testWith22Fields_copy(): Unit = {
    val r = With22Fields(
      1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
      11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
    val copied = r.copy(field1 = 99)
    assert(copied.field1 === 99)
    assert(copied.field22 === 22)
  }

  @Test
  def testWith22Fields_equality(): Unit = {
    val a = With22Fields(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
    val b = With22Fields(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
    val c = With22Fields(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  // ---------------------------------------------------------------------------
  // With23Fields (beyond Scala tuple limit)
  // ---------------------------------------------------------------------------

  @Test
  def testWith23Fields_construction(): Unit = {
    val r = With23Fields(
      1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
      11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23)
    assert(r.field1 === 1)
    assert(r.field23 === 23)
  }

  @Test
  def testWith23Fields_roundTrip(): Unit = {
    val original = With23Fields(
      1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
      11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23)
    val roundTripped =
      With23Fields.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWith23Fields_equality(): Unit = {
    val a = With23Fields(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23)
    val b = With23Fields(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23)
    assert(a === b)
  }

  @Test
  def testWith23Fields_copy(): Unit = {
    val r = With23Fields(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23)
    val copied = r.copy(field1 = 99)
    assert(copied.field1 === 99)
    assert(copied.field23 === 23)
  }

  // ---------------------------------------------------------------------------
  // DefaultLiteralEscaping
  // ---------------------------------------------------------------------------

  @Test
  def testDefaultLiteralEscaping_defaults(): Unit = {
    val r = DefaultLiteralEscaping()
    // Default has triple-quote in it
    assert(r.stringField.contains("\"\"\""))
  }

  @Test
  def testDefaultLiteralEscaping_construction(): Unit = {
    val r = DefaultLiteralEscaping(stringField = "hello")
    assert(r.stringField === "hello")
  }

  @Test
  def testDefaultLiteralEscaping_roundTrip(): Unit = {
    val original = DefaultLiteralEscaping(stringField = "world")
    val roundTripped =
      DefaultLiteralEscaping.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testDefaultLiteralEscaping_equality(): Unit = {
    val a = DefaultLiteralEscaping("x")
    val b = DefaultLiteralEscaping("x")
    val c = DefaultLiteralEscaping("y")
    assert(a === b)
    assert(a !== c)
  }

  @Test
  def testDefaultLiteralEscaping_copy(): Unit = {
    val r = DefaultLiteralEscaping("original")
    val copied = r.copy(stringField = "modified")
    assert(copied.stringField === "modified")
  }

  @Test
  def testDefaultLiteralEscaping_unapply(): Unit = {
    val r = DefaultLiteralEscaping("test")
    val DefaultLiteralEscaping(s) = r
    assert(s === "test")
  }

  // ---------------------------------------------------------------------------
  // ReservedClassFieldEscaping - additional coverage
  // ---------------------------------------------------------------------------

  @Test
  def testReservedClassFieldEscaping_roundTrip(): Unit = {
    val original = ReservedClassFieldEscaping("d", "s", "c", "cl")
    val roundTripped =
      ReservedClassFieldEscaping.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testReservedClassFieldEscaping_unapply(): Unit = {
    val r = ReservedClassFieldEscaping("d", "s", "c", "cl")
    val ReservedClassFieldEscaping(data, schema, copy, clone) = r
    assert(data === "d")
    assert(schema === "s")
    assert(copy === "c")
    assert(clone === "cl")
  }

  @Test
  def testReservedClassFieldEscaping_copy(): Unit = {
    val r = ReservedClassFieldEscaping("d", "s", "c", "cl")
    val copied = r.copy(data$ = "newData")
    assert(copied.data$ === "newData")
    assert(copied.schema$ === "s")
  }

  @Test
  def testReservedClassFieldEscaping_equality(): Unit = {
    val a = ReservedClassFieldEscaping("d", "s", "c", "cl")
    val b = ReservedClassFieldEscaping("d", "s", "c", "cl")
    val c = ReservedClassFieldEscaping("x", "s", "c", "cl")
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testReservedClassFieldEscaping_productArity(): Unit = {
    val r = ReservedClassFieldEscaping("d", "s", "c", "cl")
    assert(r.productArity === 4)
    assert(r.productElement(0) === "d")
    assert(r.productElement(1) === "s")
    assert(r.productElement(2) === "c")
    assert(r.productElement(3) === "cl")
  }

  // ---------------------------------------------------------------------------
  // Message, Note (org.coursera.records)
  // ---------------------------------------------------------------------------

  @Test
  def testMessage_construction(): Unit = {
    val m = Message(title = Some("Hello"), body = Some("World"))
    assert(m.title === Some("Hello"))
    assert(m.body === Some("World"))
  }

  @Test
  def testMessage_optionalNone(): Unit = {
    val m = Message()
    assert(m.title === None)
    assert(m.body === None)
  }

  @Test
  def testMessage_roundTrip(): Unit = {
    val original = Message(Some("t"), Some("b"))
    val roundTripped =
      Message.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testMessage_equality(): Unit = {
    val a = Message(Some("t"), Some("b"))
    val b = Message(Some("t"), Some("b"))
    val c = Message(Some("t"), None)
    assert(a === b)
    assert(a !== c)
  }

  @Test
  def testMessage_copy(): Unit = {
    val m = Message(Some("title"), Some("body"))
    val copied = m.copy(body = None)
    assert(copied.title === Some("title"))
    assert(copied.body === None)
  }

  @Test
  def testMessage_unapply(): Unit = {
    val m = Message(Some("t"), Some("b"))
    val Message(title, body) = m
    assert(title === Some("t"))
    assert(body === Some("b"))
  }

  @Test
  def testNote_construction(): Unit = {
    val n = Note(text = "some text")
    assert(n.text === "some text")
  }

  @Test
  def testNote_roundTrip(): Unit = {
    val original = Note("hello")
    val roundTripped = Note.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testNote_equality(): Unit = {
    val a = Note("x")
    val b = Note("x")
    val c = Note("y")
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
    val n = Note("hello")
    val Note(t) = n
    assert(t === "hello")
  }

  // ---------------------------------------------------------------------------
  // CourierFile
  // ---------------------------------------------------------------------------

  @Test
  def testCourierFile_construction(): Unit = {
    val cf = CourierFile(find = "file.courier")
    assert(cf.find === "file.courier")
  }

  @Test
  def testCourierFile_roundTrip(): Unit = {
    val original = CourierFile(find = "test.courier")
    val roundTripped =
      CourierFile.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testCourierFile_equality(): Unit = {
    val a = CourierFile("a.courier")
    val b = CourierFile("a.courier")
    val c = CourierFile("b.courier")
    assert(a === b)
    assert(a !== c)
  }

  @Test
  def testCourierFile_copy(): Unit = {
    val cf = CourierFile("original.courier")
    val copied = cf.copy(find = "updated.courier")
    assert(copied.find === "updated.courier")
  }

  // ---------------------------------------------------------------------------
  // WithCourierFile (org.coursera.records.test)
  // ---------------------------------------------------------------------------

  @Test
  def testWithCourierFile_construction(): Unit = {
    val cf = CourierFile(find = "test.courier")
    val r = WithCourierFile(courierFile = cf)
    assert(r.courierFile === cf)
  }

  @Test
  def testWithCourierFile_roundTrip(): Unit = {
    val original = WithCourierFile(CourierFile("f.courier"))
    val roundTripped =
      WithCourierFile.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithCourierFile_equality(): Unit = {
    val a = WithCourierFile(CourierFile("a"))
    val b = WithCourierFile(CourierFile("a"))
    val c = WithCourierFile(CourierFile("b"))
    assert(a === b)
    assert(a !== c)
  }

  // ---------------------------------------------------------------------------
  // RecursivelyDefinedRecord
  // ---------------------------------------------------------------------------

  @Test
  def testRecursivelyDefinedRecord_empty(): Unit = {
    val r = RecursivelyDefinedRecord()
    assert(r.self === None)
  }

  @Test
  def testRecursivelyDefinedRecord_recursive(): Unit = {
    val inner = RecursivelyDefinedRecord()
    val outer = RecursivelyDefinedRecord(self = Some(inner))
    assert(outer.self === Some(inner))
    assert(outer.self.get.self === None)
  }

  @Test
  def testRecursivelyDefinedRecord_roundTrip(): Unit = {
    val inner = RecursivelyDefinedRecord()
    val original = RecursivelyDefinedRecord(Some(inner))
    val roundTripped =
      RecursivelyDefinedRecord.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testRecursivelyDefinedRecord_equality(): Unit = {
    val a = RecursivelyDefinedRecord()
    val b = RecursivelyDefinedRecord()
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testRecursivelyDefinedRecord_copy(): Unit = {
    val r = RecursivelyDefinedRecord()
    val inner = RecursivelyDefinedRecord()
    val copied = r.copy(self = Some(inner))
    assert(copied.self === Some(inner))
  }

  @Test
  def testRecursivelyDefinedRecord_unapply(): Unit = {
    val inner = RecursivelyDefinedRecord()
    val r = RecursivelyDefinedRecord(Some(inner))
    val RecursivelyDefinedRecord(s) = r
    assert(s === Some(inner))
  }

  // ---------------------------------------------------------------------------
  // WithOmitField
  // ---------------------------------------------------------------------------

  @Test
  def testWithOmitField_construction(): Unit = {
    val r = WithOmitField(keep = 42, keepCustom = CustomInt(7))
    assert(r.keep === 42)
    assert(r.keepCustom === CustomInt(7))
  }

  @Test
  def testWithOmitField_roundTrip(): Unit = {
    val original = WithOmitField(1, CustomInt(2))
    val roundTripped =
      WithOmitField.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithOmitField_equality(): Unit = {
    val a = WithOmitField(1, CustomInt(2))
    val b = WithOmitField(1, CustomInt(2))
    val c = WithOmitField(3, CustomInt(2))
    assert(a === b)
    assert(a !== c)
  }

  @Test
  def testWithOmitField_copy(): Unit = {
    val r = WithOmitField(1, CustomInt(2))
    val copied = r.copy(keep = 99)
    assert(copied.keep === 99)
    assert(copied.keepCustom === CustomInt(2))
  }

}
