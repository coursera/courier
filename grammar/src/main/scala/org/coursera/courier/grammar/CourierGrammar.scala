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

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader

import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import org.apache.commons.lang3.StringEscapeUtils
import scala.collection.JavaConverters._
import scala.util.matching.Regex

import scala.util.parsing.combinator.RegexParsers

object CourierGrammar extends RegexParsers {
  def parse(input: String): Document = {
    parse(new ByteArrayInputStream(input.getBytes("UTF-8")))
  }

  def parse(input: InputStream): Document = {
    handleParseErrors(parseAll(documentParser, new InputStreamReader(input)))
  }

  def parse(input: Reader): Document = {
    handleParseErrors(parseAll(documentParser, input))
  }

  protected[grammar] def partial[T](input: String, part: Parser[T]): T = {
    partial(new ByteArrayInputStream(input.getBytes("UTF-8")), part)
  }

  protected[grammar] def partial[T](input: InputStream, part: Parser[T]): T = {
    handleParseErrors(parseAll(part, new InputStreamReader(input)))
  }

  private[this] def handleParseErrors[T](parseResult: ParseResult[T]): T = {
    parseResult match {
      case Success(result, _) => result
      case failure: NoSuccess =>
        throw new IOException(
          s"${failure.msg} line: ${failure.next.pos.line} column: ${failure.next.pos.column}")
    }
  }

  val singleLineComment = "//.*"

  // Matches /* */ but not /** */ style comments
  val multiLineComment = "(?m)/\\*(?!\\*)(\\*(?!/)|[^*])*\\*/"

  // Commas are "insignificant" and are treated as whitespace.
  override protected val whiteSpace = s"([ \\t\\n,]|$singleLineComment|$multiLineComment)+".r

  case class Document(
      namespace: Namespace,
      // TODO(jbetz): Support namespace includes. E.g. "include org.example.Fortune".
      //includes: Seq[Include] = Seq(),
      decl: NamedTypeDeclaration)

  case class Namespace(value: String)

  //case class Include(name: FullyQualifiedTypeName)

  sealed trait TypeName
  case class FullyQualifiedTypeName(
      name: SimpleTypeName,
      namespace: Namespace) extends TypeName {
    def fullname = namespace.value + "." + name.value
  }

  case class SimpleTypeName(value: String) extends TypeName

  case class Annotations(props: Seq[Property] = Seq.empty, schemaDoc: Option[SchemaDoc] = None)

  case class FieldName(value: String)

  sealed trait Annotation
  case class SchemaDoc(markdown: String) extends Annotation
  case class Property(key: String, jsValue: AnyRef) extends Annotation {
    lazy val jsObject: DataMap = {
      val dataMap = new DataMap(1)
      dataMap.put(key, jsValue)
      dataMap
    }
  }

  sealed trait TypeDeclaration
  sealed trait NamedTypeDeclaration extends TypeDeclaration {
    def annotations: Annotations
    def name: SimpleTypeName
  }
  case class RecordDeclaration(
      annotations: Annotations = Annotations(),
      name: SimpleTypeName,
      fields: FieldsSection) extends NamedTypeDeclaration

  case class EnumDeclaration(
      annotations: Annotations = Annotations(),
      name: SimpleTypeName,
      symbols: Seq[EnumSymbol]) extends NamedTypeDeclaration

  case class EnumSymbol(
      annotations: Annotations = Annotations(),
      name: String)
  case class TyperefDeclaration(
      annotations: Annotations = Annotations(),
      name: SimpleTypeName,
      ref: TypeAssignment) extends NamedTypeDeclaration

  /**
   * A pegasus "fixed" type.
   */
  case class FixedDeclaration(
      annotations: Annotations = Annotations(),
      name: SimpleTypeName,
      width: Long) extends NamedTypeDeclaration

  case class FieldsSection(elements: Seq[FieldSectionElement])
  sealed trait FieldSectionElement
  case class FieldDeclaration(
      annotations: Annotations = Annotations(),
      name: FieldName,
      typ: TypeAssignment,
      isOptional: Boolean = false,
      default: Option[Default] = None) extends FieldSectionElement

  case class FieldInclude(name: TypeName) extends FieldSectionElement

  sealed trait AnonymousTypeDeclaration extends TypeDeclaration
  case class MapDeclaration(
      keys: TypeAssignment,
      values: TypeAssignment) extends AnonymousTypeDeclaration

  case class ArrayDeclaration(items: TypeAssignment) extends AnonymousTypeDeclaration
  case class UnionDeclaration(members: Seq[TypeAssignment]) extends AnonymousTypeDeclaration

  /**
   * Identifies a type or declares an inline type.
   */
  sealed trait TypeAssignment

  /**
   * A type assignment by type name.  E.g. "org.example.SomeType" in:
   *
   * record Example {
   *   field: org.example.SomeType
   * }
   */
  case class AssignmentByName(name: TypeName) extends TypeAssignment

