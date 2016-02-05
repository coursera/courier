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
import org.coursera.courier.companions.SchemaAware

/**
 * Extends a Scala Enumeration, providing access to the Courier schema and it's properties.
 */
abstract class ScalaEnumerationTemplate extends Enumeration with ScalaTemplate with SchemaAware {
  import ScalaEnumerationTemplate._

  override def SCHEMA: EnumDataSchema

  /**
   * Reads a string as an enumeration symbol.  This method differs from `withName(String)`:
   *
   * - Unrecognized string symbols are read as the `$UNKNOWN` enum symbol, unlike `withName(String)`
   *   which throws a `NoSuchElementException` if a unrecognized symbol is encountered.
   * - Returns a `TemplateValue`, a subtype of the `Value` type returned by `withName(String)` that
   *   contains additional Courier specific convenience methods such as `property(String)`.
   */
  def fromString(s: String): TemplateValue

  /**
   * Schema properties defined on this enum, if any.
   */
  lazy val properties: Option[DataMap] = {
    Option(SCHEMA.getProperties.get(symbolProperties)).collect {
      case enumProps: DataMap => enumProps
    }
  }

  /**
   * Enumeration value type that includes Courier specific convenience methods such as
   * `property(String)`.
   */
  case class TemplateValue(name: String) extends Val(name) {
    override def compare(that: Value): Int = {
      that match {
        case templateValue: TemplateValue => this.name.compare(templateValue.name)
        case value: Value => this.id.compare(that.id)
      }
    }

    /**
     * Schema properties defined on this enum symbol, if any.
     */
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

    /**
     * Gets a schema property defined on this enum symbol.
     *
     * @param name The name of the schema property.
     * @return The schema property if found, else None.
     */
    def property(name: String): Option[AnyRef] = {
      properties.map(_.get(name))
    }
  }
}

object ScalaEnumerationTemplate {
  private val symbolProperties = "symbolProperties"
}
