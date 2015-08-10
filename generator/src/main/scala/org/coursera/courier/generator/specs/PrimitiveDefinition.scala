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

import com.linkedin.data.schema.PrimitiveDataSchema
import com.linkedin.pegasus.generator.spec.PrimitiveTemplateSpec
import org.coursera.courier.generator.TypeConversions

case class PrimitiveDefinition(override val spec: PrimitiveTemplateSpec)
  extends Definition(spec) with MaybeBoxable {

  def primitiveSchema: PrimitiveDataSchema = spec.getSchema
  def schema: Option[PrimitiveDataSchema] = Some(spec.getSchema)

  /**
   * The scala type used to represent the primitive. E.g. `Int`.
   */
  override def scalaType: String = TypeConversions.lookupScalaType(spec.getSchema)

  override def namespace: Option[String] = Option(spec.getNamespace)

  /**
   * The java class used by pegasus for the primitive. E.g. `java.lang.Integer`.
   *
   * Pegasus always uses java boxed primitive classes.
   */
  override def dataType: String = TypeConversions.lookupJavaClass(spec.getSchema).getName
  def scalaDoc: Option[String] = None

  /**
   * The pegasus name of the primitive type.  E.g. `int`.
   */
  def pegasusType = TypeConversions.lookupPegasusType(primitiveSchema)

  def directReferencedTypes: Set[Definition] = Set.empty
}
