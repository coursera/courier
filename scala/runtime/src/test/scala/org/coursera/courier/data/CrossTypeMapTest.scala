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
import org.scalatest.junit.AssertionsForJUnit

/**
 * Tests for generated cross-type map data types (Boolean/Int/Long keyed maps).
 *
 * Each map type tests: apply, get, removed (Scala 2.13 API), iterator, +, updated[V1>:V], empty.
 */
class CrossTypeMapTest extends AssertionsForJUnit {

  private val bs1 = ByteString.copy(Array(0x01.toByte))
  private val bs2 = ByteString.copy(Array(0x02.toByte))

  // ─── Boolean-keyed maps ──────────────────────────────────────────────────────

  @Test def booleanToBooleanMap_basic(): Unit = {
    val m = BooleanToBooleanMap(true -> false, false -> true)
    assertResult(Some(false))(m.get(true))
    assertResult(Some(true))(m.get(false))
    val m2 = m - (true)
    assertResult(None)(m2.get(true))
    assertResult(1)(m2.iterator.size)
    val m3 = m + (true -> true)
    assertResult(Some(true))(m3.get(true))
    assertResult(0)(BooleanToBooleanMap.empty.size)
    val m4: Map[Boolean, Any] = m.updated[Any](true, "overridden")
    assertResult(Some("overridden"))(m4.get(true))
  }

  @Test def booleanToDoubleMap_basic(): Unit = {
    val m = BooleanToDoubleMap(true -> 1.0, false -> 2.0)
    assertResult(Some(1.0))(m.get(true))
    val m2 = m - (true)
    assertResult(None)(m2.get(true))
    val m3 = m + (true -> 99.0)
    assertResult(Some(99.0))(m3.get(true))
    assertResult(0)(BooleanToDoubleMap.empty.size)
    val m4: Map[Boolean, Any] = m.updated[Any](true, "x")
    assertResult(Some("x"))(m4.get(true))
  }

  @Test def booleanToFloatMap_basic(): Unit = {
    val m = BooleanToFloatMap(true -> 1.0f, false -> 2.0f)
    assertResult(Some(1.0f))(m.get(true))
    val m2 = m - (true)
    assertResult(None)(m2.get(true))
    val m3 = m + (true -> 99.0f)
    assertResult(Some(99.0f))(m3.get(true))
    assertResult(0)(BooleanToFloatMap.empty.size)
    val m4: Map[Boolean, Any] = m.updated[Any](true, "x")
    assertResult(Some("x"))(m4.get(true))
  }

  @Test def booleanToIntMap_basic(): Unit = {
    val m = BooleanToIntMap(true -> 1, false -> 2)
    assertResult(Some(1))(m.get(true))
    val m2 = m - (true)
    assertResult(None)(m2.get(true))
    val m3 = m + (true -> 99)
    assertResult(Some(99))(m3.get(true))
    assertResult(0)(BooleanToIntMap.empty.size)
    val m4: Map[Boolean, Any] = m.updated[Any](true, "x")
    assertResult(Some("x"))(m4.get(true))
  }

  @Test def booleanToLongMap_basic(): Unit = {
    val m = BooleanToLongMap(true -> 100L, false -> 200L)
    assertResult(Some(100L))(m.get(true))
    val m2 = m - (true)
    assertResult(None)(m2.get(true))
    val m3 = m + (true -> 999L)
    assertResult(Some(999L))(m3.get(true))
    assertResult(0)(BooleanToLongMap.empty.size)
    val m4: Map[Boolean, Any] = m.updated[Any](true, "x")
    assertResult(Some("x"))(m4.get(true))
  }

  @Test def booleanToStringMap_basic(): Unit = {
    val m = BooleanToStringMap(true -> "yes", false -> "no")
    assertResult(Some("yes"))(m.get(true))
    assertResult(Some("no"))(m.get(false))
    val m2 = m - (true)
    assertResult(None)(m2.get(true))
    assertResult(1)(m2.iterator.size)
    val m3 = m + (true -> "maybe")
    assertResult(Some("maybe"))(m3.get(true))
    assertResult(0)(BooleanToStringMap.empty.size)
    val m4: Map[Boolean, Any] = m.updated[Any](true, 42)
    assertResult(Some(42))(m4.get(true))
  }

