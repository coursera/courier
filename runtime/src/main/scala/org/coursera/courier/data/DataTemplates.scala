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

package org.coursera.courier.data

import com.linkedin.data.DataComplex
import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.data.schema.validation.RequiredMode
import com.linkedin.data.schema.validation.ValidateDataAgainstSchema
import com.linkedin.data.schema.validation.ValidationOptions
import com.linkedin.data.schema.validation.ValidationResult
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.template.SetMode

import scala.collection.JavaConverters._

class DataValidationException(val validationResult: ValidationResult)
  extends RuntimeException(validationResult.getMessages.asScala.mkString(", "))

object DataTemplates {

  object DataConversion extends Enumeration {
    val SetReadOnly = Value("SetReadOnly")
    val DeepCopy = Value("DeepCopy")
  }
  type DataConversion = DataConversion.Value

  private[this] val validationOptions = new ValidationOptions(RequiredMode.FIXUP_ABSENT_WITH_DEFAULT)

  private def validate(dataMap: DataComplex, schema: DataSchema) = {
    val result = ValidateDataAgainstSchema.validate(dataMap, schema, validationOptions)
    if (result.isValid) {
      dataMap
    } else {
      throw new DataValidationException(result)
    }
  }

  def makeImmutable[T <: DataComplex](data: T, schema: DataSchema, conversion: DataConversion): T = {
    conversion match {
      case DataConversion.DeepCopy =>
        val copy = data.copy()
        validate(copy, schema)
        copy.asInstanceOf[T]
      case DataConversion.SetReadOnly =>
        validate(data, schema)
        data.setReadOnly()
        data
    }
  }

  def toUnionMap(data: Object, unionTag: String): DataMap = {
    val dataMap = new DataMap()
    dataMap.put(unionTag, data)
    dataMap.setReadOnly()
    dataMap
  }

  def putDirect[T <: AnyRef](
      dataMap: DataMap,
      field: RecordDataSchema.Field,
      valueClass: Class[T],
      dataClass: Class[_],
      obj: T): Unit = {
    putDirect(dataMap, field, valueClass, dataClass, obj, SetMode.DISALLOW_NULL)
  }

  def putDirect[T <: AnyRef](
      dataMap: DataMap,
      field: RecordDataSchema.Field,
      valueClass: Class[T],
      dataClass: Class[_],
      obj: T,
      mode: SetMode): Unit = {
    if (checkPutNullValue(field, obj, mode)) {
      dataMap.put(field.getName, DataTemplateUtil.coerceInput(obj, valueClass, dataClass))
    }
  }

  private def checkPutNullValue (field: RecordDataSchema.Field, obj: AnyRef, mode: SetMode): Boolean = {
    if (obj == null) {
      mode match {
        case SetMode.IGNORE_NULL => false
        case SetMode.REMOVE_IF_NULL => false
        case SetMode.REMOVE_OPTIONAL_IF_NULL =>
          if (field.getOptional) {
            false
          }
          else {
            throw new IllegalArgumentException(s"Cannot remove mandatory field ${field.getName}")
          }
        case SetMode.DISALLOW_NULL =>
          throw new NullPointerException(s"Cannot set field ${field.getName} to null")
      }
    }
    else {
      true
    }
  }
}
