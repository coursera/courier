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

package org.coursera.courier.benchmark.data

import com.linkedin.data.template.Custom
import com.linkedin.data.template.DirectCoercer

class ContentIdCoercer extends DirectCoercer[ContentId] {

  override def coerceInput(obj: ContentId): AnyRef = {
    Int.box(obj.value)
  }

  override def coerceOutput(obj: Any): ContentId = {
    obj match {
      case value: java.lang.Integer => new ContentId(value)
      case _: Any => throw new IllegalArgumentException()
    }
  }
}

object ContentIdCoercer {
  registerCoercer()

  def registerCoercer(): Unit = {
    Custom.registerCoercer(new ContentIdCoercer, classOf[ContentId])
  }
}

