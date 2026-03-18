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
import org.example.Apostrophe
import org.example.{record => ExampleRecord}
import org.junit.Test

/**
 * Tests for org.example record types that had 0% coverage:
 * Apostrophe (record with int field) and record (empty record).
 *
 * Note: Fortune requires org.joda.time.DateTime which is tested separately.
 */
class OrgExampleGeneratorTest extends GeneratorTest with SchemaFixtures {

  // ---------------------------------------------------------------------------
  // Apostrophe
  // ---------------------------------------------------------------------------

  @Test
  def testApostrophe_construction(): Unit = {
    val a = Apostrophe(field = 42)
    assert(a.field === 42)
  }

  @Test
  def testApostrophe_roundTrip(): Unit = {
    val original = Apostrophe(100)
    val roundTripped =
      Apostrophe.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
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
    val a = Apostrophe(10)
    val copied = a.copy(field = 20)
    assert(copied.field === 20)
  }

  @Test
  def testApostrophe_unapply(): Unit = {
    val a = Apostrophe(7)
    val Apostrophe(f) = a
    assert(f === 7)
  }

  @Test
  def testApostrophe_productArity(): Unit = {
    val a = Apostrophe(5)
    assert(a.productArity === 1)
    assert(a.productElement(0) === 5)
  }

  @Test
  def testApostrophe_productElement_outOfBounds(): Unit = {
    val a = Apostrophe(1)
    intercept[IndexOutOfBoundsException] {
      a.productElement(2)
    }
  }

  @Test
  def testApostrophe_toString(): Unit = {
    val a = Apostrophe(99)
    assert(a.toString.nonEmpty)
  }

  // ---------------------------------------------------------------------------
  // record (empty record with lowercase name)
  // ---------------------------------------------------------------------------

  @Test
  def testRecord_construction(): Unit = {
    val r = ExampleRecord()
    assert(r.productArity === 0)
  }

  @Test
  def testRecord_roundTrip(): Unit = {
    val original = ExampleRecord()
    val roundTripped =
      ExampleRecord.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testRecord_equality(): Unit = {
    val a = ExampleRecord()
    val b = ExampleRecord()
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testRecord_unapply(): Unit = {
    val r = ExampleRecord()
    // unapply returns Boolean for empty records
    assert(ExampleRecord.unapply(r) === true)
  }

  @Test
  def testRecord_toString(): Unit = {
    val r = ExampleRecord()
    assert(r.toString.nonEmpty)
  }

  @Test
  def testRecord_productElement_outOfBounds(): Unit = {
    val r = ExampleRecord()
    intercept[IndexOutOfBoundsException] {
      r.productElement(0)
    }
  }
}
