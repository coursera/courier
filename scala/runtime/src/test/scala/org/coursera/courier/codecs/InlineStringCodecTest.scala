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

package org.coursera.courier.codecs

import java.io.ByteArrayOutputStream
import java.io.StringWriter

import com.fasterxml.jackson.core.JsonFactory
import com.linkedin.data.ByteString
import com.linkedin.data.DataComplex
import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.validation.CoercionMode
import com.linkedin.data.schema.validation.RequiredMode
import com.linkedin.data.schema.validation.ValidateDataAgainstSchema
import com.linkedin.data.schema.validation.ValidationOptions
import com.linkedin.data.template.DataTemplateUtil
import org.coursera.courier.templates.DataTemplates
import org.coursera.courier.templates.DataValidationException
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

class InlineStringCodecTest extends AssertionsForJUnit {

  @Test
  def testPrimitives(): Unit = {
    assertMap("(int~100)", """{ "int": "100" }""")
    assertMap("(int~-100)", """{ "int": "-100" }""")
    assertMap(s"(long~3000000000)", """{ "long": "3000000000" }""")
    assertMap(s"(long~-3000000000)", """{ "long": "-3000000000" }""")
    assertMap("(float~3.14)", """{ "float": "3.14" }""")
    assertMap("(float~-3.14)", """{ "float": "-3.14" }""")
    assertMap("(double~2.718281828)", """{ "double": "2.718281828" }""")
    assertMap("(double~-2.718281828)", """{ "double": "-2.718281828" }""")
    assertMap("(boolean~true)", """{ "boolean": "true" }""")
    assertMap(s"(bytes~$bytes1AvroInline)", s"""{ "bytes": $bytes1AvroJson }""")
  }

  @Test
  def testMaps(): Unit = {
    assertMap("()", """{}""")
    assertMap("(k1~)", """{ "k1": "" }""")
    assertMap("(k1~v1)", """{ "k1": "v1" }""")
    assertMap("(k1~v1,k2~v2)", """{ "k1": "v1", "k2": "v2" }""")
    assertMap("(k1~v1,k2~v2,k3~v3)", """{ "k1": "v1", "k2": "v2", "k3": "v3" }""")
  }

  @Test
  def testLists(): Unit = {
    assertList("List()", """[]""")
    assertList("List(~)", """[""]""")
    assertList("List(v1)", """["v1"]""")
    assertList("List(int)", """["int"]""")
    assertList("List(v1,v2)", """["v1", "v2"]""")
    assertList("List(v1,v2,v3)", """["v1", "v2", "v3"]""")
  }

  @Test
  def testComplexTypeComposition(): Unit = {
    assertMap("(k1~())", """{ "k1": {} }""")
    assertList("List(())", """[ {} ]""")

    // map of maps
    assertMap("(k1~(x~y))", """{ "k1": { "x": "y" }}""")
    assertMap("(k1~(x1~y1,x2~y2))", """{ "k1": { "x1": "y1", "x2": "y2" }}""")

    // maps of lists
    assertMap("(k1~List(1))", """{ "k1": ["1"] }""")
    assertMap("(k1~List(1,2))", """{ "k1": ["1", "2"] }""")

    // list of lists
    assertList("List(List(1))", """[["1"]]""")
    assertList("List(List(1),List(2))", """[["1"], ["2"]]""")
    assertList("List(List(1,2),List(3,4))", """[["1", "2"], ["3", "4"]]""")

    // list of maps
    assertList("List((k1~v1))", """[{ "k1": "v1" }]""")
    assertList("List((k1~v1),(k2~v2))", """[{ "k1": "v1" }, { "k2": "v2" }]""")

    // deeply nested
    assertMap("(l1~(l2~(l3~(l4~(l5~v)))))", """{ "l1": {"l2": {"l3": {"l4": {"l5": "v"}}}}}""")
    assertList("List(List(List(List(List(v)))))", """[[[[["v"]]]]]""")
  }

  @Test
  def testEscaping(): Unit = {
    assertList("List(!))", """[")"]""")
    assertList("List(!()", """["("]""")
    assertList("List(!,)", """[","]""")
    assertList("List(!~)", """["~"]""")
    assertList("List(!!)", """["!"]""")

    assertMap( """(string~!~An !(odd!) string !!)""", """{ "string": "~An (odd) string !" }""")
  }

