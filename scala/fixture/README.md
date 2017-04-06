Courier Fixtures
-----------------

Scala utilities for fixture data generation. 

Usage
-----
`FixtureSugar` provides a simple short-hand for generating Courier record instances: 
```
import org.coursera.courier.fixture.FixtureSugar._

val cookie = fixture[FortuneCookie] // FortuneCookie(message0,Some(0.0),IntArray(0, 1, 2))
```

Under the hood, the `fixture` method creates a _generator_, which produces a series of data instances on-demand:
```
val cookieGenerator = fixtureGenerator[FortuneCookie] 
val first = cookieGenerator.next()  // FortuneCookie(message0,Some(0.0),IntArray(0, 1, 2))
val second = cookieGenerator.next() // FortuneCookie(message1,Some(1.0),IntArray(3, 4, 5))
val third = cookieGenerator.next()  // FortuneCookie(message2,Some(0.5),IntArray(6, 7, 8))
```
Generators are deterministic. Within each generated series, elements strive to be unique.  

Generators follow a builder pattern, and can be customized:
```
fixtureGenerator[FortuneCookie]
    .excludeOptional()
    .next() // FortuneCookie(message0,None,IntArray(0, 1, 2))

fixtureGenerator[FortuneCookie]
    .ignoreDefaults()
    .next() // FortuneCookie(message0,Some(0.0),IntArray(0, 1, 2))

fixtureGenerator[FortuneCookie]
    .withCollectionLength(1)
    .next() // FortuneCookie(message0,Some(0.0),IntArray(0))

fixtureGenerator[FortuneCookie]
    .withField("certainty", 0.75) 
    .next() // FortuneCookie(message0,Some(0.75),IntArray(0, 1, 2))
```

Under the hood, record generators are composed of other generators, each producing a series of values. Field generators can be overridden just like field values: 
```
val wisdomGenerator = fixtureGenerator[FortuneCookie]
    .withField("message", new PrefixedStringGenerator("Happy Birthday!")) 

wisdomGenerator.next().message // "Happy birthday!0"
wisdomGenerator.next().message // "Happy birthday!1"
wisdomGenerator.next().message // "Happy birthday!2"
```

Record data generators know how create to generators for primitive values, nested records, union members, enums, typrefs, arrays, and maps. 

## Generating Coerced Data

Without some extra information, record data generators don't know how to generate data for coerced fields or map keys with custom types. For example, consider the `Fortune` record:
```
record Fortune {
  telling: FortuneTelling 
  createdAt: DateTime
}

```
The `createdAt` field is a `typeref DateTime`, which reads and writes timestamps using a custom coercer, and presents as a `DateTime` within Scala code:
```
@scala.class = "org.joda.time.DateTime"
@scala.coercerClass = "org.coursera.courier.generator.customtypes.DateTimeCoercer"
typeref DateTime = string

```
When we try to generate `val fortune = fixture[Fortune]` the fixture data generator throws an error:

```
[error]   org.coursera.courier.fixture.generator.RecordSchemaDataGeneratorFactory$GeneratorBuilderError: Data schema with property @scala.coercerClass = "org.coursera.courier.generator.customtypes.DateTimeCoercer" must define a custom mock generator class @scala.mockGeneratorClass = ??? to ensure that mock data values are comprehensible.
```

To fix this, we have three increasingly global ways to inform our record generator about how to make `DateTime` strings. Suppose that we have defined a `DateTime` data generator:

```
package org.coursera.courier.generator.customtypes

class ConstantDateTimeGenerator extends CoercedValueGenerator[DateTime] {
    override val coercer = new DateTimeCoercer
    def nextValue(): DateTime = ConstantDateTimeGenerator.time
}

object ConstantDateTimeGenerator {
    val time = new DateTime("2016-07-05T17:19:46.087-07:00")    
}
```


The most local way to fix our generator errors is to override the `createdAt` field value for a single generator:
```
val coercer = new DateTimeCoercer()

val fortune = fixtureGenerator[Fortune]
    .withField("createdAt", coercer.coerceInput(ConstantDateTimeGenerator.time))
    .next() 

```

If, instead, we want to use the same `DateTime` generator in several tests, it would be much more convenient to define overrides within a local scope. To support this, record generators accept configurable default generators, passed implicitly to the `fixtureGenerator` and `fixture` methods:
```
implicit val defaultGenerator: DefaultGeneratorFactories = DefaultGeneratorFactories()
  .set[DateTime]((name: String) => new ConstantDateTimeGenerator)

val fortune = fixture[Fortune]
```

If we're certain that the same generator should be used in all scopes, we can annotate our `typeref` definition, making our generator available to all record generators:

```
@scala.class = "org.joda.time.DateTime"
@scala.coercerClass = "org.coursera.courier.generator.customtypes.DateTimeCoercer"
@scala.fixtureGeneratorClass = "org.coursera.courier.generator.customtypes.ConstantDateTimeGenerator"
typeref DateTime = string

```
then simply:
```
val fortune = fixture[Fortune]
```

Local overrides take precedence over global overrides, so `@scala.fixtureGeneratorClass = ...` is overshadowed by `implicit val defaultGenerator: DefaultGeneratorFactories = ...` which is overshadowed by `fixtureGenerator[Fortune].withField(...)`  

Note that, in most cases, your generator will need to coerce its values for them to be legal data elements. In the examples above `ConstantDateTimeGenerator extends CoercedValueGenerator[DateTime]` produce strings, rather than `DataTime`.

See tests for advanced usage examples. 

Tests
-----

All fixture tests are kept in the sibling `fixture-test` project.