  @Test def booleanToByteStringMap_basic(): Unit = {
    val m = BooleanToByteStringMap(true -> bs1, false -> bs2)
    assertResult(Some(bs1))(m.get(true))
    val m2 = m - (true)
    assertResult(None)(m2.get(true))
    val m3 = m + (true -> bs2)
    assertResult(Some(bs2))(m3.get(true))
    assertResult(0)(BooleanToByteStringMap.empty.size)
    val m4: Map[Boolean, Any] = m.updated[Any](true, "x")
    assertResult(Some("x"))(m4.get(true))
  }

  // ─── Int-keyed maps ──────────────────────────────────────────────────────────

  @Test def intToBooleanMap_basic(): Unit = {
    val m = IntToBooleanMap(1 -> true, 2 -> false)
    assertResult(Some(true))(m.get(1))
    assertResult(None)(m.get(99))
    val m2 = m - (1)
    assertResult(None)(m2.get(1))
    val m3 = m + (3 -> true)
    assertResult(Some(true))(m3.get(3))
    assertResult(0)(IntToBooleanMap.empty.size)
    val m4: Map[Int, Any] = m.updated[Any](1, "x")
    assertResult(Some("x"))(m4.get(1))
  }

  @Test def intToDoubleMap_basic(): Unit = {
    val m = IntToDoubleMap(1 -> 1.0, 2 -> 2.0)
    assertResult(Some(1.0))(m.get(1))
    val m2 = m - (1)
    assertResult(None)(m2.get(1))
    val m3 = m + (3 -> 3.0)
    assertResult(Some(3.0))(m3.get(3))
    assertResult(0)(IntToDoubleMap.empty.size)
    val m4: Map[Int, Any] = m.updated[Any](1, "x")
    assertResult(Some("x"))(m4.get(1))
  }

  @Test def intToFloatMap_basic(): Unit = {
    val m = IntToFloatMap(1 -> 1.0f, 2 -> 2.0f)
    assertResult(Some(1.0f))(m.get(1))
    val m2 = m - (1)
    assertResult(None)(m2.get(1))
    val m3 = m + (3 -> 3.0f)
    assertResult(Some(3.0f))(m3.get(3))
    assertResult(0)(IntToFloatMap.empty.size)
    val m4: Map[Int, Any] = m.updated[Any](1, "x")
    assertResult(Some("x"))(m4.get(1))
  }

  @Test def intToIntMap_basic(): Unit = {
    val m = IntToIntMap(1 -> 10, 2 -> 20)
    assertResult(Some(10))(m.get(1))
    assertResult(None)(m.get(99))
    val m2 = m - (1)
    assertResult(None)(m2.get(1))
    val m3 = m + (3 -> 30)
    assertResult(Some(30))(m3.get(3))
    assertResult(0)(IntToIntMap.empty.size)
    val m4: Map[Int, Any] = m.updated[Any](1, "x")
    assertResult(Some("x"))(m4.get(1))
  }

  @Test def intToLongMap_basic(): Unit = {
    val m = IntToLongMap(1 -> 100L, 2 -> 200L)
    assertResult(Some(100L))(m.get(1))
    val m2 = m - (1)
    assertResult(None)(m2.get(1))
    val m3 = m + (3 -> 300L)
    assertResult(Some(300L))(m3.get(3))
    assertResult(0)(IntToLongMap.empty.size)
    val m4: Map[Int, Any] = m.updated[Any](1, "x")
    assertResult(Some("x"))(m4.get(1))
  }

