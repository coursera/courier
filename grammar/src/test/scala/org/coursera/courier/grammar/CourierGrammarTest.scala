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

import java.io.IOException

import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import org.coursera.courier.grammar.CourierGrammar.Annotations
import org.coursera.courier.grammar.CourierGrammar.ArrayDeclaration
import org.coursera.courier.grammar.CourierGrammar.AssignmentByInlineDeclaration
import org.coursera.courier.grammar.CourierGrammar.AssignmentByName
import org.coursera.courier.grammar.CourierGrammar.Document
import org.coursera.courier.grammar.CourierGrammar.EnumDeclaration
import org.coursera.courier.grammar.CourierGrammar.EnumSymbol
import org.coursera.courier.grammar.CourierGrammar.FieldDeclaration
import org.coursera.courier.grammar.CourierGrammar.FieldInclude
import org.coursera.courier.grammar.CourierGrammar.FieldName
import org.coursera.courier.grammar.CourierGrammar.FieldsSection
import org.coursera.courier.grammar.CourierGrammar.FixedDeclaration
import org.coursera.courier.grammar.CourierGrammar.FullyQualifiedTypeName
import org.coursera.courier.grammar.CourierGrammar.Literal
import org.coursera.courier.grammar.CourierGrammar.LiteralDefault
import org.coursera.courier.grammar.CourierGrammar.MapDeclaration
import org.coursera.courier.grammar.CourierGrammar.Namespace
import org.coursera.courier.grammar.CourierGrammar.NilDefault
import org.coursera.courier.grammar.CourierGrammar.Property
import org.coursera.courier.grammar.CourierGrammar.RecordDeclaration
import org.coursera.courier.grammar.CourierGrammar.SchemaDoc
import org.coursera.courier.grammar.CourierGrammar.SimpleTypeName
import org.coursera.courier.grammar.CourierGrammar.TyperefDeclaration
import org.coursera.courier.grammar.CourierGrammar.UnionDeclaration
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitSuite

import scala.collection.JavaConverters._

class CourierGrammarTest extends JUnitSuite with AssertionsForJUnit {
  @Test
  def identifiers(): Unit = {
    assertMatch(CourierGrammar.identifierParser, ("Fortune", "Fortune"), ("telling", "telling"))
  }

  @Test
  def namespace(): Unit = {
    assertMatch(CourierGrammar.namespaceParser,
      ("org.example", Namespace("org.example")),
      ("org.coursera.common", Namespace("org.coursera.common")))
  }

  @Test
  def typeNameParser(): Unit = {
    assertMatch(CourierGrammar.simpleTypeNameParser,
      ("Fortune", SimpleTypeName("Fortune")))

    assertMatch(CourierGrammar.fullyQualifiedTypeNameParser,
      ("org.example.Fortune", FullyQualifiedTypeName(SimpleTypeName("Fortune"),
      Namespace("org.example"))))

    assertMatch(CourierGrammar.typeNameParser,
      ("Fortune", SimpleTypeName("Fortune")),
      ("org.example.Fortune", FullyQualifiedTypeName(SimpleTypeName("Fortune"),
      Namespace("org.example"))))
  }

  @Test
  def recordDeclarationParser(): Unit = {
    assertMatch(CourierGrammar.recordDeclarationParser,
      ("record Fortune {}",
        RecordDeclaration(
          name = SimpleTypeName("Fortune"),
          fields = FieldsSection(Seq()))))

    assertMatch(CourierGrammar.documentParser,
      (
        """namespace org.example
          |
          |/**
          | * A Fortune.
          | */
          |@prop("propValue")
          |record Fortune {
          |  ...AuditTimestamps
          |  telling: tellingUnion? = nil /* this optional value defaults to absent */
          |  createdAt: DateTime = 946684800000
          |}
        """.stripMargin,
        Document(
          namespace = Namespace("org.example"),
          decl = RecordDeclaration(
            annotations = Annotations(Seq(
              Property("prop", "propValue")),Some(SchemaDoc("A Fortune."))),
            name = SimpleTypeName("Fortune"),
            fields = FieldsSection(Seq(
              FieldInclude(SimpleTypeName("AuditTimestamps")),
              FieldDeclaration(
                name = FieldName("telling"),
                typ = AssignmentByName(SimpleTypeName("tellingUnion")),
                isOptional = true,
                default = Some(NilDefault)),
              FieldDeclaration(
                name = FieldName("createdAt"),
                typ = AssignmentByName(SimpleTypeName("DateTime")),
                default = Some(LiteralDefault(Literal(Long.box(946684800000L)))))))))))
  }

