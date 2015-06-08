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

import org.coursera.courier.templates.DataTemplates
import DataTemplates.DataConversion
import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.courier.generator.customtypes.CustomIntCoercer
import org.coursera.enums.Fruits
import org.coursera.records.test.Empty
import org.coursera.records.test.Simple
import org.coursera.records.test.SimpleArray
import org.coursera.records.test.SimpleMap
import org.coursera.unions.WithComplexTypesUnion
import org.coursera.unions.WithPrimitiveCustomTypesUnion
import org.coursera.unions.WithPrimitivesUnion
import org.junit.BeforeClass
import org.junit.Test

class UnionGeneratorTest extends GeneratorTest with SchemaFixtures {

  @Test
  def testWithComplexTypesUnion(): Unit = {
    import WithComplexTypesUnion._
    val recordMember = Union.EmptyMember(Empty())
    val enumMember = Union.FruitsMember(Fruits.APPLE)
    val mapMember = Union.SimpleMapMember(SimpleMap("a" -> Simple(Some("m1"))))
    val arrayMember = Union.SimpleArrayMember(SimpleArray(Simple(Some("a1"))))
    val memberLiterals = Seq(
      recordMember,
      enumMember,
      mapMember,
      arrayMember)

    memberLiterals.foreach { memberLiteral =>
      val original = WithComplexTypesUnion(memberLiteral)
      val roundTripped = WithComplexTypesUnion(
        roundTrip(original.data()), DataConversion.SetReadOnly)

      val WithComplexTypesUnion(unionMember) = original
      val reconstructed = WithComplexTypesUnion(unionMember)

      assert(original === roundTripped)
      assert(original === reconstructed)
    }

    assertJson(WithComplexTypesUnion(recordMember),
      """{
        |  "union" : {
        |    "org.coursera.records.test.Empty" : { }
        |  }
        |}""".stripMargin)

    assertJson(WithComplexTypesUnion(enumMember),
      """{
        |  "union" : {
        |    "org.coursera.enums.Fruits" : "APPLE"
        |  }
        |}""".stripMargin)

    assertJson(WithComplexTypesUnion(arrayMember),
      """{
        |  "union" : {
        |    "array" : [ { "message": "a1" } ]
        |  }
        |}""".stripMargin)

    assertJson(WithComplexTypesUnion(mapMember),
      """{
        |  "union" : {
        |    "map" : { "a": { "message": "m1" } }
        |  }
        |}""".stripMargin)
  }

  @Test
  def testWithPrimitivesUnion(): Unit = {
    val intMember = WithPrimitivesUnion.Union.IntMember(1)
    val longMember = WithPrimitivesUnion.Union.LongMember(2L)
    val floatMember = WithPrimitivesUnion.Union.FloatMember(3.0f)
    val doubleMember = WithPrimitivesUnion.Union.DoubleMember(4.0d)
    val stringMember = WithPrimitivesUnion.Union.StringMember("str")
    val booleanMember = WithPrimitivesUnion.Union.BooleanMember(true)
    val bytesMember = WithPrimitivesUnion.Union.ByteStringMember(bytes1)
    val memberLiterals = Seq(
      intMember,
      longMember,
      floatMember,
      doubleMember,
      stringMember,
      booleanMember,
      bytesMember)


    memberLiterals.foreach { memberLiteral =>
      val original = WithPrimitivesUnion(memberLiteral)
      val roundTripped = WithPrimitivesUnion(
        roundTrip(original.data()), DataConversion.SetReadOnly)

      val WithPrimitivesUnion(unionMember) = original
      val reconstructed = WithPrimitivesUnion(unionMember)

      assert(original === roundTripped)
      assert(original === reconstructed)
    }

    assertJson(WithPrimitivesUnion(intMember),
      """{
        |  "union" : {
        |    "int" : 1
        |  }
        |}""".stripMargin)

    assertJson(WithPrimitivesUnion(longMember),
      """{
        |  "union" : {
        |    "long" : 2
        |  }
        |}""".stripMargin)

    assertJson(WithPrimitivesUnion(floatMember),
      """{
        |  "union" : {
        |    "float" : 3.0
        |  }
        |}""".stripMargin)

    assertJson(WithPrimitivesUnion(doubleMember),
      """{
        |  "union" : {
        |    "double" : 4.0
        |  }
        |}""".stripMargin)

    assertJson(WithPrimitivesUnion(booleanMember),
      """{
        |  "union" : {
        |    "boolean" : true
        |  }
        |}""".stripMargin)

    assertJson(WithPrimitivesUnion(stringMember),
      """{
        |  "union" : {
        |    "string" : "str"
        |  }
        |}""".stripMargin)
    assertJson(WithPrimitivesUnion(bytesMember),
      s"""{
        |  "union" : {
        |    "bytes" : "${'\\'}u0000${'\\'}u0001${'\\'}u0002"
        |  }
        |}""".stripMargin)

  }

  @Test
  def testWithPrimitiveCustomTypesUnion(): Unit = {

    // TODO(jbetz): Register coercers correctly for unions
    CustomIntCoercer.registerCoercer()

    val original = WithPrimitiveCustomTypesUnion(
        WithPrimitiveCustomTypesUnion.Union.CustomIntMember(
          CustomInt(1)))

    val roundTripped = WithPrimitiveCustomTypesUnion(
      roundTrip(original.data()), DataConversion.SetReadOnly)

    val WithPrimitiveCustomTypesUnion(unionMember) = original
    val reconstructed = WithPrimitiveCustomTypesUnion(unionMember)

    assert(original === roundTripped)
    assert(original === reconstructed)

    Seq(original, roundTripped, reconstructed).foreach { wrapper =>
      val c = wrapper.union.asInstanceOf[WithPrimitiveCustomTypesUnion.Union.CustomIntMember].value
      assert(c === CustomInt(1))
    }

    assertJson(original,
      """{
        |  "union" : {
        |    "int" : 1
        |  }
        |}""".stripMargin)
  }

  @Test
  def testWithEmptyUnion(): Unit = {
    // TODO: default pegasus validation prevents adding cases to unions

    /*val unknownJson = """{
      |  "union" : {
      |    "unknown": { }
      |  }
      |}
    """.stripMargin

    val withUnknown = WithEmptyUnion(readJsonToMap(unknownJson), DataConversion.SetReadOnly)
    assert(withUnknown.union === WithEmptyUnion.Union.$UnknownMember)*/
  }
}
