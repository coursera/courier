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

package org.coursera.courier.templates

import com.linkedin.data.DataMap
import com.linkedin.data.schema.EnumDataSchema

/**
 * Extends a Scala Enumeration, providing access to the Courier schema and it's properties.
 */
abstract class ScalaEnumerationTemplate extends Enumeration {
  import ScalaEnumerationTemplate._

  def SCHEMA: EnumDataSchema

  def fromString(s: String): TemplateValue

  lazy val properties: Option[DataMap] = {
    Option(SCHEMA.getProperties.get(symbolProperties)).collect {
      case enumProps: DataMap => enumProps
    }
  }

  case class TemplateValue(name: String) extends Val(name) {
    override def compare(that: Value): Int = {
      that match {
        case templateValue: TemplateValue => this.name.compare(templateValue.name)
        case value: Value => this.id.compare(that.id)
      }
    }

    lazy val properties: Option[DataMap] = {
      ScalaEnumerationTemplate.this.properties.map { enumProps =>
        Option(enumProps.get(name)) match {
          case Some(symbolProps: DataMap) => symbolProps
          case other: Any =>
            throw new IllegalArgumentException(
              s"Malformed schema properties. '$name' in '$symbolProperties' must be a DataMap, " +
              s"but is a ${other.getClass}.")
        }
      }
    }

    def property(name: String): Option[AnyRef] = {
      properties.map(_.get(name))
    }
  }
}

object ScalaEnumerationTemplate {
  private val symbolProperties = "symbolProperties"
}