  @Test
  def fieldDeclarationParser(): Unit = {
    assertMatch(CourierGrammar.fieldDeclarationParser,
      ("exampleMap: map[string, int]", FieldDeclaration(
        name = FieldName("exampleMap"),
        typ = AssignmentByInlineDeclaration(
          MapDeclaration(
            AssignmentByName(SimpleTypeName("string")),
            AssignmentByName(SimpleTypeName("int")))))))
  }

  @Test
  def enumDeclarationParser(): Unit = {
    assertMatch(CourierGrammar.enumDeclarationParser,
      ("enum MagicEightBallAnswer { SYMBOL1 }",
        EnumDeclaration(
          name = SimpleTypeName("MagicEightBallAnswer"),
          symbols = Seq(EnumSymbol(name = "SYMBOL1")))))

    assertMatch(CourierGrammar.documentParser,
      (
        """namespace org.example
          |
          |enum MagicEightBallAnswer {
          |  IT_IS_CERTAIN // a enum symbol
          |  ASK_AGAIN_LATER
          |  OUTLOOK_NOT_SO_GOOD
          |}
        """.stripMargin,
        Document(
          namespace = Namespace("org.example"),
          decl = EnumDeclaration(
            name = SimpleTypeName("MagicEightBallAnswer"),
            symbols = Seq(
              EnumSymbol(name = "IT_IS_CERTAIN"),
              EnumSymbol(name = "ASK_AGAIN_LATER"),
              EnumSymbol(name = "OUTLOOK_NOT_SO_GOOD"))))))
  }

  @Test
  def fixedDeclarationParser(): Unit = {
    assertMatch(CourierGrammar.fixedDeclarationParser,
      ("fixed Fixed8 8",
        FixedDeclaration(
          name = SimpleTypeName("Fixed8"), width = 8)))
  }

  @Test
  def typerefDeclarationParser(): Unit = {
    assertMatch(CourierGrammar.typerefDeclarationParser,
      ("typeref Timestamp = long",
        TyperefDeclaration(
          name = SimpleTypeName("Timestamp"), ref = AssignmentByName(SimpleTypeName("long")))))
  }

  @Test
  def arrayDeclarationParser(): Unit = {
    assertMatch(CourierGrammar.arrayDeclarationParser,
      ("array[int]",
        ArrayDeclaration(AssignmentByName(SimpleTypeName("int")))))
  }

  @Test
  def mapDeclarationParser(): Unit = {
    assertMatch(CourierGrammar.mapDeclarationParser,
      ("map[string, int]",
        MapDeclaration(
          AssignmentByName(SimpleTypeName("string")),
          AssignmentByName(SimpleTypeName("int")))))
  }

  @Test
  def unionDeclarationParser(): Unit = {
    assertMatch(CourierGrammar.unionDeclarationParser,
      ("union[string, int, Fortune]",
        UnionDeclaration(Seq(
          AssignmentByName(SimpleTypeName("string")),
          AssignmentByName(SimpleTypeName("int")),
          AssignmentByName(SimpleTypeName("Fortune"))))))
  }

  @Test
  def propsParser(): Unit = {
    assertMatch(CourierGrammar.propsParser,
      (
        """@scala({
          |  "class": "org.joda.time.DateTime"
          |})
        """.stripMargin,
        Property("scala", new DataMap(Map("class" -> "org.joda.time.DateTime").asJava))))
  }

  @Test
  def jsValueParser(): Unit = {
    def quoted(str: String) = "\"" + str + "\""
    val oneTwoThree = new DataList(List(1,2,3).map(i => Long.box(i)).asJava)
    assertMatch(CourierGrammar.jsValueParser,
      ("1", 1L),
      ("3.14", 3.14d),
      ("true", true),
      ("false", false),
      ("null", null),
      (quoted("a string"), "a string"),
      (quoted("quote: \\\""), "quote: \""),
      (quoted("newline: \\n"), "newline: \n"),
      (quoted(s"unicode: ${"\\"}u2713"), "unicode: \u2713"),
      ("[1, 2, 3]", oneTwoThree),
      ("""{ "a": "x" }""", new DataMap(Map("a" -> "x").asJava)),
      ("""{ "a": [1, 2, 3] }""", new DataMap(Map("a" -> oneTwoThree).asJava)))
  }

  def assertMatch[T](parser: CourierGrammar.Parser[T], cases: (String, Any)*): Unit = {
    cases.foreach { case (input, expected) =>
      try {
        val result = CourierGrammar.partial(input, parser)
        assert(expected === result, cases)
      } catch {
        case io: IOException =>
          assert(false, s"input: $input error: ${io.getMessage}")
      }
    }
  }

  def assertParses[T](parser: CourierGrammar.Parser[T], input: String): Unit = {
    CourierGrammar.partial(input, parser)
  }
}
