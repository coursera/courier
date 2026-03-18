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

import com.linkedin.data.DataMap
import org.coursera.courier.data.ByteStringToStringMap
import org.coursera.courier.data.DoubleToStringMap
import org.coursera.courier.data.FloatToStringMap
import org.coursera.courier.data.IntArray
import org.coursera.courier.data.IntArrayToStringMap
import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.unions.WithPrimitiveTyperefsUnion
import org.junit.Test

/**
 * Tests covering DataBuilders for courier.data.*ToStringMap types
 * and the WithPrimitiveTyperefsUnion.
 */
class DataToStringMapBuilderTest extends GeneratorTest with SchemaFixtures {

  // ---------------------------------------------------------------------------
  // DoubleToStringMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testDoubleToStringMap_dataBuilder(): Unit = {
    val builder = DoubleToStringMap.newBuilder
    builder.addOne(1.0d -> "one")
    builder.addOne(2.0d -> "two")
    val result = builder.result()
    assert(result.size === 2)
    assert(result.get(1.0d) === Some("one"))
  }

  @Test
  def testDoubleToStringMap_dataBuilder_clear(): Unit = {
    val builder = DoubleToStringMap.newBuilder
    builder.addOne(1.0d -> "one")
    builder.clear()
    assert(builder.result().size === 0)
  }

  @Test
  def testDoubleToStringMap_operations(): Unit = {
    val m = DoubleToStringMap(1.0d -> "a", 2.0d -> "b")
    assert(m.get(1.0d) === Some("a"))
    assert(m.get(999.0d) === None)
    assert((m - 1.0d).size === 1)
    assert((m + (3.0d -> "c")).size === 3)
    assert(m.iterator.size === 2)
    assert(m.isEmpty === false)
  }

  @Test
  def testDoubleToStringMap_updated_supertype(): Unit = {
    val m = DoubleToStringMap(1.0d -> "a")
    // Adding a non-String value produces a plain Map
    val m2: Map[Double, Any] = m + (2.0d -> 42)
    assert(m2.size === 2)
    assert(m2(2.0d) === 42)
  }

  @Test
  def testDoubleToStringMap_build_roundTrip(): Unit = {
    val original = DoubleToStringMap(1.5d -> "val")
    val rebuilt = DoubleToStringMap.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === rebuilt)
  }

  // ---------------------------------------------------------------------------
  // FloatToStringMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testFloatToStringMap_dataBuilder(): Unit = {
    val builder = FloatToStringMap.newBuilder
    builder.addOne(1.0f -> "one")
    builder.addOne(2.0f -> "two")
    val result = builder.result()
    assert(result.size === 2)
    assert(result.get(1.0f) === Some("one"))
  }

  @Test
  def testFloatToStringMap_dataBuilder_clear(): Unit = {
    val builder = FloatToStringMap.newBuilder
    builder.addOne(1.0f -> "one")
    builder.clear()
    assert(builder.result().size === 0)
  }

  @Test
  def testFloatToStringMap_operations(): Unit = {
    val m = FloatToStringMap(1.0f -> "a", 2.0f -> "b")
    assert(m.get(1.0f) === Some("a"))
    assert((m - 1.0f).size === 1)
    assert((m + (3.0f -> "c")).size === 3)
  }

  @Test
  def testFloatToStringMap_updated_supertype(): Unit = {
    val m = FloatToStringMap(1.0f -> "a")
    val m2: Map[Float, Any] = m + (2.0f -> 99)
    assert(m2(2.0f) === 99)
  }

  @Test
  def testFloatToStringMap_build_roundTrip(): Unit = {
    val original = FloatToStringMap(1.5f -> "val")
    val rebuilt = FloatToStringMap.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === rebuilt)
  }

  // ---------------------------------------------------------------------------
  // ByteStringToStringMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testByteStringToStringMap_dataBuilder(): Unit = {
    val builder = ByteStringToStringMap.newBuilder
    builder.addOne(bytes1 -> "b1")
    builder.addOne(bytes2 -> "b2")
    val result = builder.result()
    assert(result.size === 2)
    assert(result.get(bytes1) === Some("b1"))
  }

  @Test
  def testByteStringToStringMap_dataBuilder_clear(): Unit = {
    val builder = ByteStringToStringMap.newBuilder
    builder.addOne(bytes1 -> "b1")
    builder.clear()
    assert(builder.result().size === 0)
  }

  @Test
  def testByteStringToStringMap_operations(): Unit = {
    val m = ByteStringToStringMap(bytes1 -> "a", bytes2 -> "b")
    assert(m.get(bytes1) === Some("a"))
    assert((m - bytes1).size === 1)
    assert((m + (bytes3 -> "c")).size === 3)
  }

  @Test
  def testByteStringToStringMap_updated_supertype(): Unit = {
    val m = ByteStringToStringMap(bytes1 -> "a")
    val m2: Map[com.linkedin.data.ByteString, Any] = m + (bytes2 -> 42)
    assert(m2.size === 2)
  }

  @Test
  def testByteStringToStringMap_build_roundTrip(): Unit = {
    val original = ByteStringToStringMap(bytes1 -> "val")
    val rebuilt =
      ByteStringToStringMap.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === rebuilt)
  }

  // ---------------------------------------------------------------------------
  // IntArrayToStringMap DataBuilder
  // ---------------------------------------------------------------------------

  @Test
  def testIntArrayToStringMap_dataBuilder(): Unit = {
    val builder = IntArrayToStringMap.newBuilder
    builder.addOne(IntArray(1, 2) -> "arr1")
    builder.addOne(IntArray(3, 4) -> "arr2")
    val result = builder.result()
    assert(result.size === 2)
  }

  @Test
  def testIntArrayToStringMap_dataBuilder_clear(): Unit = {
    val builder = IntArrayToStringMap.newBuilder
    builder.addOne(IntArray(1) -> "arr")
    builder.clear()
    assert(builder.result().size === 0)
  }

  @Test
  def testIntArrayToStringMap_operations(): Unit = {
    val arr1 = IntArray(1, 2)
    val arr2 = IntArray(3, 4)
    val m = IntArrayToStringMap(arr1 -> "a", arr2 -> "b")
    assert(m.get(arr1) === Some("a"))
    assert((m - arr1).size === 1)
    assert((m + (IntArray(5, 6) -> "c")).size === 3)
  }

  @Test
  def testIntArrayToStringMap_updated_supertype(): Unit = {
    val arr = IntArray(1)
    val m = IntArrayToStringMap(arr -> "a")
    val m2: Map[org.coursera.courier.data.IntArray, Any] = m + (arr -> 42)
    assert(m2.size === 1)
  }

  @Test
  def testIntArrayToStringMap_build_roundTrip(): Unit = {
    val original = IntArrayToStringMap(IntArray(1, 2) -> "val")
    val rebuilt =
      IntArrayToStringMap.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === rebuilt)
  }

  // ---------------------------------------------------------------------------
  // WithPrimitiveTyperefsUnion
  // ---------------------------------------------------------------------------

  @Test
  def testWithPrimitiveTyperefsUnion_customIntMember(): Unit = {
    val original = WithPrimitiveTyperefsUnion(
      WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(42)))
    val roundTripped = WithPrimitiveTyperefsUnion.build(
      roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)

    val member = original.union.asInstanceOf[WithPrimitiveTyperefsUnion.Union.CustomIntMember]
    assert(member.value === CustomInt(42))
  }

  @Test
  def testWithPrimitiveTyperefsUnion_build_unknown(): Unit = {
    val innerMap = new DataMap()
    innerMap.put("unknownKey", "val")
    innerMap.makeReadOnly()
    val built = WithPrimitiveTyperefsUnion.Union.build(innerMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[WithPrimitiveTyperefsUnion.Union.$UnknownMember])
  }

  @Test
  def testWithPrimitiveTyperefsUnion_equality(): Unit = {
    val a = WithPrimitiveTyperefsUnion(
      WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(1)))
    val b = WithPrimitiveTyperefsUnion(
      WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(1)))
    val c = WithPrimitiveTyperefsUnion(
      WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(2)))
    assert(a === b)
    assert(a !== c)
  }

  @Test
  def testWithPrimitiveTyperefsUnion_unapply(): Unit = {
    val r = WithPrimitiveTyperefsUnion(
      WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(10)))
    val WithPrimitiveTyperefsUnion(union) = r
    assert(union.isInstanceOf[WithPrimitiveTyperefsUnion.Union.CustomIntMember])
  }

  @Test
  def testWithPrimitiveTyperefsUnion_copy(): Unit = {
    val original = WithPrimitiveTyperefsUnion(
      WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(1)))
    val copied = original.copy(union =
      WithPrimitiveTyperefsUnion.Union.CustomIntMember(CustomInt(99)))
    val member = copied.union.asInstanceOf[WithPrimitiveTyperefsUnion.Union.CustomIntMember]
    assert(member.value === CustomInt(99))
  }
}
