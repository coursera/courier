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

package org.coursera.courier.generator.twirl.defs

import com.linkedin.data.ByteString
import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.BooleanDataSchema
import com.linkedin.data.schema.BytesDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.DataSchemaConstants
import com.linkedin.data.schema.DoubleDataSchema
import com.linkedin.data.schema.FloatDataSchema
import com.linkedin.data.schema.IntegerDataSchema
import com.linkedin.data.schema.LongDataSchema
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.data.schema.PrimitiveDataSchema
import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.data.schema.StringDataSchema
import com.linkedin.data.template.BooleanArray
import com.linkedin.data.template.BooleanMap
import com.linkedin.data.template.BytesArray
import com.linkedin.data.template.BytesMap
import com.linkedin.data.template.DoubleArray
import com.linkedin.data.template.DoubleMap
import com.linkedin.data.template.FloatArray
import com.linkedin.data.template.FloatMap
import com.linkedin.data.template.IntegerArray
import com.linkedin.data.template.IntegerMap
import com.linkedin.data.template.LongArray
import com.linkedin.data.template.LongMap
import com.linkedin.data.template.StringArray
import com.linkedin.data.template.StringMap
import com.linkedin.pegasus.generator.spec.ArrayTemplateSpec
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec
import com.linkedin.pegasus.generator.spec.CustomInfoSpec
import com.linkedin.pegasus.generator.spec.EnumTemplateSpec
import com.linkedin.pegasus.generator.spec.FixedTemplateSpec
import com.linkedin.pegasus.generator.spec.MapTemplateSpec
import com.linkedin.pegasus.generator.spec.PrimitiveTemplateSpec
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec.Field
import com.linkedin.pegasus.generator.spec.TyperefTemplateSpec
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec
import org.coursera.courier.generator.twirl.defs.MapDefinition
import twirl.api.Txt

import scala.collection.JavaConverters._

/**
 * Common properties shared by all pegasus data binding generator utility types.
 */
trait Definition {

  /**
   * The scala type.
   *
   * For complex types, this is the name of the generated data binding class.
   *
   * For primitive types, this is the Scala native type, e.g. "Int".
   */
  def scalaType: String

  /**
   * The pegasus "data" type.
   *
   * For complex types, this is the same as scalaType.
   *
   * For primitives, this will be the java boxed type, e.g. `java.lang.Integer` whereas the
   * scalaType would be `Int`.
   */
  def dataType: String = scalaType

  /**
   * The namespace of the type, if any.
   */
  def namespace: Option[String]

  /**
   * The schema of the type.
   */
  def schema: DataSchema

  /**
   * The fully qualified name of the type.
   */
  def fqn: String = s"${namespace.map(_ + ".").getOrElse("")}$scalaType"

  def scalaDoc: Option[String]

  def memberName: String = scalaType + "Member"
}

/**
 * Pegasus provides `ClassTemplateSpec`s to "flatten" the data schemas provided to a
 * data binding generator and make it easier to write the generator.
 *
 * These Definitions classes wrap `ClassTemplateSpec`s so that we can use idiomatic scala types
 * in our templates.  They also add language specific escaping and convenience functions
 * specifically for generating data bindings for Scala.
 */
object Definition {
  def apply(spec: ClassTemplateSpec): Definition = {
    assert(spec != null)
    spec match {
      case predef if ScalaTypes.predef.contains(predef.getSchema) =>
        ScalaTypes.predef(predef.getSchema)
      case record: RecordTemplateSpec => RecordDefinition(record)
      case union: UnionTemplateSpec => UnionDefinition(union)
      case enum: EnumTemplateSpec => EnumDefinition(enum)
      case array: ArrayTemplateSpec => ArrayDefinition(array)
      case map: MapTemplateSpec => MapDefinition(map)
      case typeref: TyperefTemplateSpec => ???
      case fixed: FixedTemplateSpec => ???
      case primitive: PrimitiveTemplateSpec => PrimitiveDefinition(primitive)
      case rawClass: ClassTemplateSpec => ClassDefinition(rawClass)
      case _ =>
        throw new IllegalArgumentException(s"Unsupported ClassTemplateSpec type: ${spec.getClass}")
    }
  }
}

