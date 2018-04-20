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

package org.coursera.courier.generator.customtypes.subtype

import java.util.UUID

import com.linkedin.data.template.Custom
import com.linkedin.data.template.DirectCoercer
import org.coursera.courier.generator.customtypes.CustomUUID

case class WrappedCustomUUID(wrapped: CustomUUID)

object WrappedCustomUUID {

  object Coercer extends DirectCoercer[WrappedCustomUUID] {

    override def coerceInput(obj: WrappedCustomUUID): AnyRef = {
      obj.wrapped.uuid.toString
    }

    override def coerceOutput(obj: Any): WrappedCustomUUID = {
      obj match {
        case value: java.lang.String => WrappedCustomUUID(CustomUUID(UUID.fromString(value)))
        case _: Any => throw new IllegalArgumentException()
      }
    }

    registerCoercer()

    final def registerCoercer(): Unit = {
      Custom.registerCoercer(this, classOf[WrappedCustomUUID])
    }
  }
}
