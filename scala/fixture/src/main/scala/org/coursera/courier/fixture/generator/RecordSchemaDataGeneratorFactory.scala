package org.coursera.courier.fixture.generator

import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.ComplexDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.EnumDataSchema
import com.linkedin.data.schema.FixedDataSchema
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.data.schema.PrimitiveDataSchema
import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.data.schema.RecordDataSchema.Field
import com.linkedin.data.schema.TyperefDataSchema
import com.linkedin.data.schema.UnionDataSchema
import com.linkedin.data.template.DataTemplateUtil

import scala.collection.JavaConverters._
import DefaultGeneratorFactories.Generator
import com.linkedin.data.codec.JacksonDataCodec

private[fixture] class RecordSchemaDataGeneratorFactory(
    recordSchema: RecordDataSchema,
    fieldGeneratorOverrides: Map[String, ValueGenerator[_ <: AnyRef]],
    config: RecordSchemaDataGeneratorFactory.Config,
    defaultGeneratorFactories: DefaultGeneratorFactories) {

  import RecordSchemaDataGeneratorFactory._

  /**
    * @return A [[DataMapValueGenerator]] whose generated values conform to `recordSchema`.
    */
  def build(): DataMapValueGenerator = {
    val fieldGenerators: Map[String, ValueGenerator[_ <: AnyRef]] =
      recordSchema.getFields.asScala.flatMap { field =>
        makeFieldGeneratorIfRequired(field, recordSchema).map(
          field.getName -> _)
      }.toMap
    new DataMapValueGenerator(fieldGenerators)
  }

  /** Builder configuration methods */
  private[this] def makeFieldGeneratorIfRequired(
      field: Field,
      parentSchema: RecordDataSchema): Option[ValueGenerator[_ <: AnyRef]] = {

    fieldGeneratorOverrides.get(field.getName).map(Some(_)).getOrElse {
      if (!config.includeOptionalFields && field.getOptional) {
        None
      } else {
        Some(makeFieldGenerator(field, parentSchema))
      }
    }
  }

  private[this] def makeFieldGenerator(
      field: Field,
      parentSchema: RecordDataSchema): Generator = {
    if (field.getDefault != null && config.useSchemaDefaults) {
      new ConstantValueGenerator(field.getDefault)
    } else {
      makeSchemaGenerator(field.getName, field.getType)
    }
  }

  private[this] def makeSchemaGenerator(name: String,
                                        dataSchema: DataSchema): Generator = {

    makeDefaultGeneratorForClassName(name, dataSchema).getOrElse {
      makeSchemaReferencedGenerator(name, dataSchema).getOrElse {

        if (config.requireCustomGeneratorsForCoercedTypes)
          verifyNoCoercer(dataSchema)

        dataSchema match {
          case schema: PrimitiveDataSchema =>
            makePrimitiveGenerator(name, schema)
          case schema: ComplexDataSchema =>
            makeComplexGenerator(name, schema)
        }
      }
    }
  }

  private[this] def makeDefaultGeneratorForClassName(
      name: String,
      dataSchema: DataSchema): Option[Generator] = {

    getScalaClassName(dataSchema)
      .flatMap(defaultGeneratorFactories.get)
      .map(_.apply(name))
  }

  /**
    * Instantiate the generator class associated with the schema, if defined.
    */
  private[this] def makeSchemaReferencedGenerator(
      name: String,
      dataSchema: DataSchema): Option[Generator] = {

    getGeneratorClassName(dataSchema)
      .map { generatorClassName =>
        Class.forName(generatorClassName).newInstance() match {
          case generator: ValueGenerator[_] => generator
          case other: Any =>
            throw new GeneratorBuilderError(
              s"Expected custom generator with class $generatorClassName to be of type " +
                s"ValueGenerator[AnyRef].")
        }
      }
      .orElse {
        dataSchema match {
          case typeref: TyperefDataSchema =>
            // If this is a typeref schema, and does not define a custom gnerator of its own, check
            // to see if the type it references has a custom generator. This allows us to (for
            // example), declare a single UUID typeref with a custom generator, and let wrapped
            // UUIDs inherit that generator definition.
            makeSchemaReferencedGenerator(name, typeref.getRef)
          case _ => None
        }
      }
  }

  /**
    * Verify that there is no coercer associated with the schema.
    */
  private[this] def verifyNoCoercer(dataSchema: DataSchema): Unit = {
    getCoercerClassName(dataSchema).foreach { coercerClassName =>
      val msg =
        s"""Data schema with property @scala.coercerClass = "$coercerClassName" """ +
          s"must define a custom mock generator class @scala.fixtureGeneratorClass = ??? to " +
          s"ensure that mock data values are comprehensible. \n " +
          s"See [[org.example.common.DateTime]] for an example generator class definition.\n"
      throw GeneratorBuilderError(msg)
    }
  }

  private[this] def makePrimitiveGenerator(
      name: String,
      primitiveSchema: PrimitiveDataSchema): Generator = {

    defaultGeneratorFactories
      .get(primitiveSchema.getType)
      .map(_.apply(name))
      .getOrElse(throw GeneratorBuilderError(
        s"Unsupported schema type ${primitiveSchema.getType} for schema $primitiveSchema."))
  }

  private[this] def makeComplexGenerator(
      name: String,
      complexSchema: ComplexDataSchema): Generator = {

    complexSchema match {
      case schema: EnumDataSchema =>
        new CyclicEnumStringGenerator(schema.getSymbols.asScala.toSet)
      case schema: RecordDataSchema =>
        new RecordSchemaDataGeneratorFactory(schema,
                                             Map.empty[String, Generator],
                                             config,
                                             defaultGeneratorFactories).build()
      case schema: TyperefDataSchema =>
        makeSchemaGenerator(name, schema.getRef)
      case schema: ArrayDataSchema =>
        val itemGenerator =
          makeSchemaGenerator(name, schema.getItems)
        new ListValueGenerator(itemGenerator, config.defaultCollectionLength)
      case schema: MapDataSchema =>
        mapGeneratorFactory(name, schema)
      case schema: UnionDataSchema =>
        val generators = schema.getTypes.asScala.toList.map { memberSchema =>
          new DataMapValueGenerator(
            Map(
              memberSchema.getUnionMemberKey -> makeSchemaGenerator(
                name,
                memberSchema)))
        }
        new CyclicGenerator(generators)
      case schema: FixedDataSchema => fixedGenerator(schema.getSize)
    }
  }

  private[this] def mapGeneratorFactory(name: String,
                                        mapSchema: MapDataSchema): Generator = {
    val keyGenerator = Option(mapSchema.getProperties.get("keys"))
      .getOrElse {
        "string" // String key type implied when `keys` property is absent.
      } match {
      case data: DataMap =>
        // `data` i un-parsed schema value
        makeSchemaGenerator(name,
                            DataTemplateUtil.parseSchema(dataToJson(data)))
      case typeString: String =>
        defaultGeneratorFactories
          .get(typeString)
          .map(_.apply(name))
          .getOrElse {
            val keySchema = MapKeySchemaRegistry.getKeySchema(typeString, mapSchema.getValues)
            makeSchemaGenerator(name, keySchema)
          }
    }

    val valueGenerator =
      makeSchemaGenerator(name, mapSchema.getValues)
    new MapValueGenerator(keyGenerator,
                          valueGenerator,
                          config.defaultCollectionLength)
  }

  private[this] def fixedGenerator(length: Int): FixedBytesValueGenerator = {
    new IntegerRangeFixedBytesGenerator(length)
  }
}