case class RecordDefinition(spec: RecordTemplateSpec) extends Definition {
  override def scalaType: String = spec.getClassName
  override def namespace: Option[String] = Option(spec.getNamespace)
  override def schema: RecordDataSchema = spec.getSchema
  def fields: Seq[RecordField] = spec.getFields.asScala.map(RecordField).toSeq

  // parameter list rendering utilities
  def fieldParamDefs = fields.map(field => s"${field.name}: ${field.fqn}").mkString(", ")
  def copyFieldParamDefs = fields.map(field => s"${field.name}: ${field.fqn} = this.${field.name}").mkString(", ")
  def fieldsAsParams = fields.map(_.name).mkString(", ")
  def fieldsAsTypeParams = fields.map(_.fqn).mkString(", ")
  def prefixedFieldParams(prefix: String) = fields.map(field => s"$prefix${field.name}").mkString(", ")

  def scalaDoc = Option(schema.getDoc).flatMap(Scaladoc.stringToScaladoc)
}

case class UnionDefinition(spec: UnionTemplateSpec) extends Definition {
  override def scalaType = spec.getClassName
  override def namespace = Option(spec.getNamespace)
  override def schema = spec.getSchema
  def scalaDoc = None
  def members = spec.getMembers.asScala.map(UnionMemberDefinition)
}

case class UnionMemberDefinition(spec: UnionTemplateSpec.Member) {
  def classDefinition = Definition(spec.getClassTemplateSpec)
  def dataClass = Definition(spec.getDataClass)
  def schema = spec.getSchema
}

case class EnumDefinition(spec: EnumTemplateSpec) extends Definition {
  override def scalaType = spec.getClassName + "." + spec.getClassName
  def enumName = spec.getClassName
  def enumFullname = s"${namespace.map(_ + ".").getOrElse("")}$enumName"
  override def namespace = Option(spec.getNamespace)
  override def schema = spec.getSchema
  def scalaDoc = Option(schema.getDoc).flatMap(Scaladoc.stringToScaladoc)
  def symbolScalaDocs = schema.getSymbolDocs.asScala.mapValues(Scaladoc.stringToScaladoc)
  def symbols = schema.getSymbols.asScala
  override def memberName = spec.getClassName + "Member"
}

case class ArrayDefinition(
    scalaType: String,
    namespace: Option[String],
    schema: ArrayDataSchema,
    scalaDoc: Option[String],
    itemClass: Definition,
    itemDataClass: Option[Definition],
    customInfo: Option[CustomInfoDefinition]) extends Definition
object ArrayDefinition {
  def apply(spec: ArrayTemplateSpec): ArrayDefinition = {
    ArrayDefinition(
      spec.getClassName,
      Option(spec.getNamespace),
      spec.getSchema,
      None,
      Definition(spec.getItemClass),
      Option(spec.getItemDataClass).map(Definition(_)),
      Option(spec.getCustomInfo).map(CustomInfoDefinition))
  }

  def forPrimitive(scalaType: String, namespace: String, primitiveDef: PrimitiveDefinition, schema: ArrayDataSchema): ArrayDefinition = {
    ArrayDefinition(
      scalaType,
      Some(namespace),
      schema,
      None,
      primitiveDef,
      Some(primitiveDef),
      None)
  }
}

case class MapDefinition(
    scalaType: String,
    namespace: Option[String],
    schema: MapDataSchema,
    scalaDoc: Option[String],
    valueClass: Definition,
    valueDataClass: Option[Definition],
    customInfo: Option[CustomInfoDefinition]) extends Definition {

}

