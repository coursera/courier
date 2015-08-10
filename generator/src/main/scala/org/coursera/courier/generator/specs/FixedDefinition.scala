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

import com.linkedin.data.schema.FixedDataSchema
import com.linkedin.pegasus.generator.spec.FixedTemplateSpec
import org.coursera.courier.generator.ScaladocEscaping

case class FixedDefinition(override val spec: FixedTemplateSpec) extends Definition(spec) {
  def fixedSchema: FixedDataSchema = spec.getSchema
  def schema: Option[FixedDataSchema] = Some(fixedSchema)

  def scalaDoc: Option[String] = {
    Option(fixedSchema.getDoc).flatMap(ScaladocEscaping.stringToScaladoc)
  }

  def directReferencedTypes: Set[Definition] = Set.empty
}
