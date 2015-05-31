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
import com.linkedin.data.schema.EnumDataSchema
import com.linkedin.data.schema.FixedDataSchema
import com.linkedin.data.schema.FloatDataSchema
import com.linkedin.data.schema.IntegerDataSchema
import com.linkedin.data.schema.LongDataSchema
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.data.schema.PrimitiveDataSchema
import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.data.schema.StringDataSchema
import com.linkedin.data.schema.TyperefDataSchema
import com.linkedin.data.schema.UnionDataSchema
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
import twirl.api.Txt

import scala.collection.JavaConverters._

/**
 * Pegasus provides `ClassTemplateSpec`s to "flatten" the data schemas provided to a
 * data binding generator and make it easier to write the generator.
 *
 * These Definitions classes wrap `ClassTemplateSpec`s so that we can use idiomatic scala types
 * in our templates.  They also add language specific escaping and convenience functions
 * specifically for generating data bindings for Scala.
 *
 * This trait is for all Scala classes that wrap ClassTemplateSpec and it's sub-classes, it
 * represents the common properties shared by all pegasus data binding generator utility types.
 */
trait Definition {

  /**
   * The scala type without namespace.
   *
   * For complex types, this is the name of the generated data binding class.
   *
   * For primitive types, this is the Scala native type, e.g. "Int".
   */
  def scalaType: String


  /**
   * The namespace of the scala type, if any.
   *
   * Only present for complex types.
   */
  def namespace: Option[String]

  /**
   * The fully qualified name of the scala type.
   */
  def scalaTypeFullname: String = s"${namespace.map(_ + ".").getOrElse("")}$scalaType"

  /**
   * The pegasus "data" type.
   *
   * This if sometimes the same as scalaType. It will be different for custom type and for
   * primitive types.
   *
   * For custom types, it will be the referenced type.
   *
   * For primitives, this will be the java boxed type, e.g. `java.lang.Integer` whereas the
   * scalaType would be `Int`.
   */
  def dataType: String = scalaType

  /**
   * The schema of the type.
   */
  def schema: DataSchema

  /**
   * Includes the opening and closing scaladoc comment tags.
   */
  def scalaDoc: Option[String]

  /**
   * The name that should be given to any Union member wrappers of this type.
   */
  def memberName: String = scalaType + "Member"
}

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
      case typeref: TyperefTemplateSpec => TyperefDefinition(typeref)
      case fixed: FixedTemplateSpec => FixedDefinition(fixed)
      case primitive: PrimitiveTemplateSpec => PrimitiveDefinition(primitive)
      case rawClass: ClassTemplateSpec => ClassDefinition(rawClass)
      case _ =>
        throw new IllegalArgumentException(s"Unsupported ClassTemplateSpec type: ${spec.getClass}")
    }
  }
}

case class TyperefDefinition(spec: TyperefTemplateSpec) extends Definition {
  override def scalaType: String = ScalaEscaping.escape(spec.getClassName)
  override def namespace: Option[String] = Option(spec.getNamespace)
  override def schema: TyperefDataSchema = spec.getSchema
  override def scalaDoc: Option[String] = Option(schema.getDoc).flatMap(Scaladoc.stringToScaladoc)
}

case class FixedDefinition(spec: FixedTemplateSpec) extends Definition {
  override def scalaType: String = ScalaEscaping.escape(spec.getClassName)
  override def namespace: Option[String] = Option(spec.getNamespace)
  override def schema: FixedDataSchema = spec.getSchema
  override def scalaDoc: Option[String] = Option(schema.getDoc).flatMap(Scaladoc.stringToScaladoc)
}

case class RecordDefinition(spec: RecordTemplateSpec) extends Definition {
  override def scalaType: String = ScalaEscaping.escape(spec.getClassName)
  override def namespace: Option[String] = Option(spec.getNamespace)
  override def schema: RecordDataSchema = spec.getSchema
  def fields: Seq[RecordField] = spec.getFields.asScala.map(RecordField).toSeq

  // parameter list rendering utilities
  def fieldParamDefs: String = {
    fields.map { field =>
      s"${field.name}: ${field.scalaTypeFullname}"
    }.mkString(", ")
  }

  def copyFieldParamDefs: String = {
    fields.map { field =>
      s"${field.name}: ${field.scalaTypeFullname} = this.${field.name}"
    }.mkString(", ")
  }

  def fieldsAsParams: String = {
    fields.map(_.name).mkString(", ")
  }

  def fieldsAsTypeParams: String = {
    fields.map(_.scalaTypeFullname).mkString(", ")
  }

  def prefixedFieldParams(prefix: String): String = {
    fields.map(field => s"$prefix${field.name}").mkString(", ")
  }

  def scalaDoc: Option[String] = Option(schema.getDoc).flatMap(Scaladoc.stringToScaladoc)
}

