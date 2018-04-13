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
import org.coursera.arrays.WithCustomArrayTestId
import org.coursera.courier.generator.customtypes.CustomArrayTestId
import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.courier.generator.customtypes.CustomIntWrapper
import org.coursera.courier.generator.customtypes.CustomMapTestKeyId
import org.coursera.courier.generator.customtypes.CustomMapTestValueId
import org.coursera.courier.generator.customtypes.CustomRecord
import org.coursera.courier.generator.customtypes.CustomRecordTestId
import org.coursera.courier.generator.customtypes.CustomUnionTestId
import org.coursera.courier.templates.DataTemplates
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.customtypes.CustomRecordArray
import org.coursera.customtypes.CustomRecordToCustomRecordMap
import org.coursera.enums.EnumProperties
import org.coursera.enums.Fruits
import org.coursera.maps.WithCustomMapTestIds
import org.coursera.records.test.Empty
import org.coursera.records.test.EmptyArray
import org.coursera.records.test.EmptyMap
import org.coursera.records.test.WithComplexTyperefs
import org.coursera.records.test.WithCustomIntWrapper
import org.coursera.records.test.WithCustomRecord
import org.coursera.records.test.WithCustomRecordTestId
import org.coursera.typerefs.UnionTyperef
import org.coursera.unions.WithCustomUnionTestId
import org.coursera.unions.WithCustomUnionTestId.Union.CustomUnionTestIdMember
import org.coursera.unions.WithRecordCustomTypeUnion
import org.junit.Test

class TyperefGeneratorTest extends GeneratorTest with SchemaFixtures {

  @Test
  def testTyperefs(): Unit = {
    // TODO: We're mainly interested in making sure the referenced type exist, but we should test
    // them further
    UnionTyperef.IntMember(1).data()
    EmptyArray(Empty()).data()
    EmptyMap("a" -> Empty()).data()
    Fruits.BANANA.toString
  }

  @Test
  def testWithComplexTyperefs(): Unit = {
    val original = WithComplexTyperefs(
      Fruits.APPLE,
      Empty(),
      EmptyMap("a" -> Empty()),
      EmptyArray(Empty()),
      UnionTyperef.IntMember(1))
    val roundTripped = WithComplexTyperefs.build(
      roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testCustomTypeOfCustomType(): Unit = {
    val original = WithCustomIntWrapper(CustomIntWrapper(CustomInt(10)))
    val roundTripped = WithCustomIntWrapper.build(
      roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testCustomTypeOfRecord(): Unit = {
    val original = WithCustomRecord(
      custom = CustomRecord("title", "body"),
      customArray = CustomRecordArray(CustomRecord("itemtitle", "itembody")),
      customMap = CustomRecordToCustomRecordMap(
        CustomRecord("keytitle", "keybody") -> CustomRecord("valuetitle", "valuebody")))

    assert(original.custom.title === "title")
    assert(original.custom.body === "body")
    assert(original.customArray.head.title === "itemtitle")
    assert(original.customArray.head.body === "itembody")
    assert(original.customMap.head._1.title === "keytitle")
    assert(original.customMap.head._1.body === "keybody")
    assert(original.customMap.head._2.title === "valuetitle")
    assert(original.customMap.head._2.body === "valuebody")

    val roundTripped = WithCustomRecord.build(
      roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testCustomTypeOfRecordDefault(): Unit = {
    val original = WithCustomRecord()
    assert(original.custom.title === "defaultTitle")
    assert(original.custom.body === "defaultBody")
  }

  @Test
  def testCustomTypeRecordInUnion(): Unit = {
    val original =
      WithRecordCustomTypeUnion(
        WithRecordCustomTypeUnion.Union.CustomRecordMember(
          CustomRecord("title", "body")))
    val roundTripped =
      WithRecordCustomTypeUnion.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testRegisterCoercerOnReadRecord(): Unit = {
    val json = """{ "id": 1 }"""
    val dataMap = DataTemplates.readDataMap(json)
    val wrapped = DataTemplateUtil.wrap(dataMap, classOf[WithCustomRecordTestId])
    assert(wrapped.id === CustomRecordTestId(1))
  }

  @Test
  def testRegisterCoercerOnReadArray(): Unit = {
    val json = """{ "array": [1, 2, 3] }"""
    val dataMap = DataTemplates.readDataMap(json)
    val wrapped = DataTemplateUtil.wrap(dataMap, classOf[WithCustomArrayTestId])
    assert(wrapped.array(0) === CustomArrayTestId(1))
  }

  @Test
  def testRegisterCoercerOnReadMap(): Unit = {
    val json = """{ "map": { "1": 100, "2": 200 } }"""
    val dataMap = DataTemplates.readDataMap(json)
    val wrapped = DataTemplateUtil.wrap(dataMap, classOf[WithCustomMapTestIds])
    assert(wrapped.map(CustomMapTestKeyId(1)) === CustomMapTestValueId(100))
    assert(wrapped.map(CustomMapTestKeyId(2)) === CustomMapTestValueId(200))
  }

  @Test
  def testRegisterCoercerOnReadUnion(): Unit = {
    val json = """{ "union": { "int": 1 } }"""
    val dataMap = DataTemplates.readDataMap(json)
    val wrapped = DataTemplateUtil.wrap(dataMap, classOf[WithCustomUnionTestId])
    assert(wrapped.union === CustomUnionTestIdMember(CustomUnionTestId(1)))
  }

  @Test
  def testMixins(): Unit = {
    val classMixin: Option[UnionTyperef] = UnionTyperef.IntMember(1).classMixinDef
    val companionMixin: Option[UnionTyperef] = UnionTyperef.companionMixinDef
  }
}
