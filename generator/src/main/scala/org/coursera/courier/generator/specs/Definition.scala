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

import com.linkedin.data.schema.DataSchema
import com.linkedin.pegasus.generator.spec.ArrayTemplateSpec
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec
import com.linkedin.pegasus.generator.spec.EnumTemplateSpec
import com.linkedin.pegasus.generator.spec.FixedTemplateSpec
import com.linkedin.pegasus.generator.spec.PrimitiveTemplateSpec
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec
import com.linkedin.pegasus.generator.spec.TyperefTemplateSpec
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec
import org.coursera.courier.api.CourierMapTemplateSpec
import org.coursera.courier.generator.CourierPredef
import org.coursera.courier.generator.ScalaEscaping

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
abstract class Definition(val spec: ClassTemplateSpec) extends Deprecatable {

  /**
   * The schema of the type. Not present for definitions of "raw" classes, such as the 'coercer'
   * and 'class' of a custom type.
   */
  def schema: Option[DataSchema]

  /**
   * The scala type without namespace.
   *
   * For complex types, this is the name of the generated data binding class.
   *
   * For primitive types, this is the Scala native type, e.g. "Int".
   */
  def scalaType: String = ScalaEscaping.escapeSymbol(spec.getClassName)


  /**
   * The namespace of the scala type, if any.
   *
   * Only present for complex types.
   */
  def namespace: Option[String] = Option(spec.getNamespace).flatMap { ns =>
    if (ns.trim.isEmpty) None else Some(ns)
  }

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

  /**
   * The containing type of this type, if any.
   *
   * When a type is contained in another type, it should be generated as a subtype of that type.
   *
   * Unions are commonly generated as contained types.
   */
  def enclosingDefinition: Option[Definition] = Option(spec.getEnclosingClass).map(Definition(_))

  def isTopLevel: Boolean = {
    val isContained = enclosingDefinition.isDefined
    val isComplex = schema match {
      case Some(schema) => schema.isComplex
      case None => false
    }
    !isContained && isComplex
  }

  def properties: Map[String, AnyRef] = {
    schema.map(_.getProperties.asScala.toMap).getOrElse(Map.empty)
  }

  /**
   * All types that should be generated as types inside this type.
   */
  def containedTypes: Set[Definition] = {
    allReferencedTypes.filter(_.enclosingDefinition == Some(this))
  }

  /**
   * Return types this type references. For a record, this will return the types of all the fields.
   * For a array, this will return the items type.
   */
  def directReferencedTypes: Set[Definition]

  /**
   * Return all types directly or transitively referenced by this type.
   */
  def allReferencedTypes: Set[Definition] = {
    findAllReferencedTypes(directReferencedTypes)
  }

  // traverses the directReferencedTypes graph, keeping track of already visited definitions
  private def findAllReferencedTypes(
    current: Set[Definition],
    visited: Set[Definition] = Set.empty,
    acc: Set[Definition] = Set.empty): Set[Definition] = {
    val nextUnvisited = current.flatMap(_.directReferencedTypes).filterNot(visited.contains)


    if (nextUnvisited.nonEmpty) {
      findAllReferencedTypes(nextUnvisited, current ++ visited, acc ++ current)
    } else {
      acc ++ current
    }
  }

  // All definitions must be uniquely identified by the Scala type name we use in generated source,
  // so we also use that for hashcode and equals.
  override def hashCode: Int = {
    scalaTypeFullname.hashCode
  }

  override def equals(other: Any): Boolean = {
    other match {
      case otherDefinition: Definition =>
        this.scalaTypeFullname.equals(otherDefinition.scalaTypeFullname)
      case _: Any => false
    }
  }
}

object Definition {
  def apply(spec: ClassTemplateSpec): Definition = {
    assert(spec != null)

    spec match {
      case predef: ClassTemplateSpec if CourierPredef.bySchema.contains(predef.getSchema) =>
        CourierPredef.bySchema(predef.getSchema)
      case record: RecordTemplateSpec => RecordDefinition(record)
      case union: UnionTemplateSpec => UnionDefinition(union)
      case enum: EnumTemplateSpec => EnumDefinition(enum)
      case array: ArrayTemplateSpec => ArrayDefinition(array)
      case map: CourierMapTemplateSpec => MapDefinition(map)
      case typeref: TyperefTemplateSpec => TyperefDefinition(typeref)
      case fixed: FixedTemplateSpec => FixedDefinition(fixed)
      case primitive: PrimitiveTemplateSpec => PrimitiveDefinition(primitive)
      case rawClass: ClassTemplateSpec => ClassDefinition(rawClass)
    }
  }
}