object RecordSchemaDataGeneratorFactory {

  val SCALA = "scala"
  val COERCER_CLASS_PROPERTY = "coercerClass"
  val MOCK_GENERATOR_CLASS_PROPERTY = "fixtureGeneratorClass"
  val CLASS_NAME_PROPERTY = "class"

  def getScalaClassName(dataSchema: DataSchema): Option[String] =
    getScalaProperty(dataSchema, CLASS_NAME_PROPERTY)

  def getCoercerClassName(dataSchema: DataSchema): Option[String] =
    getScalaProperty(dataSchema, COERCER_CLASS_PROPERTY)

  def getGeneratorClassName(dataSchema: DataSchema): Option[String] =
    getScalaProperty(dataSchema, MOCK_GENERATOR_CLASS_PROPERTY)

  case class Config(useSchemaDefaults: Boolean = true,
                    includeOptionalFields: Boolean = true,
                    defaultCollectionLength: Int = 3,
                    requireCustomGeneratorsForCoercedTypes: Boolean = true)

  private[this] def getScalaProperies(
      dataSchema: DataSchema): Option[DataMap] = {
    dataSchema.getProperties.asScala.get(SCALA).flatMap {
      case data: DataMap => Some(data)
      case _             => None
    }
  }

  private[this] def getScalaProperty(dataSchema: DataSchema,
                                     propertyName: String): Option[String] = {
    getScalaProperies(dataSchema).flatMap { properties =>
      Option(properties.getString(propertyName))
    }
  }

  case class GeneratorBuilderError(msg: String)
      extends IllegalArgumentException(msg)

  val jacksonCodec = new JacksonDataCodec()
  private def dataToJson(value: Any): String = {
    value match {
      case str: String => s""""$str""""
      case data: DataMap => jacksonCodec.mapToString(data)
      case list: DataList => jacksonCodec.listToString(list)
      case number: Number             => number.toString
      case boolean: java.lang.Boolean => boolean.toString
      case _ =>
        throw new IllegalArgumentException(
          s"Unexpected type for value $value, cannot convert to JSON.")
    }
  }
}
