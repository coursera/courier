package org.coursera.courier.generator.customtypes

import org.coursera.courier.mock.CoercedValueGenerator
import org.coursera.courier.mock.IntegerRangeGenerator

class CoercedCustomIntGenerator extends CoercedValueGenerator[CustomInt] {

  override val coercer = CoercedCustomIntGenerator.coercer

  private val intGenerator = new IntegerRangeGenerator

  override def nextValue(): CustomInt = CustomInt(intGenerator.next)
}

object CoercedCustomIntGenerator {
  private val coercer = new CustomIntCoercer
}
