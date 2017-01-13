package org.coursera.courier.fixture.generator

import com.linkedin.data.ByteString
import com.linkedin.data.schema.DataSchema
import org.coursera.courier.fixture.generator.DefaultGeneratorFactories.GeneratorFactory

import scala.reflect.ClassTag

class DefaultGeneratorFactories private(factories: Map[String, GeneratorFactory]) {

  import DefaultGeneratorFactories._

  /**
   * Get generator factory for class `klass`, if defined.
   */
  def get(klass: Class[_]): Option[GeneratorFactory] = factories.get(getName(klass))

  /**
   * Get generator factory for schema type `dataType`, if defined.
   */
  def get(dataType: DataSchema.Type): Option[GeneratorFactory] =
    dataTypeClassNames.get(dataType).flatMap(factories.get)

  /**
   * Get generator factory for class or type named `typeName`, if defined.
   */
  def get(typeName: String): Option[GeneratorFactory] = {
    factories.get(typeName).map(Some(_))
      .getOrElse(alternateNames.get(typeName).flatMap(factories.get))
  }

  /**
   * Set the generator factory for objects of type `T`.
   */
  def set[T <: Any](
      factory: GeneratorFactory)
      (implicit tag: ClassTag[T]): DefaultGeneratorFactories = {

    new DefaultGeneratorFactories(factories.updated(getName(tag.runtimeClass), factory))
  }

  /**
   * Set generator factory value by name.
   */
  def setAlias(typeName: String, factory: GeneratorFactory) = {
    new DefaultGeneratorFactories(factories.updated(typeName, factory))
  }

  private[this] def getName(klass: Class[_]): String = {
    klass.getName
  }

}

object DefaultGeneratorFactories {

  type Generator = ValueGenerator[AnyRef]
  type GeneratorFactory = String => Generator

  def apply(): DefaultGeneratorFactories = new DefaultGeneratorFactories(primitiveFactories)

  import DataSchema.Type._

  private def primitiveFactories: Map[String, GeneratorFactory] =
    DEFAULT_PRIMITIVE_GENERATORS.flatMap { case (dataType, factory) =>
      dataTypeClassNames.get(dataType).map(_ -> factory)
    }

  private val DEFAULT_PRIMITIVE_GENERATORS: Map[DataSchema.Type, GeneratorFactory] = Map(
    INT -> { (name: String) => new IntegerRangeGenerator() },
    LONG -> { (name: String) => new LongRangeGenerator() },
    FLOAT -> { (name: String) => new SpanningFloatValueGenerator() },
    DOUBLE -> { (name: String) => new SpanningDoubleValueGenerator() },
    BOOLEAN -> { (name: String) => new TrueFalseValueGenerator() },
    STRING -> { (name: String) => new PrefixedStringGenerator(name) },
    BYTES -> { (name: String) => new StringBytesValueGenerator(name) })

  private val dataTypeClassNames = Map[DataSchema.Type, String](
    INT -> Int.getClass.getName,
    LONG -> Long.getClass.getName,
    FLOAT -> Float.getClass.getName,
    DOUBLE -> Double.getClass.getName,
    BOOLEAN -> Boolean.getClass.getName,
    STRING -> "string",
    BYTES -> "bytes")

  private val alternateNames: Map[String, String] =
    Map(
      "int" -> Int.getClass.getName,
      "long" -> Long.getClass.getName,
      "float" -> Float.getClass.getName,
      "double" -> Double.getClass.getName,
      "boolean" -> Boolean.getClass.getName)

}