  @Test def intToStringMap_basic(): Unit = {
    val m = IntToStringMap(1 -> "one", 2 -> "two")
    assertResult(Some("one"))(m.get(1))
    assertResult(None)(m.get(99))
    val m2 = m - (1)
    assertResult(None)(m2.get(1))
    assertResult(1)(m2.iterator.size)
    val m3 = m + (3 -> "three")
    assertResult(Some("three"))(m3.get(3))
    assertResult(0)(IntToStringMap.empty.size)
    val m4: Map[Int, Any] = m.updated[Any](1, 42)
    assertResult(Some(42))(m4.get(1))
  }

  @Test def intToByteStringMap_basic(): Unit = {
    val m = IntToByteStringMap(1 -> bs1, 2 -> bs2)
    assertResult(Some(bs1))(m.get(1))
    val m2 = m - (1)
    assertResult(None)(m2.get(1))
    val m3 = m + (3 -> bs2)
    assertResult(Some(bs2))(m3.get(3))
    assertResult(0)(IntToByteStringMap.empty.size)
    val m4: Map[Int, Any] = m.updated[Any](1, "x")
    assertResult(Some("x"))(m4.get(1))
  }

  // ─── Long-keyed maps ─────────────────────────────────────────────────────────

  @Test def longToBooleanMap_basic(): Unit = {
    val m = LongToBooleanMap(1L -> true, 2L -> false)
    assertResult(Some(true))(m.get(1L))
    assertResult(None)(m.get(99L))
    val m2 = m - (1L)
    assertResult(None)(m2.get(1L))
    val m3 = m + (3L -> true)
    assertResult(Some(true))(m3.get(3L))
    assertResult(0)(LongToBooleanMap.empty.size)
    val m4: Map[Long, Any] = m.updated[Any](1L, "x")
    assertResult(Some("x"))(m4.get(1L))
  }

  @Test def longToDoubleMap_basic(): Unit = {
    val m = LongToDoubleMap(1L -> 1.0, 2L -> 2.0)
    assertResult(Some(1.0))(m.get(1L))
    val m2 = m - (1L)
    assertResult(None)(m2.get(1L))
    val m3 = m + (3L -> 3.0)
    assertResult(Some(3.0))(m3.get(3L))
    assertResult(0)(LongToDoubleMap.empty.size)
    val m4: Map[Long, Any] = m.updated[Any](1L, "x")
    assertResult(Some("x"))(m4.get(1L))
  }

  @Test def longToFloatMap_basic(): Unit = {
    val m = LongToFloatMap(1L -> 1.0f, 2L -> 2.0f)
    assertResult(Some(1.0f))(m.get(1L))
    val m2 = m - (1L)
    assertResult(None)(m2.get(1L))
    val m3 = m + (3L -> 3.0f)
    assertResult(Some(3.0f))(m3.get(3L))
    assertResult(0)(LongToFloatMap.empty.size)
    val m4: Map[Long, Any] = m.updated[Any](1L, "x")
    assertResult(Some("x"))(m4.get(1L))
  }

  @Test def longToIntMap_basic(): Unit = {
    val m = LongToIntMap(1L -> 10, 2L -> 20)
    assertResult(Some(10))(m.get(1L))
    assertResult(None)(m.get(99L))
    val m2 = m - (1L)
    assertResult(None)(m2.get(1L))
    val m3 = m + (3L -> 30)
    assertResult(Some(30))(m3.get(3L))
    assertResult(0)(LongToIntMap.empty.size)
    val m4: Map[Long, Any] = m.updated[Any](1L, "x")
    assertResult(Some("x"))(m4.get(1L))
  }

  @Test def longToLongMap_basic(): Unit = {
    val m = LongToLongMap(1L -> 100L, 2L -> 200L)
    assertResult(Some(100L))(m.get(1L))
    val m2 = m - (1L)
    assertResult(None)(m2.get(1L))
    val m3 = m + (3L -> 300L)
    assertResult(Some(300L))(m3.get(3L))
    assertResult(0)(LongToLongMap.empty.size)
    val m4: Map[Long, Any] = m.updated[Any](1L, "x")
    assertResult(Some("x"))(m4.get(1L))
  }

