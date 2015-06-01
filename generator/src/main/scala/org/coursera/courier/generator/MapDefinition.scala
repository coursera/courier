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

import com.linkedin.data.schema.MapDataSchema
import com.linkedin.pegasus.generator.spec.MapTemplateSpec

case class MapDefinition(spec: MapTemplateSpec) extends Definition(spec) {
  def schema: MapDataSchema = spec.getSchema
  def scalaDoc: Option[String] = None

  def valueClass: Definition = Definition(spec.getValueClass)
  def valueDataClass: Option[Definition] = Option(spec.getValueDataClass).map(Definition(_))
  def customInfo: Option[CustomInfoDefinition] = Option(spec.getCustomInfo).map(CustomInfoDefinition)
}

object MapDefinition {

  /**
   * For use when creating MapDefinition for pre-defined maps such as IntMap.
   */
  def forPredef(
      scalaType: String,
      namespace: String,
      primitiveDef: PrimitiveDefinition,
      schema: MapDataSchema): MapDefinition = {

    val spec = new MapTemplateSpec(schema)
    spec.setClassName(scalaType)
    spec.setNamespace(namespace)
    spec.setValueClass(primitiveDef.spec)
    MapDefinition(spec)
  }
}