object MapDefinition {
  def apply(spec: MapTemplateSpec): MapDefinition = {
    MapDefinition(
      spec.getClassName,
      Option(spec.getNamespace),
      spec.getSchema,
      None,
      Definition(spec.getValueClass),
      Option(spec.getValueDataClass).map(Definition(_)),
      Option(spec.getCustomInfo).map(CustomInfoDefinition))
  }
  def forPrimitive(scalaType: String, namespace: String, primitiveDef: PrimitiveDefinition, schema: MapDataSchema): MapDefinition = {
    MapDefinition(
      scalaType,
      Some(namespace),
      schema,
      None,
      primitiveDef,
      None,
      None)
  }
}

/**
 * Convenience trait for primitive types since they sometimes need to be boxed/unboxed.
 */
trait MaybeBoxable extends Definition {
  def requiresBoxing = ScalaTypes.isScalaValueType(schema)

  def maybeBox(expr: Txt): Txt = {
    if (requiresBoxing) {
      Txt(s"$scalaType.box($expr)")
    } else {
      expr
    }
  }

  def maybeUnbox(expr: Txt): Txt = {
    if (requiresBoxing) {
      Txt(s"$scalaType.unbox($expr)")
    } else {
      expr
    }
  }
}

case class PrimitiveDefinition(spec: PrimitiveTemplateSpec) extends Definition with MaybeBoxable {
  override def scalaType = ScalaTypes.scalaTypeForPrimitiveType(spec.getSchema)
  override def dataType = ScalaTypes.dataMapTypeForPrimitiveType(spec.getSchema).getName
  override def namespace = Option(spec.getNamespace)
  override def schema = spec.getSchema
  def scalaDoc = None
  def schemaType = schema match {
    case _: IntegerDataSchema => "DataSchemaConstants.INTEGER_DATA_SCHEMA"
    case _: LongDataSchema => "DataSchemaConstants.LONG_DATA_SCHEMA"
    case _: FloatDataSchema => "DataSchemaConstants.FLOAT_DATA_SCHEMA"
    case _: DoubleDataSchema => "DataSchemaConstants.DOUBLE_DATA_SCHEMA"
    case _: BooleanDataSchema => "DataSchemaConstants.BOOLEAN_DATA_SCHEMA"
    case _: StringDataSchema => "DataSchemaConstants.STRING_DATA_SCHEMA"
    case _: BytesDataSchema => "DataSchemaConstants.BYTES_DATA_SCHEMA"
  }
  def pegasusType = schema match {
    case _: IntegerDataSchema => "int"
    case _: LongDataSchema => "long"
    case _: FloatDataSchema => "float"
    case _: DoubleDataSchema => "double"
    case _: BooleanDataSchema => "boolean"
    case _: StringDataSchema => "string"
    case _: BytesDataSchema => "bytes"
  }
}

case class CustomInfoDefinition(spec: CustomInfoSpec) {
  def coercerClass = Option(spec.getCoercerClass).map(ClassDefinition)
  def customClass = ClassDefinition(spec.getCustomClass)
  def customSchema = spec.getCustomSchema
  def sourceSchema = spec.getSourceSchema
}

case class ClassDefinition(spec: ClassTemplateSpec) extends Definition with MaybeBoxable {
  override def scalaType = Option(schema).collect {
    case p: PrimitiveDataSchema => ScalaTypes.scalaTypeForPrimitiveType(p)
  }.getOrElse(spec.getClassName)

  override def dataType = Option(schema).collect {
    case p: PrimitiveDataSchema => ScalaTypes.dataMapTypeForPrimitiveType(p).getName
  }.getOrElse(spec.getClassName)

  override def namespace = Option(spec.getNamespace)
  override def schema = spec.getSchema
  def scalaDoc = None
}

