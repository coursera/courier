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

import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec
import org.coursera.courier.generator.ScaladocEscaping
import scala.collection.JavaConverters._

case class RecordDefinition(spec: RecordTemplateSpec) extends Definition(spec) {
  def schema: RecordDataSchema = spec.getSchema

  def fields: Seq[RecordField] = spec.getFields.asScala.map(RecordField).toSeq

  // parameter list rendering utilities
  def fieldParamDefs: String = {
    fields.map { field =>
      s"${field.name}: ${field.scalaTypeFullname}"
    }.mkString(", ")
  }

  def copyFieldParamDefs: String = {
    fields.map { field =>
      s"${field.name}: ${field.scalaTypeFullname} = this.${field.name}"
    }.mkString(", ")
  }

  def fieldsAsParams: String = {
    fields.map(_.name).mkString(", ")
  }

  def fieldsAsTypeParams: String = {
    fields.map(_.scalaTypeFullname).mkString(", ")
  }

  def prefixedFieldParams(prefix: String): String = {
    fields.map(field => s"$prefix${field.name}").mkString(", ")
  }

  def scalaDoc: Option[String] = Option(schema.getDoc).flatMap(ScaladocEscaping.stringToScaladoc)
}
