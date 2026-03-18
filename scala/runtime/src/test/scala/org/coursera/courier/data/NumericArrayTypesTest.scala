/*
 Copyright 2024 Coursera Inc.

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

package org.coursera.courier.data

import com.linkedin.data.ByteString
import org.junit.Test
import org.scalatestplus.junit.AssertionsForJUnit

/**
 * Tests for generated numeric and bytes array types.
 * Covers DoubleArray, FloatArray, LongArray, BytesArray — the types not covered by
 * [[PrimitiveDataTypesTest]].
 */
class NumericArrayTypesTest extends AssertionsForJUnit {

  private val bs1 = ByteString.copy(Array(0x01, 0x02, 0x03).map(_.toByte))
  private val bs2 = ByteString.copy(Array(0xFE, 0xFF).map(_.toByte))

  // ─── DoubleArray ─────────────────────────────────────────────────────────────

  @Test def doubleArray_applyVarargs(): Unit = {
    val arr = DoubleArray(1.0, 2.5, 3.14)
    assertResult(3)(arr.length)
    assertResult(1.0)(arr(0))
    assertResult(2.5)(arr(1))
    assertResult(3.14)(arr(2))
  }

  @Test def doubleArray_applyCollection(): Unit = {
    val arr = DoubleArray(List(0.1, 0.2))
    assertResult(2)(arr.length)
    assertResult(0.1)(arr(0))
    assertResult(0.2)(arr(1))
  }

  @Test def doubleArray_empty(): Unit = {
    assertResult(0)(DoubleArray.empty.length)
  }

  @Test def doubleArray_builder_addOne(): Unit = {
    val builder = DoubleArray.newBuilder
    builder.addOne(1.1)
    builder.addOne(2.2)
    val arr = builder.result()
    assertResult(2)(arr.length)
    assertResult(1.1)(arr(0))
    assertResult(2.2)(arr(1))
  }

  @Test def doubleArray_builder_clear(): Unit = {
    val builder = DoubleArray.newBuilder
    builder.addOne(9.9)
    builder.clear()
    builder.addOne(3.3)
    val arr = builder.result()
    assertResult(1)(arr.length)
    assertResult(3.3)(arr(0))
  }

  // ─── FloatArray ──────────────────────────────────────────────────────────────

  @Test def floatArray_applyVarargs(): Unit = {
    val arr = FloatArray(1.0f, 2.5f, 3.14f)
    assertResult(3)(arr.length)
    assertResult(1.0f)(arr(0))
    assertResult(2.5f)(arr(1))
  }

  @Test def floatArray_applyCollection(): Unit = {
    val arr = FloatArray(List(0.1f, 0.2f))
    assertResult(2)(arr.length)
    assertResult(0.1f)(arr(0))
  }

  @Test def floatArray_empty(): Unit = {
    assertResult(0)(FloatArray.empty.length)
  }

  @Test def floatArray_builder_addOne(): Unit = {
    val builder = FloatArray.newBuilder
    builder.addOne(1.1f)
    builder.addOne(2.2f)
    val arr = builder.result()
    assertResult(2)(arr.length)
    assertResult(1.1f)(arr(0))
  }

  @Test def floatArray_builder_clear(): Unit = {
    val builder = FloatArray.newBuilder
    builder.addOne(9.9f)
    builder.clear()
    builder.addOne(3.3f)
    val arr = builder.result()
    assertResult(1)(arr.length)
    assertResult(3.3f)(arr(0))
  }

  // ─── LongArray ───────────────────────────────────────────────────────────────

  @Test def longArray_applyVarargs(): Unit = {
    val arr = LongArray(100L, 200L, 300L)
    assertResult(3)(arr.length)
    assertResult(100L)(arr(0))
    assertResult(200L)(arr(1))
    assertResult(300L)(arr(2))
  }

  @Test def longArray_applyCollection(): Unit = {
    val arr = LongArray(List(1L, 2L))
    assertResult(2)(arr.length)
    assertResult(1L)(arr(0))
  }

  @Test def longArray_empty(): Unit = {
    assertResult(0)(LongArray.empty.length)
  }

  @Test def longArray_builder_addOne(): Unit = {
    val builder = LongArray.newBuilder
    builder.addOne(111L)
    builder.addOne(222L)
    val arr = builder.result()
    assertResult(2)(arr.length)
    assertResult(111L)(arr(0))
    assertResult(222L)(arr(1))
  }

  @Test def longArray_builder_clear(): Unit = {
    val builder = LongArray.newBuilder
    builder.addOne(999L)
    builder.clear()
    builder.addOne(333L)
    val arr = builder.result()
    assertResult(1)(arr.length)
    assertResult(333L)(arr(0))
  }

  // ─── BytesArray ──────────────────────────────────────────────────────────────

  @Test def bytesArray_applyVarargs(): Unit = {
    val arr = BytesArray(bs1, bs2)
    assertResult(2)(arr.length)
    assertResult(bs1)(arr(0))
    assertResult(bs2)(arr(1))
  }

