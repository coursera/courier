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

case class ShortId(id: Short)

case class ByteId(id: Byte)

case class CharId(id: Char)

case class IntId(id: Int)

case class LongId(id: Long) extends AnyVal

case class FloatId(id: Float)

case class DoubleId(id: Double)

case class BooleanId(id: Boolean)

case class StringId(id: String)

case class BoxedIntId(id: java.lang.Integer)

case class StringIdWrapper(id: StringId)

case class CaseClassCustomIntWrapper(int: CustomInt)
