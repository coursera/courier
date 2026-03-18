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

import com.linkedin.data.ByteString
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.records.primitivestyle.{Simple => PSSimple}
import org.coursera.records.primitivestyle.{WithComplexTypes => PSWithComplexTypes}
import org.coursera.records.primitivestyle.{WithPrimitives => PSWithPrimitives}
import org.junit.Test

/**
 * Tests for org.coursera.records.primitivestyle generated types.
 */
class PrimitivestyleGeneratorTest extends GeneratorTest with SchemaFixtures {

  // ---------------------------------------------------------------------------
  // primitivestyle.Simple
  // ---------------------------------------------------------------------------

  @Test
  def testPSSimple_construction_withMessage(): Unit = {
    val s = PSSimple(message = Some("hello"))
    assert(s.message === Some("hello"))
  }

  @Test
  def testPSSimple_construction_noMessage(): Unit = {
    val s = PSSimple()
    assert(s.message === None)
  }

  @Test
  def testPSSimple_roundTrip(): Unit = {
    val original = PSSimple(Some("round trip"))
    val roundTripped = PSSimple.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testPSSimple_equality(): Unit = {
    val a = PSSimple(Some("x"))
    val b = PSSimple(Some("x"))
    val c = PSSimple(None)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testPSSimple_copy(): Unit = {
    val s = PSSimple(Some("original"))
    val copied = s.copy(message = Some("updated"))
    assert(copied.message === Some("updated"))
  }

  @Test
  def testPSSimple_unapply(): Unit = {
    val s = PSSimple(Some("test"))
    val PSSimple(msg) = s
    assert(msg === Some("test"))
  }

  @Test
  def testPSSimple_productArity(): Unit = {
    val s = PSSimple(Some("x"))
    assert(s.productArity === 1)
    assert(s.productElement(0) === Some("x"))
  }

  @Test
  def testPSSimple_productElement_outOfBounds(): Unit = {
    val s = PSSimple()
    intercept[IndexOutOfBoundsException] {
      s.productElement(2)
    }
  }

  @Test
  def testPSSimple_toString(): Unit = {
    val s = PSSimple(Some("str"))
    assert(s.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // primitivestyle.WithPrimitives
  // ---------------------------------------------------------------------------

  @Test
  def testPSWithPrimitives_construction(): Unit = {
    val w = PSWithPrimitives(
      intField = 1,
      longField = 2L,
      floatField = 3.0f,
      doubleField = 4.0d,
      booleanField = true,
      stringField = "hello",
      bytesField = bytes1
    )
    assert(w.intField === 1)
    assert(w.longField === 2L)
    assert(w.floatField === 3.0f)
    assert(w.doubleField === 4.0d)
    assert(w.booleanField === true)
    assert(w.stringField === "hello")
    assert(w.bytesField === bytes1)
  }

  @Test
  def testPSWithPrimitives_roundTrip(): Unit = {
    val original = PSWithPrimitives(1, 2L, 3.0f, 4.0d, false, "test", bytes1)
    val roundTripped = PSWithPrimitives.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testPSWithPrimitives_equality(): Unit = {
    val a = PSWithPrimitives(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    val b = PSWithPrimitives(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    val c = PSWithPrimitives(9, 2L, 3.0f, 4.0d, true, "s", bytes1)
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testPSWithPrimitives_copy(): Unit = {
    val w = PSWithPrimitives(1, 2L, 3.0f, 4.0d, true, "original", bytes1)
    val copied = w.copy(stringField = "updated")
    assert(copied.stringField === "updated")
    assert(copied.intField === 1)
  }

  @Test
  def testPSWithPrimitives_unapply(): Unit = {
    val w = PSWithPrimitives(5, 6L, 7.0f, 8.0d, false, "x", bytes1)
    val PSWithPrimitives(i, l, f, d, b, s, bs) = w
    assert(i === 5)
    assert(s === "x")
  }

  @Test
  def testPSWithPrimitives_productArity(): Unit = {
    val w = PSWithPrimitives(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    assert(w.productArity === 7)
    assert(w.productElement(0) === 1)
  }

  @Test
  def testPSWithPrimitives_toString(): Unit = {
    val w = PSWithPrimitives(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    assert(w.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // primitivestyle.WithComplexTypes
  // ---------------------------------------------------------------------------

  @Test
  def testPSWithComplexTypes_construction(): Unit = {
    val w = PSWithComplexTypes(
      intField = 1,
      longField = 2L,
      floatField = 3.0f,
      doubleField = 4.0d,
      booleanField = true,
      stringField = "hello",
      bytesField = bytes1
    )
    assert(w.intField === 1)
    assert(w.stringField === "hello")
    assert(w.bytesField === bytes1)
  }

  @Test
  def testPSWithComplexTypes_roundTrip(): Unit = {
    val original = PSWithComplexTypes(1, 2L, 3.0f, 4.0d, false, "test", bytes1)
    val roundTripped =
      PSWithComplexTypes.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testPSWithComplexTypes_equality(): Unit = {
    val a = PSWithComplexTypes(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    val b = PSWithComplexTypes(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    val c = PSWithComplexTypes(9, 2L, 3.0f, 4.0d, true, "s", bytes1)
    assert(a === b)
    assert(a !== c)
  }

  @Test
  def testPSWithComplexTypes_copy(): Unit = {
    val w = PSWithComplexTypes(1, 2L, 3.0f, 4.0d, true, "original", bytes1)
    val copied = w.copy(stringField = "updated")
    assert(copied.stringField === "updated")
  }

  @Test
  def testPSWithComplexTypes_unapply(): Unit = {
    val w = PSWithComplexTypes(5, 6L, 7.0f, 8.0d, false, "x", bytes1)
    val PSWithComplexTypes(i, l, f, d, b, s, bs) = w
    assert(i === 5)
    assert(s === "x")
  }

  @Test
  def testPSWithComplexTypes_productArity(): Unit = {
    val w = PSWithComplexTypes(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    assert(w.productArity === 7)
    assert(w.productElement(0) === 1)
  }

  @Test
  def testPSWithComplexTypes_toString(): Unit = {
    val w = PSWithComplexTypes(1, 2L, 3.0f, 4.0d, true, "s", bytes1)
    assert(w.toString.nonEmpty)
  }
}
