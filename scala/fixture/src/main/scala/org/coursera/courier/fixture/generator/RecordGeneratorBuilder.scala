package org.coursera.courier.fixture.generator

import org.coursera.courier.companions.RecordCompanion
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.courier.templates.ScalaRecordTemplate

class RecordGeneratorBuilder[K <: ScalaRecordTemplate](
    companion: RecordCompanion[K],
    customFieldGenerators: Map[String, ValueGenerator[_ <: AnyRef]] = Map.empty,
    config: RecordSchemaDataGeneratorFactory.Config = RecordSchemaDataGeneratorFactory.Config(),
    defaultGeneratorFactories: DefaultGeneratorFactories = DefaultGeneratorFactories())
  extends RecordValueGenerator[K] {

  lazy val dataGenerator = new RecordSchemaDataGeneratorFactory(
      companion.SCHEMA, customFieldGenerators, config, defaultGeneratorFactories).build()

  override def next(): K = companion.build(dataGenerator.next(), DataConversion.SetReadOnly)

  /**
   * Set a constant value for a named field.
   *
   * @param fieldName Name of field.
   * @param value Constant value for the field.
   * @return A new [[RecordSchemaDataGeneratorFactory]]
   */
  def withField(
      fieldName: String,
      value: Any): RecordGeneratorBuilder[K] = {

    val valueObject = value match {
      case obj: AnyRef => obj
      case _ => GeneratorUtil.anyToData(value)
    }
    withField(fieldName, new ConstantValueGenerator(valueObject))
  }

  /**
   * Set the generator for a named field.
   *
   * @param fieldName Name of field.
   * @param generator Field value generator.
   * @return A new [[RecordSchemaDataGeneratorFactory]]
   */
  def withField(
      fieldName: String,
      generator: ValueGenerator[_ <: AnyRef]): RecordGeneratorBuilder[K] = {

    Option(companion.SCHEMA.getField(fieldName)).map { field =>
      val dataGenerator = GeneratorUtil.getUnionSchema(field.getType)
        .map(_ => generator.map(GeneratorUtil.wrapUnionMember))
        .getOrElse(generator.map(GeneratorUtil.anyToData))
      withFieldGeneratorOverrides(customFieldGenerators.updated(fieldName, dataGenerator))
    }.getOrElse {
      throw new IllegalArgumentException(s"Record schema has is no field named $fieldName.")
    }
  }

  /**
   * Use default field values for schema fields that define them.
   *
   * @return updated builder.
   */
  def useDefaults(): RecordGeneratorBuilder[K] =
    copyWithConfig(config.copy(useSchemaDefaults = true))

  /**
   * Generate values, ignoring schema-defined defaults.
   *
   * @return updated builder.
   */
  def ignoreDefaults(): RecordGeneratorBuilder[K] =
    copyWithConfig(config.copy(useSchemaDefaults = false))

  /**
   * Generate values for all optional fields.
   *
   * @return updated builder.
   */
  def includeOptional(): RecordGeneratorBuilder[K] =
    copyWithConfig(config.copy(includeOptionalFields = true))

  /**
   * Do not generate values for optional fields.
   *
   * @return updated builder.
   */
  def excludeOptional(): RecordGeneratorBuilder[K] =
    copyWithConfig(config.copy(includeOptionalFields = false))

  /**
   * Set length for generated collections.
   *
   * @param collectionLength
   * @return updated builder.
   */
  def withCollectionLength(collectionLength: Int): RecordGeneratorBuilder[K] =
    copyWithConfig(config.copy(defaultCollectionLength = collectionLength))

  private[this] def copyWithConfig(config: RecordSchemaDataGeneratorFactory.Config):
    RecordGeneratorBuilder[K] = new RecordGeneratorBuilder(
      companion, customFieldGenerators, config, defaultGeneratorFactories)

  private[this] def withFieldGeneratorOverrides(
      overrides: Map[String, ValueGenerator[_ <: AnyRef]]): RecordGeneratorBuilder[K] =
    new RecordGeneratorBuilder(companion, overrides, config, defaultGeneratorFactories)

}

