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

import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import org.coursera.arrays.WithAnonymousUnionArray
import org.coursera.arrays.WithAnonymousUnionArray.UnionsArray
import org.coursera.arrays.WithAnonymousUnionArray.UnionsArrayArray
import org.coursera.arrays.WithAnonymousUnionArray.UnionsMap
import org.coursera.arrays.WithAnonymousUnionArray.UnionsMapMap
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.junit.Test

/**
 * Tests for the WithAnonymousUnionArray generated record, which contains:
 *   - unionsArray: array[union[int, string]]  (UnionsArrayArray of UnionsArray)
 *   - unionsMap:   map[string, union[string, int]] (UnionsMapMap of UnionsMap)
 *
 * These classes had 0% scoverage before this test file was added.
 */
class AnonymousUnionGeneratorTest extends GeneratorTest with SchemaFixtures {

  // ---------------------------------------------------------------------------
  // UnionsArray — the inline union for array items
  // ---------------------------------------------------------------------------

  @Test
  def testUnionsArray_intMember_roundTrip(): Unit = {
    val member = UnionsArray.IntMember(42)
    assert(member.value === 42)

    val UnionsArray.IntMember(v) = member
    assert(v === 42)
  }

  @Test
  def testUnionsArray_stringMember_roundTrip(): Unit = {
    val member = UnionsArray.StringMember("hello")
    assert(member.value === "hello")

    val UnionsArray.StringMember(v) = member
    assert(v === "hello")
  }

