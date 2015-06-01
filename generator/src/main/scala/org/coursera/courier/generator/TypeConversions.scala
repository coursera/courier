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

import com.linkedin.data.ByteString
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.PrimitiveDataSchema

/**
 * Provides conversions between the various type systems involved in pegasus data binding
 * generation.
 */
object TypeConversions {

  import com.linkedin.data.schema.DataSchemaConstants._

  case class PrimitiveMapping(
    schema: DataSchema,
    scalaType: String,
    isValueType: Boolean, // True for all AnyVal
    pegasusType: String,
    javaClass: Class[_])

  private val primitiveMappings = Seq(
    PrimitiveMapping(INTEGER_DATA_SCHEMA, "Int", true, "int", classOf[java.lang.Integer]),
    PrimitiveMapping(LONG_DATA_SCHEMA, "Long", true, "long", classOf[java.lang.Long]),
    PrimitiveMapping(FLOAT_DATA_SCHEMA, "Float", true, "float", classOf[java.lang.Float]),
    PrimitiveMapping(DOUBLE_DATA_SCHEMA, "Double", true, "double", classOf[java.lang.Double]),
    PrimitiveMapping(BOOLEAN_DATA_SCHEMA, "Boolean", true, "boolean", classOf[java.lang.Boolean]),
    PrimitiveMapping(BYTES_DATA_SCHEMA, "ByteString", false, "bytes", classOf[ByteString]),
    PrimitiveMapping(STRING_DATA_SCHEMA, "String", false, "string", classOf[String]),
    PrimitiveMapping(NULL_DATA_SCHEMA, "Null", false, "null", classOf[Null]))

  private val primitivesBySchema = primitiveMappings.map(mapping => mapping.schema -> mapping).toMap
  private val scalaTypeForPrimitiveType = primitivesBySchema.mapValues(_.scalaType)
  private val javaClassForPrimitiveType = primitivesBySchema.mapValues(_.javaClass)
  private val pegasusTypeForPrimitiveType = primitivesBySchema.mapValues(_.pegasusType)
  private val isValueTypeForPrimitiveType = primitivesBySchema.mapValues(_.isValueType)

  val primitiveSchemas: Set[DataSchema] = primitiveMappings.map(_.schema).toSet

  def lookupScalaType(schema: PrimitiveDataSchema): String = {
    scalaTypeForPrimitiveType(schema)
  }

  def lookupJavaClass(schema: PrimitiveDataSchema): Class[_] = {
    javaClassForPrimitiveType(schema)
  }

  def lookupPegasusType(schema: PrimitiveDataSchema): String = {
    pegasusTypeForPrimitiveType(schema)
  }

  def isScalaValueType(schema: DataSchema): Boolean = {
    isValueTypeForPrimitiveType.getOrElse(schema, false)
  }
}


