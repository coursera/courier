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

import com.linkedin.data.DataMap
import org.coursera.courier.data.IntArray
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.example.FortuneCookie
import org.example.FortuneTelling
import org.example.MagicEightBall
import org.example.MagicEightBallAnswer
import org.junit.Test

/**
 * Tests for org.example.* generated types: FortuneCookie, MagicEightBall,
 * MagicEightBallAnswer, FortuneTelling (a typeref union).
 */
class FortuneGeneratorTest extends GeneratorTest with SchemaFixtures {

  // ---------------------------------------------------------------------------
  // MagicEightBallAnswer enum
  // ---------------------------------------------------------------------------

  @Test
  def testMagicEightBallAnswer_values(): Unit = {
    assert(MagicEightBallAnswer.withName("IT_IS_CERTAIN") === MagicEightBallAnswer.IT_IS_CERTAIN)
    assert(MagicEightBallAnswer.withName("ASK_AGAIN_LATER") === MagicEightBallAnswer.ASK_AGAIN_LATER)
    assert(MagicEightBallAnswer.withName("OUTLOOK_NOT_SO_GOOD") === MagicEightBallAnswer.OUTLOOK_NOT_SO_GOOD)
    assert(MagicEightBallAnswer.withName("UNKNOWN") === MagicEightBallAnswer.$UNKNOWN)
  }

  @Test
  def testMagicEightBallAnswer_symbols(): Unit = {
    assert(MagicEightBallAnswer.symbols.contains(MagicEightBallAnswer.IT_IS_CERTAIN))
    assert(MagicEightBallAnswer.symbols.contains(MagicEightBallAnswer.ASK_AGAIN_LATER))
    assert(MagicEightBallAnswer.symbols.contains(MagicEightBallAnswer.OUTLOOK_NOT_SO_GOOD))
  }

  @Test
  def testMagicEightBallAnswer_toString(): Unit = {
    assert(MagicEightBallAnswer.IT_IS_CERTAIN.toString === "IT_IS_CERTAIN")
    assert(MagicEightBallAnswer.ASK_AGAIN_LATER.toString === "ASK_AGAIN_LATER")
  }

  // ---------------------------------------------------------------------------
  // FortuneCookie record
  // ---------------------------------------------------------------------------

  @Test
  def testFortuneCookie_construction(): Unit = {
    val fc = FortuneCookie(
      message = "You will prosper.",
      certainty = Some(0.9f),
      luckyNumbers = IntArray(7, 13, 42))
    assert(fc.message === "You will prosper.")
    assert(fc.certainty === Some(0.9f))
    assert(fc.luckyNumbers === IntArray(7, 13, 42))
  }

  @Test
  def testFortuneCookie_optionalCertaintyNone(): Unit = {
    val fc = FortuneCookie("A fortune.", luckyNumbers = IntArray(1))
    assert(fc.certainty === None)
  }

