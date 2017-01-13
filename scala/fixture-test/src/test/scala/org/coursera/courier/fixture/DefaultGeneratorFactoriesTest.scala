package org.coursera.courier.fixture

import org.coursera.courier.fixture.generator.CoercedValueGenerator
import org.coursera.courier.fixture.generator.DefaultGeneratorFactories
import org.coursera.courier.generator.customtypes.CoercedDateTimeGenerator
import org.coursera.courier.generator.customtypes.DateTimeCoercer
import org.example.Fortune
import org.joda.time.DateTime
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitSuite

import FixtureSugar._

class DefaultGeneratorFactoriesTest extends JUnitSuite with AssertionsForJUnit {

  val defaultTime = new DateTime("2016-07-05T17:19:46.087-07:00")
  class ConstantDateTimeGenerator extends CoercedValueGenerator[DateTime] {
    override val coercer = new DateTimeCoercer
    def nextValue(): DateTime = defaultTime
  }

  @Test
  def fixtureGenerator_WithoutDefaultTimeGenerator_UseDefinedTimeGenerator(): Unit = {
    val generator = fixtureGenerator[Fortune]

    val expectedTime = new CoercedDateTimeGenerator().nextValue()
    assertResult(expectedTime)(generator.next().createdAt)
  }

  @Test
  def fixtureGenerator_WithDefaultGeneratorForType_UseDefaultTypeGenerator(): Unit = {
    implicit val defaultGenerator: DefaultGeneratorFactories = DefaultGeneratorFactories()
      .set[DateTime]((name: String) => new ConstantDateTimeGenerator)

    val generator = fixtureGenerator[Fortune]

    assertResult(defaultTime)(generator.next().createdAt)
  }

  @Test
  def fixtureGenerator_WithFieldOverride_UseGeneratorOverride(): Unit = {
    implicit val defaultGenerator: DefaultGeneratorFactories = DefaultGeneratorFactories()
      .set[DateTime]((name: String) => new ConstantDateTimeGenerator)

    val customTime = defaultTime.plus(10000L)
    val coercer = new DateTimeCoercer()
    val generator = fixtureGenerator[Fortune]
        .withField("createdAt", coercer.coerceInput(customTime))

    assertResult(customTime)(generator.next().createdAt)
  }

}
