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

package org.coursera.courier.generator.customtypes

import com.linkedin.data.DataMap
import com.linkedin.data.template.Custom
import com.linkedin.data.template.DirectCoercer
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.records.test.Message

class CustomRecordCoercer extends DirectCoercer[CustomRecord] {

  override def coerceInput(obj: CustomRecord): AnyRef = {
    Message(obj.title, obj.body).data()
  }

  override def coerceOutput(obj: Any): CustomRecord = {
    obj match {
      case data: DataMap =>
        val message = Message(data, DataConversion.SetReadOnly)
        CustomRecord(message.title, message.body)
      case _: Any => throw new IllegalArgumentException()
    }
  }
}

object CustomRecordCoercer {
  registerCoercer()

  def registerCoercer(): Unit = {
    Custom.registerCoercer(new CustomRecordCoercer, classOf[CustomRecord])
  }
}


