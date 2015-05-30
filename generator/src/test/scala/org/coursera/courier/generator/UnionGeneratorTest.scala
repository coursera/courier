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

import org.coursera.courier.data.DataTemplates.DataConversion
import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.courier.generator.customtypes.CustomIntCoercer
import org.coursera.enums.Fruits
import org.coursera.records.test.Empty
import org.coursera.unions.WithComplexTypesUnion
import org.coursera.unions.WithPrimitiveCustomTypesUnion
import org.coursera.unions.WithPrimitivesUnion
import org.junit.BeforeClass
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

object UnionGeneratorTest extends SchemaFixtures with GeneratorTest {

  @BeforeClass
  def setup(): Unit = {
    generateTestSchemas(Seq(
      Records.Empty,
      Unions.WithComplexTypesUnion,
      Unions.WithPrimitivesUnion,
      Unions.WithPrimitiveTyperefsUnion,
      Unions.WithPrimitiveCustomTypesUnion))
  }
}

class UnionGeneratorTest extends GeneratorTest with SchemaFixtures with AssertionsForJUnit {

  @Test
  def testWithComplexTypesUnion(): Unit = {
    val recordMember = WithComplexTypesUnion.Union.EmptyMember(Empty())
    val enumMember = WithComplexTypesUnion.Union.FruitsMember(Fruits.APPLE)
    val memberLiterals = Seq(
      recordMember,
      enumMember)

    memberLiterals.foreach { memberLiteral =>
      val original = WithComplexTypesUnion(memberLiteral)
      val roundTripped = WithComplexTypesUnion(
        roundTrip(original.data()), DataConversion.SetReadOnly)

      val WithComplexTypesUnion(unionMember) = original
      val reconstructed = WithComplexTypesUnion(unionMember)

      assert(original === roundTripped)
      assert(original === reconstructed)
    }

    assert(mapToJson(WithComplexTypesUnion(recordMember).data()) ===
      """{
        |  "union" : {
        |    "org.coursera.records.test.Empty" : { }
        |  }
        |}""".stripMargin)

    assert(mapToJson(WithComplexTypesUnion(enumMember).data()) ===
      """{
        |  "union" : {
        |    "org.coursera.enums.Fruits" : "APPLE"
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

    assert(mapToJson(WithPrimitivesUnion(intMember).data()) ===
      """{
        |  "union" : {
        |    "int" : 1
        |  }
        |}""".stripMargin)

    assert(mapToJson(WithPrimitivesUnion(longMember).data()) ===
      """{
        |  "union" : {
        |    "long" : 2
        |  }
        |}""".stripMargin)

    assert(mapToJson(WithPrimitivesUnion(floatMember).data()) ===
      """{
        |  "union" : {
        |    "float" : 3.0
        |  }
        |}""".stripMargin)

    assert(mapToJson(WithPrimitivesUnion(doubleMember).data()) ===
      """{
        |  "union" : {
        |    "double" : 4.0
        |  }
        |}""".stripMargin)

    assert(mapToJson(WithPrimitivesUnion(booleanMember).data()) ===
      """{
        |  "union" : {
        |    "boolean" : true
        |  }
        |}""".stripMargin)

    assert(mapToJson(WithPrimitivesUnion(stringMember).data()) ===
      """{
        |  "union" : {
        |    "string" : "str"
        |  }
        |}""".stripMargin)
    assert(mapToJson(WithPrimitivesUnion(bytesMember).data()) ===
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

    assert(mapToJson(original.data()) ===
      """{
        |  "union" : {
        |    "int" : 1
        |  }
        |}""".stripMargin)
  }
}