case class RecordField(field: Field) {
  def customInfo: Option[CustomInfoDefinition] = Option(field.getCustomInfo).map(CustomInfoDefinition)
  def dataClass: Option[Definition] = Option(field.getDataClass).map(Definition(_))

  def typ: Definition = Definition(field.getType)
  def enclosingClass: Option[Definition] = Option(field.getType.getEnclosingClass).map(Definition(_))

  /**
   * Fields are aware of optionality, so the scalaType of a field type can be wrapped
   * with Option[] if it is an optional field.
   */
  def scalaType = if (isOptional) {
    s"Option[${typ.scalaType}]"
  } else {
    typ.scalaType
  }

  /**
   * Fields are aware of optionality, so the fully qualified name of a field type can be wrapped
   * with Option[] if it is an optional field.
   */
  def fqn = if (isOptional) {
    s"Option[${typ.fqn}]"
  } else {
    typ.fqn
  }

  /**
   * If this field is optional, wrap the provided expression with Option(expr), else return the
   * expression.
   */
  def wrapIfOption(expr: Txt): Txt = {
    if (isOptional) {
      typ match {
        case primitive: PrimitiveDefinition if ScalaTypes.isScalaValueType(primitive.schema) =>
          Txt(s"Option($expr).map(${primitive.maybeUnbox(Txt("_"))})")
        case _: Any =>
          Txt(s"Option($expr)")
      }
    } else {
      expr
    }
  }

  /**
   * If the ref is optional, apply the expression in a foreach body to the ref, else apply it
   * directly to the ref.
   */
  def applyIfOption(ref: String)(f: String => Txt): Txt = {
    if (isOptional) {
      Txt(s"$ref.foreach(value => ${f("value")})")
    } else {
      f(ref)
    }
  }

  def wrapAndMapIfOption(ref: Txt)(f: Txt => Txt): Txt = {
    if (isOptional) {
      Txt(s"Option($ref).map(value => ${f(Txt("value"))})")
    } else {
      f(ref)
    }
  }

  def schemaField = field.getSchemaField
  def name = schemaField.getName
  def doc = Option(schemaField.getDoc)
  def isOptional = schemaField.getOptional
  def default = schemaField.getDefault
  def scalaDoc = doc.flatMap(Scaladoc.stringToScaladoc)
}

object Scaladoc {
  def stringToScaladoc(raw: String): Option[String] = {
    // TODO(jbetz): Add proper escaping of comment chars and scaladoc chars (markdown and html?)
    raw.trim match {
      case empty if empty.isEmpty => None
      case nonEmpty =>
        Some(s"""/**
                | * ${escape(raw).replaceAll("\n", "\n * ")}
                | */""".stripMargin)
    }
  }

  def escape(raw: String): String = {
    raw // TODO: escape scaladoc reserved chars, incl. html tags, as well as "/** */"
  }
}

object ScalaEscaping {
  def escape(symbol: String): String = {
    // TODO: escape all language keywords
    // TODO: check for illegal symbols (symbols that start with numbers, etc..) and either escape or throw ILA
    symbol
  }
}

object ScalaTypes {

  // Ideally, we could just use classOf[Int] here, but classOf[Int].getName returns "int" and
  // we need "Int" so that we can use it to generate correct scala source.
  val scalaTypeForPrimitiveType = Map(
    DataSchemaConstants.INTEGER_DATA_SCHEMA -> "Int",
    DataSchemaConstants.LONG_DATA_SCHEMA -> "Long",
    DataSchemaConstants.FLOAT_DATA_SCHEMA -> "Float",
    DataSchemaConstants.DOUBLE_DATA_SCHEMA -> "Double",
    DataSchemaConstants.BOOLEAN_DATA_SCHEMA -> "Boolean",
    DataSchemaConstants.BYTES_DATA_SCHEMA -> "ByteString",
    DataSchemaConstants.STRING_DATA_SCHEMA -> "String",
    DataSchemaConstants.NULL_DATA_SCHEMA -> "Null")

