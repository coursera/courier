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

package org.coursera.courier.templates

import com.linkedin.data.DataMap
import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.data.schema.TyperefDataSchema
import com.linkedin.data.schema.UnionDataSchema
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.template.RecordTemplate
import com.linkedin.data.template.UnionTemplate
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test

class DataTemplatesTest extends AssertionsForJUnit {
  import DataTemplatesTest._

  @Test
  def readWriteRecord(): Unit = {
    val json = """{"string":"a","int":1}"""
    val union = DataTemplates.readRecord[MockRecord](json)
    val roundTripped = DataTemplates.writeRecord(union)
    assert(DataTemplates.readDataMap(roundTripped) === DataTemplates.readDataMap(json))
  }

  @Test
  def readWriteUnion(): Unit = {
    val json = """{"int":1}"""
    val union = DataTemplates.readUnion[MockTyperefUnion](json)
    val roundTripped = DataTemplates.writeUnion(union)
    assert(DataTemplates.readDataMap(roundTripped) === DataTemplates.readDataMap(json))
  }

  @Test
  def testGetSchema(): Unit = {
    val schemaFromClass = DataTemplates.getSchema(classOf[MockRecord])
    assert(schemaFromClass === MockRecord.SCHEMA)

    val schemaFromClassTag = DataTemplates.getSchema[MockRecord]
    assert(schemaFromClassTag === MockRecord.SCHEMA)
  }
}

object DataTemplatesTest {
  class MockRecord(private val dataMap: DataMap)
    extends RecordTemplate(dataMap, MockRecord.SCHEMA) {
    dataMap.makeReadOnly()
  }

  object MockRecord {
    val SCHEMA_JSON =
      """
        |{
        |  "name": "MockRecord",
        |  "type": "record",
        |  "fields": [
        |    { "name": "string", "type": "string" },
        |    { "name": "int", "type": "int" }
        |  ]
        |}
        |""".stripMargin

    val SCHEMA = DataTemplateUtil.parseSchema(SCHEMA_JSON).asInstanceOf[RecordDataSchema]

    def apply(dataMap: DataMap, dataConversion: DataConversion) = {
      new MockRecord(dataMap)
    }
  }

  class MockTyperefUnion(private val dataMap: DataMap)
    extends UnionTemplate(dataMap, MockTyperefUnion.SCHEMA) {
    dataMap.makeReadOnly()
  }

  object MockTyperefUnion {
    val SCHEMA_JSON =
      """
        |[ "int", "string" ]
        |""".stripMargin

    val SCHEMA = DataTemplateUtil.parseSchema(SCHEMA_JSON).asInstanceOf[UnionDataSchema]

    val TYPEREF_SCHEMA_JSON =
      """
        |{
        |  "name": "MockTyperefUnion",
        |  "type": "typeref",
        |  "ref": [ "int", "string" ]
        |}
        |""".stripMargin

    val TYPEREF_SCHEMA =
      DataTemplateUtil.parseSchema(TYPEREF_SCHEMA_JSON).asInstanceOf[TyperefDataSchema]

    def apply(dataMap: DataMap, dataConversion: DataConversion) = {
      new MockTyperefUnion(dataMap)
    }
  }
}