case class UnionDefinition(spec: UnionTemplateSpec) extends Definition {
  /**
   * A inferred name fo the Union.
   *
   * Note that all unions are defined anonymously, so pegasus makes a best effort to give
   * them a reasonable name.
   *
   * E.g. A union defined as the type of a field of a record will be named after that field.
   */
  override def scalaType: String = ScalaEscaping.escape(spec.getClassName)
  override def namespace: Option[String] = Option(spec.getNamespace)
  override def schema: UnionDataSchema = spec.getSchema
  def scalaDoc: Option[String] = None

  /**
   * The union member types.
   */
  def members: Seq[UnionMemberDefinition] = spec.getMembers.asScala.map(UnionMemberDefinition)
}

case class UnionMemberDefinition(spec: UnionTemplateSpec.Member) {

  /**
   * The member type definition, may be any pegasus type
   * (record, primitive, enum, union, ...).
   */
  def classDefinition: Definition = Definition(spec.getClassTemplateSpec)

  /**
   * The pegasus data type of the member.
   *
   * For the select and obtain methods in UnionTemplate (be it direct, wrapped or customType),
   * this is the type that UnionTemplate expects for the "dataClass".
   */
  def dataClass: Definition = Definition(spec.getDataClass)
  def schema: DataSchema = spec.getSchema
}

case class EnumDefinition(spec: EnumTemplateSpec) extends Definition {

  /**
   * The enumeration's type.
   *
   * It's important to note that the type for the Enumerations we generate is
   * different than the enumeration object's name.
   *
   * A enumeration type is `SomeEnum.SomeEnum`, where the first `SomeEnum` is the name of the
   * enumeration scala object, and the second `SomeEnum` is a member type defined inside the
   * object for the actual type of the enumeration.
   */
  override def scalaType: String = s"$enumName.$enumName"

  /**
   * Because the scalaType is not the same as the enumeration object name, we have separate fields
   * for the enumeration object name.
   */
  def enumName: String = ScalaEscaping.escape(spec.getClassName)
  def enumFullname: String = s"${namespace.map(_ + ".").getOrElse("")}$enumName"
  override def namespace: Option[String] = Option(spec.getNamespace)

  override def schema: EnumDataSchema = spec.getSchema

  /**
   * The scaladoc for the entire enumeration.
   */
  def scalaDoc: Option[String] = Option(schema.getDoc).flatMap(Scaladoc.stringToScaladoc)

  /**
   * For enumerations, each symbol may have it's own documentation, this is provided as map
   * from symbol name to documentation string.
   */
  def symbolScalaDocs = schema.getSymbolDocs.asScala.mapValues(Scaladoc.stringToScaladoc)

  /**
   * Enumeration symbol strings, not including $UNKNOWN.
   */
  def symbols: Seq[String] = schema.getSymbols.asScala.map(ScalaEscaping.escape)

  override def memberName: String = enumName + "Member"
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
      scalaType = ScalaEscaping.escape(spec.getClassName),
      namespace = Option(spec.getNamespace),
      schema = spec.getSchema,
      scalaDoc = None,
      itemClass = Definition(spec.getItemClass),
      itemDataClass = Option(spec.getItemDataClass).map(Definition(_)),
      customInfo = Option(spec.getCustomInfo).map(CustomInfoDefinition))
  }

  /**
   * For use when creating ArrayDefinitions for pre-defined arrays such as IntArray.
   */
  def forPrimitive(
      scalaType: String,
      namespace: String,
      primitiveDef: PrimitiveDefinition,
      schema: ArrayDataSchema): ArrayDefinition = {
    ArrayDefinition(
      scalaType = ScalaEscaping.escape(scalaType),
      namespace = Some(namespace),
      schema = schema,
      scalaDoc = None,
      itemClass = primitiveDef,
      itemDataClass = Some(primitiveDef),
      customInfo = None)
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
      scalaType = ScalaEscaping.escape(spec.getClassName),
      namespace = Option(spec.getNamespace),
      schema = spec.getSchema,
      scalaDoc = None,
      valueClass = Definition(spec.getValueClass),
      valueDataClass = Option(spec.getValueDataClass).map(Definition(_)),
      customInfo = Option(spec.getCustomInfo).map(CustomInfoDefinition))
  }

  /**
   * For use when creating MapDefinition for pre-defined maps such as IntMap.
   */
  def forPrimitive(
      scalaType: String,
      namespace: String,
      primitiveDef: PrimitiveDefinition,
      schema: MapDataSchema): MapDefinition = {
    MapDefinition(
      scalaType = ScalaEscaping.escape(scalaType),
      namespace = Some(namespace),
      schema = schema,
      scalaDoc = None,
      valueClass = primitiveDef,
      valueDataClass = None,
      customInfo = None)
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

  /**
   * The scala type used to represent the primitive. E.g. `Int`.
   */
  override def scalaType: String = ScalaTypes.scalaTypeForPrimitiveType(spec.getSchema)

  /**
   * The java class used by pegasus for the primitive. E.g. `java.lang.Integer`.
   *
   * Pegasus always uses java boxed primitive classes.
   */
  override def dataType: String = ScalaTypes.dataMapTypeForPrimitiveType(spec.getSchema).getName
  override def namespace: Option[String] = Option(spec.getNamespace)
  override def schema: PrimitiveDataSchema = spec.getSchema
  override def scalaDoc: Option[String] = None

  /**
   * The pegasus name of the primitive type.  E.g. `int`.
   */
  def pegasusType = ScalaTypes.lookupPegasusTypeString(schema)
}

