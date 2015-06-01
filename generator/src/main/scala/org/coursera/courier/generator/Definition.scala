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
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.PrimitiveDataSchema
import com.linkedin.pegasus.generator.spec.ArrayTemplateSpec
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec
import com.linkedin.pegasus.generator.spec.EnumTemplateSpec
import com.linkedin.pegasus.generator.spec.FixedTemplateSpec
import com.linkedin.pegasus.generator.spec.MapTemplateSpec
import com.linkedin.pegasus.generator.spec.PrimitiveTemplateSpec
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec
import com.linkedin.pegasus.generator.spec.TyperefTemplateSpec
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec

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
abstract class Definition(spec: ClassTemplateSpec) {

  /**
   * The schema of the type.
   */
  def schema: DataSchema

  /**
   * The scala type without namespace.
   *
   * For complex types, this is the name of the generated data binding class.
   *
   * For primitive types, this is the Scala native type, e.g. "Int".
   */
  def scalaType: String = ScalaEscaping.escape(spec.getClassName)


  /**
   * The namespace of the scala type, if any.
   *
   * Only present for complex types.
   */
  def namespace: Option[String] = Option(spec.getNamespace)

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
   * Includes the opening and closing scaladoc comment tags.
   */
  def scalaDoc: Option[String]

  /**
   * The name that should be given to any Union member wrappers of this type.
   */
  def memberName: String = scalaType + "Member"

  def enclosingDefinition = Option(spec.getEnclosingClass).map(Definition(_))
}

object Definition {
  def apply(spec: ClassTemplateSpec): Definition = {
    assert(spec != null)

    spec match {
      case predef if CourierPredef.bySchema.contains(predef.getSchema) =>
        CourierPredef.bySchema(predef.getSchema)
      case record: RecordTemplateSpec => RecordDefinition(record)
      case union: UnionTemplateSpec => UnionDefinition(union)
      case enum: EnumTemplateSpec => EnumDefinition(enum)
      case array: ArrayTemplateSpec => ArrayDefinition(array)
      case map: MapTemplateSpec => MapDefinition(map)
      case typeref: TyperefTemplateSpec => TyperefDefinition(typeref)
      case fixed: FixedTemplateSpec => FixedDefinition(fixed)
      case primitive: PrimitiveTemplateSpec => PrimitiveDefinition(primitive)
      case rawClass: ClassTemplateSpec => ClassDefinition(rawClass)
    }
  }
}

/**
 * Convenience trait for primitive types since they sometimes need to be boxed/unboxed.
 */
trait MaybeBoxable extends Definition {
  def requiresBoxing = TypeConversions.isScalaValueType(schema)

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




