package org.coursera.courier.fixture

import org.coursera.courier.fixture.generator.CyclicEnumSymbolGenerator
import org.coursera.courier.fixture.generator.DefaultGeneratorFactories
import org.coursera.courier.fixture.generator.IntegerRangeFixedBytesGenerator
import org.coursera.courier.fixture.generator.MapValueGenerator
import org.coursera.courier.fixture.generator.PrefixedStringGenerator
import org.coursera.courier.fixture.generator.RecordSchemaDataGeneratorFactory
import org.coursera.courier.fixture.generator.ValueGenerator
import org.coursera.courier.generator.customtypes.CoercedCustomIntGenerator
import org.coursera.courier.generator.customtypes.CustomInt
import org.coursera.enums.Fruits
import org.coursera.fixed.Fixed8
import org.coursera.maps.Toggle
import org.coursera.maps.WithComplexTypesMap
import org.coursera.maps.WithCustomTypesMap
import org.coursera.maps.WithTypedKeyMap
import org.coursera.records.test.Simple
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitSuite

import org.coursera.courier.fixture.FixtureSugar._


class MapFieldsTest extends JUnitSuite with AssertionsForJUnit {

  val COLLECTION_LENGTH = 2

  @Test
  def customTypeValues(): Unit = {
    val element = fixtureGenerator[WithCustomTypesMap]
      .withCollectionLength(COLLECTION_LENGTH)
      .next()

    assertResult(COLLECTION_LENGTH)(element.ints.size)
  }

  @Test
  def complexTypeValues(): Unit = {
    val element = fixtureGenerator[WithComplexTypesMap]
      .withCollectionLength(COLLECTION_LENGTH)
      .next()

    assertResult(COLLECTION_LENGTH)(element.empties.size)
    assertResult(COLLECTION_LENGTH)(element.fruits.size)
    assertResult(COLLECTION_LENGTH)(element.arrays.size)
    assertResult(COLLECTION_LENGTH)(element.maps.size)
    assertResult(COLLECTION_LENGTH)(element.unions.size)
    assertResult(COLLECTION_LENGTH)(element.fixed.size)
  }

  @Test
  def typedKeys_WithoutFixedOverrideOnly_Succeeds(): Unit = {
    // For some reason, the factory-constructed key generator for `fixed`
    // generates byte strings of the wrong length. This does not happen for generated values
    // in other contexts. Leaving as-is for now since fixed-type data is not widely used
    // by Coursera, least of all as map keys. 
    def fixedKeyMapGenerator = new MapValueGenerator(
      new IntegerRangeFixedBytesGenerator(8).map(Fixed8(_)),
      new PrefixedStringGenerator("value"), COLLECTION_LENGTH)

    val element = fixtureGenerator[WithTypedKeyMap]
      .withField("fixed", fixedKeyMapGenerator)
      .withCollectionLength(COLLECTION_LENGTH).next()

    assertResult(COLLECTION_LENGTH)(element.record.size)
    assertResult(COLLECTION_LENGTH)(element.enum.size)
    assertResult(COLLECTION_LENGTH)(element.custom.size)
    assertResult(COLLECTION_LENGTH)(element.fixed.size)
    assertResult(COLLECTION_LENGTH)(element.samePackageEnum.get.size)
  }

  @Test
  def typedKeys_withMapFields(): Unit = {
    def mapGenerator(keyGenerator: ValueGenerator[_ <: AnyRef]) =
      new MapValueGenerator(keyGenerator, new PrefixedStringGenerator("value"), COLLECTION_LENGTH)

    val element = fixtureGenerator[WithTypedKeyMap]
      .withField("record", mapGenerator(fixtureGenerator[Simple]))
      .withField("enum", mapGenerator(new CyclicEnumSymbolGenerator(Fruits)))
      .withField("custom", mapGenerator(new CoercedCustomIntGenerator))
      .withField("fixed", mapGenerator(new IntegerRangeFixedBytesGenerator(8).map(Fixed8(_))))
      .withField("samePackageEnum", mapGenerator(new CyclicEnumSymbolGenerator(Toggle)))
      .next()

    assertResult(COLLECTION_LENGTH)(element.record.size)
    assertResult(COLLECTION_LENGTH)(element.enum.size)
    assertResult(COLLECTION_LENGTH)(element.custom.size)
    assertResult(COLLECTION_LENGTH)(element.fixed.size)
    assertResult(COLLECTION_LENGTH)(element.samePackageEnum.get.size)
  }

  @Test
  def typedKeys_ImplicitDefaultGenerators(): Unit = {
    val simpleGenerator = fixtureGenerator[Simple](
      Simple,
      defaultGeneratorFactories = DefaultGeneratorFactories())

    // With default `DefaultGeneratorFactories` defined implicitly for each custom key type, the
    // data generator factory can infer key generators for custom map types.
    implicit val defaultGenerator: DefaultGeneratorFactories =
      DefaultGeneratorFactories()
        .setUnchecked[Simple](simpleGenerator)
        .set[Fruits](new CyclicEnumSymbolGenerator(Fruits))
        // name of `CustomInt` typref schema does not match `CustomInt` class name, so use
        // `setAlias` instead of `set[CustomInt]`
        .setAlias("org.coursera.customtypes.CustomInt",
          (name: String) => new CoercedCustomIntGenerator)
        .set[Toggle](new CyclicEnumSymbolGenerator(Toggle))
        .setUnchecked[Fixed8](new IntegerRangeFixedBytesGenerator(8).map(Fixed8(_)))

    val element = fixtureGenerator[WithTypedKeyMap]
      .withCollectionLength(COLLECTION_LENGTH)
      .next()

    assertResult(COLLECTION_LENGTH)(element.record.size)
    assertResult(COLLECTION_LENGTH)(element.enum.size)
    assertResult(COLLECTION_LENGTH)(element.custom.size)
    assertResult(COLLECTION_LENGTH)(element.fixed.size)
    assertResult(COLLECTION_LENGTH)(element.samePackageEnum.get.size)
  }

}
