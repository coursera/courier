package org.coursera.courier.mock

import org.coursera.courier.templates.DataTemplates.DataConversion
import org.example.Fortune
import org.example.FortuneCookie
import org.example.MagicEightBall
import org.example.MagicEightBallAnswer
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitSuite

class RecordSchemaDataGeneratorBuilderTest extends JUnitSuite with AssertionsForJUnit {

  @Test
  def build_FortuneCookie(): Unit = {
    val generator = RecordSchemaDataGeneratorBuilder(FortuneCookie.SCHEMA).build()
    val cookie = FortuneCookie(generator.next(), DataConversion.SetReadOnly)

    assertResult("message0")(cookie.message)
    assertResult(Some(0.0))(cookie.certainty)
    assertResult(List(0, 1, 2))(cookie.luckyNumbers)
  }

  @Test
  def build_MagicEightBall(): Unit = {
    val generator = RecordSchemaDataGeneratorBuilder(MagicEightBall.SCHEMA).build()
    val eightBall = MagicEightBall(generator.next(), DataConversion.SetReadOnly)

    assert(MagicEightBallAnswer.symbols.contains(eightBall.answer))
    assertResult("question0")(eightBall.question)
  }

  @Test
  def build_Fortune(): Unit = {
    val generator = RecordSchemaDataGeneratorBuilder(Fortune.SCHEMA).build()

    val fortunes = (1 to 3).map(_ => Fortune(generator.next(), DataConversion.SetReadOnly))

    // Verify all [[DateTime]] members are valid and distinct
    assertResult(fortunes.size)(fortunes.map(_.createdAt).toSet.size)

    // Verify all [[FortuneTelling]] union members are valid and distinct
    assertResult(fortunes.size)(fortunes.map(_.telling).toSet.size)
  }

}
