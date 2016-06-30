package org.coursera.courier.mock

import org.coursera.courier.companions.RecordCompanion
import org.coursera.courier.generator.customtypes.CoercedCustomIntGenerator
import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.enums.Fruits
import org.coursera.fixed.Fixed8
import org.coursera.fixed.WithFixed8
import org.coursera.maps.Toggle
import org.coursera.maps.WithCustomTypesMap
import org.coursera.maps.WithTypedKeyMap
import org.coursera.records.test.Simple
import org.example.Fortune
import org.example.FortuneCookie
import org.example.MagicEightBall
import org.example.MagicEightBallAnswer
import org.junit.Ignore
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitSuite

class RecordGeneratorTest extends JUnitSuite with AssertionsForJUnit {

  @Test
  def build_FortuneCookie(): Unit = {
    val generator = RecordGenerator(FortuneCookie)
    val cookie = generator.next()

    assertResult("message0")(cookie.message)
    assertResult(Some(0.0))(cookie.certainty)
    assertResult(List(0, 1, 2))(cookie.luckyNumbers)
  }

  @Test
  def build_MagicEightBall(): Unit = {
    val generator = RecordGenerator(MagicEightBall)
    val eightBall = generator.next()

    assert(MagicEightBallAnswer.symbols.contains(eightBall.answer))
    assertResult("question0")(eightBall.question)
  }

  @Test
  def build_Fortune(): Unit = {
    val generator = RecordGenerator(Fortune)

    val fortunes = (1 to 3).map(_ => generator.next())

    // Verify all [[DateTime]] members are valid and distinct
    assertResult(fortunes.size)(fortunes.map(_.createdAt).toSet.size)

    // Verify all [[FortuneTelling]] union members are valid and distinct
    assertResult(fortunes.size)(fortunes.map(_.telling).toSet.size)
  }

  @Test
  def build_WithCustomTypesMap(): Unit = {
    val generator = RecordGenerator(WithCustomTypesMap)

    // Verify that the map is non-empty
    assert(generator.next().ints.nonEmpty)
  }

  @Test
  def build_WithTypedKeyMap(): Unit = {
    def mapGenerator(keyGenerator: ValueGenerator[_ <: AnyRef]) =
      new MapValueGenerator(keyGenerator, new PrefixedStringGenerator("value"), 3)

    val generator = RecordGenerator(WithTypedKeyMap)
      .withGenerator("record", mapGenerator(RecordGenerator(Simple)))
      .withGenerator("enum", mapGenerator(new CyclicEnumSymbolGenerator(Fruits.symbols)))
      .withGenerator("custom", mapGenerator(new CoercedCustomIntGenerator))
      .withGenerator("fixed", mapGenerator(new IntegerRangeFixedBytesGenerator(8)))
      .withGenerator("samePackageEnum", mapGenerator(new CyclicEnumSymbolGenerator(Toggle.symbols)))

    val result = generator.next()
  }

  @Test
  def build_WithFixed8(): Unit = {
    val generator = RecordGenerator(WithFixed8)

    val result = generator.next()
  }


}
