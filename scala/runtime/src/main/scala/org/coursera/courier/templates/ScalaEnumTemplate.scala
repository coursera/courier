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
import org.coursera.common.collection.AliasedEnumSymbol
import org.coursera.common.collection.Enum
import org.coursera.courier.companions.SchemaAware

/**
 * Extends a Scala Enumeration, providing access to the Courier schema and it's properties.
 */
abstract class ScalaEnumTemplate[T <: ScalaEnumTemplateSymbol]
  extends Enum[T] with ScalaTemplate with SchemaAware {
  import ScalaEnumTemplate._

  override def SCHEMA: EnumDataSchema

  /**
   * Reads a string as an enumeration symbol
   *
   * Unrecognized string symbols are read as the `$UNKNOWN` enum symbol.
   */
  def withName(s: String): T

  /**
   * Schema properties defined on this enum, if any.
   */
  lazy val properties: Option[DataMap] = {
    Option(SCHEMA.getProperties.get(symbolProperties)).collect {
      case enumProps: DataMap => enumProps
    }
  }

  protected def properties(symbolName: String): Option[DataMap] = {
    properties.flatMap { enumProps =>
      Option(enumProps.get(symbolName)).collect {
        case symbolProps: DataMap => symbolProps
        case other: Any =>
          throw new IllegalArgumentException(
            s"Malformed schema properties. '$symbolName' in '$symbolProperties' must be a " +
            s"DataMap, but found ${other.getClass}.")
      }
    }
  }
}

/**
 * Enumeration value type that includes Courier specific convenience methods such as
 * `property(String)`.
 *
 * @param properties Schema properties defined on this enum symbol, if any.
 */
abstract class ScalaEnumTemplateSymbol(name: String, val properties: Option[DataMap])
  extends AliasedEnumSymbol(name) {

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

object ScalaEnumTemplate {
  private val symbolProperties = "symbolProperties"
}
