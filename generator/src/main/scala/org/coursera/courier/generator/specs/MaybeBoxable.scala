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

package org.coursera.courier.generator.specs

import com.linkedin.data.schema.DataSchema
import org.coursera.courier.generator.TypeConversions
import play.twirl.api.Txt

/**
 * Convenience trait for primitive types since they sometimes need to be boxed/unboxed.
 */
trait MaybeBoxable extends Definition {

  def requiresBoxing = schema.exists(TypeConversions.isScalaValueType)

  def maybeBox(expr: Txt): Txt = {
    if (requiresBoxing) {
      Txt(s"$scalaType.box($expr)")
    } else {
      expr
    }
  }

  /**
   * If this definition requires boxing, return a Scala expression that boxes the given Scala
   * expression.
   *
   * The given Scala expression must return AnyVal (and hence, must not evaluate to null).
   */
  def maybeUnbox(expr: Txt): Txt = {
    if (requiresBoxing) {
      Txt(s"$scalaType.unbox($expr)")
    } else {
      expr
    }
  }
}
