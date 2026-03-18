/*
 Copyright 2024 Coursera Inc.

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

import com.linkedin.data.ByteString
import com.linkedin.data.template.DataTemplateUtil
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

/**
 * Tests for [[SingleElementCaseClassCoercer]] and [[CaseClassReflect]].
 */
class SingleElementCaseClassCoercerTest extends AssertionsForJUnit {

  import SingleElementCaseClassCoercerTest._

  // ─── CaseClassReflect ────────────────────────────────────────────────────────

  @Test def caseClassReflect_productArity_singleField(): Unit = {
    assertResult(1)(CaseClassReflect.productArity(classOf[StringId]))
  }

  @Test def caseClassReflect_productArity_twoFields(): Unit = {
    assertResult(2)(CaseClassReflect.productArity(classOf[PairId]))
  }

  @Test def caseClassReflect_productElementType_string(): Unit = {
    assertResult(classOf[String])(CaseClassReflect.productElementType(classOf[StringId], 0))
  }

  @Test def caseClassReflect_newInstance_string(): Unit = {
    val result = CaseClassReflect.newInstance(classOf[StringId], "hello")
    assertResult(StringId("hello"))(result)
  }

  @Test def caseClassReflect_newInstance_int(): Unit = {
    val result = CaseClassReflect.newInstance(classOf[IntId], Int.box(42))
    assertResult(IntId(42))(result)
  }

  // ─── String-wrapping coercer ─────────────────────────────────────────────────

  @Test def stringIdCoercer_coerceInput_wrapsString(): Unit = {
    val coercer = makeCoercer[StringId](classOf[String])
    assertResult("hello")(coercer.coerceInput(StringId("hello")))
  }

  @Test def stringIdCoercer_coerceOutput_unwrapsString(): Unit = {
    val coercer = makeCoercer[StringId](classOf[String])
    assertResult(StringId("world"))(coercer.coerceOutput("world"))
  }

  // ─── Int-wrapping coercer ────────────────────────────────────────────────────

  @Test def intIdCoercer_coerceInput_wrapsInt(): Unit = {
    val coercer = makeCoercer[IntId](classOf[java.lang.Integer])
    assertResult(Int.box(7))(coercer.coerceInput(IntId(7)))
  }

  @Test def intIdCoercer_coerceOutput_unwrapsInt(): Unit = {
    val coercer = makeCoercer[IntId](classOf[java.lang.Integer])
    assertResult(IntId(99))(coercer.coerceOutput(Int.box(99)))
  }

  // ─── Long-wrapping coercer ───────────────────────────────────────────────────

  @Test def longIdCoercer_coerceInput_wrapsLong(): Unit = {
    val coercer = makeCoercer[LongId](classOf[java.lang.Long])
    assertResult(Long.box(123L))(coercer.coerceInput(LongId(123L)))
  }

  @Test def longIdCoercer_coerceOutput_unwrapsLong(): Unit = {
    val coercer = makeCoercer[LongId](classOf[java.lang.Long])
    assertResult(LongId(456L))(coercer.coerceOutput(Long.box(456L)))
  }

  // ─── Boolean-wrapping coercer ────────────────────────────────────────────────

  @Test def boolIdCoercer_coerceInput(): Unit = {
    val coercer = makeCoercer[BoolId](classOf[java.lang.Boolean])
    assertResult(true)(coercer.coerceInput(BoolId(true)))
  }

  @Test def boolIdCoercer_coerceOutput(): Unit = {
    val coercer = makeCoercer[BoolId](classOf[java.lang.Boolean])
    assertResult(BoolId(false))(coercer.coerceOutput(Boolean.box(false)))
  }

  // ─── Double-wrapping coercer ─────────────────────────────────────────────────

  @Test def doubleIdCoercer_coerceInput(): Unit = {
    val coercer = makeCoercer[DoubleId](classOf[java.lang.Double])
    assertResult(3.14)(coercer.coerceInput(DoubleId(3.14)))
  }

  @Test def doubleIdCoercer_coerceOutput(): Unit = {
    val coercer = makeCoercer[DoubleId](classOf[java.lang.Double])
    assertResult(DoubleId(2.71))(coercer.coerceOutput(Double.box(2.71)))
  }

  // ─── Float-wrapping coercer ──────────────────────────────────────────────────

  @Test def floatIdCoercer_coerceInput(): Unit = {
    val coercer = makeCoercer[FloatId](classOf[java.lang.Float])
    assertResult(1.5f)(coercer.coerceInput(FloatId(1.5f)))
  }

