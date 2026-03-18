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

import org.coursera.courier.data.IntArray
import org.coursera.courier.generator.customtypes.CustomArrayTestId
import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.courier.generator.customtypes.CustomMapTestKeyId
import org.coursera.courier.generator.customtypes.CustomMapTestValueId
import org.coursera.courier.generator.customtypes.CustomRecord
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.customtypes.CustomArrayTestIdArray
import org.coursera.customtypes.CustomIntArray
import org.coursera.customtypes.CustomIntMap
import org.coursera.customtypes.CustomIntToStringMap
import org.coursera.customtypes.CustomMapTestKeyIdToCustomMapTestValueIdMap
import org.coursera.customtypes.CustomRecordArray
import org.coursera.customtypes.CustomRecordToCustomRecordMap
import org.coursera.customtypes.IntIdArray
import org.coursera.customtypes.IntIdMap
import org.coursera.customtypes.IntIdToStringMap
import org.coursera.courier.generator.customtypes.IntId
import org.coursera.enums.Fruits
import org.coursera.enums.FruitsArray
import org.coursera.enums.FruitsMap
import org.coursera.enums.FruitsToStringMap
import org.coursera.fixed.Fixed8
import org.coursera.fixed.Fixed8Array
import org.coursera.fixed.Fixed8Map
import org.coursera.fixed.Fixed8ToStringMap
import org.coursera.records.test.Empty
import org.coursera.records.test.EmptyArray
import org.coursera.records.test.EmptyMap
import org.coursera.records.test.Simple
import org.coursera.records.test.SimpleArray
import org.coursera.records.test.SimpleArrayArray
import org.coursera.records.test.SimpleArrayMap
import org.coursera.records.test.SimpleMap
import org.coursera.records.test.SimpleMapArray
import org.coursera.records.test.SimpleMapMap
import org.coursera.records.test.SimpleToStringMap
import org.junit.Test

/**
 * Tests exercising DataBuilder classes that previously had 0% coverage.
 * These are the inner `DataBuilder` classes inside array and map companions.
 */
class DataBuilderCoverageTest extends GeneratorTest with SchemaFixtures {

