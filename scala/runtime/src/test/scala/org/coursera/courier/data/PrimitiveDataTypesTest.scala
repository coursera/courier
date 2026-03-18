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

import org.junit.Test
import org.scalatestplus.junit.AssertionsForJUnit

/**
 * Tests for generated primitive data type classes (Array/Map templates).
 *
 * These classes were modified for Scala 2.13 compatibility: builder API changed from
 * `+=` to `addOne`, Map trait requires `removed` and `updated[V1 >: V]`, and
 * `CanBuildFrom` was removed in favour of the new collections API.
 *
 * Each test section covers construction, element access, mutation (via copy), and
 * the mutable Builder path — precisely the code paths changed in the migration.
 */
class PrimitiveDataTypesTest extends AssertionsForJUnit {

  // ─── BooleanArray ────────────────────────────────────────────────────────────

  @Test
  def booleanArray_applyVarargs(): Unit = {
    val arr = BooleanArray(true, false, true)
    assertResult(3)(arr.length)
    assertResult(true)(arr(0))
    assertResult(false)(arr(1))
    assertResult(true)(arr(2))
  }

  @Test
  def booleanArray_applyCollection(): Unit = {
    val arr = BooleanArray(List(false, true))
    assertResult(2)(arr.length)
    assertResult(false)(arr(0))
    assertResult(true)(arr(1))
  }

  @Test
  def booleanArray_empty(): Unit = {
    assertResult(0)(BooleanArray.empty.length)
  }

  @Test
  def booleanArray_builder_addOne(): Unit = {
    // addOne replaced += in Scala 2.13 Builder API
    val builder = BooleanArray.newBuilder
    builder.addOne(true)
    builder.addOne(false)
    val arr = builder.result()
    assertResult(2)(arr.length)
    assertResult(true)(arr(0))
    assertResult(false)(arr(1))
  }

  @Test
  def booleanArray_builder_clear(): Unit = {
    val builder = BooleanArray.newBuilder
    builder.addOne(true)
    builder.clear()
    builder.addOne(false)
    val arr = builder.result()
    assertResult(1)(arr.length)
    assertResult(false)(arr(0))
  }

  @Test
  def booleanArray_implicitWrap(): Unit = {
    val arr: BooleanArray = List(true, false)
    assertResult(2)(arr.length)
  }

  // ─── BooleanMap ──────────────────────────────────────────────────────────────

  @Test
  def booleanMap_applyMap(): Unit = {
    val m = BooleanMap(Map("a" -> true, "b" -> false))
    assertResult(Some(true))(m.get("a"))
    assertResult(Some(false))(m.get("b"))
    assertResult(None)(m.get("missing"))
  }

  @Test
  def booleanMap_applyVarargs(): Unit = {
    val m = BooleanMap("x" -> true, "y" -> false)
    assertResult(2)(m.size)
    assertResult(Some(true))(m.get("x"))
  }

  @Test
  def booleanMap_empty(): Unit = {
    assertResult(0)(BooleanMap.empty.size)
  }

  @Test
  def booleanMap_plus(): Unit = {
    // + must return a new immutable Map without mutating the original
    val m = BooleanMap("a" -> true)
    val m2 = m + ("b" -> false)
    assertResult(Some(true))(m2.get("a"))
    assertResult(Some(false))(m2.get("b"))
    assertResult(None)(m.get("b"))  // original unchanged
  }

  @Test
  def booleanMap_removed(): Unit = {
    // removed replaces - in Scala 2.13 Map trait
    val m = BooleanMap("a" -> true, "b" -> false)
    val m2 = m.removed("a")
    assertResult(None)(m2.get("a"))
    assertResult(Some(false))(m2.get("b"))
    assertResult(Some(true))(m.get("a"))  // original unchanged
  }

  @Test
  def booleanMap_removed_missingKey(): Unit = {
    val m = BooleanMap("a" -> true)
    val m2 = m.removed("nonexistent")
    assertResult(1)(m2.size)
  }

