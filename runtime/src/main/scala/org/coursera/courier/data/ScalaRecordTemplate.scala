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

import com.linkedin.data.DataMap
import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.GetMode
import com.linkedin.data.template.RecordTemplate
import com.linkedin.data.template.SetMode

abstract class ScalaRecordTemplate(dataMap: DataMap, schema: RecordDataSchema) extends RecordTemplate(dataMap, schema) {

  protected def putDirectBoxed[T, U](field: RecordDataSchema.Field, valueClass: Class[T], obj: U): Unit = {
    putDirect(field, valueClass, obj.asInstanceOf[T])
  }

  protected def putDirectOptional[T](
      field: RecordDataSchema.Field, valueClass: Class[T], obj: Option[T]): Unit = {
    obj.foreach { o =>
      putDirect(field, valueClass, o)
    }
  }

  protected def putDirectOptionalBoxed[T, U](
      field: RecordDataSchema.Field, valueClass: Class[T], obj: Option[U]): Unit = {
    obj.foreach { o =>
      putDirect(field, valueClass, o.asInstanceOf[T])
    }
  }

  protected def putWrappedOptional[T <: DataTemplate[_]](
      field: RecordDataSchema.Field, valueClass: Class[T], obj: Option[T]): Unit = {
    obj.foreach { o =>
      putWrapped(field, valueClass, o)
    }
  }

  protected def putCustomType[T](
      field: RecordDataSchema.Field, valueClass: Class[T], dataClass: Class[_], obj: T): Unit = {
    putCustomType(field, valueClass, dataClass, obj, SetMode.DISALLOW_NULL)
  }

  protected def putCustomTypeOptional[T](
      field: RecordDataSchema.Field, valueClass: Class[T], dataClass: Class[_], obj: Option[T]): Unit = {
    obj.foreach { o =>
      putCustomType(field, valueClass, dataClass, o, SetMode.DISALLOW_NULL)
    }
  }

  protected def obtainDirect[T](field: RecordDataSchema.Field, valueClass: Class[T]): T = {
    obtainDirect(field, valueClass, GetMode.STRICT)
  }

  protected def obtainDirectBoxed[T, U](field: RecordDataSchema.Field, valueClass: Class[T], unboxedClass: Class[U]): U = {
    obtainDirect(field, valueClass, GetMode.STRICT).asInstanceOf[U]
  }

  protected def obtainDirectOptional[T](field: RecordDataSchema.Field, valueClass: Class[T]): Option[T] = {
    Option(obtainDirect(field, valueClass, GetMode.STRICT))
  }

  protected def obtainDirectOptionalBoxed[T, U](field: RecordDataSchema.Field, valueClass: Class[T], unboxedClass: Class[U]): Option[U] = {
    Option(obtainDirect(field, valueClass, GetMode.STRICT)).map(_.asInstanceOf[U])
  }

  protected def obtainWrapped[T <: DataTemplate[_]](field: RecordDataSchema.Field, valueClass: Class[T]): T = {
    obtainWrapped(field, valueClass, GetMode.STRICT)
  }

  protected def obtainWrappedOptional[T <: DataTemplate[_]](field: RecordDataSchema.Field, valueClass: Class[T]): Option[T] = {
    Option(obtainWrapped(field, valueClass, GetMode.STRICT))
  }

  protected def obtainCustomType[T](field: RecordDataSchema.Field, valueClass: Class[T]): T = {
    obtainCustomType(field, valueClass, GetMode.STRICT)
  }

  protected def obtainCustomTypeOptional[T](field: RecordDataSchema.Field, valueClass: Class[T]): Option[T] = {
    Option(obtainCustomType(field, valueClass, GetMode.STRICT))
  }
}
