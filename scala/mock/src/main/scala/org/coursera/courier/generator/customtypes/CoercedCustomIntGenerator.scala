package org.coursera.courier.generator.customtypes

import org.coursera.courier.mock.CoercedValueGenerator

class CoercedCustomIntGenerator extends
  CoercedValueGenerator[CustomInt]((0 to 1000).map(CustomInt), new CustomIntCoercer)