  @Test def longToStringMap_basic(): Unit = {
    val m = LongToStringMap(1L -> "one", 2L -> "two")
    assertResult(Some("one"))(m.get(1L))
    assertResult(None)(m.get(99L))
    val m2 = m - (1L)
    assertResult(None)(m2.get(1L))
    assertResult(1)(m2.iterator.size)
    val m3 = m + (3L -> "three")
    assertResult(Some("three"))(m3.get(3L))
    assertResult(0)(LongToStringMap.empty.size)
    val m4: Map[Long, Any] = m.updated[Any](1L, 42)
    assertResult(Some(42))(m4.get(1L))
  }

  @Test def longToByteStringMap_basic(): Unit = {
    val m = LongToByteStringMap(1L -> bs1, 2L -> bs2)
    assertResult(Some(bs1))(m.get(1L))
    val m2 = m - (1L)
    assertResult(None)(m2.get(1L))
    val m3 = m + (3L -> bs2)
    assertResult(Some(bs2))(m3.get(3L))
    assertResult(0)(LongToByteStringMap.empty.size)
    val m4: Map[Long, Any] = m.updated[Any](1L, "x")
    assertResult(Some("x"))(m4.get(1L))
  }

  // ─── Implicit wrap (triggers coerceKeyOutput / apply(Map[K,V]) path) ─────────

  @Test def booleanToBooleanMap_implicitWrap(): Unit = {
    import BooleanToBooleanMap._
    val m: BooleanToBooleanMap = Map(true -> false, false -> true)
    assertResult(Some(false))(m.get(true))
    assertResult(Some(true))(m.get(false))
  }

  @Test def booleanToDoubleMap_implicitWrap(): Unit = {
    import BooleanToDoubleMap._
    val m: BooleanToDoubleMap = Map(true -> 1.0, false -> 2.0)
    assertResult(Some(1.0))(m.get(true))
  }

  @Test def booleanToFloatMap_implicitWrap(): Unit = {
    import BooleanToFloatMap._
    val m: BooleanToFloatMap = Map(true -> 1.0f)
    assertResult(Some(1.0f))(m.get(true))
  }

  @Test def booleanToIntMap_implicitWrap(): Unit = {
    import BooleanToIntMap._
    val m: BooleanToIntMap = Map(true -> 42, false -> 0)
    assertResult(Some(42))(m.get(true))
  }

  @Test def booleanToLongMap_implicitWrap(): Unit = {
    import BooleanToLongMap._
    val m: BooleanToLongMap = Map(true -> 100L)
    assertResult(Some(100L))(m.get(true))
  }

  @Test def booleanToStringMap_implicitWrap(): Unit = {
    import BooleanToStringMap._
    val m: BooleanToStringMap = Map(true -> "yes", false -> "no")
    assertResult(Some("yes"))(m.get(true))
    assertResult(Some("no"))(m.get(false))
  }

  @Test def booleanToByteStringMap_implicitWrap(): Unit = {
    import BooleanToByteStringMap._
    val m: BooleanToByteStringMap = Map(true -> bs1)
    assertResult(Some(bs1))(m.get(true))
  }

  @Test def intToBooleanMap_implicitWrap(): Unit = {
    import IntToBooleanMap._
    val m: IntToBooleanMap = Map(1 -> true, 2 -> false)
    assertResult(Some(true))(m.get(1))
  }

  @Test def intToDoubleMap_implicitWrap(): Unit = {
    import IntToDoubleMap._
    val m: IntToDoubleMap = Map(1 -> 3.14)
    assertResult(Some(3.14))(m.get(1))
  }

  @Test def intToFloatMap_implicitWrap(): Unit = {
    import IntToFloatMap._
    val m: IntToFloatMap = Map(1 -> 1.5f)
    assertResult(Some(1.5f))(m.get(1))
  }

  @Test def intToIntMap_implicitWrap(): Unit = {
    import IntToIntMap._
    val m: IntToIntMap = Map(1 -> 10, 2 -> 20)
    assertResult(Some(10))(m.get(1))
  }

