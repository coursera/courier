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

object CourierEscaping {
  val reservedClassFields = Set("data", "schema", "productArity", "productPrefix",
    "productIterator", "hashCode", "toString", "copy", "clone")

  def escapeReservedClassField(symbol: String): String = {
    if(reservedClassFields.contains(symbol)) {
      symbol + "$" // $ is not an allowed character in pegasus field names, so this is safe
    } else {
      symbol
    }
  }
}
