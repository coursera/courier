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
 * Tests for generated numeric and bytes map types.
 * Covers DoubleMap, FloatMap, LongMap, BytesMap.
 */
class NumericMapTypesTest extends AssertionsForJUnit {

  private val bs1 = ByteString.copy(Array(0x01, 0x02).map(_.toByte))
  private val bs2 = ByteString.copy(Array(0x03, 0x04).map(_.toByte))

  // ─── DoubleMap ───────────────────────────────────────────────────────────────

  @Test def doubleMap_applyPairs(): Unit = {
    val m = DoubleMap("x" -> 1.0, "y" -> 2.0)
    assertResult(Some(1.0))(m.get("x"))
    assertResult(Some(2.0))(m.get("y"))
    assertResult(None)(m.get("z"))
  }

  @Test def doubleMap_removed(): Unit = {
    val m = DoubleMap("a" -> 1.0, "b" -> 2.0)
    val m2 = m.removed("a")
    assertResult(None)(m2.get("a"))
    assertResult(Some(1.0))(m.get("a")) // original unchanged
  }

  @Test def doubleMap_iterator(): Unit = {
    val m = DoubleMap("x" -> 3.14)
    assertResult(1)(m.iterator.size)
    assertResult(("x", 3.14))(m.iterator.next())
  }

  @Test def doubleMap_plus(): Unit = {
    val m = DoubleMap("a" -> 1.0)
    val m2 = m + ("b" -> 2.0)
    assertResult(Some(2.0))(m2.get("b"))
    assertResult(None)(m.get("b"))
  }

  @Test def doubleMap_empty(): Unit = {
    assertResult(0)(DoubleMap.empty.size)
  }

  @Test def doubleMap_updatedSupertype(): Unit = {
    val m = DoubleMap("a" -> 1.0)
    val m2: Map[String, Any] = m.updated[Any]("a", "not a double")
    assertResult(Some("not a double"))(m2.get("a"))
  }

  // ─── FloatMap ────────────────────────────────────────────────────────────────

  @Test def floatMap_applyPairs(): Unit = {
    val m = FloatMap("x" -> 1.0f, "y" -> 2.0f)
    assertResult(Some(1.0f))(m.get("x"))
    assertResult(Some(2.0f))(m.get("y"))
    assertResult(None)(m.get("z"))
  }

  @Test def floatMap_removed(): Unit = {
    val m = FloatMap("a" -> 1.0f, "b" -> 2.0f)
    val m2 = m.removed("a")
    assertResult(None)(m2.get("a"))
    assertResult(Some(1.0f))(m.get("a"))
  }

  @Test def floatMap_iterator(): Unit = {
    val m = FloatMap("x" -> 3.14f)
    assertResult(1)(m.iterator.size)
  }

  @Test def floatMap_plus(): Unit = {
    val m = FloatMap("a" -> 1.0f)
    val m2 = m + ("b" -> 2.0f)
    assertResult(Some(2.0f))(m2.get("b"))
    assertResult(None)(m.get("b"))
  }

  @Test def floatMap_empty(): Unit = {
    assertResult(0)(FloatMap.empty.size)
  }

  @Test def floatMap_updatedSupertype(): Unit = {
    val m = FloatMap("a" -> 1.0f)
    val m2: Map[String, Any] = m.updated[Any]("a", "not a float")
    assertResult(Some("not a float"))(m2.get("a"))
  }

  // ─── LongMap ─────────────────────────────────────────────────────────────────

  @Test def longMap_applyPairs(): Unit = {
    val m = LongMap("x" -> 100L, "y" -> 200L)
    assertResult(Some(100L))(m.get("x"))
    assertResult(Some(200L))(m.get("y"))
    assertResult(None)(m.get("z"))
  }

  @Test def longMap_removed(): Unit = {
    val m = LongMap("a" -> 100L, "b" -> 200L)
    val m2 = m.removed("a")
    assertResult(None)(m2.get("a"))
    assertResult(Some(100L))(m.get("a"))
  }

  @Test def longMap_iterator(): Unit = {
    val m = LongMap("k" -> 42L)
    assertResult(1)(m.iterator.size)
  }

  @Test def longMap_plus(): Unit = {
    val m = LongMap("a" -> 1L)
    val m2 = m + ("b" -> 2L)
    assertResult(Some(2L))(m2.get("b"))
    assertResult(None)(m.get("b"))
  }

  @Test def longMap_empty(): Unit = {
    assertResult(0)(LongMap.empty.size)
  }

  @Test def longMap_updatedSupertype(): Unit = {
    val m = LongMap("a" -> 1L)
    val m2: Map[String, Any] = m.updated[Any]("a", "not a long")
    assertResult(Some("not a long"))(m2.get("a"))
  }

  // ─── BytesMap ────────────────────────────────────────────────────────────────

