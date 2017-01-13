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

import com.linkedin.data.template.DataTemplateUtil
import org.coursera.courier.coercers.SingleElementCaseClassCoercer
import org.coursera.courier.generator.customtypes.BooleanId
import org.coursera.courier.generator.customtypes.BoxedIntId
import org.coursera.courier.generator.customtypes.ByteId
import org.coursera.courier.generator.customtypes.CaseClassCustomIntWrapper
import org.coursera.courier.generator.customtypes.CharId
import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.courier.generator.customtypes.DoubleId
import org.coursera.courier.generator.customtypes.FloatId
import org.coursera.courier.generator.customtypes.IntId
import org.coursera.courier.generator.customtypes.LongId
import org.coursera.courier.generator.customtypes.ShortId
import org.coursera.courier.generator.customtypes.StringId
import org.coursera.courier.generator.customtypes.StringIdWrapper
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.customtypes.IntIdArray
import org.coursera.customtypes.IntIdMap
import org.coursera.customtypes.IntIdToStringMap
import org.coursera.records.test.WithCaseClassCustomType
import org.junit.Test

class SingleElementCaseClassCoercerTest extends GeneratorTest with SchemaFixtures {

  @Test
  def testCoerceString(): Unit = {
    SingleElementCaseClassCoercer.registerCoercer(classOf[StringId], classOf[String])

    val stringId = DataTemplateUtil.coerceOutput("acme", classOf[StringId])
    assert(stringId === StringId("acme"))

    val name = DataTemplateUtil.coerceInput(StringId("CHOAM"), classOf[StringId], classOf[String])
    assert(name === "CHOAM")

  }

  @Test
  def testCoerceAnyVal(): Unit = {
    SingleElementCaseClassCoercer.registerCoercer(classOf[LongId], classOf[Long])

    val longId = DataTemplateUtil.coerceOutput(Long.box(10L), classOf[LongId])
    assert(longId === LongId(10L))

    val value = DataTemplateUtil.coerceInput(LongId(10L), classOf[LongId], classOf[Long])
    assert(value === 10L)
  }

  @Test
  def testWithCaseClassCustomType(): Unit = {
    val original =
      WithCaseClassCustomType(
        ShortId(1),
        ByteId(0x01),
        CharId('x'),
        IntId(2),
        LongId(400L),
        FloatId(3.14f),
        DoubleId(2.71f),
        StringId("CHOAM"),
        BooleanId(true),
        BoxedIntId(20),
        IntIdMap("one" -> IntId(501)),
        IntIdToStringMap(IntId(550) -> "value"),
        IntIdArray(IntId(650)),
        StringIdWrapper(StringId("wrapped")),
        CaseClassCustomIntWrapper(CustomInt(999)))
    val roundTripped =
      WithCaseClassCustomType.build(roundTrip(original.data()), DataConversion.SetReadOnly)

    assert(roundTripped.short.id === 1)
    assert(roundTripped.byte.id === 0x01)
    assert(roundTripped.char.id === 'x')
    assert(roundTripped.int.id === 2)
    assert(roundTripped.long.id === 400L)
    assert(roundTripped.float.id === 3.14f)
    assert(roundTripped.double.id === 2.71f)
    assert(roundTripped.string.id === "CHOAM")
    assert(roundTripped.boolean.id === true)
    assert(roundTripped.boxedInt.id === 20)
    assert(roundTripped.map("one").id === 501)
    assert(roundTripped.mapKeys.keySet === Set(IntId(550)))
    assert(roundTripped.array.head.id === 650)
    assert(roundTripped.chained.id.id === "wrapped")
    assert(roundTripped.chainedToCoercer.int.value === 999)

    assert(original === roundTripped)
  }
}