  @Test
  def booleanMap_updated(): Unit = {
    // updated[V1 >: V] is required by Scala 2.13 Map trait
    val m = BooleanMap("a" -> true)
    val m2 = m.updated("a", false)
    assertResult(Some(false))(m2.get("a"))
    assertResult(Some(true))(m.get("a"))  // original unchanged
  }

  @Test
  def booleanMap_builder_addOne(): Unit = {
    val builder = BooleanMap.newBuilder
    builder.addOne("k1" -> true)
    builder.addOne("k2" -> false)
    val m = builder.result()
    assertResult(Some(true))(m.get("k1"))
    assertResult(Some(false))(m.get("k2"))
  }

  @Test
  def booleanMap_builder_clear(): Unit = {
    val builder = BooleanMap.newBuilder
    builder.addOne("k1" -> true)
    builder.clear()
    builder.addOne("k2" -> false)
    val m = builder.result()
    assertResult(None)(m.get("k1"))
    assertResult(Some(false))(m.get("k2"))
  }

  @Test
  def booleanMap_iterator(): Unit = {
    val m = BooleanMap("a" -> true, "b" -> false)
    val pairs = m.iterator.toSet
    assert(pairs.contains("a" -> true))
    assert(pairs.contains("b" -> false))
  }

  // ─── IntArray ────────────────────────────────────────────────────────────────

  @Test
  def intArray_applyVarargs(): Unit = {
    val arr = IntArray(1, 2, 3)
    assertResult(3)(arr.length)
    assertResult(1)(arr(0))
    assertResult(3)(arr(2))
  }

  @Test
  def intArray_empty(): Unit = {
    assertResult(0)(IntArray.empty.length)
  }

  @Test
  def intArray_builder_addOne(): Unit = {
    val builder = IntArray.newBuilder
    builder.addOne(10)
    builder.addOne(20)
    val arr = builder.result()
    assertResult(2)(arr.length)
    assertResult(10)(arr(0))
    assertResult(20)(arr(1))
  }

  @Test
  def intArray_applyCollection(): Unit = {
    val arr = IntArray(Seq(5, 10, 15))
    assertResult(3)(arr.length)
    assertResult(15)(arr(2))
  }

  // ─── IntMap ──────────────────────────────────────────────────────────────────

  @Test
  def intMap_applyMap(): Unit = {
    val m = IntMap(Map("one" -> 1, "two" -> 2))
    assertResult(Some(1))(m.get("one"))
    assertResult(Some(2))(m.get("two"))
    assertResult(None)(m.get("three"))
  }

  @Test
  def intMap_plus(): Unit = {
    val m = IntMap("a" -> 1)
    val m2 = m + ("b" -> 2)
    assertResult(Some(2))(m2.get("b"))
    assertResult(None)(m.get("b"))
  }

  @Test
  def intMap_removed(): Unit = {
    val m = IntMap("a" -> 1, "b" -> 2)
    val m2 = m.removed("a")
    assertResult(None)(m2.get("a"))
    assertResult(Some(2))(m2.get("b"))
  }

  @Test
  def intMap_updated(): Unit = {
    val m = IntMap("a" -> 1)
    val m2 = m.updated("a", 99)
    assertResult(Some(99))(m2.get("a"))
    assertResult(Some(1))(m.get("a"))
  }

  @Test
  def intMap_builder_addOne(): Unit = {
    val builder = IntMap.newBuilder
    builder.addOne("x" -> 42)
    val m = builder.result()
    assertResult(Some(42))(m.get("x"))
  }

  // ─── StringArray ─────────────────────────────────────────────────────────────

  @Test
  def stringArray_applyVarargs(): Unit = {
    val arr = StringArray("hello", "world")
    assertResult(2)(arr.length)
    assertResult("hello")(arr(0))
    assertResult("world")(arr(1))
  }

  @Test
  def stringArray_empty(): Unit = {
    assertResult(0)(StringArray.empty.length)
  }