  @Test def bytesArray_applyCollection(): Unit = {
    val arr = BytesArray(List(bs1))
    assertResult(1)(arr.length)
    assertResult(bs1)(arr(0))
  }

  @Test def bytesArray_empty(): Unit = {
    assertResult(0)(BytesArray.empty.length)
  }

  @Test def bytesArray_builder_addOne(): Unit = {
    val builder = BytesArray.newBuilder
    builder.addOne(bs1)
    builder.addOne(bs2)
    val arr = builder.result()
    assertResult(2)(arr.length)
    assertResult(bs1)(arr(0))
    assertResult(bs2)(arr(1))
  }

  @Test def bytesArray_builder_clear(): Unit = {
    val builder = BytesArray.newBuilder
    builder.addOne(bs1)
    builder.clear()
    builder.addOne(bs2)
    val arr = builder.result()
    assertResult(1)(arr.length)
    assertResult(bs2)(arr(0))
  }

  // ─── BytesArray extra coverage ────────────────────────────────────────────────

  @Test def bytesArray_productArity(): Unit = {
    val arr = BytesArray(bs1, bs2)
    assertResult(2)(arr.productArity)
  }

  @Test def bytesArray_productElement(): Unit = {
    val arr = BytesArray(bs1, bs2)
    assert(arr.productElement(0) != null)
  }

  @Test def bytesArray_schema(): Unit = {
    val arr = BytesArray(bs1)
    assertResult(BytesArray.SCHEMA)(arr.schema())
  }

  @Test def bytesArray_clone(): Unit = {
    val arr = BytesArray(bs1)
    val cloned = arr.clone().asInstanceOf[BytesArray]
    assertResult(1)(cloned.length)
  }

  @Test def bytesArray_copy_setReadOnly(): Unit = {
    import org.coursera.courier.templates.DataTemplates.DataConversion
    val arr = BytesArray(bs1, bs2)
    val copy = arr.copy(arr.data(), DataConversion.SetReadOnly).asInstanceOf[BytesArray]
    assertResult(2)(copy.length)
  }

  @Test def bytesArray_build_setReadOnly(): Unit = {
    import org.coursera.courier.templates.DataTemplates.DataConversion
    val arr = BytesArray.build(BytesArray(bs1).data(), DataConversion.SetReadOnly)
    assertResult(1)(arr.length)
  }

  @Test def bytesArray_build_deepCopy(): Unit = {
    import org.coursera.courier.templates.DataTemplates.DataConversion
    val arr = BytesArray.build(BytesArray(bs2).data(), DataConversion.DeepCopy)
    assertResult(1)(arr.length)
  }

  @Test def bytesArray_implicitWrap(): Unit = {
    import BytesArray._
    val arr: BytesArray = List(bs1, bs2)
    assertResult(2)(arr.length)
  }

  // ─── DoubleArray extra coverage ───────────────────────────────────────────────

  @Test def doubleArray_productArity(): Unit = {
    val arr = DoubleArray(1.0, 2.0, 3.0)
    assertResult(3)(arr.productArity)
  }

  @Test def doubleArray_productElement(): Unit = {
    val arr = DoubleArray(3.14, 2.71)
    assert(arr.productElement(0) != null)
  }

  @Test def doubleArray_schema(): Unit = {
    val arr = DoubleArray(1.0)
    assertResult(DoubleArray.SCHEMA)(arr.schema())
  }

  @Test def doubleArray_clone(): Unit = {
    val arr = DoubleArray(1.0, 2.0)
    val cloned = arr.clone().asInstanceOf[DoubleArray]
    assertResult(2)(cloned.length)
  }

  @Test def doubleArray_copy_setReadOnly(): Unit = {
    import org.coursera.courier.templates.DataTemplates.DataConversion
    val arr = DoubleArray(1.0, 2.0)
    val copy = arr.copy(arr.data(), DataConversion.SetReadOnly).asInstanceOf[DoubleArray]
    assertResult(2)(copy.length)
  }

  @Test def doubleArray_build_setReadOnly(): Unit = {
    import org.coursera.courier.templates.DataTemplates.DataConversion
    val arr = DoubleArray.build(DoubleArray(1.0, 2.0).data(), DataConversion.SetReadOnly)
    assertResult(2)(arr.length)
  }

  @Test def doubleArray_build_deepCopy(): Unit = {
    import org.coursera.courier.templates.DataTemplates.DataConversion
    val arr = DoubleArray.build(DoubleArray(3.14).data(), DataConversion.DeepCopy)
    assertResult(1)(arr.length)
  }

  @Test def doubleArray_implicitWrap(): Unit = {
    import DoubleArray._
    val arr: DoubleArray = List(1.0, 2.0, 3.0)
    assertResult(3)(arr.length)
  }

  // ─── FloatArray extra coverage ────────────────────────────────────────────────

  @Test def floatArray_productArity(): Unit = {
    val arr = FloatArray(1.0f, 2.0f)
    assertResult(2)(arr.productArity)
  }

  @Test def floatArray_productElement(): Unit = {
    val arr = FloatArray(3.14f)
    assert(arr.productElement(0) != null)
  }
}
