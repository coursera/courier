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

import com.linkedin.data.DataComplex
import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.codec.JacksonDataCodec
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.validation.RequiredMode
import com.linkedin.data.schema.validation.ValidateDataAgainstSchema
import com.linkedin.data.schema.validation.ValidationOptions

object DataTemplates {

  object DataConversion extends Enumeration {
    val SetReadOnly = Value("SetReadOnly")
    val DeepCopy = Value("DeepCopy")
  }
  type DataConversion = DataConversion.Value

  private[this] val validationOptions =
    new ValidationOptions(RequiredMode.FIXUP_ABSENT_WITH_DEFAULT)

  private def validate(dataMap: DataComplex, schema: DataSchema) = {
    val result = ValidateDataAgainstSchema.validate(dataMap, schema, validationOptions)
    if (result.isValid) {
      dataMap
    } else {
      throw new DataValidationException(result)
    }
  }

  def makeImmutable[T <: DataComplex](
      data: T, schema: DataSchema, conversion: DataConversion): T = {
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

  private val dataCodec = new JacksonDataCodec

  def readDataMap(json: String): DataMap = {
    dataCodec.stringToMap(json)
  }

  def readDataList(json: String): DataList = {
    dataCodec.stringToList(json)
  }

  def writeDataMap(dataMap: DataMap): String = {
    dataCodec.mapToString(dataMap)
  }

  def writeDataList(dataList: DataList): String = {
    dataCodec.listToString(dataList)
  }
}
