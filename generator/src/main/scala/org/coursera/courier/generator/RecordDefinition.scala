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

import _root_.twirl.api.Txt
import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec.Field
import scala.collection.JavaConverters._

case class RecordDefinition(spec: RecordTemplateSpec) extends Definition(spec) {
  def schema: RecordDataSchema = spec.getSchema

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

  def scalaDoc: Option[String] = Option(schema.getDoc).flatMap(ScaladocEscaping.stringToScaladoc)
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
        case primitive: PrimitiveDefinition if TypeConversions.isScalaValueType(primitive.schema) =>
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
  def scalaDoc = Option(schemaField.getDoc).flatMap(ScaladocEscaping.stringToScaladoc)
}
