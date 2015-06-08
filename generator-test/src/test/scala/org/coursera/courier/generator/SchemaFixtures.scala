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

case class TestSchema(
    schema: NamedDataSchema,
    resolver: DataSchemaResolver,
    location: DataSchemaLocation)

object TestSchema {
  def apply(schemaJson: String): TestSchema = {
    val schema = DataTemplateUtil.parseSchema(schemaJson).asInstanceOf[NamedDataSchema]
    val location = new StringDataSchemaLocation(schema.getFullName)

    val resolver = new DefaultDataSchemaResolver()
    resolver.bindNameToSchema(new Name(schema.getFullName), schema, location)

    TestSchema(schema, resolver, location)
  }

  val pegasusPath = new File(sys.props("project.dir") + "/src/test/pegasus")
  val fileResolver = new FileDataSchemaResolver(
    SchemaParserFactory.instance, pegasusPath.getAbsolutePath)

  def load(schemaName: String): TestSchema = {
    val why = new java.lang.StringBuilder
    val schema = fileResolver.findDataSchema(schemaName, why)
    val location = new FileDataSchemaLocation(
      new File(s"$pegasusPath/${schemaName.replace('.', '/')}.pdsc"))
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

  val bytes1 = ByteString.copy(Array[Byte](0x0, 0x1, 0x2))
  val bytes2 = ByteString.copy(Array[Byte](0x3, 0x4, 0x5))
  val bytes3 = ByteString.copy(Array[Byte](0x6, 0x7, 0x8))
}