  @Test def bytesMap_applyPairs(): Unit = {
    val m = BytesMap("x" -> bs1, "y" -> bs2)
    assertResult(Some(bs1))(m.get("x"))
    assertResult(Some(bs2))(m.get("y"))
    assertResult(None)(m.get("z"))
  }

  @Test def bytesMap_removed(): Unit = {
    val m = BytesMap("a" -> bs1, "b" -> bs2)
    val m2 = m.removed("a")
    assertResult(None)(m2.get("a"))
    assertResult(Some(bs1))(m.get("a"))
  }

  @Test def bytesMap_iterator(): Unit = {
    val m = BytesMap("k" -> bs1)
    assertResult(1)(m.iterator.size)
  }

  @Test def bytesMap_plus(): Unit = {
    val m = BytesMap("a" -> bs1)
    val m2 = m + ("b" -> bs2)
    assertResult(Some(bs2))(m2.get("b"))
    assertResult(None)(m.get("b"))
  }

  @Test def bytesMap_empty(): Unit = {
    assertResult(0)(BytesMap.empty.size)
  }

  @Test def bytesMap_updatedSupertype(): Unit = {
    val m = BytesMap("a" -> bs1)
    val m2: Map[String, Any] = m.updated[Any]("a", "not bytes")
    assertResult(Some("not bytes"))(m2.get("a"))
  }

  // ─── Implicit wrap (triggers coerceKeyOutput / apply(Map[K,V]) path) ─────────

  @Test def doubleMap_implicitWrap(): Unit = {
    import DoubleMap._
    val m: DoubleMap = Map("x" -> 1.0, "y" -> 2.0)
    assertResult(Some(1.0))(m.get("x"))
    assertResult(Some(2.0))(m.get("y"))
  }

  @Test def floatMap_implicitWrap(): Unit = {
    import FloatMap._
    val m: FloatMap = Map("a" -> 3.14f)
    assertResult(Some(3.14f))(m.get("a"))
  }

  @Test def longMap_implicitWrap(): Unit = {
    import LongMap._
    val m: LongMap = Map("k" -> 42L)
    assertResult(Some(42L))(m.get("k"))
  }

  @Test def bytesMap_implicitWrap(): Unit = {
    import BytesMap._
    val m: BytesMap = Map("x" -> bs1, "y" -> bs2)
    assertResult(Some(bs1))(m.get("x"))
    assertResult(Some(bs2))(m.get("y"))
  }

  // ─── DataBuilder (addOne + clear) ────────────────────────────────────────────

  @Test def doubleMap_builder_addOne(): Unit = {
    val builder = DoubleMap.newBuilder
    builder.addOne("a" -> 1.1)
    builder.addOne("b" -> 2.2)
    val m = builder.result()
    assertResult(Some(1.1))(m.get("a"))
    assertResult(Some(2.2))(m.get("b"))
  }

  @Test def doubleMap_builder_clear(): Unit = {
    val builder = DoubleMap.newBuilder
    builder.addOne("a" -> 9.9)
    builder.clear()
    builder.addOne("b" -> 3.3)
    val m = builder.result()
    assertResult(None)(m.get("a"))
    assertResult(Some(3.3))(m.get("b"))
  }

  @Test def floatMap_builder_addOne(): Unit = {
    val builder = FloatMap.newBuilder
    builder.addOne("a" -> 1.1f)
    val m = builder.result()
    assertResult(Some(1.1f))(m.get("a"))
  }

  @Test def floatMap_builder_clear(): Unit = {
    val builder = FloatMap.newBuilder
    builder.addOne("a" -> 9.9f)
    builder.clear()
    builder.addOne("b" -> 3.3f)
    val m = builder.result()
    assertResult(None)(m.get("a"))
    assertResult(Some(3.3f))(m.get("b"))
  }

  @Test def longMap_builder_addOne(): Unit = {
    val builder = LongMap.newBuilder
    builder.addOne("a" -> 100L)
    val m = builder.result()
    assertResult(Some(100L))(m.get("a"))
  }

  @Test def longMap_builder_clear(): Unit = {
    val builder = LongMap.newBuilder
    builder.addOne("a" -> 999L)
    builder.clear()
    builder.addOne("b" -> 42L)
    val m = builder.result()
    assertResult(None)(m.get("a"))
    assertResult(Some(42L))(m.get("b"))
  }

  @Test def bytesMap_builder_addOne(): Unit = {
    val builder = BytesMap.newBuilder
    builder.addOne("x" -> bs1)
    builder.addOne("y" -> bs2)
    val m = builder.result()
    assertResult(Some(bs1))(m.get("x"))
    assertResult(Some(bs2))(m.get("y"))
  }

  @Test def bytesMap_builder_clear(): Unit = {
    val builder = BytesMap.newBuilder
    builder.addOne("x" -> bs1)
    builder.clear()
    builder.addOne("y" -> bs2)
    val m = builder.result()
    assertResult(None)(m.get("x"))
    assertResult(Some(bs2))(m.get("y"))
  }
}
