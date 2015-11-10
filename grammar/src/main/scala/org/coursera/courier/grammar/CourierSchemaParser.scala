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

import java.io._

import com.linkedin.data.DataMap
import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.DataSchemaResolver
import com.linkedin.data.schema.EnumDataSchema
import com.linkedin.data.schema.FixedDataSchema
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.data.schema.Name
import com.linkedin.data.schema.NamedDataSchema
import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.data.schema.RecordDataSchema.Field
import com.linkedin.data.schema.SchemaParser
import com.linkedin.data.schema.TyperefDataSchema
import com.linkedin.data.schema.UnionDataSchema
import org.coursera.courier.grammar.CourierGrammar.ArrayDeclaration
import org.coursera.courier.grammar.CourierGrammar.AssignmentByInlineDeclaration
import org.coursera.courier.grammar.CourierGrammar.AssignmentByName
import org.coursera.courier.grammar.CourierGrammar.Document
import org.coursera.courier.grammar.CourierGrammar.EnumDeclaration
import org.coursera.courier.grammar.CourierGrammar.FieldDeclaration
import org.coursera.courier.grammar.CourierGrammar.FieldInclude
import org.coursera.courier.grammar.CourierGrammar.FieldsSection
import org.coursera.courier.grammar.CourierGrammar.FixedDeclaration
import org.coursera.courier.grammar.CourierGrammar.FullyQualifiedTypeName
import org.coursera.courier.grammar.CourierGrammar.LiteralDefault
import org.coursera.courier.grammar.CourierGrammar.MapDeclaration
import org.coursera.courier.grammar.CourierGrammar.NamedTypeDeclaration
import org.coursera.courier.grammar.CourierGrammar.NilDefault
import org.coursera.courier.grammar.CourierGrammar.Property
import org.coursera.courier.grammar.CourierGrammar.RecordDeclaration
import org.coursera.courier.grammar.CourierGrammar.SimpleTypeName
import org.coursera.courier.grammar.CourierGrammar.TypeAssignment
import org.coursera.courier.grammar.CourierGrammar.TypeDeclaration
import org.coursera.courier.grammar.CourierGrammar.TyperefDeclaration
import org.coursera.courier.grammar.CourierGrammar.UnionDeclaration

import scala.collection.JavaConverters._

class CourierSchemaParser(resolver: DataSchemaResolver) extends SchemaParser(resolver) {
  // TODO(jbetz): Gahhh! We will submit pull request to rest.li so we can remove this hack.
  val _topLevelDataSchemasField = classOf[SchemaParser].getDeclaredField("_topLevelDataSchemas")
  _topLevelDataSchemasField.setAccessible(true)
  val topLevelSchemas = _topLevelDataSchemasField.get(this).asInstanceOf[java.util.List[DataSchema]]

  /**
   * Parse a JSON representation of a schema from an {{java.io.InputStream}}.
   *
   * The top level {{DataSchema}}'s parsed are in {{#topLevelDataSchemas}}.
   * These are the types that are not defined within other types.
   * Parse errors are in {{#errorMessageBuilder}} and indicated
   * by {{#hasError()}}.
   *
   * @param inputStream with the JSON representation of the schema.
   */
  override def parse(inputStream: InputStream) {
    parse(new InputStreamReader(inputStream))
  }

  /**
   * Parse a JSON representation of a schema from a {{java.io.Reader}}.
   *
   * The top level {{DataSchema}}'s parsed are in {{#topLevelDataSchemas}}.
   * These are the types that are not defined within other types.
   * Parse errors are in {{#errorMessageBuilder}} and indicated
   * by {{#hasError()}}.
   *
   * @param reader with the JSON representation of the schema.
   */
  override def parse(reader: Reader) {
    try {
      val document = CourierGrammar.parse(reader)
      parse(document)
    } catch {
      case e: CourierParseError =>
        val message = s"${e.error} [Source: ${getLocation.getSourceFile.getAbsolutePath}; " +
          s"line: ${e.line}, column: ${e.column}]"
        startErrorMessage().append(message)
        System.err.println(message)
      case io: IOException =>
        val message = s"${io.getMessage} [Source: ${getLocation.getSourceFile.getAbsolutePath}]"
        startErrorMessage().append(message)
        System.err.println(message)
    }
  }

  /**
   * Parse a representation of a schema from source
   *
   * The top level {{DataSchema}'s parsed are in {{#topLevelDataSchemas}.
   * These are the types that are not defined within other types.
   * Parse errors are in {{#errorMessageBuilder} and indicated
   * by {{#hasError()}.
   *
   * @param source with the source code representation of the schema.
   */
  override def parse(source: String) {
    parse(new StringReader(source))
  }

  /**
   * Parse list of Data objects.
   *
   * The {{DataSchema}'s parsed are in {{#topLevelDataSchemas}.
   * Parse errors are in {{#errorMessageBuilder} and indicated
   * by {{#hasError()}.
   *
   * @param document provides the source code in AST form
   */
  def parse(document: Document) {
    setCurrentNamespace(document.namespace.value)
    val schema: DataSchema = parseNamedType(document.decl)
    topLevelSchemas.add(schema)
  }

  def parseType(typ: TypeDeclaration): DataSchema = {
    typ match {
      case named: NamedTypeDeclaration => parseNamedType(named)
      case union: UnionDeclaration => parseUnion(union)
      case map: MapDeclaration => parseMap(map)
      case array: ArrayDeclaration => parseArray(array)
    }
  }

  def parseNamedType(namedType: NamedTypeDeclaration): NamedDataSchema = {
    namedType match {
      case record: RecordDeclaration => parseRecord(record)
      case typeref: TyperefDeclaration => parseTyperef(typeref)
      case fixed: FixedDeclaration => parseFixed(fixed)
      case enum: EnumDeclaration => parseEnum(enum)
    }
  }

