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

package org.coursera.graphql

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

import scala.util.parsing.combinator.RegexParsers

/**
 * Implementation of https://facebook.github.io/graphql/
 */
object GraphQL {
  object Parser extends RegexParsers {

    def parse(input: String): Document = {
      parse(new ByteArrayInputStream(input.getBytes("UTF-8")))
    }

    def parse(input: InputStream): Document = {
      handleParseErrors(parseAll(documentParser, new InputStreamReader(input)))
    }

    protected[graphql] def partial[T](input: String, part: Parser[T]): T = {
      partial(new ByteArrayInputStream(input.getBytes("UTF-8")), part)
    }

    protected[graphql] def partial[T](input: InputStream, part: Parser[T]): T = {
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

    // TODO(jbetz): switch to unicode ranges provided in spec
    //val sourceCharacter = """[\\u0009\\u000A\\u000D\\u0020-\\uFFFF]"""
    val sourceCharacter = """[\t\n\w]"""

    val unicodeBom = "\uFEFF"
    val graphQLWhiteSpace = """[\t ]*"""
    val lineTerminator = "(\\n|\\r\\n)"
    val commentChar = s"(?!$lineTerminator)$sourceCharacter" // SourceCharacter but not LineTerminator
    val comment = s"#$commentChar*"
    val comma = "," // GraphQL has "Insignificant commas"

    val name = "[_A-Za-z][_0-9A-Za-z]*"
    val unicodeChar = "\\\\u[0-9A-Fa-f]{4}"
    val escapeChar = "\\[\"\\/bfnrt]"

    val integerPart =  "(-?0|-?[1-9][0-9]*)"
    val fractionalPart = "\\.[0-9]+"
    val exponentPart = s"[eE][+-][0-9]+"

    // TODO: parse unicode correctly and handle escape chars
    //def stringCharacter = s"""((?!($lineTerminator|\\))$sourceCharacter|$unicodeChar|$escapeChar)"""
    val stringCharacter = sourceCharacter

    // Ignored Tokens
    // TODO(jbetz): Fix whitespace, comments are being ignored and we're not quite up to spec
    //override protected val whiteSpace = s"($unicodeBom|$graphQLWhiteSpace|$lineTerminator|$comment|$comma)*".r
    override protected val whiteSpace = "[ \\t\\n,]*".r

    case class Document(definitions: Seq[Definition])

    sealed trait Definition

    sealed trait OperationDefinition extends Definition
    case class OperationDefinition_Compound(opType: OperationType, name: Option[Name], variableDefinitions: Option[VariableDefinitions], directives: Option[Directives], selectionSet: SelectionSet) extends OperationDefinition
    case class OperationDefinition_SelectionSet(selectionSet: SelectionSet) extends OperationDefinition

    sealed trait OperationType
    object Query extends OperationType {
      override def toString = "Query"
    }
    object Mutation extends OperationType {
      override def toString = "Mutation"
    }

    case class SelectionSet(selections: Seq[Selection])
    sealed trait Selection
    case class Field(alias: Option[Alias] = None, name: Name, arguments: Option[Arguments] = None, directives: Option[Directives] = None, selectionSet: Option[SelectionSet] = None) extends Selection
    case class FragmentSpread(fragmentName: FragmentName, directives: Option[Directives]) extends Selection // "..." FragmentName Directives(opt)
    case class FragmentName(name: Name) // Name but not "on"

    case class Argument(name: Name, value: Value)
    case class Arguments(arguments: Seq[Argument])

    case class Alias(name: Name)

    case class FragmentDefinition(fragmentName: FragmentName, typeCondition: TypeCondition, directives: Option[Directives], selectionSet: SelectionSet) extends Definition // "fragment" FragmentName TypeCondition Directives(opt) SelectionSet

    case class TypeCondition(namedType: NamedType) // "on" NamedType

    case class InlineFragment(typeCondition: Option[TypeCondition], directives: Option[Directives], selectionSet: SelectionSet) extends Selection // "..." TypeCondition(opt) Directives(opt) SelectionSet

    sealed trait Value // [Const]
    case class VariableValue(variable: Variable) extends Value // [~Const]
    case class IntValue(value: Int) extends Value
    case class FloatValue(value: Float) extends Value
    case class StringValue(value: String) extends Value
    case class BooleanValue(value: Boolean) extends Value
    case class EnumValue(value: Enum) extends Value

    case class ListValue(values: Seq[Value]) extends Value
    case class ObjectField(name: Name, value: Value)
    case class ObjectValue(fields: Seq[ObjectField]) extends Value

    case class Variable(name: Name)
    case class VariableDefinitions(defs: Seq[VariableDefinition])
    case class VariableDefinition(variable: Variable, typ: Type, defaultValue: Option[DefaultValue])
    case class DefaultValue(value: Value)

    sealed trait Type
    case class NamedType(name: Name) extends Type
    case class ListType(typ: Type) extends Type
    sealed trait NonNullType extends Type
    case class NonNullType_NamedType(named: NamedType) extends NonNullType
    case class NonNullType_ListType(list: ListType) extends NonNullType

    case class Directive(name: Name, arguments: Option[Arguments])
    case class Directives(directives: Seq[Directive])

    case class Name(value: String)
    case class Enum(value: String)

    def nameParser: Parser[Name] = name.r ^^ { Name }

    def directiveParser: Parser[Directive] = "@" ~> nameParser ~ argumentsParser.? ^^ {
      case n ~ args => Directive(n, args)
    }
    def directivesParser: Parser[Directives] = directiveParser.+ ^^ Directives

    def namedTypeParser: Parser[NamedType] = nameParser ^^ NamedType
    def listTypeParser: Parser[ListType] = "[" ~> typeParser <~ "]" ^^ ListType
    def nonNullType_NamedTypeParser: Parser[NonNullType_NamedType] = namedTypeParser <~ "!" ^^ NonNullType_NamedType
    def nonNullType_ListTypeParser: Parser[NonNullType_ListType] = listTypeParser <~ "!" ^^ NonNullType_ListType
    def nonNullTypeParser: Parser[NonNullType] = nonNullType_NamedTypeParser | nonNullType_ListTypeParser
    def typeParser: Parser[Type] = namedTypeParser | listTypeParser | nonNullTypeParser

    def defaultValueParser: Parser[DefaultValue] = valueParser ^^ DefaultValue
    def variableParser: Parser[Variable] = "$" ~> nameParser ^^ Variable
    def variableDefinitionParser: Parser[VariableDefinition] = variableParser ~ ":" ~ typeParser ~ defaultValueParser.? ^^ {
      case variable ~ _ ~ typ ~ maybeDefault => VariableDefinition(variable, typ, maybeDefault)
    }
    def variableDefinitionsParser: Parser[VariableDefinitions] = "(" ~> variableDefinitionParser.+ <~ ")" ^^ VariableDefinitions

    def intParser: Parser[Int] = integerPart.r ^^ { _.toInt }
    def floatParser: Parser[Float] = s"$integerPart($fractionalPart)?($exponentPart)?".r ^^ { _.toFloat }
    def booleanParser: Parser[Boolean] = "(true|false)".r ^^ { _.toBoolean }
    def enumParser: Parser[Enum] = name.r ^^ { Enum } // TODO: not (true|false|null)
    def stringParser: Parser[String] = "\"" ~> stringCharacter.r.* <~ "\"" ^^ { value => value.mkString("") }

    def variableValueParser: Parser[VariableValue] = variableParser ^^ VariableValue
    def intValueParser: Parser[IntValue] = intParser ^^ IntValue
    def floatValueParser: Parser[FloatValue] = floatParser ^^ FloatValue
    def stringValueParser: Parser[StringValue] = stringParser ^^ StringValue
    def booleanValueParser: Parser[BooleanValue] = booleanParser ^^ BooleanValue
    def enumValueParser: Parser[EnumValue] = enumParser ^^ EnumValue
    def listValueParser: Parser[ListValue] = "[" ~> valueParser.* <~ "]" ^^ ListValue
    def objectFieldParser: Parser[ObjectField] = nameParser ~ ":" ~ valueParser ^^ {
      case n ~ _ ~ v => ObjectField(n, v)
    }
    def objectValueParser: Parser[ObjectValue] = "{" ~> objectFieldParser.* <~ "}" ^^ ObjectValue

    def valueParser: Parser[Value] =
      variableValueParser |
        intValueParser |
        floatValueParser |
        stringValueParser |
        enumValueParser |
        listValueParser |
        objectValueParser

    def typeConditionParser: Parser[TypeCondition] = "on" ~> namedTypeParser ^^ TypeCondition

    def inlineFragmentParser: Parser[InlineFragment] = "..." ~> typeConditionParser.? ~ directivesParser.? ~ selectionSetParser ^^ {
      case typedCondition ~ directives ~ selectionSet => InlineFragment(typedCondition, directives, selectionSet)
    }

    def fragmentNameParser: Parser[FragmentName] = nameParser ^^ FragmentName // TODO: but not "on"
    def fragmentSpreadParser: Parser[FragmentSpread] = "..." ~> fragmentNameParser ~ directivesParser.? ^^ {
      case fragmentName ~ directives => FragmentSpread(fragmentName, directives)
    }

    def aliasParser: Parser[Alias] = nameParser <~ ":" ^^ { name => Alias(name) }

    def argumentParser: Parser[Argument] = nameParser ~ ":" ~ valueParser ^^ {
      case n ~ _ ~ value => Argument(n, value)
    }

    def argumentsParser: Parser[Arguments] = "(" ~> argumentParser.+ <~ ")" ^^ Arguments

    def fieldParser: Parser[Field] = aliasParser.? ~ nameParser ~ argumentsParser.? ~ directivesParser.? ~ selectionSetParser.? ^^ {
      case alias ~ n ~ args ~ directives ~ selectionSet =>
        Field(alias, n, args, directives, selectionSet)
    }

    def selectionParser: Parser[Selection] = fieldParser | fragmentSpreadParser | inlineFragmentParser

    def selectionSetParser: Parser[SelectionSet] = "{" ~> selectionParser.* <~ "}" ^^ SelectionSet

    def operationTypeParser: Parser[OperationType] = "(query|mutation)".r ^^ {
      case "query" => Query
      case "mutation" => Mutation
    }

    def operationDefinition_Compound: Parser[OperationDefinition_Compound] = {
      operationTypeParser ~ nameParser.? ~ variableDefinitionsParser.? ~ directivesParser.? ~ selectionSetParser ^^ {
        case o ~ n ~ vds ~ ds ~ ss => OperationDefinition_Compound(o, n, vds, ds, ss)
      }
    }

    def operationDefinition_SelectionSet: Parser[OperationDefinition_SelectionSet] = selectionSetParser ^^ OperationDefinition_SelectionSet

    def fragmentDefinitionParser: Parser[FragmentDefinition] = "fragment" ~> fragmentNameParser ~ typeConditionParser ~ directivesParser.? ~ selectionSetParser ^^ {
      case fragmentName ~ typeCondition ~ directives ~ selectionSet => FragmentDefinition(fragmentName, typeCondition, directives, selectionSet)
    }

    def operationDefinitionParser: Parser[OperationDefinition] = operationDefinition_Compound | operationDefinition_SelectionSet

    def definitionParser: Parser[Definition] = operationDefinitionParser | fragmentDefinitionParser

    def documentParser: Parser[Document] = definitionParser.+ ^^ Document

  }
}