  /**
   * An inline declaration of a named type.  E.g. "record Inline { }" in:
   *
   * record Example {
   *   field: record Inline { }
   * }
   */
  case class AssignmentByInlineDeclaration(decl: TypeDeclaration) extends TypeAssignment

  case class Literal(literal: AnyRef)

  sealed trait Default
  object NilDefault extends Default
  case class LiteralDefault(literal: Literal) extends Default

  lazy val documentParser: Parser[Document] =
    namespaceDeclarationParser ~! namedTypeDeclarationParser ^^ {
      case ns ~ /*includes ~*/ decl =>
        Document(ns, /*includes, */ decl)
  }

  lazy val typeNameParser: Parser[TypeName] = fullyQualifiedTypeNameParser | simpleTypeNameParser

  val identifier = "[A-Za-z_][A-Za-z0-9_]*" // We use Avro/Pegasus identifiers
  lazy val identifierParser: Parser[String] = "`" ~> identifier.r <~ "`" | identifier.r

  lazy val simpleTypeNameParser: Parser[SimpleTypeName] = identifierParser ^^ SimpleTypeName

  val namespace = s"$identifier(\\.$identifier)*"
  lazy val namespaceParser: Parser[Namespace] = namespace.r ^^ Namespace
  lazy val namespaceDeclarationParser: Parser[Namespace] = "namespace" ~> namespaceParser
  //lazy val includesParser: Parser[Seq[Include]] = includeParser.*
  //lazy val includeParser: Parser[Include] = "include" ~> fullyQualifiedTypeNameParser ^^ Include

  val fulyQualifiedName = s"$identifier(\\.$identifier)+"
  lazy val fullyQualifiedTypeNameParser: Parser[FullyQualifiedTypeName] =
    fulyQualifiedName.r ^^ { path =>
      val i = path.lastIndexOf(".")
      val (namespace, dotName) = path.splitAt(i)
      FullyQualifiedTypeName(SimpleTypeName(dotName.drop(1)), Namespace(namespace))
    }

  lazy val annotationsParser: Parser[Annotations] = schemaDocParser.? ~ propsParser.* ^^ {
    case maybeSchemaDoc ~ props => Annotations(props, maybeSchemaDoc)
  }

  lazy val fieldNameParser: Parser[FieldName] = identifierParser ^^ FieldName

  val schemaDoc = "(\\*(?!/)|[^*])*"
  lazy val schemaDocParser: Parser[SchemaDoc] = "/**" ~> schemaDoc.r <~ "*/" ^^ { rawComment =>
    SchemaDoc(rawComment.stripMargin('*').trim())
  }
  lazy val propsParser: Parser[Property] = "@" ~> identifierParser ~
    ("(" ~> jsValueParser <~ ")").? ^^ {
    case ident ~ jsValue => Property(ident, jsValue.getOrElse(Boolean.box(true)))
  }

  lazy val typeDeclarationParser: Parser[TypeDeclaration] =
    anonymousTypeDeclarationParser | namedTypeDeclarationParser

  lazy val namedTypeDeclarationParser: Parser[NamedTypeDeclaration] =
    recordDeclarationParser |
      enumDeclarationParser |
      typerefDeclarationParser |
      fixedDeclarationParser

  lazy val recordDeclarationParser: Parser[RecordDeclaration] =
    annotationsParser ~ ("record" ~> simpleTypeNameParser) ~!
      ("{" ~> fieldsSectionParser <~ "}") ^^ {
      case annotations ~ name ~ fields => RecordDeclaration(annotations, name, fields)
  }
  lazy val enumDeclarationParser: Parser[EnumDeclaration] =
    annotationsParser ~ ("enum" ~> simpleTypeNameParser) ~! ("{" ~> enumSymbolsParser <~ "}") ^^ {
      case annotations ~ name ~ enumSymbols =>
        EnumDeclaration(annotations, name, enumSymbols)
  }
  lazy val enumSymbolsParser: Parser[Seq[EnumSymbol]] = enumSymbolParser.*
  lazy val enumSymbolParser: Parser[EnumSymbol] = annotationsParser ~ identifierParser ^^ {
    case annotations ~ name => EnumSymbol(annotations, name)
  }

  lazy val typerefDeclarationParser: Parser[TyperefDeclaration] =
    annotationsParser ~ ("typeref" ~> simpleTypeNameParser) ~! ("=" ~> typeAssignmentParser) ^^ {
      case annotations ~ name ~ ref =>
        TyperefDeclaration(annotations, name, ref)
  }
  lazy val fixedDeclarationParser: Parser[FixedDeclaration] =
    annotationsParser ~ ("fixed" ~> simpleTypeNameParser) ~! nonNegativeIntegerParser ^^ {
      case annotations ~ name ~ width =>
        FixedDeclaration(annotations, name, width)
  }
  lazy val fieldsSectionParser: Parser[FieldsSection] = fieldSectionElementParser.* ^^ FieldsSection
  lazy val fieldSectionElementParser: Parser[FieldSectionElement] =
    fieldIncludeParser | fieldDeclarationParser