  @Test def floatIdCoercer_coerceOutput(): Unit = {
    val coercer = makeCoercer[FloatId](classOf[java.lang.Float])
    assertResult(FloatId(2.5f))(coercer.coerceOutput(Float.box(2.5f)))
  }

  // ─── Short-wrapping coercer (extended primitive) ─────────────────────────────

  @Test def shortIdCoercer_coerceInput_convertsToInt(): Unit = {
    val coercer = makeCoercer[ShortId](classOf[java.lang.Integer])
    val result = coercer.coerceInput(ShortId(5.toShort))
    assertResult(Int.box(5))(result)
  }

  @Test def shortIdCoercer_coerceOutput_convertsFromInt(): Unit = {
    val coercer = makeCoercer[ShortId](classOf[java.lang.Integer])
    assertResult(ShortId(10.toShort))(coercer.coerceOutput(Int.box(10)))
  }

  // ─── Char-wrapping coercer (extended primitive) ──────────────────────────────

  @Test def charIdCoercer_coerceInput_convertsToString(): Unit = {
    val coercer = makeCoercer[CharId](classOf[java.lang.String])
    assertResult("A")(coercer.coerceInput(CharId('A')))
  }

  @Test def charIdCoercer_coerceOutput_convertsFromString(): Unit = {
    val coercer = makeCoercer[CharId](classOf[java.lang.String])
    assertResult(CharId('Z'))(coercer.coerceOutput("Z"))
  }

  // ─── Byte-wrapping coercer (extended primitive) ──────────────────────────────

  @Test def byteIdCoercer_coerceInput_convertsToByteString(): Unit = {
    val coercer = makeCoercer[ByteId](classOf[ByteString])
    val result = coercer.coerceInput(ByteId(0x7F.toByte))
    result match {
      case bs: ByteString => assertResult(1)(bs.length())
      case other          => fail(s"Expected ByteString but got $other")
    }
  }

  @Test def byteIdCoercer_coerceOutput_convertsFromByteString(): Unit = {
    val coercer = makeCoercer[ByteId](classOf[ByteString])
    val bs = ByteString.copy(Array(0x42.toByte))
    assertResult(ByteId(0x42.toByte))(coercer.coerceOutput(bs))
  }

  // ─── Arity validation ────────────────────────────────────────────────────────

  @Test def registerCoercer_nonUnitArity_throwsForArity2(): Unit = {
    intercept[IllegalArgumentException] {
      // PairId has 2 fields — must throw
      SingleElementCaseClassCoercer.registerCoercer(classOf[PairId], classOf[String])
    }
  }

  // ─── CaseClassReflect error paths ────────────────────────────────────────────

  @Test def caseClassReflect_newInstance_wrongType_throwsIllegalArgument(): Unit = {
    // Passing an Int where String is expected triggers the catch block in newInstance
    intercept[IllegalArgumentException] {
      CaseClassReflect.newInstance(classOf[StringId], Int.box(42))
    }
  }

  @Test def caseClassReflect_productArity_noPublicCtor_throws(): Unit = {
    // Product trait has no constructors — triggers the getOrElse throw in caseClassConstructor
    intercept[IllegalArgumentException] {
      CaseClassReflect.productArity(classOf[Product])
    }
  }
}

object SingleElementCaseClassCoercerTest {

  case class StringId(id: String)
  case class IntId(id: Int)
  case class LongId(id: Long)
  case class BoolId(flag: Boolean)
  case class DoubleId(value: Double)
  case class FloatId(value: Float)
  case class ShortId(value: Short)
  case class CharId(ch: Char)
  case class ByteId(b: Byte)
  case class PairId(a: String, b: String) // arity 2 — invalid for coercer

  /**
   * Registers a coercer for `T` and returns a round-trip function.
   * Uses `DataTemplateUtil.coerceInput`/`coerceOutput` to exercise the coercer.
   */
  def makeCoercer[T <: Product](dataType: Class[_])(implicit m: Manifest[T]): RoundTrip[T] = {
    val caseClassType = m.runtimeClass.asInstanceOf[Class[T]]
    SingleElementCaseClassCoercer.registerCoercer(caseClassType, dataType)
    new RoundTrip[T] {
      def coerceInput(v: T): AnyRef = DataTemplateUtil.coerceInput(v, caseClassType, dataType)
      def coerceOutput(v: AnyRef): T = DataTemplateUtil.coerceOutput(v, caseClassType)
    }
  }

  trait RoundTrip[T] {
    def coerceInput(v: T): AnyRef
    def coerceOutput(v: AnyRef): T
  }
}