  @Test def intToLongMap_implicitWrap(): Unit = {
    import IntToLongMap._
    val m: IntToLongMap = Map(1 -> 100L)
    assertResult(Some(100L))(m.get(1))
  }

  @Test def intToStringMap_implicitWrap(): Unit = {
    import IntToStringMap._
    val m: IntToStringMap = Map(1 -> "one", 2 -> "two")
    assertResult(Some("one"))(m.get(1))
  }

  @Test def intToByteStringMap_implicitWrap(): Unit = {
    import IntToByteStringMap._
    val m: IntToByteStringMap = Map(1 -> bs1)
    assertResult(Some(bs1))(m.get(1))
  }

  @Test def longToBooleanMap_implicitWrap(): Unit = {
    import LongToBooleanMap._
    val m: LongToBooleanMap = Map(1L -> true)
    assertResult(Some(true))(m.get(1L))
  }

  @Test def longToDoubleMap_implicitWrap(): Unit = {
    import LongToDoubleMap._
    val m: LongToDoubleMap = Map(1L -> 2.71)
    assertResult(Some(2.71))(m.get(1L))
  }

  @Test def longToFloatMap_implicitWrap(): Unit = {
    import LongToFloatMap._
    val m: LongToFloatMap = Map(1L -> 9.9f)
    assertResult(Some(9.9f))(m.get(1L))
  }

  @Test def longToIntMap_implicitWrap(): Unit = {
    import LongToIntMap._
    val m: LongToIntMap = Map(1L -> 42)
    assertResult(Some(42))(m.get(1L))
  }

  @Test def longToLongMap_implicitWrap(): Unit = {
    import LongToLongMap._
    val m: LongToLongMap = Map(1L -> 999L)
    assertResult(Some(999L))(m.get(1L))
  }

  @Test def longToStringMap_implicitWrap(): Unit = {
    import LongToStringMap._
    val m: LongToStringMap = Map(1L -> "one")
    assertResult(Some("one"))(m.get(1L))
  }

  @Test def longToByteStringMap_implicitWrap(): Unit = {
    import LongToByteStringMap._
    val m: LongToByteStringMap = Map(1L -> bs1)
    assertResult(Some(bs1))(m.get(1L))
  }

  // ─── DataBuilder (addOne + clear) ────────────────────────────────────────────

  @Test def booleanToStringMap_builder_addOne(): Unit = {
    val builder = BooleanToStringMap.newBuilder
    builder += (true -> "yes")
    builder += (false -> "no")
    val m = builder.result()
    assertResult(Some("yes"))(m.get(true))
    assertResult(Some("no"))(m.get(false))
  }

  @Test def booleanToStringMap_builder_clear(): Unit = {
    val builder = BooleanToStringMap.newBuilder
    builder += (true -> "old")
    builder.clear()
    builder += (false -> "kept")
    val m = builder.result()
    assertResult(None)(m.get(true))
    assertResult(Some("kept"))(m.get(false))
  }

  @Test def intToStringMap_builder_addOne(): Unit = {
    val builder = IntToStringMap.newBuilder
    builder += (1 -> "one")
    builder += (2 -> "two")
    val m = builder.result()
    assertResult(Some("one"))(m.get(1))
    assertResult(Some("two"))(m.get(2))
  }

  @Test def intToStringMap_builder_clear(): Unit = {
    val builder = IntToStringMap.newBuilder
    builder += (1 -> "old")
    builder.clear()
    builder += (2 -> "new")
    val m = builder.result()
    assertResult(None)(m.get(1))
    assertResult(Some("new"))(m.get(2))
  }

  @Test def longToStringMap_builder_addOne(): Unit = {
    val builder = LongToStringMap.newBuilder
    builder += (1L -> "one")
    builder += (2L -> "two")
    val m = builder.result()
    assertResult(Some("one"))(m.get(1L))
    assertResult(Some("two"))(m.get(2L))
  }

  @Test def longToStringMap_builder_clear(): Unit = {
    val builder = LongToStringMap.newBuilder
    builder += (1L -> "old")
    builder.clear()
    builder += (2L -> "new")
    val m = builder.result()
    assertResult(None)(m.get(1L))
    assertResult(Some("new"))(m.get(2L))
  }

