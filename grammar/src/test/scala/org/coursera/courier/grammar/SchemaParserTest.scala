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

import com.linkedin.data.schema.JsonBuilder
import com.linkedin.data.schema.SchemaToJsonEncoder
import com.linkedin.data.schema.resolver.DefaultDataSchemaResolver
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitSuite

class SchemaParserTest extends JUnitSuite with AssertionsForJUnit {
  @Test
  def record(): Unit = {
    val schema =
      """namespace org.example
        |
        |/**
        | * A Fortune.
        | */
        |@fortuneProp("value1")
        |@deprecated("This will be deleted soon.")
        |record Fortune {
        |  @tellingProp("value2")
        |  telling: string? = nil /* this optional value defaults to absent */
        |  createdAt: long = 946684800000
        |  exampleMap: map[string, int]
        |  exampleArray: array[float]
        |  exampleUnion: union[string, int]
        |}
      """.stripMargin

    val parser = new CourierSchemaParser(new DefaultDataSchemaResolver())

    parser.parse(schema)

    val errors = parser.errorMessageBuilder.toString
    assert(errors.isEmpty, "SchemaParse Errors: " + errors)
    val recordSchema = parser.topLevelDataSchemas.get(0)
    val json = SchemaToJsonEncoder.schemaToJson(recordSchema, JsonBuilder.Pretty.INDENTED)
  }

  @Test
  def enum(): Unit = {
    val schema =
      """namespace org.example
        |
        |/**
        | * Magic eight ball answers.
        | */
        |@enumProp("value1")
        |@deprecated("This will be deleted soon.")
        |enum MagicEightBallAnswer {
        |  IT_IS_CERTAIN
        |  ASK_AGAIN_LATER
        |  OUTLOOK_NOT_SO_GOOD
        |}
      """.stripMargin

    val parser = new CourierSchemaParser(new DefaultDataSchemaResolver())

    parser.parse(schema)

    val errors = parser.errorMessageBuilder.toString
    assert(errors.isEmpty, "SchemaParse Errors: " + errors)
    val recordSchema = parser.topLevelDataSchemas.get(0)
    val json = SchemaToJsonEncoder.schemaToJson(recordSchema, JsonBuilder.Pretty.INDENTED)
  }
}