  val dataMapTypeForPrimitiveType = Map(
    DataSchemaConstants.INTEGER_DATA_SCHEMA -> classOf[java.lang.Integer],
    DataSchemaConstants.LONG_DATA_SCHEMA -> classOf[java.lang.Long],
    DataSchemaConstants.FLOAT_DATA_SCHEMA -> classOf[java.lang.Float],
    DataSchemaConstants.DOUBLE_DATA_SCHEMA -> classOf[java.lang.Double],
    DataSchemaConstants.BOOLEAN_DATA_SCHEMA -> classOf[java.lang.Boolean],
    DataSchemaConstants.BYTES_DATA_SCHEMA -> classOf[ByteString],
    DataSchemaConstants.STRING_DATA_SCHEMA -> classOf[String]
    /* Java has no type for null */)

  val primitiveSchemas = scalaTypeForPrimitiveType.keys

  def isScalaValueType(schema: DataSchema): Boolean = {
    schema match {
      case _: IntegerDataSchema | _: LongDataSchema |
           _: FloatDataSchema | _: DoubleDataSchema |
           _: BooleanDataSchema => true
      case _: Any => false
      case null => false
    }
  }


  val dataNamespace = "org.coursera.courier.data"
  val intArraySchema = new IntegerArray().schema()
  val predef = Map[DataSchema, Definition](
    arrayPredef("IntArray", DataSchema.Type.INT),
    arrayPredef("LongArray", DataSchema.Type.LONG),
    arrayPredef("FloatArray", DataSchema.Type.FLOAT),
    arrayPredef("DoubleArray", DataSchema.Type.DOUBLE),
    arrayPredef("BooleanArray", DataSchema.Type.BOOLEAN),
    arrayPredef("StringArray", DataSchema.Type.STRING),
    arrayPredef("BytesArray", DataSchema.Type.BYTES),

    mapPredef("IntMap", DataSchema.Type.INT),
    mapPredef("LongMap", DataSchema.Type.LONG),
    mapPredef("FloatMap", DataSchema.Type.FLOAT),
    mapPredef("DoubleMap", DataSchema.Type.DOUBLE),
    mapPredef("BooleanMap", DataSchema.Type.BOOLEAN),
    mapPredef("StringMap", DataSchema.Type.STRING),
    mapPredef("BytesMap", DataSchema.Type.BYTES)
  )

  def arrayPredef(className: String, dataType: DataSchema.Type): (ArrayDataSchema, ArrayDefinition) = {
    val definition = PrimitiveDefinition(PrimitiveTemplateSpec.getInstance(dataType))
    val arraySchema = getArraySchema(dataType)

    arraySchema -> ArrayDefinition.forPrimitive(className, dataNamespace, definition, arraySchema)
  }

  def mapPredef(className: String, dataType: DataSchema.Type): (MapDataSchema, MapDefinition) = {
    val definition = PrimitiveDefinition(PrimitiveTemplateSpec.getInstance(dataType))
    val mapSchema = getMapSchema(dataType)

    mapSchema -> MapDefinition.forPrimitive(className, dataNamespace, definition, mapSchema)
  }

  def getArraySchema(dataType: DataSchema.Type): ArrayDataSchema = {
    import DataSchema.Type._
    dataType match {
      case INT => new IntegerArray().schema()
      case LONG => new LongArray().schema()
      case FLOAT => new FloatArray().schema()
      case DOUBLE => new DoubleArray().schema()
      case BOOLEAN => new BooleanArray().schema()
      case STRING => new StringArray().schema()
      case BYTES => new BytesArray().schema()
    }
  }

  def getMapSchema(dataType: DataSchema.Type): MapDataSchema = {
    import DataSchema.Type._
    dataType match {
      case INT => new IntegerMap().schema()
      case LONG => new LongMap().schema()
      case FLOAT => new FloatMap().schema()
      case DOUBLE => new DoubleMap().schema()
      case BOOLEAN => new BooleanMap().schema()
      case STRING => new StringMap().schema()
      case BYTES => new BytesMap().schema()
    }
  }
}
