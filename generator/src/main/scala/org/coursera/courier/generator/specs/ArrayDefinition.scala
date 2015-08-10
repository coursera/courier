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

import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.pegasus.generator.spec.ArrayTemplateSpec

case class ArrayDefinition(override val spec: ArrayTemplateSpec) extends Definition(spec) {
  def arraySchema: ArrayDataSchema = spec.getSchema
  def schema: Option[ArrayDataSchema] = Some(arraySchema)
  def scalaDoc: Option[String] = None

  def itemClass: Definition = Definition(spec.getItemClass)
  def itemDataClass: Option[Definition] = Option(spec.getItemDataClass).map(Definition(_))
  def directReferencedTypes: Set[Definition] = Set(itemClass)

  def customInfo: Option[CustomInfoDefinition] = {
    Option(spec.getCustomInfo).map(CustomInfoDefinition)
  }
}

object ArrayDefinition {

  /**
   * For use when creating ArrayDefinitions for pre-defined arrays such as IntArray.
   */
  def forPredef(
      scalaType: String,
      namespace: String,
      primitiveDef: PrimitiveDefinition,
      schema: ArrayDataSchema): ArrayDefinition = {
    val spec = new ArrayTemplateSpec(schema)
    spec.setClassName(scalaType)
    spec.setNamespace(namespace)
    spec.setItemClass(primitiveDef.spec)
    ArrayDefinition(spec)
  }
}
