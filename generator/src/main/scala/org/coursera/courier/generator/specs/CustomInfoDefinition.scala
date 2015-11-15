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
import com.linkedin.data.schema.TyperefDataSchema
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec
import com.linkedin.pegasus.generator.spec.CustomInfoSpec
import org.coursera.courier.coercers.SingleElementCaseClassCoercer

case class CustomInfoDefinition(spec: CustomInfoSpec) {
  def coercerClass = Option(spec.getCoercerClass).map(ClassDefinition)
  def customClass = ClassDefinition(spec.getCustomClass)
  def customSchema = spec.getCustomSchema
  def sourceSchema = spec.getSourceSchema

  def dereferencedType = {
    Definition(ClassTemplateSpec.createFromDataSchema(sourceSchema.getDereferencedDataSchema))
  }

  /**
   * Returns all custom infos to register to initialize this custom info.
   *
   * When `SingleElementCaseClassCoercer` is used, custom infos may depend on other custom infos.
   *
   * Dependent custom info are returned before custom infos that depend on them.
   */
  def customInfosToRegister: List[CustomInfoDefinition] = {
    sourceSchema match {
      case typerefSchema: TyperefDataSchema =>
        typerefChain(typerefSchema).map(t => TyperefDefinition.customInfo(sourceSchema, t))
          .takeWhile(_.isDefined).flatten.reverse
      case _: AnyRef => Nil
    }
  }

  private[this] def typerefChain(typerefSchema: TyperefDataSchema): List[TyperefDataSchema] = {
    typerefSchema.getRef match {
      case refTyperefSchema: TyperefDataSchema => typerefSchema :: typerefChain(refTyperefSchema)
      case _: DataSchema => typerefSchema :: Nil
    }
  }
}
