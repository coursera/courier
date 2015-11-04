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

package org.coursera.courier.grammar

import java.io.File

import com.linkedin.data.DataMap
import com.linkedin.data.codec.JacksonDataCodec
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.DataSchemaLocation
import com.linkedin.data.schema.DataSchemaResolver
import com.linkedin.data.schema.JsonBuilder
import com.linkedin.data.schema.Name
import com.linkedin.data.schema.NamedDataSchema
import com.linkedin.data.schema.SchemaParserFactory
import com.linkedin.data.schema.SchemaToJsonEncoder
import com.linkedin.data.schema.StringDataSchemaLocation
import com.linkedin.data.schema.resolver.DefaultDataSchemaResolver
import com.linkedin.data.schema.resolver.FileDataSchemaLocation
import com.linkedin.data.schema.resolver.FileDataSchemaResolver
import com.linkedin.data.template.DataTemplateUtil
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitSuite

class ConversionTest extends JUnitSuite with AssertionsForJUnit {
  @Test
  def courierToPdsc(): Unit = {
    Seq(
      "org.example.Fortune",
      "org.example.FortuneCookie",
      "org.example.MagicEightBall",
      "org.example.MagicEightBallAnswer",
      "org.example.TyperefExample",
      "org.example.common.DateTime",
      "org.example.common.Timestamp").foreach { schemaName =>
      assertSame(
        TestSchema.loadPegasus(schemaName).schema,
        TestSchema.loadCourier(schemaName).schema)
    }
  }

  private val dataCodec = new JacksonDataCodec
  def readJsonToMap(string: String): DataMap = dataCodec.stringToMap(string)

  def assertSame(lhs: DataSchema, rhs: DataSchema): Unit = {
    assert(readJsonToMap(toJson(lhs)) === readJsonToMap(toJson(rhs)))
  }

  def toJson(schema: DataSchema) = {
    SchemaToJsonEncoder.schemaToJson(schema, JsonBuilder.Pretty.INDENTED)
  }
}

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
  val pegasusFileResolver = new FileDataSchemaResolver(
    SchemaParserFactory.instance, pegasusPath.getAbsolutePath)

  def loadPegasus(schemaName: String): TestSchema = {
    val why = new java.lang.StringBuilder
    val schema = pegasusFileResolver.findDataSchema(schemaName, why)
    val location = new FileDataSchemaLocation(
      new File(s"$pegasusPath/${schemaName.replace('.', '/')}.pdsc"))
    assert(schema != null, why)
    TestSchema(schema, pegasusFileResolver, location)
  }

  val courierPath = new File(sys.props("project.dir") + "/src/test/courier")
  val courierFileResolver = new FileDataSchemaResolver(
    new CourierSchemaParserFactory, courierPath.getAbsolutePath)
  courierFileResolver.setExtension(".courier")

  def loadCourier(schemaName: String): TestSchema = {
    val why = new java.lang.StringBuilder
    val schema = courierFileResolver.findDataSchema(schemaName, why)
    val location = new FileDataSchemaLocation(
      new File(s"$courierPath/${schemaName.replace('.', '/')}.courier"))
    assert(schema != null, why)
    TestSchema(schema, courierFileResolver, location)
  }
}
