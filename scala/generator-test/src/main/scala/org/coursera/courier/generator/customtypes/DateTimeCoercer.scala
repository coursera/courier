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

import com.linkedin.data.template.Custom
import com.linkedin.data.template.DirectCoercer

class DateTimeCoercer extends DirectCoercer[CustomTime] {

   override def coerceInput(obj: CustomTime): AnyRef = {
     obj.timeString
   }

   override def coerceOutput(obj: Any): CustomTime = {
     obj match {
       case string: String => new CustomTime(string)
       case _: Any =>
         throw new IllegalArgumentException(
           s"DateTime field must be string, but was ${obj.getClass}")
     }
   }
 }

object DateTimeCoercer {

   registerCoercer()

   def registerCoercer(): Unit = {
     Custom.registerCoercer(new DateTimeCoercer, classOf[CustomTime])
   }
 }