  @Test def booleanToBooleanMap_builder(): Unit = {
    val b = BooleanToBooleanMap.newBuilder
    b += (true -> false); b += (false -> true)
    val m = b.result()
    assertResult(Some(false))(m.get(true))
    val b2 = BooleanToBooleanMap.newBuilder; b2 += (true -> true); b2.clear(); b2 += (false -> false)
    assertResult(None)(b2.result().get(true))
  }

  @Test def booleanToDoubleMap_builder(): Unit = {
    val b = BooleanToDoubleMap.newBuilder
    b += (true -> 1.1); b += (false -> 2.2)
    val m = b.result()
    assertResult(Some(1.1))(m.get(true))
    val b2 = BooleanToDoubleMap.newBuilder; b2 += (true -> 9.9); b2.clear(); b2 += (false -> 3.3)
    assertResult(None)(b2.result().get(true))
  }

  @Test def booleanToFloatMap_builder(): Unit = {
    val b = BooleanToFloatMap.newBuilder
    b += (true -> 1.1f); b += (false -> 2.2f)
    val m = b.result()
    assertResult(Some(1.1f))(m.get(true))
    val b2 = BooleanToFloatMap.newBuilder; b2 += (true -> 9.9f); b2.clear(); b2 += (false -> 3.3f)
    assertResult(None)(b2.result().get(true))
  }

  @Test def booleanToIntMap_builder(): Unit = {
    val b = BooleanToIntMap.newBuilder
    b += (true -> 1); b += (false -> 2)
    val m = b.result()
    assertResult(Some(1))(m.get(true))
    val b2 = BooleanToIntMap.newBuilder; b2 += (true -> 9); b2.clear(); b2 += (false -> 3)
    assertResult(None)(b2.result().get(true))
  }

  @Test def booleanToLongMap_builder(): Unit = {
    val b = BooleanToLongMap.newBuilder
    b += (true -> 100L); b += (false -> 200L)
    val m = b.result()
    assertResult(Some(100L))(m.get(true))
    val b2 = BooleanToLongMap.newBuilder; b2 += (true -> 999L); b2.clear(); b2 += (false -> 1L)
    assertResult(None)(b2.result().get(true))
  }

  @Test def booleanToByteStringMap_builder(): Unit = {
    val b = BooleanToByteStringMap.newBuilder
    b += (true -> bs1); b += (false -> bs2)
    val m = b.result()
    assertResult(Some(bs1))(m.get(true))
    val b2 = BooleanToByteStringMap.newBuilder; b2 += (true -> bs1); b2.clear(); b2 += (false -> bs2)
    assertResult(None)(b2.result().get(true))
  }

  @Test def intToBooleanMap_builder(): Unit = {
    val b = IntToBooleanMap.newBuilder
    b += (1 -> true); b += (2 -> false)
    val m = b.result()
    assertResult(Some(true))(m.get(1))
    val b2 = IntToBooleanMap.newBuilder; b2 += (1 -> true); b2.clear(); b2 += (2 -> false)
    assertResult(None)(b2.result().get(1))
  }

  @Test def intToDoubleMap_builder(): Unit = {
    val b = IntToDoubleMap.newBuilder
    b += (1 -> 1.1); b += (2 -> 2.2)
    val m = b.result()
    assertResult(Some(1.1))(m.get(1))
    val b2 = IntToDoubleMap.newBuilder; b2 += (1 -> 9.9); b2.clear(); b2 += (2 -> 3.3)
    assertResult(None)(b2.result().get(1))
  }

  @Test def intToFloatMap_builder(): Unit = {
    val b = IntToFloatMap.newBuilder
    b += (1 -> 1.1f); b += (2 -> 2.2f)
    val m = b.result()
    assertResult(Some(1.1f))(m.get(1))
    val b2 = IntToFloatMap.newBuilder; b2 += (1 -> 9.9f); b2.clear(); b2 += (2 -> 3.3f)
    assertResult(None)(b2.result().get(1))
  }

