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

package org.coursera.courier.generator.specs

import com.linkedin.pegasus.generator.spec.RecordTemplateSpec.Field
import org.coursera.courier.generator.ScalaEscaping
import org.coursera.courier.generator.ScaladocEscaping
import org.coursera.courier.generator.TypeConversions
import play.twirl.api.Txt
import scala.collection.JavaConverters._

trait FieldDefault
case class RequiredFieldDefault(default: AnyRef) extends FieldDefault
case class OptionalFieldDefault(default: Option[AnyRef]) extends FieldDefault

/**
 * The field of a record, may be either a field directly defined in the record or an "include"
 * field.
 */
case class RecordField(field: Field) extends Deprecatable {
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
  def enclosingClass: Option[Definition] = {
    Option(field.getType.getEnclosingClass).map(Definition(_))
  }

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
        case primitive: PrimitiveDefinition
            if TypeConversions.isScalaValueType(primitive.primitiveSchema) =>
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
  def name = ScalaEscaping.escapeSymbol(schemaField.getName)

  /**
   * Unescaped name.
   */
  def pegasusName = schemaField.getName

  def isOptional = schemaField.getOptional

  /**
   * Custom property supported by Courier. If "defaultNone" is true for an optional value,
   * it's default value in Scala will be None.
   *
   * This is useful in Scala, because Scala supports default values on parameters, allowing
   * defaults to be provided by the generator to record type "constructors" (apply methods really).
   */
  val isDefaultNone = {
    properties.get("defaultNone") == Some(java.lang.Boolean.TRUE)
  }

  def default: Option[FieldDefault] = {
    val maybeDefault = Option(schemaField.getDefault)

    if (isDefaultNone) {
      assert(isOptional, s"Field '$name' marked as 'defaultNone' must be 'optional'")
      assert(!maybeDefault.isDefined,
        "Optional field '$name' may have either 'default' or 'defaultNone', not both.")
    }

    (maybeDefault, isDefaultNone, isOptional) match {
      // Required field with a default value
      case (Some(defaultValue), false, false) => Some(RequiredFieldDefault(defaultValue))
      // Optional field with some default value
      case (Some(defaultValue), false, true) => Some(OptionalFieldDefault(Some(defaultValue)))
      // Optional fied with a default value of None
      case (None, true, true) => Some(OptionalFieldDefault(None))
      // does not have a default value
      case _ => None
    }
  }
  def properties: Map[String, AnyRef] = {
    schemaField.getProperties.asScala.toMap
  }

  def scalaDoc = Option(schemaField.getDoc).flatMap(ScaladocEscaping.stringToScaladoc)
}
