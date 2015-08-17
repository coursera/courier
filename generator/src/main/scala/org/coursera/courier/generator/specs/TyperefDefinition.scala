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
import com.linkedin.data.schema.NamedDataSchema
import com.linkedin.data.schema.TyperefDataSchema
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec
import com.linkedin.pegasus.generator.spec.CustomInfoSpec
import com.linkedin.pegasus.generator.spec.TyperefTemplateSpec
import org.coursera.courier.generator.ScaladocEscaping

import scala.collection.JavaConverters._

case class TyperefDefinition(override val spec: TyperefTemplateSpec) extends Definition(spec) {
  def typerefSchema: TyperefDataSchema = spec.getSchema
  def schema: Option[TyperefDataSchema] = Some(typerefSchema)

  def scalaDoc: Option[String] = {
    Option(typerefSchema.getDoc).flatMap(ScaladocEscaping.stringToScaladoc)
  }

  def directReferencedTypes: Set[Definition] = Set.empty
}

object TyperefDefinition {
  def customInfo(
      sourceSchema: NamedDataSchema,
      typeref: TyperefDataSchema): Option[CustomInfoDefinition] = {

    def classInfoSpec(fullname: String) = {
      val custom = new ClassTemplateSpec()
      custom.setFullName(fullname)
      custom
    }

    typeref.getProperties.asScala.get("scala").collect {
      case dataMap: DataMap =>
        val customClass = Option(dataMap.getString("class")).map(classInfoSpec)
        val coercerClass = Option(dataMap.getString("coercerClass")).map(classInfoSpec)
        customClass.map { custom =>
          CustomInfoDefinition(
            new CustomInfoSpec(sourceSchema, typeref, custom, coercerClass.orNull))
        }
    }.flatten
  }
}
