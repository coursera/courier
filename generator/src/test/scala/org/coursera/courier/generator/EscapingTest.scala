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

import org.coursera.escaping.KeywordEscaping
import org.coursera.escaping.`class`
import org.junit.BeforeClass
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

object EscapingTest extends SchemaFixtures with GeneratorTest {

  @BeforeClass
  def setup(): Unit = {
    generateTestSchemas(Seq(
      Escaping.KeywordEscaping,
      Escaping.RecordNameEscaping))
  }
}

class EscapingTest extends GeneratorTest with SchemaFixtures with AssertionsForJUnit {

  @Test
  def testEscaping(): Unit = {
    val withKeywordEscaping = KeywordEscaping(`type` = "test")
    assert(withKeywordEscaping.`type` === "test")
    assert(mapToJson(withKeywordEscaping.data()) ===
      """{
        |  "type" : "test"
        |}""".stripMargin)

    // Please never actually name a schema like this!
    val withRecordNameEscaping = `class`()
    assert(mapToJson(withRecordNameEscaping.data()) === "{ }")
  }
}
