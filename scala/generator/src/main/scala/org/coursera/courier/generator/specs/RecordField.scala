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

import com.linkedin.data.DataMap
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec.Field
import org.coursera.courier.generator.CourierEscaping
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
  def dataClass: Definition = {
    customInfo.map(_.dereferencedType)
      .orElse {
        Option(field.getDataClass).map(Definition(_))
      }.getOrElse {
        typ
      }
  }

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
    val trimmedExpr = Txt(expr.body.trim)
    if (isOptional) {
      typ match {
        case primitive: PrimitiveDefinition
            if TypeConversions.isScalaValueType(primitive.primitiveSchema) =>
          Txt(s"Option($trimmedExpr).map(${primitive.maybeUnbox(Txt("_"))})")
        case _: Any =>
          Txt(s"Option($trimmedExpr)")
      }
    } else {
      trimmedExpr
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
    val trimmedRef = Txt(ref.body.trim)
    if (isOptional) {
      Txt(s"Option($trimmedRef).map(value => ${f(Txt("value"))})")
    } else {
      f(trimmedRef)
    }
  }

  def schemaField = field.getSchemaField

  /**
   * Escaped name for use in scala source.
   */
  def name = {
    CourierEscaping.escapeReservedClassField(
      ScalaEscaping.escapeSymbol(schemaField.getName))
  }

  /**
   * Unescaped name.
   */
  def pegasusName = schemaField.getName

  def isOptional = schemaField.getOptional

  def omit: Boolean =
    scalaProperties.exists(props => Option(props.getBoolean("omit")).exists(_.booleanValue()))

  /**
   * Custom property supported by Courier. If "explicit" is true for an optional value,
   * it will not be defaulted to None in generated data bindings.
   *
   * This is useful in Scala, because Scala supports default values on parameters, allowing
   * defaults to be provided by the generator to record type "constructors" (apply methods really).
   */
  val explicit = {
    properties.get("explicit") == Some(java.lang.Boolean.TRUE)
  }

  def default: Option[FieldDefault] = {
    val maybeDefault = Option(schemaField.getDefault)

    if (explicit) {
      assert(isOptional, s"Field '$name' marked as 'explicit' must be 'optional'")
      assert(maybeDefault.isEmpty,
        "Optional field '$name' may have either 'default' or 'explicit', not both.")
    }

    (maybeDefault, explicit, isOptional) match {
      // Required field with a default value
      case (Some(defaultValue), _, false) => Some(RequiredFieldDefault(defaultValue))
      // Optional field with some default value
      case (Some(defaultValue), false, true) => Some(OptionalFieldDefault(Some(defaultValue)))
      // Optional fied with a default value of None
      case (None, false, true) => Some(OptionalFieldDefault(None))
      // does not have a default value
      case _ => None
    }
  }
  def properties: Map[String, AnyRef] = {
    schemaField.getProperties.asScala.toMap
  }

  def scalaProperties: Option[DataMap] = {
    properties.get("scala").collect { case dataMap: DataMap => dataMap }
  }

  def scalaDoc = Option(schemaField.getDoc).flatMap(ScaladocEscaping.stringToScaladoc)
}
