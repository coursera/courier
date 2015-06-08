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
import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.GetMode
import com.linkedin.data.template.RecordTemplate
import com.linkedin.data.template.SetMode

abstract class ScalaRecordTemplate(dataMap: DataMap, schema: RecordDataSchema)
  extends RecordTemplate(dataMap, schema) {

  // TODO(jbetz): Add more utility methods here to simplify obtain and put methods that generator
  // produces.

  protected def putCustomType[T](
      field: RecordDataSchema.Field, valueClass: Class[T], dataClass: Class[_], obj: T): Unit = {
    putCustomType(field, valueClass, dataClass, obj, SetMode.DISALLOW_NULL)
  }

  protected def obtainDirect[T](field: RecordDataSchema.Field, valueClass: Class[T]): T = {
    obtainDirect(field, valueClass, GetMode.STRICT)
  }

  protected def obtainWrapped[T <: DataTemplate[_]](
      field: RecordDataSchema.Field, valueClass: Class[T]): T = {
    obtainWrapped(field, valueClass, GetMode.STRICT)
  }

  protected def obtainCustomType[T](field: RecordDataSchema.Field, valueClass: Class[T]): T = {
    obtainCustomType(field, valueClass, GetMode.STRICT)
  }
}