  @Test
  def testFortuneCookie_roundTrip(): Unit = {
    val original = FortuneCookie("Round trip!", Some(0.5f), IntArray(1, 2, 3))
    val roundTripped =
      FortuneCookie.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testFortuneCookie_equality(): Unit = {
    val a = FortuneCookie("msg", None, IntArray(1))
    val b = FortuneCookie("msg", None, IntArray(1))
    val c = FortuneCookie("other", None, IntArray(1))
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testFortuneCookie_copy(): Unit = {
    val fc = FortuneCookie("msg", Some(0.5f), IntArray(1))
    val copied = fc.copy(certainty = None)
    assert(copied.message === "msg")
    assert(copied.certainty === None)
  }

  @Test
  def testFortuneCookie_unapply(): Unit = {
    val fc = FortuneCookie("msg", Some(0.7f), IntArray(99))
    val FortuneCookie(message, certainty, numbers) = fc
    assert(message === "msg")
    assert(certainty === Some(0.7f))
    assert(numbers(0) === 99)
  }

  @Test
  def testFortuneCookie_productArity(): Unit = {
    val fc = FortuneCookie("msg", None, IntArray(1))
    assert(fc.productArity === 3)
    assert(fc.productElement(0) === "msg")
  }

  // ---------------------------------------------------------------------------
  // MagicEightBall record
  // ---------------------------------------------------------------------------

  @Test
  def testMagicEightBall_construction(): Unit = {
    val m = MagicEightBall("Will I win?", MagicEightBallAnswer.IT_IS_CERTAIN)
    assert(m.question === "Will I win?")
    assert(m.answer === MagicEightBallAnswer.IT_IS_CERTAIN)
  }

  @Test
  def testMagicEightBall_roundTrip(): Unit = {
    val original = MagicEightBall("Q?", MagicEightBallAnswer.ASK_AGAIN_LATER)
    val roundTripped =
      MagicEightBall.build(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }

  @Test
  def testMagicEightBall_equality(): Unit = {
    val a = MagicEightBall("Q", MagicEightBallAnswer.IT_IS_CERTAIN)
    val b = MagicEightBall("Q", MagicEightBallAnswer.IT_IS_CERTAIN)
    val c = MagicEightBall("Q", MagicEightBallAnswer.OUTLOOK_NOT_SO_GOOD)
    assert(a === b)
    assert(a !== c)
  }

  @Test
  def testMagicEightBall_copy(): Unit = {
    val m = MagicEightBall("Q?", MagicEightBallAnswer.IT_IS_CERTAIN)
    val copied = m.copy(answer = MagicEightBallAnswer.OUTLOOK_NOT_SO_GOOD)
    assert(copied.question === "Q?")
    assert(copied.answer === MagicEightBallAnswer.OUTLOOK_NOT_SO_GOOD)
  }

  @Test
  def testMagicEightBall_unapply(): Unit = {
    val m = MagicEightBall("Q?", MagicEightBallAnswer.ASK_AGAIN_LATER)
    val MagicEightBall(q, a) = m
    assert(q === "Q?")
    assert(a === MagicEightBallAnswer.ASK_AGAIN_LATER)
  }

  // ---------------------------------------------------------------------------
  // FortuneTelling union (typeref of FortuneCookie | MagicEightBall | String)
  // ---------------------------------------------------------------------------

  @Test
  def testFortuneTelling_fortuneCookieMember(): Unit = {
    val cookie = FortuneCookie("You will succeed.", None, IntArray(7))
    val member = FortuneTelling.FortuneCookieMember(cookie)
    assert(member.value === cookie)

    val FortuneTelling.FortuneCookieMember(v) = member
    assert(v.message === "You will succeed.")
  }

  @Test
  def testFortuneTelling_magicEightBallMember(): Unit = {
    val ball = MagicEightBall("Is this a test?", MagicEightBallAnswer.IT_IS_CERTAIN)
    val member = FortuneTelling.MagicEightBallMember(ball)
    assert(member.value === ball)
  }

  @Test
  def testFortuneTelling_stringMember(): Unit = {
    val member = FortuneTelling.StringMember("just a string")
    assert(member.value === "just a string")

    val FortuneTelling.StringMember(s) = member
    assert(s === "just a string")
  }

  @Test
  def testFortuneTelling_build_fortuneCookie(): Unit = {
    val cookie = FortuneCookie("msg", None, IntArray(1))
    val wrapper = FortuneTelling.FortuneCookieMember(cookie)
    val built = FortuneTelling.build(roundTrip(wrapper.data().asInstanceOf[DataMap]), DataConversion.SetReadOnly)
    assert(built.isInstanceOf[FortuneTelling.FortuneCookieMember])
    assert(built.asInstanceOf[FortuneTelling.FortuneCookieMember].value.message === "msg")
  }

  @Test
  def testFortuneTelling_build_magicEightBall(): Unit = {
    val ball = MagicEightBall("?", MagicEightBallAnswer.ASK_AGAIN_LATER)
    val wrapper = FortuneTelling.MagicEightBallMember(ball)
    val built = FortuneTelling.build(roundTrip(wrapper.data().asInstanceOf[DataMap]), DataConversion.SetReadOnly)
    assert(built.isInstanceOf[FortuneTelling.MagicEightBallMember])
  }

  @Test
  def testFortuneTelling_build_string(): Unit = {
    val wrapper = FortuneTelling.StringMember("hello")
    val built = FortuneTelling.build(roundTrip(wrapper.data().asInstanceOf[DataMap]), DataConversion.SetReadOnly)
    assert(built.isInstanceOf[FortuneTelling.StringMember])
    assert(built.asInstanceOf[FortuneTelling.StringMember].value === "hello")
  }

  @Test
  def testFortuneTelling_build_unknown(): Unit = {
    val dataMap = new DataMap()
    dataMap.put("unknownType", "someValue")
    dataMap.makeReadOnly()
    val built = FortuneTelling.build(dataMap, DataConversion.SetReadOnly)
    assert(built.isInstanceOf[FortuneTelling.$UnknownMember])
  }

  @Test
  def testFortuneTelling_equality(): Unit = {
    val a = FortuneTelling.StringMember("x")
    val b = FortuneTelling.StringMember("x")
    val c = FortuneTelling.StringMember("y")
    assert(a === b)
    assert(a !== c)
    assert(a.hashCode === b.hashCode)
  }

  @Test
  def testFortuneTelling_toString(): Unit = {
    val m = FortuneTelling.StringMember("hello")
    assert(m.toString.nonEmpty)
  }

  @Test
  def testFortuneTelling_implicit_cookie(): Unit = {
    val cookie = FortuneCookie("msg", None, IntArray(1))
    val telling: FortuneTelling = cookie
    assert(telling.isInstanceOf[FortuneTelling.FortuneCookieMember])
  }

  @Test
  def testFortuneTelling_implicit_ball(): Unit = {
    val ball = MagicEightBall("q", MagicEightBallAnswer.IT_IS_CERTAIN)
    val telling: FortuneTelling = ball
    assert(telling.isInstanceOf[FortuneTelling.MagicEightBallMember])
  }

  @Test
  def testFortuneTelling_implicit_string(): Unit = {
    val telling: FortuneTelling = FortuneTelling.wrap("hello")
    assert(telling.isInstanceOf[FortuneTelling.StringMember])
  }
}
