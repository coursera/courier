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
import com.linkedin.data.schema.PrimitiveDataSchema
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec
import org.coursera.courier.generator.TypeConversions

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
case class ClassDefinition(override val spec: ClassTemplateSpec) extends Definition(spec) with MaybeBoxable {
  def schema: Option[DataSchema] = Option(spec.getSchema)

  override def scalaType = schema.collect {
    case p: PrimitiveDataSchema => TypeConversions.lookupScalaType(p)
  }.getOrElse(spec.getClassName)

  override def dataType = schema.collect {
    case p: PrimitiveDataSchema => TypeConversions.lookupJavaClass(p).getName
  }.getOrElse(spec.getClassName)

  override def namespace = Option(spec.getNamespace)
  def scalaDoc = None
  def directReferencedTypes: Set[Definition] = Set.empty
}
