package org.coursera.courier.mock

import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.BooleanDataSchema
import com.linkedin.data.schema.ComplexDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.DoubleDataSchema
import com.linkedin.data.schema.EnumDataSchema
import com.linkedin.data.schema.FixedDataSchema
import com.linkedin.data.schema.FloatDataSchema
import com.linkedin.data.schema.IntegerDataSchema
import com.linkedin.data.schema.LongDataSchema
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.data.schema.PrimitiveDataSchema
import com.linkedin.data.schema.RecordDataSchema.Field
import com.linkedin.data.schema.StringDataSchema
import com.linkedin.data.schema.TyperefDataSchema

import collection.JavaConverters._

import com.linkedin.data.schema.RecordDataSchema

class RecordSchemaGeneratorBuilder(
    recordSchema: RecordDataSchema,
    config: RecordSchemaGeneratorBuilder.Config) {

  def build(): DataMapValueGenerator = {
    val fieldGenerators: Map[String, ValueGenerator[_ <: AnyRef]] =
      recordSchema.getFields.asScala.flatMap { field =>
        getFieldGenerator(field).map(field.getName -> _)
      }.toMap
    new DataMapValueGenerator(fieldGenerators)
  }

  private[this] def getFieldGenerator(field: Field):
    Option[ValueGenerator[_ <: AnyRef]] = {

    config.fieldGeneratorOverrides.get(field.getName).map(Some(_)).getOrElse {
      if (!config.includeOptionalFields && field.getOptional) {
        None
      } else {
        Some(makeFieldGenerator(field))
      }
    }
  }

  private[mock] def makeFieldGenerator(field: Field): ValueGenerator[_ <: AnyRef] = {
    if (field.getDefault != null) {
      new ConstantValueGenerator(field.getDefault)
    } else {
      makeSchemaValueGenerator(field.getName, field.getType)
    }
  }

  private[mock] def makeSchemaValueGenerator(
      name: String,
      dataSchema: DataSchema): ValueGenerator[_ <: AnyRef] = {

    dataSchema match {
      case schema: PrimitiveDataSchema => makePrimitiveSchemaValueGenerator(name, schema)
      case schema: ComplexDataSchema => makeComplexSchemaDataGenerator(name, schema)
    }
  }

  def makePrimitiveSchemaValueGenerator(
      name: String,
      primitiveSchema: PrimitiveDataSchema): PrimitiveValueGenerator[_ <: AnyRef] = {

    primitiveSchema match {
      case schema: BooleanDataSchema => new TrueFalseValueGenerator
      case schema: IntegerDataSchema => new IntegerRangeGenerator()
      case schema: LongDataSchema => new LongRangeGenerator()
      case schema: FloatDataSchema => new SpanningFloatValueGenerator()
      case schema: DoubleDataSchema => new SpanningDoubleValueGenerator()
      case schema: StringDataSchema => new PrefixedStringGenerator(name)
        // TODO amory: throw error
//      case schema: BytesDataSchema => ???
//      case schema: NullDataSchema => ???
    }
  }

  def makeComplexSchemaDataGenerator(
      name: String,
      complexSchema: ComplexDataSchema): ValueGenerator[_ <: AnyRef] = {

    complexSchema match {
//      case schema: EnumDataSchema =>
//      case schema: FixedDataSchema =>
//      case schema: RecordDataSchema =>
//      case schema: TyperefDataSchema =>
      case schema: ArrayDataSchema =>
        val itemGenerator = makeSchemaValueGenerator(name, schema.getItems)
        new ListValueGenerator(itemGenerator, config.defaultCollectionLength)
      case schema: MapDataSchema =>
        val keyGenerator: StringKeyGenerator = ???
        val valueGenerator = makeSchemaValueGenerator(name, schema.getValues)
        new MapValueGenerator(keyGenerator, valueGenerator, config.defaultCollectionLength)
    }
  }
}

object RecordSchemaGeneratorBuilder {

  case class Config(
      fieldGeneratorOverrides: Map[String, ValueGenerator[_ <: AnyRef]] = Map.empty,
      useDefaultFields: Boolean = true,
      includeOptionalFields: Boolean = true,
      defaultCollectionLength: Int = 3)

}