  @Test
  def testFixedUp(): Unit = {
    val schema = DataTemplateUtil.parseSchema(
      """
        |{
        |  "name": "WithInt",
        |  "type": "record",
        |  "fields": [
        |    { "name": "int", "type": "int" },
        |    { "name": "long", "type": "long" },
        |    { "name": "float", "type": "float" },
        |    { "name": "double", "type": "double" },
        |    { "name": "boolean", "type": "boolean" },
        |    { "name": "string", "type": "string" },
        |    { "name": "bytes", "type": "bytes" }
        |  ]
        |}
        |""".stripMargin)
    assertFixedUpMap(
      s"(int~1,long~100,float~3.14,double~2.71,boolean~true,string~hello,bytes~$bytes1AvroInline)",
      s"""{
        |  "int": 1,
        |  "long": 100,
        |  "float": 3.14,
        |  "double": 2.71,
        |  "boolean": true,
        |  "string": "hello",
        |  "bytes": $bytes1AvroJson
        |}
        |""".stripMargin, schema)
  }

  @Test
  def testBytes(): Unit = {
    val data = new DataMap()
    data.put("bytes", bytes1)
    assertMap(s"(bytes~$bytes1AvroInline)", DataTemplates.writeDataMap(data))
  }

  @Test
  def testWriteMap(): Unit = {
    val in = """{"k1": "v1"}"""
    val map = DataTemplates.readDataMap(in)
    val out = new ByteArrayOutputStream()
    codec.writeMap(map, out)
    val result = new String(out.toByteArray, InlineStringCodec.charset)
    assert(result === "(k1~v1)")
  }

  @Test
  def testWriteList(): Unit = {
    val in = """["v1"]"""
    val map = DataTemplates.readDataList(in)
    val out = new ByteArrayOutputStream()
    codec.writeList(map, out)
    val result = new String(out.toByteArray, InlineStringCodec.charset)
    assert(result === "List(v1)")
  }

  val bytes1 = ByteString.copy(Array(0x1, 0x2, 0x3, 0x7E, 0x7F, 0xFE, 0xFF).map(_.toByte))
  val bytes1AvroJson = escapeJsonString(bytes1.asAvroString())
  val bytes1AvroInline = InlineStringCodec.escape(bytes1.asAvroString())

  private val codec = new InlineStringCodec()

  private def assertFixedUpMap(stringFormat: String, json: String, schema: DataSchema): Unit = {
    val rawData = codec.bytesToMap(stringFormat.getBytes(InlineStringCodec.charset))
    fixUp(rawData, schema)
    assertJson(rawData, json)
  }

  private[this] val validationOptions =
    new ValidationOptions(RequiredMode.FIXUP_ABSENT_WITH_DEFAULT, CoercionMode.STRING_TO_PRIMITIVE)

  private def fixUp(dataMap: DataComplex, schema: DataSchema) = {
    val result = ValidateDataAgainstSchema.validate(dataMap, schema, validationOptions)
    if (result.isValid) {
      dataMap
    } else {
      throw new DataValidationException(result)
    }
  }

  private def assertMap(stringFormat: String, json: String): Unit = {
    assertJson(codec.bytesToMap(stringFormat.getBytes(InlineStringCodec.charset)), json)
    assert(stringFormat ===
      new String(codec.mapToBytes(DataTemplates.readDataMap(json)), InlineStringCodec.charset))
  }

  private def assertList(stringFormat: String, json: String): Unit = {
    assertJson(codec.bytesToList(stringFormat.getBytes(InlineStringCodec.charset)), json)
    assert(stringFormat ===
      new String(codec.listToBytes(DataTemplates.readDataList(json)), InlineStringCodec.charset))
  }

  private def assertJson(left: DataMap, right: String): Unit = {
    val leftMap = DataTemplates.readDataMap(DataTemplates.writeDataMap(left))
    val rightMap = DataTemplates.readDataMap(right)
    assert(leftMap === rightMap)
  }

  private def assertJson(left: DataList, right: String): Unit = {
    val leftMap = DataTemplates.readDataList(DataTemplates.writeDataList(left))
    val rightMap = DataTemplates.readDataList(right)
    assert(leftMap === rightMap)
  }

  private def escapeJsonString(plainString: String): String = {
    val writer = new StringWriter
    val generator = new JsonFactory().createGenerator(writer)
    generator.writeString(plainString)
    generator.close()
    writer.getBuffer.toString
  }
}
