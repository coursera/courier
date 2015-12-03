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

package org.coursera.courier.coercers

/**
 * Provides reflection utilities specifically for working with Scala case classes.
 */
object CaseClassReflect {
  def productArity[T <: Product](productType: Class[T]): Int = {
    caseClassConstructor(productType).getParameterTypes.size
  }

  def productElementType[T <: Product](productType: Class[T], n: Int): Class[_] = {
    val types = caseClassConstructor(productType).getParameterTypes
    types(n)
  }

  def newInstance[T <: Product](productType: Class[T], fields: AnyRef*): T = {
    val constructor = caseClassConstructor(productType)
    try {
      constructor.newInstance(fields: _*).asInstanceOf[T]
    } catch {
      case e: IllegalArgumentException =>
        throw new IllegalArgumentException(
          s"${constructor.getParameterTypes.mkString(", ")} - ${fields.mkString(", ")}")
    }
  }

  private[this] def caseClassConstructor[T <: Product](productType: Class[T]) = {
    productType.getConstructors.headOption.getOrElse(
      throw new IllegalArgumentException(
        "productType must be a case class with a public constructor, but no public" +
        "constructor found.")
    )
  }
}