  @Test def intToIntMap_builder(): Unit = {
    val b = IntToIntMap.newBuilder
    b += (1 -> 10); b += (2 -> 20)
    val m = b.result()
    assertResult(Some(10))(m.get(1))
    val b2 = IntToIntMap.newBuilder; b2 += (1 -> 99); b2.clear(); b2 += (2 -> 1)
    assertResult(None)(b2.result().get(1))
  }

  @Test def intToLongMap_builder(): Unit = {
    val b = IntToLongMap.newBuilder
    b += (1 -> 100L); b += (2 -> 200L)
    val m = b.result()
    assertResult(Some(100L))(m.get(1))
    val b2 = IntToLongMap.newBuilder; b2 += (1 -> 999L); b2.clear(); b2 += (2 -> 1L)
    assertResult(None)(b2.result().get(1))
  }

  @Test def intToByteStringMap_builder(): Unit = {
    val b = IntToByteStringMap.newBuilder
    b += (1 -> bs1); b += (2 -> bs2)
    val m = b.result()
    assertResult(Some(bs1))(m.get(1))
    val b2 = IntToByteStringMap.newBuilder; b2 += (1 -> bs1); b2.clear(); b2 += (2 -> bs2)
    assertResult(None)(b2.result().get(1))
  }

  @Test def longToBooleanMap_builder(): Unit = {
    val b = LongToBooleanMap.newBuilder
    b += (1L -> true); b += (2L -> false)
    val m = b.result()
    assertResult(Some(true))(m.get(1L))
    val b2 = LongToBooleanMap.newBuilder; b2 += (1L -> true); b2.clear(); b2 += (2L -> false)
    assertResult(None)(b2.result().get(1L))
  }

  @Test def longToDoubleMap_builder(): Unit = {
    val b = LongToDoubleMap.newBuilder
    b += (1L -> 1.1); b += (2L -> 2.2)
    val m = b.result()
    assertResult(Some(1.1))(m.get(1L))
    val b2 = LongToDoubleMap.newBuilder; b2 += (1L -> 9.9); b2.clear(); b2 += (2L -> 3.3)
    assertResult(None)(b2.result().get(1L))
  }

  @Test def longToFloatMap_builder(): Unit = {
    val b = LongToFloatMap.newBuilder
    b += (1L -> 1.1f); b += (2L -> 2.2f)
    val m = b.result()
    assertResult(Some(1.1f))(m.get(1L))
    val b2 = LongToFloatMap.newBuilder; b2 += (1L -> 9.9f); b2.clear(); b2 += (2L -> 3.3f)
    assertResult(None)(b2.result().get(1L))
  }

  @Test def longToIntMap_builder(): Unit = {
    val b = LongToIntMap.newBuilder
    b += (1L -> 10); b += (2L -> 20)
    val m = b.result()
    assertResult(Some(10))(m.get(1L))
    val b2 = LongToIntMap.newBuilder; b2 += (1L -> 99); b2.clear(); b2 += (2L -> 1)
    assertResult(None)(b2.result().get(1L))
  }

  @Test def longToLongMap_builder(): Unit = {
    val b = LongToLongMap.newBuilder
    b += (1L -> 100L); b += (2L -> 200L)
    val m = b.result()
    assertResult(Some(100L))(m.get(1L))
    val b2 = LongToLongMap.newBuilder; b2 += (1L -> 999L); b2.clear(); b2 += (2L -> 1L)
    assertResult(None)(b2.result().get(1L))
  }

  @Test def longToByteStringMap_builder(): Unit = {
    val b = LongToByteStringMap.newBuilder
    b += (1L -> bs1); b += (2L -> bs2)
    val m = b.result()
    assertResult(Some(bs1))(m.get(1L))
    val b2 = LongToByteStringMap.newBuilder; b2 += (1L -> bs1); b2.clear(); b2 += (2L -> bs2)
    assertResult(None)(b2.result().get(1L))
  }
}
