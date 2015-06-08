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
import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.codec.JacksonDataCodec
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

  private val dataCodec = new JacksonDataCodec

  /**
   * Provided anyRef must be a "pegasus" data type: java.lang.* primitives, ByteString, DataMap
   * or DataList.
   *
   * THe returned String is scala source that evaluates to the provided any ref.
   *
   * E.g.
   *
   * For an float of 3.14, the returned string would be 3.14f (unquoted), which in scala evaluates
   * to the 3.14 float.
   *
   * For a string of "text" the returned string would be "text" (with quotes).
   *
   * For A DataMap containing { "message": "hello!" }, the returned string would be:
   * DataTemplates.mapLiteral("""{ "message": "hello!" }"""), which also evaluates to the
   * provided DataMap provided.
   */
  def anyToLiteral(any: AnyRef): String = {
    any match {
        case int: java.lang.Integer => toLiteral(int)
        case long: java.lang.Long => toLiteral(long)
        case float: java.lang.Float => toLiteral(float)
        case double: java.lang.Double => toLiteral(double)
        case boolean: java.lang.Boolean => toLiteral(boolean)
        case string: String => toLiteral(string)
        case bytes: ByteString => toLiteral(bytes)
        case data: DataMap => toLiteral(data)
        case data: DataList => toLiteral(data)
        case unknown: Any =>
          throw new IllegalArgumentException(s"Unsupported type: ${unknown.getClass}")
    }
  }

  def toLiteral(int: java.lang.Integer): String = int.toString

  def toLiteral(long: java.lang.Long): String = long.toString + "L"

  def toLiteral(float: java.lang.Float): String = float.toString + "f"

  def toLiteral(double: java.lang.Double): String = double.toString + "d"

  def toLiteral(boolean: java.lang.Boolean): String = boolean.toString

  def toLiteral(bytes: ByteString): String = {
    val byteLiterals = bytes.copyBytes().map(byte => s"0x${byte.toString}").mkString(", ")
    s"ByteString.copy(Array[Byte]($byteLiterals))"
  }

  def toLiteral(string: String): String = ScalaEscaping.escapeStringLiteral(string)

  def toLiteral(data: DataMap): String = {
    val json = dataCodec.mapToString(data)
    s"DataTemplates.readDataMap(${toLiteral(json)})"
  }

  def toLiteral(data: DataList): String = {
    val json = dataCodec.listToString(data)
    s"DataTemplates.readDataList(${toLiteral(json)})"
  }
}
