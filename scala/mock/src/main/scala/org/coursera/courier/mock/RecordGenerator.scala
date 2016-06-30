package org.coursera.courier.mock

import org.coursera.courier.companions.RecordCompanion
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.courier.templates.ScalaRecordTemplate

import scala.reflect.ClassTag

class RecordGenerator[K <: ScalaRecordTemplate] private (
    companion: RecordCompanion[K],
    config: RecordSchemaDataGeneratorFactory.Config) {

  lazy val dataGenerator =
    new RecordSchemaDataGeneratorFactory(companion.SCHEMA, config).build()

  def next(): K = companion.apply(dataGenerator.next(), DataConversion.SetReadOnly)

  /**
   * Set the generator for a named field.
   *
   * @param fieldName Name of field.
   * @param generator Field value generator.
   * @return A new [[RecordSchemaDataGeneratorFactory]]
   */
  def withGenerator(
      fieldName: String,
      generator: ValueGenerator[_ <: AnyRef]): RecordGenerator[K] = {

    val field = companion.SCHEMA.getField(fieldName)
    if (field != null) {
      if (RecordSchemaDataGeneratorFactory.validGeneratorForField(generator, field)) {
        copyWithConfig(config.copy(
          fieldGeneratorOverrides = config.fieldGeneratorOverrides.updated(fieldName, generator)))
      } else {
        throw new IllegalArgumentException(
          s"Generator $generator will not generate valid data " +
            s"for field $fieldName with schema:\n ${field.getType}")
      }
    } else {
      throw new IllegalArgumentException(s"Record schema has is no field named $fieldName.")
    }
  }

  /**
   * Use default field values for schema fields that define them.
   *
   * @return updated builder.
   */
  def useDefaults(): RecordGenerator[K] =
    copyWithConfig(config.copy(useSchemaDefaults = true))

  /**
   * Generate values, ignoring schema-defined defaults.
   *
   * @return updated builder.
   */
  def ignoreDefaults(): RecordGenerator[K] =
    copyWithConfig(config.copy(useSchemaDefaults = false))

  /**
   * Generate values for all optional fields.
   *
   * @return updated builder.
   */
  def includeOptional(): RecordGenerator[K] =
    copyWithConfig(config.copy(includeOptionalFields = true))

  /**
   * Do not generate values for optional fields.
   *
   * @return updated builder.
   */
  def excludeOptional(): RecordGenerator[K] =
    copyWithConfig(config.copy(includeOptionalFields = false))

  /**
   * Set length for generated collections.
   *
   * @param collectionLength
   * @return updated builder.
   */
  def withCollectionLength(collectionLength: Int): RecordGenerator[K] =
    copyWithConfig(config.copy(defaultCollectionLength = collectionLength))


  private[this] def copyWithConfig(config: RecordSchemaDataGeneratorFactory.Config):
    RecordGenerator[K] = new RecordGenerator(companion, config)

}

object RecordGenerator {

  def apply[K <: ScalaRecordTemplate](companion: RecordCompanion[K]): RecordGenerator[K] = {
    new RecordGenerator[K](companion, RecordSchemaDataGeneratorFactory.Config())
  }

}
