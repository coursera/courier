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

import com.linkedin.data.DataMap
import com.linkedin.data.codec.JacksonDataCodec
import com.linkedin.data.template.PrettyPrinterJacksonDataTemplateCodec
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitSuite

/**
 * Tests for WithoutNamespace — a generated record with no package declaration.
 * Must be tested from a file with no package declaration to access the class.
 */
class WithoutNamespaceGeneratorTest extends JUnitSuite with AssertionsForJUnit {

  private val prettyPrinter = new PrettyPrinterJacksonDataTemplateCodec
  private val dataCodec = new JacksonDataCodec

  private def roundTrip(dataMap: DataMap): DataMap =
    dataCodec.stringToMap(prettyPrinter.mapToString(dataMap))

  @Test
  def testWithoutNamespace_construction(): Unit = {
    val r = WithoutNamespace(field1 = "hello")
    assert(r.field1 === "hello")
  }

  @Test
  def testWithoutNamespace_roundTrip(): Unit = {
    val original = WithoutNamespace("test")
    val roundTripped =
      WithoutNamespace.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testWithoutNamespace_equality(): Unit = {
    val a = WithoutNamespace("x")
    val b = WithoutNamespace("x")
    val c = WithoutNamespace("y")
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithoutNamespace_copy(): Unit = {
    val r = WithoutNamespace("original")
    val copied = r.copy(field1 = "updated")
    assert(copied.field1 === "updated")
  }

  @Test
  def testWithoutNamespace_unapply(): Unit = {
    val r = WithoutNamespace("abc")
    val WithoutNamespace(f) = r
    assert(f === "abc")
  }

  @Test
  def testWithoutNamespace_productArity(): Unit = {
    val r = WithoutNamespace("val")
    assert(r.productArity === 1)
    assert(r.productElement(0) === "val")
  }

  @Test
  def testWithoutNamespace_toString(): Unit = {
    val r = WithoutNamespace("str")
    assert(r.toString.nonEmpty)
  }
}
