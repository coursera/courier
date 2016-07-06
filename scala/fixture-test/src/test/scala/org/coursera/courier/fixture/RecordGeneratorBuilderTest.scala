package org.coursera.courier.fixture

import org.coursera.courier.fixture.generator.DefaultGeneratorFactories
import org.coursera.records.test.WithComplexTypeDefaults
import org.example.Fortune
import org.example.FortuneCookie
import org.example.FortuneTelling.FortuneCookieMember
import org.example.MagicEightBall
import org.example.MagicEightBallAnswer
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitSuite

import FixtureSugar._

class RecordGeneratorBuilderTest extends JUnitSuite with AssertionsForJUnit {

  @Test
  def withoutOptional(): Unit = {
    val cookie = fixtureGenerator[FortuneCookie]
      .excludeOptional()
      .next()

    assert(cookie.certainty.isEmpty)
  }

  @Test
  def withOptional(): Unit = {
    val cookie = fixtureGenerator[FortuneCookie]
      .includeOptional()
      .next()

    assert(cookie.certainty.nonEmpty)
  }

  @Test
  def withDefaults(): Unit = {
    val element = fixtureGenerator[WithComplexTypeDefaults]
      .useDefaults()
      .next()

    assertResult(WithComplexTypeDefaults())(element)
  }

  @Test
  def ignoreDefaults(): Unit = {
    val element = fixtureGenerator[WithComplexTypeDefaults]
      .ignoreDefaults()
      .next()

    assert(WithComplexTypeDefaults() != element)
  }

  @Test
  def withField_UnionMember(): Unit = {
    val cookie = FortuneCookie("A message", Some(1.0f), List(1, 2, 3))
    val fortune = fixtureGenerator[Fortune]
      .withField("telling", cookie)
      .next()

    assertResult(FortuneCookieMember(cookie))(fortune.telling)
  }

  @Test
  def withField_UnionMemberWrapped(): Unit = {
    val wrappedCookie = FortuneCookieMember(FortuneCookie("A message", Some(1.0f), List(1, 2, 3)))
    val fortune = fixtureGenerator[Fortune]
      .withField("telling", wrappedCookie)
      .next()

    assertResult(wrappedCookie)(fortune.telling)
  }

  @Test
  def withField_Enum(): Unit = {
    val answer = MagicEightBallAnswer.OUTLOOK_NOT_SO_GOOD
    val ball = fixtureGenerator[MagicEightBall]
      .withField("answer", answer)
      .next()

    assertResult(answer)(ball.answer)
  }

  @Test
  def withField_Some(): Unit = {
    val certainty = Some(1.0f)
    val cookie = fixtureGenerator[FortuneCookie]
      .withField("certainty", certainty)
      .next()

    assertResult(certainty)(cookie.certainty)
  }

  @Test
  def withField_None(): Unit = {
    val cookie = fixtureGenerator[FortuneCookie]
      .withField("certainty", None)
      .next()

    assertResult(None)(cookie.certainty)
  }

  @Test
  def withField_SomeUnwrapped(): Unit = {
    val certainty = 1.0f
    val cookie = fixtureGenerator[FortuneCookie]
      .withField("certainty", certainty)
      .next()

    assertResult(Some(certainty))(cookie.certainty)
  }

  @Test
  def withField_NotInSchema_Fails(): Unit = {
    intercept[IllegalArgumentException] {
      fixtureGenerator[FortuneCookie].withField("nonExistentField", 1.0).next()
    }
  }

  @Test
  def withCollectionLength(): Unit = {
    val cookie = fixtureGenerator[FortuneCookie]
      .withCollectionLength(5)
      .next()

    assertResult(5)(cookie.luckyNumbers.length)
  }

}
