package org.coursera.courier.generator.customtypes

import org.coursera.courier.fixture.generator.CoercedValueGenerator
import org.coursera.courier.fixture.generator.LongRangeGenerator
import org.joda.time.DateTime


class CoercedDateTimeGenerator extends CoercedValueGenerator[DateTime] {

  override val coercer = CoercedDateTimeGenerator.coercer

  private val longGenerator = new LongRangeGenerator

  override def nextValue(): DateTime = new DateTime(longGenerator.next())
}

object CoercedDateTimeGenerator {
  private val coercer = new DateTimeCoercer
}
