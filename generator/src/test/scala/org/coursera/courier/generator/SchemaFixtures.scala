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

import java.io.File

import com.linkedin.data.ByteString
import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.schema.BooleanDataSchema
import com.linkedin.data.schema.BytesDataSchema
import com.linkedin.data.schema.DataSchemaResolver
import com.linkedin.data.schema.DoubleDataSchema
import com.linkedin.data.schema.FloatDataSchema
import com.linkedin.data.schema.IntegerDataSchema
import com.linkedin.data.schema.LongDataSchema
import com.linkedin.data.schema.Name
import com.linkedin.data.schema.NamedDataSchema
import com.linkedin.data.schema.SchemaParserFactory
import com.linkedin.data.schema.StringDataSchema
import com.linkedin.data.schema.StringDataSchemaLocation
import com.linkedin.data.schema.resolver.DefaultDataSchemaResolver
import com.linkedin.data.schema.resolver.FileDataSchemaResolver
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.template.JacksonDataTemplateCodec
import com.linkedin.data.template.PrettyPrinterJacksonDataTemplateCodec

case class TestSchema(schema: NamedDataSchema, resolver: DataSchemaResolver)

object TestSchema {
  def apply(schemaJson: String): TestSchema = {
    val schema = DataTemplateUtil.parseSchema(schemaJson).asInstanceOf[NamedDataSchema]
    val location = new StringDataSchemaLocation(schema.getFullName)

    val resolver = new DefaultDataSchemaResolver()
    resolver.bindNameToSchema(new Name(schema.getFullName), schema, location)

    TestSchema(schema, resolver)
  }

  val pegasusPath = new File(sys.props("project.dir") + "/src/test/pegasus")
  val fileResolver = new FileDataSchemaResolver(SchemaParserFactory.instance, pegasusPath.getAbsolutePath)

  def load(schemaName: String): TestSchema = {
    val why = new java.lang.StringBuilder
    val schema = fileResolver.findDataSchema(schemaName, why)
    assert(schema != null, why)
    TestSchema(schema, fileResolver)
  }
}

trait SchemaFixtures {
  object Primitives {
    val int = new IntegerDataSchema
    val long = new LongDataSchema
    val float = new FloatDataSchema
    val double = new DoubleDataSchema
    val boolean = new BooleanDataSchema
    val string = new StringDataSchema
    val bytes = new BytesDataSchema
  }

  object Records {
    val ns = "org.coursera.records.test"
    val WithPrimitives = TestSchema.load(s"$ns.WithPrimitives")
    val WithOptionalPrimitives = TestSchema.load(s"$ns.WithOptionalPrimitives")
    val WithPrimitiveTyperefs = TestSchema.load(s"$ns.WithPrimitiveTyperefs")
    val WithOptionalPrimitiveTyperefs = TestSchema.load(s"$ns.WithOptionalPrimitiveTyperefs")
    val WithPrimitiveCustomTypes = TestSchema.load(s"$ns.WithPrimitiveCustomTypes")
    val WithOptionalPrimitiveCustomTypes = TestSchema.load(s"$ns.WithOptionalPrimitiveCustomTypes")
    val WithInlineRecord = TestSchema.load(s"$ns.WithInlineRecord")
    val WithComplexTypes = TestSchema.load(s"$ns.WithComplexTypes")
    val Empty = TestSchema.load(s"$ns.Empty")
  }

  object Enums {
    val Fruits = TestSchema.load("org.coursera.enums.Fruits")
  }

  object Unions {
    val ns = "org.coursera.unions"
    val WithComplexTypesUnion = TestSchema.load(s"$ns.WithComplexTypesUnion")
    val WithPrimitivesUnion = TestSchema.load(s"$ns.WithPrimitivesUnion")
    val WithPrimitiveTyperefsUnion = TestSchema.load(s"$ns.WithPrimitiveTyperefsUnion")
    val WithPrimitiveCustomTypesUnion = TestSchema.load(s"$ns.WithPrimitiveCustomTypesUnion")
  }

  object Arrays {
    val ns = "org.coursera.arrays"
    val WithRecordArray = TestSchema.load(s"$ns.WithRecordArray")
    val WithPrimitivesArray = TestSchema.load(s"$ns.WithPrimitivesArray")
    val WithCustomTypesArray = TestSchema.load(s"$ns.WithCustomTypesArray")
  }

  object Maps {
    val ns = "org.coursera.maps"
    val WithComplexTypesMap = TestSchema.load(s"$ns.WithComplexTypesMap")
    val WithPrimitivesMap = TestSchema.load(s"$ns.WithPrimitivesMap")
    val WithCustomTypesMap = TestSchema.load(s"$ns.WithCustomTypesMap")
  }

  object CustomTypes {
    val ns = "org.coursera.customtypes"
    val CustomInt = TestSchema.load(s"$ns.CustomInt")
  }

  val bytes1 = ByteString.copy(Array[Byte](0x0, 0x1, 0x2))
  val bytes2 = ByteString.copy(Array[Byte](0x3, 0x4, 0x5))
  val bytes3 = ByteString.copy(Array[Byte](0x6, 0x7, 0x8))

  val prettyPrinter = new PrettyPrinterJacksonDataTemplateCodec

  def printJson(dataTemplate: DataTemplate[DataMap]): Unit = printJson(dataTemplate.data)

  def printJson(dataMap: DataMap): Unit = println(mapToJson(dataMap))

  def mapToJson(dataTemplate: DataTemplate[DataMap]): String = mapToJson(dataTemplate.data)

  def listToJson(dataTemplate: DataTemplate[DataList]): String = listToJson(dataTemplate.data)

  def mapToJson(dataMap: DataMap): String = prettyPrinter.mapToString(dataMap)

  def listToJson(dataList: DataList): String = prettyPrinter.listToString(dataList)

  val codec = new JacksonDataTemplateCodec

  def readJsonToMap(string: String): DataMap = codec.stringToMap(string)

  def readJsonToList(string: String): DataList = codec.stringToList(string)

  def roundTrip(complex: DataMap): DataMap = {
    readJsonToMap(mapToJson(complex))
  }

  def roundTrip(complex: DataList): DataList = {
    readJsonToList(listToJson(complex))
  }
}