  def parseFixed(fixed: FixedDeclaration): FixedDataSchema = {
    val name = toName(fixed.name)
    val schema = new FixedDataSchema(name)

    bindNameToSchema(name, schema)

    schema.setSize(fixed.width.toInt, errorMessageBuilder)
    setAnnotations(fixed, schema)
    schema
  }

  def parseEnum(enum: EnumDeclaration): EnumDataSchema = {
    val name = toName(enum.name)
    val schema = new EnumDataSchema(name)

    bindNameToSchema(name, schema)

    schema.setSymbols(enum.symbols.map(_.name).asJava, errorMessageBuilder)
    val props = setAnnotations(enum, schema)
    val symbolDocs = enum.symbols.flatMap { symbol =>
      symbol.annotations.schemaDoc.map(doc => symbol.name -> doc.markdown)
    }.toMap
    if (symbolDocs.nonEmpty) {
      props.put("symbolDocs", new DataMap(symbolDocs.asJava))
    }

    def propsWhere(condition: Property => Boolean) = enum.symbols.flatMap { symbol =>
      symbol.annotations.props.collect {
        case p: Property if condition(p) => symbol.name -> p.jsValue
      }.toMap
    }.toMap

    val deprecatedSymbols = propsWhere(_.key == "deprecated")
    val symbolProperties = propsWhere(_.key != "deprecated")

    if (symbolProperties.nonEmpty) {
      props.put("symbolProperties", new DataMap(symbolProperties.asJava))
    }

    if (deprecatedSymbols.nonEmpty) {
      props.put("deprecatedSymbols", new DataMap(deprecatedSymbols.asJava))
    }

    schema.setProperties(props)
    schema
  }

  def parseTyperef(typeref: TyperefDeclaration): TyperefDataSchema = {
    val name = toName(typeref.name)
    val schema = new TyperefDataSchema(name)

    bindNameToSchema(name, schema)

    schema.setReferencedType(toDataSchema(typeref.ref))
    setAnnotations(typeref, schema)

    schema
  }

  def parseArray(array: ArrayDeclaration): ArrayDataSchema = {
    new ArrayDataSchema(toDataSchema(array.items))
  }

  def parseMap(map: MapDeclaration): MapDataSchema = {
    val schema = new MapDataSchema(toDataSchema(map.values))
    val propsToAdd = new java.util.HashMap[String, AnyRef]()

    // TODO(jbetz): Add map keys. It's important to retain the original structure of the map key.
    // If the key is a fqn, put the fqn in the properties, if it's an inline declaration, put that
    // in the properties (that will require using SchemaToJsonEncoder).

    schema.setProperties(propsToAdd)
    schema
  }

  def parseUnion(union: UnionDeclaration): UnionDataSchema = {
    val schema = new UnionDataSchema()

    val types = union.members.map { memberType =>
      toDataSchema(memberType)
    }
    schema.setTypes(types.asJava, errorMessageBuilder)
    schema
  }

  def parseRecord(record: RecordDeclaration): RecordDataSchema = {
    val name = toName(record.name)
    val schema = new RecordDataSchema(name, RecordDataSchema.RecordType.RECORD)

    bindNameToSchema(name, schema)

    schema.setFields(parseFields(schema, record.fields).asJava, errorMessageBuilder)
    setAnnotations(record, schema)

    topLevelSchemas.add(schema)
    schema
  }

  def setAnnotations(
      source: NamedTypeDeclaration, target: NamedDataSchema): java.util.HashMap[String, AnyRef] = {
    val properties = new java.util.HashMap[String, AnyRef]()
    properties.putAll(target.getProperties)

    source.annotations.schemaDoc.foreach { schemaDoc =>
      target.setDoc(schemaDoc.markdown)
    }
    source.annotations.props.foreach { props =>
      properties.putAll(props.jsObject)
    }
    target.setProperties(properties)
    properties
  }

  def parseFields(
      recordSchema: RecordDataSchema, fields: FieldsSection): Seq[RecordDataSchema.Field] = {
    fields.elements.map {
      case field: FieldDeclaration =>
        val fieldType = toDataSchema(field.typ)
        val result = new Field(fieldType)
        val properties = new java.util.HashMap[String, AnyRef]()
        result.setName(field.name.value, errorMessageBuilder)
        result.setOptional(field.isOptional)
        field.default.foreach {
          case NilDefault =>
            properties.put("defaultNone", Boolean.box(true))
          case LiteralDefault(literal) => result.setDefault(literal.literal)
        }
        field.annotations.props.foreach { props =>
          properties.putAll(props.jsObject)
        }
        field.annotations.schemaDoc.foreach { schemaDoc =>
          result.setDoc(schemaDoc.markdown)
        }
        result.setRecord(recordSchema)

        result.setProperties(properties)
        result
      case fieldInclude: FieldInclude => ??? // TODO(jbetz): Add support for field includes
    }
  }

  private def toDataSchema(typeAssignment: TypeAssignment): DataSchema = {
    typeAssignment match {
      case AssignmentByName(typeName) =>
        typeName match {
          case fqn: FullyQualifiedTypeName =>
            Option(stringToDataSchema(fqn.fullname))
              .getOrElse(throw new IOException(errorMessage()))
          case SimpleTypeName(name) =>
            Option(stringToDataSchema(name))
              .getOrElse(throw new IOException(errorMessage()))
        }
      case AssignmentByInlineDeclaration(typeDeclaration) =>
        parseType(typeDeclaration)
    }
  }

  private def toName(name: SimpleTypeName): Name = {
    new Name(name.value, getCurrentNamespace, errorMessageBuilder)
  }
}