  @Test
  def testUnionsArray_build_intMember(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("int", Integer.valueOf(7))
    dataMap.makeReadOnly()
    val built = UnionsArray.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[UnionsArray.IntMember])
    assert(built.asInstanceOf[UnionsArray.IntMember].value === 7)
  }

  @Test
  def testUnionsArray_build_stringMember(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("string", "world")
    dataMap.makeReadOnly()
    val built = UnionsArray.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[UnionsArray.StringMember])
    assert(built.asInstanceOf[UnionsArray.StringMember].value === "world")
  }

  @Test
  def testUnionsArray_build_unknownMember(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("float", java.lang.Float.valueOf(1.5f))
    dataMap.makeReadOnly()
    val built = UnionsArray.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[UnionsArray.$UnknownMember])
  }

  @Test
  def testUnionsArray_equality(): Unit = {
    val a = UnionsArray.IntMember(1)
    val b = UnionsArray.IntMember(1)
    val c = UnionsArray.IntMember(2)
    assert(a === b)
    assert(a !== c)
  }

  @Test
  def testUnionsArray_toString(): Unit = {
    val m = UnionsArray.IntMember(5)
    // Just verify it doesn't throw and returns a non-empty string
    assert(m.toString.nonEmpty)
  }

  @Test
  def testUnionsArray_hashCode(): Unit = {
    val a = UnionsArray.IntMember(99)
    val b = UnionsArray.IntMember(99)
    assert(a.hashCode === b.hashCode)
  }

  // ---------------------------------------------------------------------------
  // UnionsArrayArray — array of UnionsArray
  // ---------------------------------------------------------------------------

  @Test
  def testUnionsArrayArray_apply_varargs(): Unit = {
    val arr = UnionsArrayArray(UnionsArray.IntMember(1), UnionsArray.StringMember("s"))
    assert(arr.length === 2)
    assert(arr(0).asInstanceOf[UnionsArray.IntMember].value === 1)
    assert(arr(1).asInstanceOf[UnionsArray.StringMember].value === "s")
  }

  @Test
  def testUnionsArrayArray_apply_iterable(): Unit = {
    val items = List(UnionsArray.IntMember(10), UnionsArray.StringMember("x"))
    val arr: UnionsArrayArray = UnionsArrayArray(items)
    assert(arr.length === 2)
    assert(arr(0).asInstanceOf[UnionsArray.IntMember].value === 10)
  }

  @Test
  def testUnionsArrayArray_empty(): Unit = {
    val arr = UnionsArrayArray.empty
    assert(arr.length === 0)
  }

  @Test
  def testUnionsArrayArray_dataBuilder(): Unit = {
    val builder = UnionsArrayArray.newBuilder
    builder += (UnionsArray.IntMember(3))
    builder += (UnionsArray.StringMember("t"))
    val result = builder.result()
    assert(result.length === 2)
    assert(result(0).asInstanceOf[UnionsArray.IntMember].value === 3)
  }

  @Test
  def testUnionsArrayArray_dataBuilder_clear(): Unit = {
    val builder = UnionsArrayArray.newBuilder
    builder += (UnionsArray.IntMember(1))
    builder.clear()
    val result = builder.result()
    assert(result.length === 0)
  }

  @Test
  def testUnionsArrayArray_build_roundTrip(): Unit = {
    val original = UnionsArrayArray(UnionsArray.IntMember(5), UnionsArray.StringMember("y"))
    val rebuilt = UnionsArrayArray.build(
      roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === rebuilt)
  }

  @Test
  def testUnionsArrayArray_copy(): Unit = {
    val arr = UnionsArrayArray(UnionsArray.IntMember(7))
    val copied = arr.copy(arr.data().copy(), DataConversion.SetReadOnly)
    assert(arr === copied)
  }

  @Test
  def testUnionsArrayArray_wrapImplicit(): Unit = {
    // The implicit wrap for non-array/map items
    val items: Iterable[UnionsArray] = List(UnionsArray.IntMember(1))
    val arr: UnionsArrayArray = items
    assert(arr.length === 1)
  }

  // ---------------------------------------------------------------------------
  // UnionsMap — the inline union for map values
  // ---------------------------------------------------------------------------

  @Test
  def testUnionsMap_stringMember_roundTrip(): Unit = {
    val member = UnionsMap.StringMember("value")
    assert(member.value === "value")

    val UnionsMap.StringMember(v) = member
    assert(v === "value")
  }

  @Test
  def testUnionsMap_intMember_roundTrip(): Unit = {
    val member = UnionsMap.IntMember(100)
    assert(member.value === 100)
  }

  @Test
  def testUnionsMap_build_stringMember(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("string", "test")
    dataMap.makeReadOnly()
    val built = UnionsMap.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[UnionsMap.StringMember])
    assert(built.asInstanceOf[UnionsMap.StringMember].value === "test")
  }

  @Test
  def testUnionsMap_build_intMember(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("int", Integer.valueOf(77))
    dataMap.makeReadOnly()
    val built = UnionsMap.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[UnionsMap.IntMember])
    assert(built.asInstanceOf[UnionsMap.IntMember].value === 77)
  }

  @Test
  def testUnionsMap_build_unknownMember(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("double", java.lang.Double.valueOf(3.14))
    dataMap.makeReadOnly()
    val built = UnionsMap.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[UnionsMap.$UnknownMember])
  }

  // ---------------------------------------------------------------------------
  // UnionsMapMap — map[string, UnionsMap]
  // ---------------------------------------------------------------------------

  @Test
  def testUnionsMapMap_apply_pairs(): Unit = {
    val m = UnionsMapMap("a" -> UnionsMap.StringMember("hello"), "b" -> UnionsMap.IntMember(1))
    assert(m.size === 2)
    assert(m.get("a").map(_.asInstanceOf[UnionsMap.StringMember].value) === Some("hello"))
    assert(m.get("b").map(_.asInstanceOf[UnionsMap.IntMember].value) === Some(1))
  }

  @Test
  def testUnionsMapMap_get_missing(): Unit = {
    val m = UnionsMapMap("k" -> UnionsMap.StringMember("v"))
    assert(m.get("nope") === None)
  }

  @Test
  def testUnionsMapMap_iterator(): Unit = {
    val m = UnionsMapMap("x" -> UnionsMap.IntMember(42))
    val pairs = m.iterator.toSeq
    assert(pairs.size === 1)
    assert(pairs.head._1 === "x")
    assert(pairs.head._2.asInstanceOf[UnionsMap.IntMember].value === 42)
  }

  @Test
  def testUnionsMapMap_removed(): Unit = {
    val m = UnionsMapMap("a" -> UnionsMap.IntMember(1), "b" -> UnionsMap.IntMember(2))
    val afterRemove = m - "a"
    assert(afterRemove.size === 1)
    assert(afterRemove.get("a") === None)
    assert(afterRemove.get("b").isDefined)
  }

  @Test
  def testUnionsMapMap_updated_sameType(): Unit = {
    val m = UnionsMapMap("a" -> UnionsMap.IntMember(1))
    // updated with same value type → stays UnionsMapMap
    val updated = m.updated("b", UnionsMap.StringMember("new"))
    assert(updated.size === 2)
  }

  @Test
  def testUnionsMapMap_plus_sameType(): Unit = {
    val m = UnionsMapMap("a" -> UnionsMap.IntMember(1))
    val m2 = m + ("b" -> UnionsMap.IntMember(2))
    assert(m2.size === 2)
  }

  @Test
  def testUnionsMapMap_plus_supertype(): Unit = {
    // Adding a value of a supertype (not UnionsMap) forces fallback to plain Map
    val m = UnionsMapMap("a" -> UnionsMap.IntMember(1))
    val m2: Map[String, Any] = m + ("b" -> "notAUnionsMap")
    assert(m2.size === 2)
    assert(m2("b") === "notAUnionsMap")
  }

  @Test
  def testUnionsMapMap_empty(): Unit = {
    val m = UnionsMapMap.empty
    assert(m.size === 0)
    assert(m.isEmpty)
  }

  @Test
  def testUnionsMapMap_dataBuilder(): Unit = {
    val builder = UnionsMapMap.newBuilder
    builder += ("k1" -> UnionsMap.IntMember(10))
    builder += ("k2" -> UnionsMap.StringMember("v"))
    val result = builder.result()
    assert(result.size === 2)
  }

  @Test
  def testUnionsMapMap_dataBuilder_clear(): Unit = {
    val builder = UnionsMapMap.newBuilder
    builder += ("k" -> UnionsMap.IntMember(1))
    builder.clear()
    val result = builder.result()
    assert(result.size === 0)
  }

  @Test
  def testUnionsMapMap_build_roundTrip(): Unit = {
    val original = UnionsMapMap("k" -> UnionsMap.StringMember("round"))
    val rebuilt = UnionsMapMap.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === rebuilt)
  }

  @Test
  def testUnionsMapMap_wrapImplicit(): Unit = {
    val plain: Map[String, UnionsMap] = Map("k" -> UnionsMap.IntMember(5))
    val m: UnionsMapMap = plain
    assert(m.size === 1)
  }

  // ---------------------------------------------------------------------------
  // WithAnonymousUnionArray record — full round-trip
  // ---------------------------------------------------------------------------

  @Test
  def testWithAnonymousUnionArray_roundTrip(): Unit = {
    val unionsArray = UnionsArrayArray(
      UnionsArray.IntMember(1),
      UnionsArray.StringMember("s1"),
      UnionsArray.IntMember(2))
    val unionsMap = UnionsMapMap(
      "int_val"    -> UnionsMap.IntMember(99),
      "str_val"    -> UnionsMap.StringMember("hello"))

    val original = WithAnonymousUnionArray(unionsArray, unionsMap)
    val rebuilt = WithAnonymousUnionArray.build(
      roundTrip(original.data()), DataConversion.SetReadOnly)

    assert(original === rebuilt)
  }

  @Test
  def testWithAnonymousUnionArray_fieldAccess(): Unit = {
    val arr = UnionsArrayArray(UnionsArray.IntMember(10))
    val map = UnionsMapMap("k" -> UnionsMap.StringMember("v"))
    val record = WithAnonymousUnionArray(arr, map)

    assert(record.unionsArray.length === 1)
    assert(record.unionsArray(0).asInstanceOf[UnionsArray.IntMember].value === 10)
    assert(record.unionsMap.get("k").map(_.asInstanceOf[UnionsMap.StringMember].value) === Some("v"))
  }

  @Test
  def testWithAnonymousUnionArray_copy(): Unit = {
    val arr  = UnionsArrayArray(UnionsArray.IntMember(1))
    val map  = UnionsMapMap("a" -> UnionsMap.IntMember(2))
    val orig = WithAnonymousUnionArray(arr, map)

    val newArr = UnionsArrayArray(UnionsArray.StringMember("copied"))
    val copied = orig.copy(unionsArray = newArr)
    assert(copied.unionsArray(0).asInstanceOf[UnionsArray.StringMember].value === "copied")
    assert(copied.unionsMap === orig.unionsMap)
  }

  @Test
  def testWithAnonymousUnionArray_equality(): Unit = {
    val arr = UnionsArrayArray(UnionsArray.IntMember(1))
    val map = UnionsMapMap("k" -> UnionsMap.StringMember("v"))
    val a = WithAnonymousUnionArray(arr, map)
    val b = WithAnonymousUnionArray(arr, map)
    assert(a === b)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testWithAnonymousUnionArray_toString(): Unit = {
    val arr = UnionsArrayArray(UnionsArray.IntMember(1))
    val map = UnionsMapMap()
    val r = WithAnonymousUnionArray(arr, map)
    assert(r.toString.nonEmpty)
  }

  @Test
  def testWithAnonymousUnionArray_productArity(): Unit = {
    val arr = UnionsArrayArray(UnionsArray.IntMember(1))
    val map = UnionsMapMap()
    val r = WithAnonymousUnionArray(arr, map)
    assert(r.productArity === 2)
    assert(r.productElement(0) === arr)
    assert(r.productElement(1) === map)
  }

  // ---------------------------------------------------------------------------
  // WithAnonymousUnionArray.unapply — exercise all branches incl. exception paths
  // ---------------------------------------------------------------------------

  @Test
  def testWithAnonymousUnionArray_unapply_success(): Unit = {
    val arr = UnionsArrayArray(UnionsArray.IntMember(1))
    val map = UnionsMapMap("k" -> UnionsMap.StringMember("v"))
    val r = WithAnonymousUnionArray(arr, map)
    val result = WithAnonymousUnionArray.unapply(r)
    assert(result.isDefined)
    val (a, m) = result.get
    assert(a === arr)
    assert(m === map)
  }

  @Test
  def testWithAnonymousUnionArray_companionMixinDef(): Unit = {
    assert(WithAnonymousUnionArray.companionMixinDef === None)
  }

  @Test
  def testWithAnonymousUnionArray_classMixinDef(): Unit = {
    val arr = UnionsArrayArray(UnionsArray.IntMember(1))
    val map = UnionsMapMap()
    val r = WithAnonymousUnionArray(arr, map)
    assert(r.classMixinDef === None)
  }

  // ---------------------------------------------------------------------------
  // UnionsMap — canEqual, equals, toString, hashCode, classMixinDef, implicits
  // ---------------------------------------------------------------------------

  @Test
  def testUnionsMap_canEqual(): Unit = {
    val m = UnionsMap.IntMember(1)
    assert(m.canEqual(m))
    assert(!m.canEqual("notAMember"))
  }

  @Test
  def testUnionsMap_equals_differentProductArity(): Unit = {
    val m = UnionsMap.IntMember(1)
    // A product with different arity should not be equal
    val tuple = Tuple2(1, 2)
    assert(!(m == tuple))
  }

  @Test
  def testUnionsMap_toString_hashCode(): Unit = {
    val s = UnionsMap.StringMember("test")
    assert(s.toString.nonEmpty)
    val i = UnionsMap.IntMember(42)
    assert(i.toString.nonEmpty)
    assert(i.hashCode === i.hashCode)
  }

  @Test
  def testUnionsMap_classMixinDef(): Unit = {
    val m = UnionsMap.IntMember(1)
    assert(m.classMixinDef === None)
  }

  @Test
  def testUnionsMap_companionMixinDef(): Unit = {
    assert(UnionsMap.companionMixinDef === None)
  }

  @Test
  def testUnionsMap_wrap_implicit_string(): Unit = {
    // Exercises the implicit wrap(value: String): StringMember
    val member: UnionsMap.StringMember = UnionsMap.wrap("wrapped")
    assert(member.value === "wrapped")
  }

  @Test
  def testUnionsMap_wrap_implicit_int(): Unit = {
    // Exercises the implicit wrap(value: Int): IntMember
    val member: UnionsMap.IntMember = UnionsMap.wrap(99)
    assert(member.value === 99)
  }

  @Test
  def testUnionsMap_StringMember_declaringTyperefSchema(): Unit = {
    val m = UnionsMap.StringMember("x")
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testUnionsMap_StringMember_productElement_1(): Unit = {
    val m = UnionsMap.StringMember("hello")
    assert(m._1 === "hello")
  }

  @Test
  def testUnionsMap_IntMember_declaringTyperefSchema(): Unit = {
    val m = UnionsMap.IntMember(5)
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testUnionsMap_IntMember_unapply(): Unit = {
    val m = UnionsMap.IntMember(77)
    val UnionsMap.IntMember(v) = m
    assert(v === 77)
  }

  @Test
  def testUnionsMap_IntMember_unionCompanion(): Unit = {
    assert(UnionsMap.IntMember.unionCompanion eq UnionsMap)
  }

  @Test
  def testUnionsMap_UnknownMember_declaringTyperefSchema(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("bogus", java.lang.Double.valueOf(1.1))
    dataMap.makeReadOnly()
    val unknown = UnionsMap.build(dataMap, DataConversion.SetReadOnly)
    assert(unknown.isInstanceOf[UnionsMap.$UnknownMember])
    assert(unknown.asInstanceOf[UnionsMap.$UnknownMember].declaringTyperefSchema === None)
  }

  // ---------------------------------------------------------------------------
  // UnionsArray — canEqual, equals, toString, hashCode, classMixinDef, implicits
  // ---------------------------------------------------------------------------

  @Test
  def testUnionsArray_canEqual(): Unit = {
    val m = UnionsArray.IntMember(1)
    assert(m.canEqual(m))
    assert(!m.canEqual("other"))
  }

  @Test
  def testUnionsArray_equals_differentProductArity(): Unit = {
    val m = UnionsArray.StringMember("a")
    assert(!(m == Tuple2("a", "b")))
  }

  @Test
  def testUnionsArray_toString_hashCode_coverage(): Unit = {
    val s = UnionsArray.StringMember("s")
    assert(s.toString.nonEmpty)
    assert(s.hashCode === s.hashCode)
    val i = UnionsArray.IntMember(3)
    assert(i.toString.nonEmpty)
  }

  @Test
  def testUnionsArray_classMixinDef(): Unit = {
    val m = UnionsArray.IntMember(1)
    assert(m.classMixinDef === None)
  }

  @Test
  def testUnionsArray_companionMixinDef(): Unit = {
    assert(UnionsArray.companionMixinDef === None)
  }

  @Test
  def testUnionsArray_wrap_implicit_int(): Unit = {
    val member: UnionsArray.IntMember = UnionsArray.wrap(55)
    assert(member.value === 55)
  }

  @Test
  def testUnionsArray_wrap_implicit_string(): Unit = {
    val member: UnionsArray.StringMember = UnionsArray.wrap("wrapped")
    assert(member.value === "wrapped")
  }

  @Test
  def testUnionsArray_IntMember_declaringTyperefSchema(): Unit = {
    val m = UnionsArray.IntMember(10)
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testUnionsArray_IntMember_unionCompanion(): Unit = {
    assert(UnionsArray.IntMember.unionCompanion eq UnionsArray)
  }

  @Test
  def testUnionsArray_StringMember_declaringTyperefSchema(): Unit = {
    val m = UnionsArray.StringMember("y")
    assert(m.declaringTyperefSchema === None)
  }

  @Test
  def testUnionsArray_StringMember_productElement_1(): Unit = {
    val m = UnionsArray.StringMember("hello")
    assert(m._1 === "hello")
  }

  @Test
  def testUnionsArray_UnknownMember_declaringTyperefSchema(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("float", java.lang.Float.valueOf(2.5f))
    dataMap.makeReadOnly()
    val unknown = UnionsArray.build(dataMap, DataConversion.SetReadOnly)
    assert(unknown.isInstanceOf[UnionsArray.$UnknownMember])
    assert(unknown.asInstanceOf[UnionsArray.$UnknownMember].declaringTyperefSchema === None)
  }

  // ---------------------------------------------------------------------------
  // UnionsArrayArray — productElement, productArity, companionMixinDef
  // ---------------------------------------------------------------------------

  @Test
  def testUnionsArrayArray_productElement_and_productArity(): Unit = {
    val arr = UnionsArrayArray(UnionsArray.IntMember(7), UnionsArray.StringMember("z"))
    assert(arr.productArity === 2)
    // productElement returns the raw DataMap from the DataList
    assert(arr.productElement(0) != null)
    assert(arr.productElement(1) != null)
  }

  @Test
  def testUnionsArrayArray_companionMixinDef(): Unit = {
    assert(UnionsArrayArray.companionMixinDef === None)
  }

  // ---------------------------------------------------------------------------
  // UnionsMapMap — copy(), clone(), companionMixinDef
  // ---------------------------------------------------------------------------

  @Test
  def testUnionsMapMap_copy_and_clone(): Unit = {
    val m = UnionsMapMap("a" -> UnionsMap.IntMember(1))
    val copied = m.copy()
    assert(copied eq m)  // copy() returns this
    val cloned = m.clone()
    assert(cloned eq m)  // clone() returns this
  }

  @Test
  def testUnionsMapMap_classMixinDef(): Unit = {
    val m = UnionsMapMap("a" -> UnionsMap.IntMember(1))
    assert(m.classMixinDef === None)
  }

  @Test
  def testUnionsMapMap_companionMixinDef(): Unit = {
    assert(UnionsMapMap.companionMixinDef === None)
  }
}
