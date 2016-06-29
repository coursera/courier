package org.coursera.courier.generator.customtypes

import org.coursera.courier.mock.CoercedValueGenerator
import org.joda.time.DateTime

class CoercedDateTimeGenerator extends
  CoercedValueGenerator[DateTime](
    (0 to 1000).map(new DateTime(_)),
    new DateTimeCoercer)