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

import com.linkedin.data.ByteString
import org.coursera.courier.data.DataTemplates.DataConversion
import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.courier.generator.customtypes.CustomIntCoercer
import org.coursera.enums.Fruits
import org.coursera.records.test.Empty
import org.coursera.unions.WithComplexTypesUnion
import org.coursera.unions.WithComplexTypesUnion
import org.coursera.unions.WithComplexTypesUnion
import org.coursera.unions.WithPrimitiveCustomTypesUnion
import org.coursera.unions.WithPrimitivesUnion
import org.coursera.unions.WithRecordUnion
import org.junit.BeforeClass
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

object UnionGeneratorTest extends SchemaFixtures with GeneratorTest {

  @BeforeClass
  def setup(): Unit = {
    generateTestSchemas(Seq(
      Records.Empty.schema,
      Records.WithComplexTypesUnion.schema,
      Records.WithPrimitivesUnion.schema,
      Records.WithPrimitiveTyperefsUnion.schema,
      Records.WithPrimitiveCustomTypesUnion.schema))
  }
}

class UnionGeneratorTest extends GeneratorTest with SchemaFixtures with AssertionsForJUnit {

  @Test
  def testWithComplexTypesUnion(): Unit = {
    val memberLiterals = Seq(
      WithComplexTypesUnion.Union.EmptyMember(Empty()),
      WithComplexTypesUnion.Union.FruitsMember(Fruits.APPLE))

    memberLiterals.foreach { memberLiteral =>
      val original = WithComplexTypesUnion(WithComplexTypesUnion.Union.EmptyMember(Empty()))
      //mapToJson(original.data()) === ""
      val roundTripped = WithComplexTypesUnion(
        readJsonToMap(mapToJson(original.data())), DataConversion.SetReadOnly)

      val WithComplexTypesUnion(unionMember) = original
      val reconstructed = WithComplexTypesUnion(unionMember)

      assert(original === roundTripped)
      assert(original === reconstructed)
    }

    // TODO: test values
  }

  @Test
  def testWithPrimitivesUnion(): Unit = {
    val memberLiterals = Seq(
      WithPrimitivesUnion.Union.IntMember(1),
      WithPrimitivesUnion.Union.LongMember(2),
      WithPrimitivesUnion.Union.FloatMember(3),
      WithPrimitivesUnion.Union.DoubleMember(4),
      WithPrimitivesUnion.Union.StringMember("str"),
      WithPrimitivesUnion.Union.BooleanMember(true),
      WithPrimitivesUnion.Union.ByteStringMember(ByteString.copy(Array[Byte](0x1,0x2,0x3))))

    memberLiterals.foreach { memberLiteral =>

      val original = WithPrimitivesUnion(memberLiteral)
      //mapToJson(original.data()) === ""
      val roundTripped = WithPrimitivesUnion(
        readJsonToMap(mapToJson(original.data())), DataConversion.SetReadOnly)

      val WithPrimitivesUnion(unionMember) = original
      val reconstructed = WithPrimitivesUnion(unionMember)

      assert(original === roundTripped)
      assert(original === reconstructed)
    }
  }

  @Test
  def testWithPrimitiveCustomTypesUnion(): Unit = {

    // TODO(jbetz): Register coercers in Union as needed
    CustomIntCoercer.registerCoercer()

    val original = WithPrimitiveCustomTypesUnion(
        WithPrimitiveCustomTypesUnion.Union.CustomIntMember(
          CustomInt(1)))

    val roundTripped = WithPrimitiveCustomTypesUnion(
      readJsonToMap(mapToJson(original.data())), DataConversion.SetReadOnly)

    val WithPrimitiveCustomTypesUnion(unionMember) = original
    val reconstructed = WithPrimitiveCustomTypesUnion(unionMember)

    assert(original === roundTripped)
    assert(original === reconstructed)

    Seq(original, roundTripped, reconstructed).foreach { wrapper =>
      val c = wrapper.union.asInstanceOf[WithPrimitiveCustomTypesUnion.Union.CustomIntMember].value
      assert(c === CustomInt(1))
    }
  }
}