  @Test
  def stringArray_builder_addOne(): Unit = {
    val builder = StringArray.newBuilder
    builder.addOne("foo")
    builder.addOne("bar")
    val arr = builder.result()
    assertResult(2)(arr.length)
    assertResult("foo")(arr(0))
    assertResult("bar")(arr(1))
  }

  // ─── StringMap ───────────────────────────────────────────────────────────────

  @Test
  def stringMap_applyMap(): Unit = {
    val m = StringMap(Map("k1" -> "v1", "k2" -> "v2"))
    assertResult(Some("v1"))(m.get("k1"))
    assertResult(None)(m.get("missing"))
  }

  @Test
  def stringMap_plus(): Unit = {
    val m = StringMap("a" -> "alpha")
    val m2 = m + ("b" -> "beta")
    assertResult(Some("beta"))(m2.get("b"))
    assertResult(None)(m.get("b"))
  }

  @Test
  def stringMap_removed(): Unit = {
    val m = StringMap("a" -> "alpha", "b" -> "beta")
    val m2 = m.removed("a")
    assertResult(None)(m2.get("a"))
    assertResult(Some("beta"))(m2.get("b"))
    // original unchanged
    assertResult(Some("alpha"))(m.get("a"))
  }

  @Test
  def stringMap_updated(): Unit = {
    val m = StringMap("key" -> "old")
    val m2 = m.updated("key", "new")
    assertResult(Some("new"))(m2.get("key"))
    assertResult(Some("old"))(m.get("key"))
  }

  @Test
  def stringMap_builder_addOne(): Unit = {
    val builder = StringMap.newBuilder
    builder.addOne("greeting" -> "hello")
    builder.addOne("farewell" -> "bye")
    val m = builder.result()
    assertResult(Some("hello"))(m.get("greeting"))
    assertResult(Some("bye"))(m.get("farewell"))
  }

  @Test
  def stringMap_builder_clear(): Unit = {
    val builder = StringMap.newBuilder
    builder.addOne("k1" -> "v1")
    builder.clear()
    builder.addOne("k2" -> "v2")
    val m = builder.result()
    assertResult(None)(m.get("k1"))
    assertResult(Some("v2"))(m.get("k2"))
  }

  // ─── Implicit wrap tests ──────────────────────────────────────────────────────

  @Test
  def intArray_implicitWrap(): Unit = {
    val arr: IntArray = List(1, 2, 3)
    assertResult(3)(arr.length)
    assertResult(1)(arr(0))
  }

  @Test
  def stringArray_implicitWrap(): Unit = {
    val arr: StringArray = List("hello", "world")
    assertResult(2)(arr.length)
    assertResult("hello")(arr(0))
  }

  @Test
  def booleanMap_implicitWrap(): Unit = {
    import BooleanMap._
    val m: BooleanMap = Map("a" -> true, "b" -> false)
    assertResult(Some(true))(m.get("a"))
    assertResult(Some(false))(m.get("b"))
  }

  @Test
  def intMap_implicitWrap(): Unit = {
    import IntMap._
    val m: IntMap = Map("x" -> 42)
    assertResult(Some(42))(m.get("x"))
  }

  @Test
  def stringMap_implicitWrap(): Unit = {
    import StringMap._
    val m: StringMap = Map("greeting" -> "hello")
    assertResult(Some("hello"))(m.get("greeting"))
  }

  // ─── StringArray builder clear ────────────────────────────────────────────────

  @Test
  def stringArray_builder_clear(): Unit = {
    val builder = StringArray.newBuilder
    builder.addOne("foo")
    builder.clear()
    builder.addOne("bar")
    val arr = builder.result()
    assertResult(1)(arr.length)
    assertResult("bar")(arr(0))
  }

  // ─── IntArray builder clear ───────────────────────────────────────────────────

  @Test
  def intArray_builder_clear(): Unit = {
    val builder = IntArray.newBuilder
    builder.addOne(99)
    builder.clear()
    builder.addOne(1)
    val arr = builder.result()
    assertResult(1)(arr.length)
    assertResult(1)(arr(0))
  }
}
