package org.coursera.courier.fixture

import org.coursera.courier.companions.RecordCompanion
import org.coursera.courier.fixture.generator.DefaultGeneratorFactories
import org.coursera.courier.fixture.generator.RecordGeneratorBuilder
import org.coursera.courier.templates.ScalaRecordTemplate

trait FixtureSugar {

  /**
   * Instantiate a fixture generator for the record type.
   */
  def fixtureGenerator[T <: ScalaRecordTemplate](
      implicit companion: RecordCompanion[T],
      defaultGeneratorFactories: DefaultGeneratorFactories = DefaultGeneratorFactories()):
    RecordGeneratorBuilder[T] = new RecordGeneratorBuilder(
      companion, defaultGeneratorFactories = defaultGeneratorFactories)

  /**
   * Instantiate a fixture instance for the record type.
   */
  def fixture[T <: ScalaRecordTemplate](
      implicit companion: RecordCompanion[T],
      defaultGeneratorFactories: DefaultGeneratorFactories = DefaultGeneratorFactories()): T =
    fixtureGenerator(companion, defaultGeneratorFactories).next()

}

object FixtureSugar extends FixtureSugar
