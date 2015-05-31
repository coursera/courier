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
import com.linkedin.data.schema.DataSchemaLocation
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
import com.linkedin.data.schema.resolver.FileDataSchemaLocation
import com.linkedin.data.schema.resolver.FileDataSchemaResolver
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.template.JacksonDataTemplateCodec
import com.linkedin.data.template.PrettyPrinterJacksonDataTemplateCodec
import com.linkedin.data.template.RecordTemplate
import com.linkedin.pegasus.generator.DataSchemaParser

case class TestSchema(schema: NamedDataSchema, resolver: DataSchemaResolver, location: DataSchemaLocation)

object TestSchema {
  def apply(schemaJson: String): TestSchema = {
    val schema = DataTemplateUtil.parseSchema(schemaJson).asInstanceOf[NamedDataSchema]
    val location = new StringDataSchemaLocation(schema.getFullName)

    val resolver = new DefaultDataSchemaResolver()
    resolver.bindNameToSchema(new Name(schema.getFullName), schema, location)

    TestSchema(schema, resolver, location)
  }

  val pegasusPath = new File(sys.props("project.dir") + "/src/test/pegasus")
  val fileResolver = new FileDataSchemaResolver(SchemaParserFactory.instance, pegasusPath.getAbsolutePath)

  def load(schemaName: String): TestSchema = {
    val why = new java.lang.StringBuilder
    val schema = fileResolver.findDataSchema(schemaName, why)
    val location = new FileDataSchemaLocation(new File(s"$pegasusPath/${schemaName.replace('.', '/')}.pdsc"))
    assert(schema != null, why)
    TestSchema(schema, fileResolver, location)
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
    val WithEmptyUnion = TestSchema.load(s"$ns.WithEmptyUnion")
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

  object Typerefs {
    val ns = "org.coursera.typerefs"
    val IntTyperef = TestSchema.load(s"$ns.IntTyperef")
    val EnumTyperef = TestSchema.load(s"$ns.EnumTyperef")
    val UnionTyperef = TestSchema.load(s"$ns.UnionTyperef")
    val ArrayTyperef = TestSchema.load(s"$ns.ArrayTyperef")
    val MapTyperef = TestSchema.load(s"$ns.MapTyperef")
    val RecordTyperef = TestSchema.load(s"$ns.RecordTyperef")
  }

  object CustomTypes {
    val ns = "org.coursera.customtypes"
    val CustomInt = TestSchema.load(s"$ns.CustomInt")
  }

  object Escaping {
    val ns = "org.coursera.escaping"
    val KeywordEscaping = TestSchema.load(s"$ns.KeywordEscaping")
    val RecordNameEscaping = TestSchema.load(s"$ns.class")
  }

  val bytes1 = ByteString.copy(Array[Byte](0x0, 0x1, 0x2))
  val bytes2 = ByteString.copy(Array[Byte](0x3, 0x4, 0x5))
  val bytes3 = ByteString.copy(Array[Byte](0x6, 0x7, 0x8))
}
