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

import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.enums.Fruits
import org.coursera.escaping.KeywordEscaping
import org.coursera.escaping.{`class` => EscapingClass}
import org.coursera.records.test.Empty
import org.coursera.records.test.Empty2
import org.coursera.records.test.WithOptionalPrimitiveDefaultNone
import org.coursera.records.test.WithOptionalPrimitiveDefaults
import org.coursera.records.test.WithPrimitiveDefaults
import org.coursera.records.test.WithPrimitiveTyperefs
import org.coursera.records.test.WithPrimitives
import org.coursera.records.test.packaging.{Empty => PackagingEmpty}
import org.junit.Test

/**
 * Extended tests providing branch/method coverage for records already partially
 * covered by RecordGeneratorTest. Focuses on copy, equality, productArity,
 * productElement, toString, unapply, and outOfBounds.
 */
class RecordExtendedCoverageTest extends GeneratorTest with SchemaFixtures {

  // ---------------------------------------------------------------------------
  // WithPrimitives
  // ---------------------------------------------------------------------------

  @Test
  def testWithPrimitives_equality(): Unit = {
    val a = WithPrimitives(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    val b = WithPrimitives(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    val c = WithPrimitives(9, 2L, 3.0f, 4.0d, true, "s", bytes1)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithPrimitives_copy(): Unit = {
    val w = WithPrimitives(1, 2L, 3.0f, 4.0d, true, "original", bytes1)
    val copied = w.copy(stringField = "updated", intField = 99)
    assert(copied.stringField === "updated")
    assert(copied.intField === 99)
    assert(copied.longField === 2L)
  }

  @Test
  def testWithPrimitives_productArity(): Unit = {
    val w = WithPrimitives(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    assert(w.productArity === 7)
    assert(w.productElement(0) === 1)
    assert(w.productElement(1) === 2L)
    assert(w.productElement(5) === "s")
  }

  @Test
  def testWithPrimitives_productElement_outOfBounds(): Unit = {
    val w = WithPrimitives(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    intercept[IndexOutOfBoundsException] {
      w.productElement(7)
    }
  }

  @Test
  def testWithPrimitives_toString(): Unit = {
    val w = WithPrimitives(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    assert(w.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // WithPrimitiveTyperefs
  // ---------------------------------------------------------------------------

  @Test
  def testWithPrimitiveTyperefs_equality(): Unit = {
    val a = WithPrimitiveTyperefs(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    val b = WithPrimitiveTyperefs(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    val c = WithPrimitiveTyperefs(9, 2L, 3.0f, 4.0d, true, "s", bytes1)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithPrimitiveTyperefs_copy(): Unit = {
    val w = WithPrimitiveTyperefs(1, 2L, 3.0f, 4.0d, true, "original", bytes1)
    val copied = w.copy(stringField = "updated")
    assert(copied.stringField === "updated")
    assert(copied.intField === 1)
  }

  @Test
  def testWithPrimitiveTyperefs_productArity(): Unit = {
    val w = WithPrimitiveTyperefs(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    assert(w.productArity === 7)
    assert(w.productElement(0) === 1)
  }

  @Test
  def testWithPrimitiveTyperefs_productElement_outOfBounds(): Unit = {
    val w = WithPrimitiveTyperefs(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    intercept[IndexOutOfBoundsException] {
      w.productElement(7)
    }
  }

  @Test
  def testWithPrimitiveTyperefs_toString(): Unit = {
    val w = WithPrimitiveTyperefs(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    assert(w.toString.nonEmpty)
  }

  @Test
  def testWithPrimitiveTyperefs_roundTrip(): Unit = {
    val original = WithPrimitiveTyperefs(1, 2L, 3.0f, 4.0d, false, "test", bytes1)
    val roundTripped =
      WithPrimitiveTyperefs.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  // ---------------------------------------------------------------------------
  // WithPrimitiveDefaults
  // ---------------------------------------------------------------------------

  @Test
  def testWithPrimitiveDefaults_equality(): Unit = {
    val a = WithPrimitiveDefaults()
    val b = WithPrimitiveDefaults()
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithPrimitiveDefaults_copy(): Unit = {
    val w = WithPrimitiveDefaults()
    val copied = w.copy(stringWithDefault = "custom")
    assert(copied.stringWithDefault === "custom")
    assert(copied.intWithDefault === 1)
  }

  @Test
  def testWithPrimitiveDefaults_productArity(): Unit = {
    val w = WithPrimitiveDefaults()
    assert(w.productArity === 8)
    assert(w.productElement(0) === 1)
  }

  @Test
  def testWithPrimitiveDefaults_productElement_outOfBounds(): Unit = {
    val w = WithPrimitiveDefaults()
    intercept[IndexOutOfBoundsException] {
      w.productElement(8)
    }
  }

  @Test
  def testWithPrimitiveDefaults_toString(): Unit = {
    val w = WithPrimitiveDefaults()
    assert(w.toString.nonEmpty)
  }

  @Test
  def testWithPrimitiveDefaults_unapply(): Unit = {
    val w = WithPrimitiveDefaults()
    val WithPrimitiveDefaults(i, l, f, d, b, s, bs, e) = w
    assert(i === 1)
    assert(s === "DEFAULT")
    assert(e === Fruits.APPLE)
  }

  // ---------------------------------------------------------------------------
  // WithOptionalPrimitiveDefaults
  // ---------------------------------------------------------------------------

  @Test
  def testWithOptionalPrimitiveDefaults_equality(): Unit = {
    val a = WithOptionalPrimitiveDefaults()
    val b = WithOptionalPrimitiveDefaults()
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithOptionalPrimitiveDefaults_copy(): Unit = {
    val w = WithOptionalPrimitiveDefaults()
    val copied = w.copy(stringWithDefault = Some("custom"))
    assert(copied.stringWithDefault === Some("custom"))
    assert(copied.intWithDefault === Some(1))
  }

  @Test
  def testWithOptionalPrimitiveDefaults_productArity(): Unit = {
    val w = WithOptionalPrimitiveDefaults()
    assert(w.productArity === 8)
    assert(w.productElement(0) === Some(1))
  }

  @Test
  def testWithOptionalPrimitiveDefaults_productElement_outOfBounds(): Unit = {
    val w = WithOptionalPrimitiveDefaults()
    intercept[IndexOutOfBoundsException] {
      w.productElement(8)
    }
  }

  @Test
  def testWithOptionalPrimitiveDefaults_toString(): Unit = {
    val w = WithOptionalPrimitiveDefaults()
    assert(w.toString.nonEmpty)
  }

  @Test
  def testWithOptionalPrimitiveDefaults_unapply(): Unit = {
    val w = WithOptionalPrimitiveDefaults()
    val WithOptionalPrimitiveDefaults(i, l, f, d, b, s, bs, e) = w
    assert(i === Some(1))
    assert(s === Some("DEFAULT"))
    assert(e === Some(Fruits.APPLE))
  }

  @Test
  def testWithOptionalPrimitiveDefaults_roundTrip(): Unit = {
    val original = WithOptionalPrimitiveDefaults()
    val roundTripped =
      WithOptionalPrimitiveDefaults.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  // ---------------------------------------------------------------------------
  // WithOptionalPrimitiveDefaultNone
  // ---------------------------------------------------------------------------

  @Test
  def testWithOptionalPrimitiveDefaultNone_equality(): Unit = {
    val a = WithOptionalPrimitiveDefaultNone()
    val b = WithOptionalPrimitiveDefaultNone()
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithOptionalPrimitiveDefaultNone_copy(): Unit = {
    val w = WithOptionalPrimitiveDefaultNone()
    val copied = w.copy(stringWithDefault = Some("hello"))
    assert(copied.stringWithDefault === Some("hello"))
    assert(copied.intWithDefault === None)
  }

  @Test
  def testWithOptionalPrimitiveDefaultNone_productArity(): Unit = {
    val w = WithOptionalPrimitiveDefaultNone()
    assert(w.productArity === 8)
    assert(w.productElement(0) === None)
  }

  @Test
  def testWithOptionalPrimitiveDefaultNone_productElement_outOfBounds(): Unit = {
    val w = WithOptionalPrimitiveDefaultNone()
    intercept[IndexOutOfBoundsException] {
      w.productElement(8)
    }
  }

  @Test
  def testWithOptionalPrimitiveDefaultNone_toString(): Unit = {
    val w = WithOptionalPrimitiveDefaultNone()
    assert(w.toString.nonEmpty)
  }

  @Test
  def testWithOptionalPrimitiveDefaultNone_unapply(): Unit = {
    val w = WithOptionalPrimitiveDefaultNone(intWithDefault = Some(42))
    val WithOptionalPrimitiveDefaultNone(i, l, f, d, b, s, bs, e) = w
    assert(i === Some(42))
    assert(s === None)
  }

  @Test
  def testWithOptionalPrimitiveDefaultNone_roundTrip(): Unit = {
    val original = WithOptionalPrimitiveDefaultNone(
      intWithDefault = Some(1),
      stringWithDefault = Some("test"),
      enumWithDefault = Some(Fruits.APPLE)
    )
    val roundTripped =
      WithOptionalPrimitiveDefaultNone.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithOptionalPrimitiveDefaultNone_withEnum(): Unit = {
    val w = WithOptionalPrimitiveDefaultNone(enumWithDefault = Some(Fruits.APPLE))
    assert(w.enumWithDefault === Some(Fruits.APPLE))
  }

  // ---------------------------------------------------------------------------
  // Empty records
  // ---------------------------------------------------------------------------

  @Test
  def testEmpty_equality(): Unit = {
    val a = Empty()
    val b = Empty()
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testEmpty_roundTrip(): Unit = {
    val original = Empty()
    val roundTripped = Empty.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testEmpty_unapply(): Unit = {
    val r = Empty()
    assert(Empty.unapply(r) === true)
  }

  @Test
  def testEmpty_productArity(): Unit = {
    val r = Empty()
    assert(r.productArity === 0)
  }

  @Test
  def testEmpty_productElement_outOfBounds(): Unit = {
    val r = Empty()
    intercept[IndexOutOfBoundsException] {
      r.productElement(0)
    }
  }

  @Test
  def testEmpty_toString(): Unit = {
    val r = Empty()
    assert(r.toString.nonEmpty)
  }

  @Test
  def testEmpty2_equality(): Unit = {
    val a = Empty2()
    val b = Empty2()
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testEmpty2_roundTrip(): Unit = {
    val original = Empty2()
    val roundTripped = Empty2.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testEmpty2_unapply(): Unit = {
    val r = Empty2()
    assert(Empty2.unapply(r) === true)
  }

  @Test
  def testEmpty2_toString(): Unit = {
    val r = Empty2()
    assert(r.toString.nonEmpty)
  }

  @Test
  def testPackagingEmpty_equality(): Unit = {
    val a = PackagingEmpty()
    val b = PackagingEmpty()
    assert(a === b)
  }

  @Test
  def testPackagingEmpty_roundTrip(): Unit = {
    val original = PackagingEmpty()
    val roundTripped = PackagingEmpty.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testPackagingEmpty_unapply(): Unit = {
    val r = PackagingEmpty()
    assert(PackagingEmpty.unapply(r) === true)
  }

  @Test
  def testPackagingEmpty_toString(): Unit = {
    val r = PackagingEmpty()
    assert(r.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // KeywordEscaping
  // ---------------------------------------------------------------------------

  @Test
  def testKeywordEscaping_construction(): Unit = {
    val k = KeywordEscaping(`type` = "myType")
    assert(k.`type` === "myType")
  }

  @Test
  def testKeywordEscaping_roundTrip(): Unit = {
    val original = KeywordEscaping("testType")
    val roundTripped = KeywordEscaping.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testKeywordEscaping_equality(): Unit = {
    val a = KeywordEscaping("x")
    val b = KeywordEscaping("x")
    val c = KeywordEscaping("y")
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testKeywordEscaping_copy(): Unit = {
    val k = KeywordEscaping("original")
    val copied = k.copy(`type` = "updated")
    assert(copied.`type` === "updated")
  }

  @Test
  def testKeywordEscaping_unapply(): Unit = {
    val k = KeywordEscaping("abc")
    val KeywordEscaping(t) = k
    assert(t === "abc")
  }

  @Test
  def testKeywordEscaping_productArity(): Unit = {
    val k = KeywordEscaping("val")
    assert(k.productArity === 1)
    assert(k.productElement(0) === "val")
  }

  @Test
  def testKeywordEscaping_productElement_outOfBounds(): Unit = {
    val k = KeywordEscaping("x")
    intercept[IndexOutOfBoundsException] {
      k.productElement(1)
    }
  }

  @Test
  def testKeywordEscaping_toString(): Unit = {
    val k = KeywordEscaping("str")
    assert(k.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // org.coursera.escaping.`class` (keyword-named record in escaping package)
  // ---------------------------------------------------------------------------

  @Test
  def testEscapingClass_construction(): Unit = {
    val r = EscapingClass()
    assert(r.productArity === 0)
  }

  @Test
  def testEscapingClass_roundTrip(): Unit = {
    val original = EscapingClass()
    val roundTripped = EscapingClass.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testEscapingClass_equality(): Unit = {
    val a = EscapingClass()
    val b = EscapingClass()
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testEscapingClass_unapply(): Unit = {
    val r = EscapingClass()
    assert(EscapingClass.unapply(r) === true)
  }

  @Test
  def testEscapingClass_productElement_outOfBounds(): Unit = {
    val r = EscapingClass()
    intercept[IndexOutOfBoundsException] {
      r.productElement(0)
    }
  }

  @Test
  def testEscapingClass_toString(): Unit = {
    val r = EscapingClass()
    assert(r.toString.nonEmpty)
  }
}
