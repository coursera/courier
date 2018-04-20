package org.coursera.courier.generator.customtypes

import java.util.UUID

import org.coursera.courier.fixture.generator.CoercedValueGenerator
import org.coursera.courier.fixture.generator.LongRangeGenerator

class CoercedUUIDGenerator extends CoercedValueGenerator[UUID] {

  override val coercer = UUIDCoercer

  private val longGenerator = new LongRangeGenerator

  override def nextValue(): UUID = new UUID(longGenerator.next(), 0)
}