  // ---------------------------------------------------------------------------
  // SimpleArray DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleArray_dataBuilder(): Unit = {
    val builder = SimpleArray.newBuilder
    builder.addOne(Simple(Some("a")))
    builder.addOne(Simple(Some("b")))
    val result = builder.result()
    assert(result.length === 2)
    assert(result(0).message === Some("a"))
    assert(result(1).message === Some("b"))
  }

  @Test
  def testSimpleArray_dataBuilder_clear(): Unit = {
    val builder = SimpleArray.newBuilder
    builder.addOne(Simple(Some("x")))
    builder.clear()
    val result = builder.result()
    assert(result.length === 0)
  }

  // ---------------------------------------------------------------------------
  // SimpleArrayArray DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleArrayArray_dataBuilder(): Unit = {
    val builder = SimpleArrayArray.newBuilder
    builder.addOne(SimpleArray(Simple(Some("a"))))
    val result = builder.result()
    assert(result.length === 1)
  }

  @Test
  def testSimpleArrayArray_dataBuilder_clear(): Unit = {
    val builder = SimpleArrayArray.newBuilder
    builder.addOne(SimpleArray(Simple(Some("x"))))
    builder.clear()
    assert(builder.result().length === 0)
  }

  // ---------------------------------------------------------------------------
  // SimpleMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleMap_dataBuilder(): Unit = {
    val builder = SimpleMap.newBuilder
    builder.addOne("k1" -> Simple(Some("v1")))
    builder.addOne("k2" -> Simple(Some("v2")))
    val result = builder.result()
    assert(result.size === 2)
    assert(result.get("k1").flatMap(_.message) === Some("v1"))
  }

  @Test
  def testSimpleMap_dataBuilder_clear(): Unit = {
    val builder = SimpleMap.newBuilder
    builder.addOne("k" -> Simple(Some("v")))
    builder.clear()
    assert(builder.result().size === 0)
  }

  // ---------------------------------------------------------------------------
  // SimpleMapArray DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleMapArray_dataBuilder(): Unit = {
    val builder = SimpleMapArray.newBuilder
    builder.addOne(SimpleMap("k" -> Simple(Some("v"))))
    val result = builder.result()
    assert(result.length === 1)
  }

  @Test
  def testSimpleMapArray_dataBuilder_clear(): Unit = {
    val builder = SimpleMapArray.newBuilder
    builder.addOne(SimpleMap("k" -> Simple(Some("v"))))
    builder.clear()
    assert(builder.result().length === 0)
  }

  // ---------------------------------------------------------------------------
  // SimpleMapMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleMapMap_dataBuilder(): Unit = {
    val builder = SimpleMapMap.newBuilder
    builder.addOne("outer" -> SimpleMap("inner" -> Simple(Some("v"))))
    val result = builder.result()
    assert(result.size === 1)
  }

  @Test
  def testSimpleMapMap_dataBuilder_clear(): Unit = {
    val builder = SimpleMapMap.newBuilder
    builder.addOne("k" -> SimpleMap("kk" -> Simple(Some("v"))))
    builder.clear()
    assert(builder.result().size === 0)
  }

  // ---------------------------------------------------------------------------
  // SimpleArrayMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleArrayMap_dataBuilder(): Unit = {
    val builder = SimpleArrayMap.newBuilder
    builder.addOne("key" -> SimpleArray(Simple(Some("v"))))
    val result = builder.result()
    assert(result.size === 1)
  }

  // ---------------------------------------------------------------------------
  // SimpleToStringMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testSimpleToStringMap_dataBuilder(): Unit = {
    val builder = SimpleToStringMap.newBuilder
    builder.addOne(Simple(Some("k")) -> "value")
    val result = builder.result()
    assert(result.size === 1)
  }

  @Test
  def testSimpleToStringMap_dataBuilder_clear(): Unit = {
    val builder = SimpleToStringMap.newBuilder
    builder.addOne(Simple(Some("k")) -> "v")
    builder.clear()
    assert(builder.result().size === 0)
  }

  // ---------------------------------------------------------------------------
  // EmptyArray DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testEmptyArray_dataBuilder(): Unit = {
    val builder = EmptyArray.newBuilder
    builder.addOne(Empty())
    builder.addOne(Empty())
    val result = builder.result()
    assert(result.length === 2)
  }

  @Test
  def testEmptyArray_dataBuilder_clear(): Unit = {
    val builder = EmptyArray.newBuilder
    builder.addOne(Empty())
    builder.clear()
    assert(builder.result().length === 0)
  }

  // ---------------------------------------------------------------------------
  // EmptyMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testEmptyMap_dataBuilder(): Unit = {
    val builder = EmptyMap.newBuilder
    builder.addOne("k1" -> Empty())
    builder.addOne("k2" -> Empty())
    val result = builder.result()
    assert(result.size === 2)
  }

  @Test
  def testEmptyMap_dataBuilder_clear(): Unit = {
    val builder = EmptyMap.newBuilder
    builder.addOne("k" -> Empty())
    builder.clear()
    assert(builder.result().size === 0)
  }

  // ---------------------------------------------------------------------------
  // FruitsArray DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testFruitsArray_dataBuilder(): Unit = {
    val builder = FruitsArray.newBuilder
    builder.addOne(Fruits.APPLE)
    builder.addOne(Fruits.BANANA)
    val result = builder.result()
    assert(result.length === 2)
    assert(result(0) === Fruits.APPLE)
    assert(result(1) === Fruits.BANANA)
  }

  @Test
  def testFruitsArray_dataBuilder_clear(): Unit = {
    val builder = FruitsArray.newBuilder
    builder.addOne(Fruits.APPLE)
    builder.clear()
    assert(builder.result().length === 0)
  }

  // ---------------------------------------------------------------------------
  // FruitsMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testFruitsMap_dataBuilder(): Unit = {
    val builder = FruitsMap.newBuilder
    builder.addOne("a" -> Fruits.APPLE)
    builder.addOne("b" -> Fruits.BANANA)
    val result = builder.result()
    assert(result.size === 2)
    assert(result.get("a") === Some(Fruits.APPLE))
  }

  @Test
  def testFruitsMap_dataBuilder_clear(): Unit = {
    val builder = FruitsMap.newBuilder
    builder.addOne("k" -> Fruits.APPLE)
    builder.clear()
    assert(builder.result().size === 0)
  }

  // ---------------------------------------------------------------------------
  // FruitsToStringMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testFruitsToStringMap_dataBuilder(): Unit = {
    val builder = FruitsToStringMap.newBuilder
    builder.addOne(Fruits.APPLE -> "apple")
    builder.addOne(Fruits.BANANA -> "banana")
    val result = builder.result()
    assert(result.size === 2)
    assert(result.get(Fruits.APPLE) === Some("apple"))
  }

  @Test
  def testFruitsToStringMap_dataBuilder_clear(): Unit = {
    val builder = FruitsToStringMap.newBuilder
    builder.addOne(Fruits.APPLE -> "apple")
    builder.clear()
    assert(builder.result().size === 0)
  }

  // ---------------------------------------------------------------------------
  // Fixed8Array DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testFixed8Array_dataBuilder(): Unit = {
    val builder = Fixed8Array.newBuilder
    builder.addOne(Fixed8(bytesFixed8))
    val result = builder.result()
    assert(result.length === 1)
  }

  @Test
  def testFixed8Array_dataBuilder_clear(): Unit = {
    val builder = Fixed8Array.newBuilder
    builder.addOne(Fixed8(bytesFixed8))
    builder.clear()
    assert(builder.result().length === 0)
  }

  // ---------------------------------------------------------------------------
  // Fixed8Map DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testFixed8Map_dataBuilder(): Unit = {
    val builder = Fixed8Map.newBuilder
    builder.addOne("k" -> Fixed8(bytesFixed8))
    val result = builder.result()
    assert(result.size === 1)
  }

  @Test
  def testFixed8Map_dataBuilder_clear(): Unit = {
    val builder = Fixed8Map.newBuilder
    builder.addOne("k" -> Fixed8(bytesFixed8))
    builder.clear()
    assert(builder.result().size === 0)
  }

  // ---------------------------------------------------------------------------
  // Fixed8ToStringMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testFixed8ToStringMap_dataBuilder(): Unit = {
    val builder = Fixed8ToStringMap.newBuilder
    builder.addOne(Fixed8(bytesFixed8) -> "fixed")
    val result = builder.result()
    assert(result.size === 1)
  }

  @Test
  def testFixed8ToStringMap_dataBuilder_clear(): Unit = {
    val builder = Fixed8ToStringMap.newBuilder
    builder.addOne(Fixed8(bytesFixed8) -> "fixed")
    builder.clear()
    assert(builder.result().size === 0)
  }

  // ---------------------------------------------------------------------------
  // CustomIntArray DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testCustomIntArray_dataBuilder(): Unit = {
    val builder = CustomIntArray.newBuilder
    builder.addOne(CustomInt(1))
    builder.addOne(CustomInt(2))
    val result = builder.result()
    assert(result.length === 2)
    assert(result(0) === CustomInt(1))
  }

  @Test
  def testCustomIntArray_dataBuilder_clear(): Unit = {
    val builder = CustomIntArray.newBuilder
    builder.addOne(CustomInt(1))
    builder.clear()
    assert(builder.result().length === 0)
  }

  // ---------------------------------------------------------------------------
  // CustomIntMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testCustomIntMap_dataBuilder(): Unit = {
    val builder = CustomIntMap.newBuilder
    builder.addOne("a" -> CustomInt(10))
    builder.addOne("b" -> CustomInt(20))
    val result = builder.result()
    assert(result.size === 2)
    assert(result.get("a") === Some(CustomInt(10)))
  }

  @Test
  def testCustomIntMap_dataBuilder_clear(): Unit = {
    val builder = CustomIntMap.newBuilder
    builder.addOne("k" -> CustomInt(1))
    builder.clear()
    assert(builder.result().size === 0)
  }

  // ---------------------------------------------------------------------------
  // CustomIntToStringMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testCustomIntToStringMap_dataBuilder(): Unit = {
    val builder = CustomIntToStringMap.newBuilder
    builder.addOne(CustomInt(1) -> "one")
    val result = builder.result()
    assert(result.size === 1)
    assert(result.get(CustomInt(1)) === Some("one"))
  }

  @Test
  def testCustomIntToStringMap_dataBuilder_clear(): Unit = {
    val builder = CustomIntToStringMap.newBuilder
    builder.addOne(CustomInt(1) -> "one")
    builder.clear()
    assert(builder.result().size === 0)
  }

  // ---------------------------------------------------------------------------
  // CustomRecordArray DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testCustomRecordArray_dataBuilder(): Unit = {
    val builder = CustomRecordArray.newBuilder
    builder.addOne(CustomRecord("t1", "b1"))
    builder.addOne(CustomRecord("t2", "b2"))
    val result = builder.result()
    assert(result.length === 2)
    assert(result(0).title === "t1")
  }

  @Test
  def testCustomRecordArray_dataBuilder_clear(): Unit = {
    val builder = CustomRecordArray.newBuilder
    builder.addOne(CustomRecord("t", "b"))
    builder.clear()
    assert(builder.result().length === 0)
  }

  // ---------------------------------------------------------------------------
  // CustomRecordToCustomRecordMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testCustomRecordToCustomRecordMap_dataBuilder(): Unit = {
    val builder = CustomRecordToCustomRecordMap.newBuilder
    builder.addOne(CustomRecord("kt", "kb") -> CustomRecord("vt", "vb"))
    val result = builder.result()
    assert(result.size === 1)
  }

  @Test
  def testCustomRecordToCustomRecordMap_dataBuilder_clear(): Unit = {
    val builder = CustomRecordToCustomRecordMap.newBuilder
    builder.addOne(CustomRecord("k", "k") -> CustomRecord("v", "v"))
    builder.clear()
    assert(builder.result().size === 0)
  }

  // ---------------------------------------------------------------------------
  // CustomArrayTestIdArray DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testCustomArrayTestIdArray_dataBuilder(): Unit = {
    val builder = CustomArrayTestIdArray.newBuilder
    builder.addOne(CustomArrayTestId(1))
    builder.addOne(CustomArrayTestId(2))
    val result = builder.result()
    assert(result.length === 2)
  }

  @Test
  def testCustomArrayTestIdArray_dataBuilder_clear(): Unit = {
    val builder = CustomArrayTestIdArray.newBuilder
    builder.addOne(CustomArrayTestId(1))
    builder.clear()
    assert(builder.result().length === 0)
  }

  // ---------------------------------------------------------------------------
  // CustomMapTestKeyIdToCustomMapTestValueIdMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testCustomMapTestKeyIdMap_dataBuilder(): Unit = {
    val builder = CustomMapTestKeyIdToCustomMapTestValueIdMap.newBuilder
    builder.addOne(CustomMapTestKeyId(1) -> CustomMapTestValueId(100))
    val result = builder.result()
    assert(result.size === 1)
    assert(result.get(CustomMapTestKeyId(1)) === Some(CustomMapTestValueId(100)))
  }

  @Test
  def testCustomMapTestKeyIdMap_dataBuilder_clear(): Unit = {
    val builder = CustomMapTestKeyIdToCustomMapTestValueIdMap.newBuilder
    builder.addOne(CustomMapTestKeyId(1) -> CustomMapTestValueId(10))
    builder.clear()
    assert(builder.result().size === 0)
  }

  // ---------------------------------------------------------------------------
  // IntIdArray DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testIntIdArray_dataBuilder(): Unit = {
    val builder = IntIdArray.newBuilder
    builder.addOne(IntId(1))
    builder.addOne(IntId(2))
    val result = builder.result()
    assert(result.length === 2)
  }

  @Test
  def testIntIdArray_dataBuilder_clear(): Unit = {
    val builder = IntIdArray.newBuilder
    builder.addOne(IntId(1))
    builder.clear()
    assert(builder.result().length === 0)
  }

  // ---------------------------------------------------------------------------
  // IntIdMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testIntIdMap_dataBuilder(): Unit = {
    val builder = IntIdMap.newBuilder
    builder.addOne("k" -> IntId(10))
    val result = builder.result()
    assert(result.size === 1)
  }

  @Test
  def testIntIdMap_dataBuilder_clear(): Unit = {
    val builder = IntIdMap.newBuilder
    builder.addOne("k" -> IntId(1))
    builder.clear()
    assert(builder.result().size === 0)
  }

  // ---------------------------------------------------------------------------
  // IntIdToStringMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testIntIdToStringMap_dataBuilder(): Unit = {
    val builder = IntIdToStringMap.newBuilder
    builder.addOne(IntId(1) -> "one")
    val result = builder.result()
    assert(result.size === 1)
  }

  @Test
  def testIntIdToStringMap_dataBuilder_clear(): Unit = {
    val builder = IntIdToStringMap.newBuilder
    builder.addOne(IntId(1) -> "one")
    builder.clear()
    assert(builder.result().size === 0)
  }
}
