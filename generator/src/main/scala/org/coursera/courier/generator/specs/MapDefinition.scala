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
import com.linkedin.data.schema.DataSchemaConstants
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.pegasus.generator.spec.PrimitiveTemplateSpec
import org.coursera.courier.api.CourierMapTemplateSpec

case class MapDefinition(override val spec: CourierMapTemplateSpec) extends Definition(spec) {
  import MapDefinition._

  def mapSchema: MapDataSchema = spec.getSchema
  def schema: Option[MapDataSchema] = Some(mapSchema)
  def scalaDoc: Option[String] = None

  def valueClass: Definition = Definition(spec.getValueClass)
  def valueDataClass: Option[Definition] = Option(spec.getValueDataClass).map(Definition(_))
  def customInfo: Option[CustomInfoDefinition] = {
    Option(spec.getCustomInfo).map(CustomInfoDefinition)
  }

  private def declaredKeyClass: Option[Definition] = Option(spec.getKeyClass).map(Definition(_))
  def keyClass: Definition = declaredKeyClass.getOrElse(stringDef)
  def keyDataClass: Option[Definition] = Option(spec.getKeyDataClass).map(Definition(_))

  def keyCustomInfo: Option[CustomInfoDefinition] = {
    Option(spec.getKeyCustomInfo).map(CustomInfoDefinition)
  }

  def keySchema: DataSchema = {
    if (keyCustomInfo.isDefined) {
      keyCustomInfo.get.customSchema
    } else {
      Option(spec.getKeyClass)
        .flatMap(k => Option(k.getSchema)).getOrElse(DataSchemaConstants.STRING_DATA_SCHEMA)
    }
  }

  def customInfos: Seq[CustomInfoDefinition] = Seq(customInfo, keyCustomInfo).flatten
  def directReferencedTypes: Set[Definition] = Set(Some(valueClass), declaredKeyClass).flatten
}

object MapDefinition {

  val stringDef = PrimitiveDefinition(PrimitiveTemplateSpec.getInstance(DataSchema.Type.STRING))

  /**
   * For use when creating MapDefinition for pre-defined maps such as IntMap.
   */
  def forPredef(
      scalaType: String,
      namespace: String,
      primitiveDef: PrimitiveDefinition,
      schema: MapDataSchema,
      keyPrimitiveDef: Option[PrimitiveDefinition] = None): MapDefinition = {

    val spec = new CourierMapTemplateSpec(schema)
    spec.setClassName(scalaType)
    spec.setNamespace(namespace)
    spec.setValueClass(primitiveDef.spec)
    keyPrimitiveDef.foreach { keyDef =>
      spec.setKeyClass(keyDef.spec)
    }
    MapDefinition(spec)
  }
}