case class CustomInfoDefinition(spec: CustomInfoSpec) {
  def coercerClass = Option(spec.getCoercerClass).map(ClassDefinition)
  def customClass = ClassDefinition(spec.getCustomClass)
  def customSchema = spec.getCustomSchema
  def sourceSchema = spec.getSourceSchema
}

/**
 * A "raw" class definition.
 *
 * Purely a reference to a type. The type should already exist and should not be generated.
 *
 * May refer to a primitive type.
 *
 * Main Uses:
 *   A custom class
 *   A coercer for a custom class
 *   ???
 *
 */
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

/**
 * The field of a record, may be either a field directly defined in the record or an "include"
 * field.
 */
case class RecordField(field: Field) {
  /**
   * Present only if the type of a field is a custom type.
   */
  def customInfo: Option[CustomInfoDefinition] = {
    Option(field.getCustomInfo).map(CustomInfoDefinition)
  }

  /**
   * The type definition of the field, may be any pegasus type
   * (record, primitive, enum, union, ...).
   */
  def typ: Definition = Definition(field.getType)

  /**
   * The pegasus data type of the field.
   *
   * For the put and obtain methods in RecordTemplate (be it direct, wrapped or customType),
   * this is the type that RecordTemplate expects for the "dataClass".
   */
  def dataClass: Option[Definition] = Option(field.getDataClass).map(Definition(_))

  /**
   * If the field type is enclosed in another type, the enclosing class.
   *
   * When generating classes, if the enclosing type is the current type being generated, then the
   * type of this field should be generated as a subclass.
   */
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
  def scalaTypeFullname = if (isOptional) {
    s"Option[${typ.scalaTypeFullname}]"
  } else {
    typ.scalaTypeFullname
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
   * If this field is optional, apply the expression in a foreach body to the ref, else apply it
   * directly to the ref.
   */
  def applyIfOption(ref: String)(f: String => Txt): Txt = {
    if (isOptional) {
      Txt(s"$ref.foreach(value => ${f("value")})")
    } else {
      f(ref)
    }
  }

  /**
   * If this field is optional, wrap the provided ref expression with Option(ref) and then map
   * the option with the provided `f` function.
   */
  def wrapAndMapIfOption(ref: Txt)(f: Txt => Txt): Txt = {
    if (isOptional) {
      Txt(s"Option($ref).map(value => ${f(Txt("value"))})")
    } else {
      f(ref)
    }
  }

  def schemaField = field.getSchemaField

  /**
   * Escaped name for use in scala source.
   */
  def name = ScalaEscaping.escape(schemaField.getName)

  /**
   * Unescaped name.
   */
  def pegasusName = schemaField.getName

  def isOptional = schemaField.getOptional
  def default = schemaField.getDefault
  def scalaDoc = Option(schemaField.getDoc).flatMap(Scaladoc.stringToScaladoc)
}

object Scaladoc {
  def stringToScaladoc(raw: String): Option[String] = {
    raw.trim match {
      case empty if empty.isEmpty => None
      case nonEmpty =>
        Some(s"""/**
                | * ${escape(raw).replaceAll("\n", "\n * ")}
                | */""".stripMargin)
    }
  }

  def escape(raw: String): String = {
    // TODO(jbetz): Add proper escaping of comment chars and scaladoc chars (markdown and html?)
    raw
  }
}

object ScalaEscaping {
  // Avro's naming rules (https://avro.apache.org/docs/1.7.7/spec.html#Names) are fairly strict, and
  // we only need to escape valid avro names, which makes this fairly straight-forward.
  //
  // This keyword list is based on:
  // http://stackoverflow.com/questions/22037430/programatically-checking-whether-a-string-is-a-reserved-word-in-scala
  val keywords = Set(
    "abstract", "true", "val", "do", "throw", "package", "macro", "object", "false",
    "this", "if", "then", "var", "trait", ".", "catch", "with", "def", "else", "class", "type",
    "lazy", "null", "override", "protected", "private", "sealed", "finally", "new",
    "implicit", "extends", "final", "for", "return", "case", "import", "forSome", "super",
    "while", "yield", "try", "match")

  def escape(symbol: String): String = {
    if (keywords.contains(symbol)) {
      s"`$symbol`"
    } else {
      symbol
    }
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

  def lookupPegasusTypeString(dataSchema: DataSchema) = dataSchema match {
    case _: IntegerDataSchema => "int"
    case _: LongDataSchema => "long"
    case _: FloatDataSchema => "float"
    case _: DoubleDataSchema => "double"
    case _: BooleanDataSchema => "boolean"
    case _: StringDataSchema => "string"
    case _: BytesDataSchema => "bytes"
    case _ => throw new IllegalArgumentException(s"Unsupported DataSchema: ${dataSchema.getClass}")
  }

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
      case _ => throw new IllegalArgumentException(s"Unsupported DataSchema.Type: $dataType")
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
      case _ => throw new IllegalArgumentException(s"Unsupported DataSchema.Type: $dataType")
    }
  }
}
