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

import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec
import org.coursera.courier.generator.ScaladocEscaping
import org.coursera.courier.generator.TypeConversions
import scala.collection.JavaConverters._

case class RecordDefinition(override val spec: RecordTemplateSpec) extends Definition(spec) {
  def recordSchema: RecordDataSchema = spec.getSchema
  def schema: Option[RecordDataSchema] = Some(recordSchema)

  override def rawDataType = classOf[DataMap].getSimpleName

  def fields: Seq[RecordField] = spec.getFields.asScala.map(RecordField).filter(!_.omit).toSeq

  def directReferencedTypes: Set[Definition] = fields.map(_.typ).toSet

  def customInfosToRegister: Seq[(RecordField, Seq[CustomInfoDefinition])] = {
    // customInfos only appear once per field set, so we must consider even omitted field when
    // accumulating customInfos.
    // TODO(jbetz): Improve pegasus internals to always return customInfos fields.  De-duplication
    // should happen up at routines like this one, not in the RecordTemplateSpec.
    val fieldsInclOmitted = spec.getFields.asScala.map(RecordField).toSeq

    fieldsInclOmitted.map { field =>
      field -> field.customInfo.toSeq.flatMap { customInfo =>
        customInfo.customInfosToRegister
      }
    }
  }

  // parameter list rendering utilities
  def fieldParamDefs: String = {
    fields.map { field =>
      val default = defaultLiteral(field)
      s"${field.name}: ${field.scalaTypeFullname}" + default.map(v => s" = $v").getOrElse("")
    }.mkString(", ")
  }

  private def defaultLiteral(field: RecordField): Option[String] = {
    field.default.map {
      case RequiredFieldDefault(default) =>
        toDefaultLiteral(default, field.typ)
      case OptionalFieldDefault(Some(default)) =>
        val literal = toDefaultLiteral(default, field.typ)
        s"Some($literal)"
      case OptionalFieldDefault(None) =>
        "None"
    }
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

  def scalaDoc: Option[String] = Option(recordSchema.getDoc)
    .flatMap(ScaladocEscaping.stringToScaladoc)

  private[this] def toDefaultLiteral(any: AnyRef, definition: Definition): String = {
    definition match {
      case _: PrimitiveDefinition | _: TyperefDefinition =>
        TypeConversions.anyToLiteral(any)
      case enum: EnumDefinition =>
        any match {
          case symbol: String => enumToDefault(enum, symbol)
          case _: Any =>
            throw new IllegalArgumentException("'any' must be a string for an enum")
        }
      case _: RecordDefinition | _: MapDefinition | _: UnionDefinition =>
        any match {
          case data: DataMap => dataMapToDefault(definition, data)
          case _: Any =>
            throw new IllegalArgumentException("'any' must be a DataMap for a record, map or union")
        }
      case _: ArrayDefinition =>
        any match {
          case data: DataList => dataListToDefault(definition, data)
          case _: Any =>
            throw new IllegalArgumentException("'any' must be a DataList for an array")
        }
      case customType: ClassDefinition => customTypeToDefault(customType, any)
      case fixed: FixedDefinition => ??? // TODO: support fixed types
    }
  }

  private[this] def customTypeToDefault(customType: ClassDefinition, any: AnyRef): String = {
    val value = TypeConversions.anyToLiteral(any)
    s"DataTemplateUtil.coerceOutput($value, classOf[${customType.scalaTypeFullname}])"
  }

  private[this] def enumToDefault(enum: EnumDefinition, symbol: String): String = {
    val symbolLiteral = TypeConversions.toLiteral(symbol)
    s"${enum.enumFullname}.fromString($symbolLiteral)"
  }

  // TODO(jbetz): This is an "ugly" way to generateRecord the default value. If/when we have time,
  // we should instead generateRecord the default entirely in scala types, e.g.:
  // Foo(Bar(1), Baz(2)) instead of the current
  // Foo(DataTemplates.mapLiteral("""{ "bar": 1, "baz": 2 }"""), ...)
  private[this] def dataMapToDefault(definition: Definition, data: DataMap): String = {
    val mapLiteral = TypeConversions.toLiteral(data)
    s"${definition.scalaTypeFullname}($mapLiteral, DataConversion.SetReadOnly)"
  }

  // TODO(jbetz): Same as for maps, we should improve the default representation if/when we have
  // time
  private[this] def dataListToDefault(definition: Definition, data: DataList): String = {
    val arrayLiteral = TypeConversions.toLiteral(data)
    s"${definition.scalaTypeFullname}($arrayLiteral, DataConversion.SetReadOnly)"
  }
}