  lazy val someLiteralParser: Parser[Default] = literalParser ^^ LiteralDefault
  lazy val nilParser: Parser[Default] = "nil" ^^ { _ => NilDefault }
  lazy val optionalLiteralParser: Parser[Default] = someLiteralParser | nilParser
  lazy val fieldDeclarationParser: Parser[FieldDeclaration] =
    annotationsParser ~ fieldNameParser ~! (":" ~> typeAssignmentParser) ~! "?".? ~
      ("=" ~> optionalLiteralParser).? ^^ {
        case annotations ~ fieldName ~ typeAssignment ~ isOptional ~ maybeLiteral =>
          FieldDeclaration(
            annotations, fieldName, typeAssignment, isOptional.isDefined, maybeLiteral)
  }
  lazy val fieldIncludeParser: Parser[FieldInclude] = "..." ~> typeNameParser ^^ FieldInclude
  lazy val anonymousTypeDeclarationParser: Parser[AnonymousTypeDeclaration] =
    mapDeclarationParser | arrayDeclarationParser | unionDeclarationParser

  lazy val mapDeclarationParser: Parser[MapDeclaration] =
    "map" ~> "[" ~> typeAssignmentParser ~ typeAssignmentParser <~ "]" ^^ {
      case keys ~ values  => MapDeclaration(keys, values)
  }
  lazy val arrayDeclarationParser: Parser[ArrayDeclaration] =
    "array" ~> "[" ~> typeAssignmentParser <~ "]" ^^ ArrayDeclaration

  lazy val unionDeclarationParser: Parser[UnionDeclaration] = unionMembersParser ^^ UnionDeclaration
  lazy val unionMembersParser: Parser[Seq[TypeAssignment]] =
    "union" ~> "[" ~> rep(typeAssignmentParser) <~ "]"

  lazy val typeAssignmentParser: Parser[TypeAssignment] =
    assignmentByInlineDeclarationParser | assignmentByNameParser

  lazy val assignmentByNameParser: Parser[AssignmentByName] = typeNameParser ^^ AssignmentByName
  lazy val assignmentByInlineDeclarationParser: Parser[AssignmentByInlineDeclaration] =
    typeDeclarationParser ^^ AssignmentByInlineDeclaration

  lazy val literalParser: Parser[Literal] = jsValueParser ^^ Literal

  // TODO(jbetz): The below JSON parser should be separated out as much as possible, can it be put
  // into it's own parser?
  // Also, it currently is using the incorrect whitespace rules (with insignificant commas) from
  // the main grammar this needs to be changed.
  val nonNegativeIntegerPart = "(?:0|[1-9][0-9]*)"
  val integerPart =  s"-?$nonNegativeIntegerPart"
  val fractionalPart = "\\.[0-9]+"
  val exponentPart = s"[eE][+-][0-9]+"

  lazy val nonNegativeIntegerParser: Parser[Long] = nonNegativeIntegerPart.r ^^ { _.toLong }

  lazy val jsKVParser: Parser[(String, AnyRef)] = jsStringParser ~ (":" ~> jsValueParser) ^^ {
    case key ~ value => (key, value)
  }

  lazy val jsObjectParser: Parser[DataMap] = "{" ~> rep(jsKVParser /*, ","*/) <~ "}" ^^ { tuples =>
    new DataMap(tuples.toMap.asJava)
  }

  lazy val jsArrayParser: Parser[DataList] = "[" ~> rep(jsValueParser /*, ","*/) <~ "]" ^^ { vals =>
    new DataList(vals.asJava)
  }

  lazy val jsValueParser: Parser[AnyRef] =
    jsStringParser |
      jsNumberParser |
      jsObjectParser |
      jsArrayParser |
      jsBooleanParser |
      jsNullParser

  val escapeChar = "\\\\"
  val quoteChar = "\""
  val reservedChars = s"$quoteChar$escapeChar"
  val escapableChars = s"$reservedChars/bfnrt"

  val unicodeHex = "[0-9a-fA-F]{4}"
  val escaped = s"$escapeChar([$escapableChars]|u$unicodeHex)"

  lazy val jsStringParser: Parser[String] =
    "\"" ~> s"([^$reservedChars]|$escaped)*".r <~ "\"" ^^ StringEscapeUtils.unescapeJson

  lazy val jsBooleanParser: Parser[java.lang.Boolean] = "(true|false)".r ^^ { _.toBoolean }
  lazy val jsNullParser: Parser[Null] = "null" ^^ { _ => null }
  lazy val jsNumberParser: Parser[java.lang.Number] =
    s"$integerPart($fractionalPart)?($exponentPart)?".r ^^ { str =>
      if (str.contains(".")) {
        str.toDouble
      } else {
        str.toLong
      }
    }
}
