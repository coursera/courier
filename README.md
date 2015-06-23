Courier
=======

Generate Scala idiomatic data bindings from schemas and use them with multiple data formats
including JSON and [Avro](http://avro.apache.org/).

* [Source](http://github.com/coursera/courier)
* [Documentation](https://github.com/coursera/courier/wiki)
* [Discussion Group](https://groups.google.com/d/forum/courier)

Overview
--------

Courier is a language binding for Scala for the
[Pegasus](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates) schema and data
system, part of the [Rest.li](http://rest.li) umbrella project.

Pegasus contains an expressive schema language for JSON structured data that is based on the Avro
schema language, but adds optional fields a few other conveniences to make it easy to define the
structure of natural looking JSON. Pegasus also has a rich feature set including schema
based validation, data translation between multiple data formats, schema compatibility with
Avro, and generated Java data bindings.

By using Courier, all the features of Pegasus can be leveraged by Scala developers but with
Scala idiomatic data bindings that look and feel natural to a Scala developer.

Features
--------

* Scala Idiomatic Data Binding Generator
* SBT Plugin
* Data and Schema Compatible with [Pegasus](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates)
* Data and Schema Compatible with [Avro](http://avro.apache.org/)
* Support for multiple "Data Codecs", including JSON, PSON, and Avro binary

Development Status
------------------

Courier is currently in the early development phases. Our current goal is to have the generator
built and ready for early adoption by end of June.

For the current design proposal, see: [Courier Design](https://github.com/coursera/courier/wiki/Design)

For development progress, see: [Discussion Group](https://groups.google.com/d/forum/courier)

Getting Started
---------------

Add the generator dependencies to your SBT plugins:

`project/plugins.sbt`:

```scala
addSbtPlugin("org.coursera.courier" % "courier-sbt-plugin" % "0.1.7")
```

Enable the generator to SBT build:

`project/Build.scala`:

```scala
import sbt._
import Keys._
import org.coursera.courier.sbt.CourierPlugin._

object Example extends Build {

  val courierVersion = "0.1.6"

  lazy val example = Project("example", file("example"))
    .dependsOn(schemas)
    .aggregate(schemas)
    // ...

  lazy val schemas = Project("schemas", file("schemas"))
    .settings(courierSettings: _*)
    .settings(libraryDependencies += "org.coursera.courier" %% "courier-runtime" % courierVersion)
}
```

Add `.pdsc` files to the `src/main/pegasus` directory of your project. For example:

`schemas/src/main/pegasus/org/example/fortune/Fortune.pdsc`:

```json
{
  "name": "Fortune",
  "namespace": "org.example.fortune",
  "type": "record",
  "fields": [
    { "name": "message", "type": "string" }
  ]
}

```

In SBT, run:

```sh
project example
compile
```

When run, the `org.example.fortune.Fortune` Scala class is generated. It behaves the same as
a case class, but can be serialized to JSON, or any other data format a Pegasus codec is available
for.  For example:

`example/src/main/scala/Main.scala`:

```scala
import com.linkedin.data.template.PrettyPrinterJacksonDataTemplateCodec
import org.example.fortune.Fortune

object Example extends App {
  val fortune = Fortune(message = "Today is your lucky day!")

  val codec = new PrettyPrinterJacksonDataTemplateCodec
  println(codec.mapToString(fortune.dataMap))
  // -> { "message": "Today is your lucky day!" }
}
```

The generator is run automatically before `src/main/scala` compilation. It also registers for
triggered execution to support SBT commands like `~compile`, which will cause the generator to
run immediately whenever a .pdsc file is changed.

The generator will write Scala files to the `target/scala-<scalaMajorVersion>/courier` directory of
your project and add them to the compile classpath.

For details on the `.pdsc` file format, see
[Pegasus Schemas and Data](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates).

The code generator is an extension of the Rest.li SBT Plugin, for more details, see
[the rest.li-sbt-plugin wiki](https://github.com/linkedin/rest.li-sbt-plugin).

#### Testing

`.pdsc` files only needed for tests may be added to `src/test/pegasus`.

Record Types
------------

[Pegasus Records](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates#record-type)
contain any number of fields, which may be any pegasus type, including
primitives, enums, unions, maps and arrays.

For example, a basic record type containing a few fields:

```json
{
  "name": "Example",
  "namespace": "org.example",
  "doc": "A simple record.",
  "type": "record",
  "fields": [
    { "name": "field1", "type": "string" },
    { "name": "field2", "type": "int", "optional": true }
  ]
}
```

This will be generated as:

```scala
/** A simple record */
case class Example(field1: String, field2: Option[Int])
```

[Record Fields](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates#record-field-attributes)
may be optional and/or may have default values.

Schema Field                                 | Generated Scala
---------------------------------------------|------------------------------------------------------
`"type": "string"`                           | `case class Record(field: String)`
`..., "default": "message"`                  | `case class Record(field: String = "message")`
`..., "optional": true`                      | `case class Record(field: Option[String])`
`..., "optional": true "default": "message"` | `case class Record(field: Option[String] = Some("message"))`
`..., "optional": true, "defaultNone": true` | `case class Record(field: Option[String] = None)`

Note that `"defaultNone"` is not part of Pegasus, but is a custom property supported by Courier
specifically added it make it possible to generate idiomatic Scala bindings.

Schema fields may also be documented or marked as deprecated:

Schema Field                                 | Generated Scala
---------------------------------------------|------------------------------------------------------
`..., "doc": "A documented field"`           | `case class Record(/** A documented field */ field: String)`
`..., "deprecated": "Use field X instead"`   | `case class Record(@deprecated(message = "Use field X instead") field: String)`

Records may [include fields from other records](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates#including-fields-from-another-record)
using `"include"`:

```scala
{
  "name" : "WithIncluded",
  "type" : "record",
  "include" : [ "Foo" ],
  "fields" : [ ... ]
}
```

In pegasus, field inclusion does not imply inheritance, it is merely a
convenience to reduce duplication when writing schemas.

#### Record Backward Compatibility

The backward compatibility rules for records are:

Compatible changes:

* Adding an optional fields
* Adding a field with a default (required or optional)

When accessing fields:

* Unrecognized fields must be ignored.
* Fields with defaults should always be written, either with the desired value or
  the default value.
* The default value for a field should be assumed if the field is absent and is
  needed by the reader.

Primitive Types
---------------

The [Pegasus primitive](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates#primitive-types)
types are: int, long, float, double, boolean, string and bytes.

Schema Type | Scala Type    | Example JSON data
------------|---------------|------------------
"int"       | Int           | 100
"long"      | Long          | 10000000
"float"     | Float         | 3.14
"double"    | Double        | 2.718281
"boolean"   | Boolean       | true
"string"    | String        | "coursera"
"bytes"     | ByteString    | "\u0001\u0002"

A 'null' type also exists, but should generally be avoided in favor of optional fields.

Array Type
----------

[Pegasus Arrays](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates#array-type)
are defined with a `items` type using the form:

```json
{ "type": "array", "items": "org.example.Fortune" }
```

This will be generated as:

```scala
class FortuneArray extends IndexedSeq[Fortune]
```

For example, to define a field of a record containing an array, use:

```json
{
  "name": "Fortune",
  "namespace": "org.example.fortune",
  "type": "record",
  "fields": [
    { "name": "arrayField", "type": { "type": "array", "items": "int" } }
  ]
}
```

This will be generated as:

```scala
case class Fortune(arrayField: IntArray)
```

Array items may be any pegasus type.

The array types for all primitive value types (`IntArray`, `StringArray`, ...) are pre-generated by Courier and
provided in the `courier-runtime` artifact in the `org.coursera.courier.data` package. The generator
is aware of these classes and will refer to them instead of generating them when primitive arrays are used.

Schema type                                         | Scala type
----------------------------------------------------|--------------------------------------------------
`{ "type": "array", "items": "int" }`               | `org.coursera.courier.data.IntArray` (predefined)
`{ "type": "array", "items": "org.example.Record" }`| `org.example.RecordArray` (generated)

All generated Arrays implement Scala's `IndexedSeq`, `Traversable` and `Product` traits and behave
like a standard Scala collection type.

```scala
val array = IntArray(10, 20, 30)

array(0)

array.map { int => ... }

array.zipWithIndex

array.filter(_ > 20)

array.toSet
```

Unsurprisingly, Pegasus arrays are represented in JSON as arrays.

Scala Expression                                  | Equivalent JSON data
--------------------------------------------------|------------------------------------
IntArray(1, 2, 3)                                 |`[1, 2, 3]`
RecordArray(Record(field = 1), Record(field = 2)) |`[ { "field": 1 }, { "field": 2 } ]`


Ordinarily, arrays are defined inline inside other types. But if needed,
typerefs allow a map to be defined in a separate .pdsc file and be assigned a
unique type name. See below for more details about typerefs.

Map Type
--------

[Pegasus Maps](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates#map-type)
are always defined with a `values` type using the form:

```json
{ "type": "map", "values": "org.example.Fortune" }
```

This will be generated as:

```scala
class FortuneMap extends Map[String, Fortune]
```

All maps are keyed by `String`.

For example, to define a field of a record containing a map, use:

```json
{
  "name": "Fortune",
  "namespace": "org.example.fortune",
  "type": "record",
  "fields": [
    { "name": "mapField", "type": { "type": "amp", "values": "int" } }
  ]
}
```

This will be generated as:

```scala
case class Fortune(mapField: IntMap)
```

Like arrays, map values can be of any type, and the map types for all primitives
are predefined.

Schema type                                        | Scala type
---------------------------------------------------|------------------------------------------------
`{ "type": "map", "values": "int" }`               | `org.coursera.courier.data.IntMap` (predefined)
`{ "type": "map", "values": "org.example.Record" }`| `org.example.RecordMap` (generated)

All generated Maps implement Scala's `Map` and `Iterable` traits and behave
like a standard Scala collection type.

```scala
val map = IntMap("a" -> 1, "b" -> 2, "c" -> 3)

map.get("a")

map.getOrElse("b", 0)

map.contains("c")

map.mapValues { v => ... }

map.filterKeys { _.startsWith("a") }
```

Maps are represented in JSON as objects:

Scala Expression                                              | Equivalent JSON data
--------------------------------------------------------------|--------------------------------------------
IntMap("a" -> 1, "b" -> 2, "c" -> 3)                          |`{ "a": 1, "b": 2, "c": 3 }`
RecordMap("a" -> Record(field = 1), "b" -> Record(field = 2)) |`{ "a": { "field": 1 }, "b": { "field": 2 } }`


Ordinarily, maps are defined inline inside other types. But if needed,
typerefs allow a map to be defined in a separate .pdsc file and be assigned a
unique type name name. See below for more details about typerefs.

Union Type
----------

[Pegasus Unions](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates#union-type)
 are [tagged union](http://en.wikipedia.org/wiki/Tagged_union) types.

A union type may be defined with any number of member types.  Each member may be any pegasus
type except union: primitive, record, enum, map or array.

Unions types are defined in using the form:

```json
[ "<MemberType1>", "<MemberType2>" ]
```

For example, a union that holds an `int`, `string` or a `Fortune`
would be defined as:

```json
[ "int", "string", "org.example.Fortune" ]
```

The member type names also serve as the "member keys" (sometimes called "union tags"),
and identify which union member type data holds.

For example:

Schema type           | Member key            | Example JSON data
----------------------|-----------------------|---------------------------------------------
"int"                 | "int"                 | `{ "int": 1 }`
"string"              | "string"              | `{ "string": "coursera" }`
"org.example.Fortune" | "org.example.Fortune" | `{ "org.example.Fortune": { "message": "Today is your lucky day!" }`

Let's look at an example of a union in use.  To define a field of a record containing a
union of two other records, we would define:

```json
{
  "name": "Question",
  "namespace": "org.example",
  "type": "record",
  "fields": [
    { "name": "answerFormat", "type": [ "MultipleChoice", "TextEntry" ] }
  ]
}
```

This will be generated as:

```scala
case class Question(answerFormat: Question.AnswerFormat)

object Question {
  // ...

  sealed abstract class AnswerFormat()
  case class MultipleChoiceMmeber(value: MultipleChoice) extends AnswerFormat
  case class TextEntryMember(value: TextEntry) extends AnswerFormat
  case class $UnknownMember() extends AnswerFormat // for backward compatibility (see below)
}
```

Here, because the union was defined inline in in the Question record, it is
generated as a class scoped within Question type.
It is also assigned a name based on the field is is contained in.

If the union were instead defined with a typeref it would be assigned the name
of the typeref and be generated as a top level type. This will be covered
in more detail later.

Note that each member type is "boxed" in a `<Type>Member` case class. This is
because Scala does not (yet) support disjoint types directly in the type system.

Here's how the `AnswerFormat` union can be used to create a new `Question`:

Scala Expression                                     | Equivalent JSON data
-----------------------------------------------------|-----------------------------------------------------
`Question(TextEntryMember(TextEntry(...)))`           | `{ "answerFormat": { "org.example.TextEntry": { ... } } }`
`Question(MultipleChoiceMmeber(MultipleChoice(...)))` | `{ "answerFormat": { "org.example.MultipleChoice": { ... } }}`

To read the union, pattern matching may be used, e.g.:

```scala
question.answerFormat match {
  case TextEntryMember(textEntry) => ...
  case MultipleChoiceMember(multipleChoice) => ...
  case $UnknownMember => ... // for backward compatibility (see below)
}
```

Because the union is defined using a sealed base type, Scala can statically
check that the cases used are exhaustive.

The member key of primitives, maps, arrays and unions are the same as their type name:

 Scala Expression                         | Equivalent JSON data
------------------------------------------|-------------------------
`Record(field = IntMember(1))`                    | `{ "field": { "int": 1 } }`
`Record(field = StringMember("a"))`               | `{ "field": { "string": "a" } }`
`Record(field = IntMapMember(IntMap("a" -> 1)))`  | `{ "field": { "map": { "a": 1 } } }`
`Record(field = IntArrayMember(IntArray(1,2,3)))` | `{ "field": { "array": [1, 2, 3] } }`

Ordinarily, unions are defined inside other types. But if needed,
typerefs may be used to define a union in a separate .pdsc file and give the union
any desired name. See below for more details about typerefs.

#### Union Backward Compatibility

`$UnknownMember` indicates an unrecognized union member was read
from serialized data. `$UnknownMember` is primarily intended to ease
managing backward compatibility in systems where reader and writers of the data
may be using different versions of a schema, because, in such system, a reader might
receive data containing union members they do not yet recognize.

Note that the presence of the `$UnknownMember` symbol does not, by itself, guarantee
that adding an a member to the a union is backward compatible. In order to ensure
this, one must be sure that all readers of the union
handle reading the `$UnknownMember` in a backward compatible way. Depending on
the semantic meaning of the union, this may or may not be possible, and so
the backward compatibility of changes to union members should be approached
with care.

Enum Type
---------

[Pegasus enums](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates#enum-type)

Enums types may contain any number of symbols, for example:

```json
{
  "type" : "enum",
  "name" : "Fruits",
  "namespace" : "org.example",
  "symbols" : ["APPLE", "BANANA", "ORANGE"]
}
```

This will be generated as:

```scala
object Fruits extend Enumeration
```

where symbols are referenced as:

```scala
Fruits.APPLE
```

and the enum's Scala type is:

```scala
Fruits.Fruits
```

Enums are referenced in other schemas either by name, e.g.:

```json
{
  "type": "record",
  "name": "FruitBasket",
  "namespace": "org.example",
  "fields": [
    { "name": "fruit", "type": "org.example.Fruits" }
  ]
}
```

..or by inlining their type definition, e.g.:

```json
{
  "type": "record",
  "name": "FruitBasket",
  "namespace": "org.example",
  "fields": [
    {
      "name": "fruit",
      "type": {
        "type": "enum",
        "name": "Fruits",
        "symbols": ["APPLE", "BANANA", "ORANGE"]
      }
    }
  ]
}
```

This fully generated enum looks like:

```scala
object Fruits extends Enumeration {
  type Fruits = Value

  val APPLE = Value("APPLE")
  val BANANA = Value("BANANA")
  val ORANGE = Value("ORANGE")

  val $UNKNOWN = Value("$UNKNOWN") // for backward compatibility (see below)
}
```

Enums are represented in JSON as strings, e.g. `"APPLE"`

#### Enum Backward Compatibility

`$UNKNOWN` indicates an unrecognized symbol was
read from serialized data. `$UNKNOWN` is primarily intended to ease
managing backward compatibility in systems where reader and writers of the data
may be using different versions of a schema, because, in such system, a reader might
receive data containing enum symbols they do not yet recognize.

Note that the presence of the `$UNKNOWN` symbol does not, by itself, guarantee
that adding an a symbol to the enum is backward compatible. In order to ensure
this, one must be sure that all readers of the enum
handle the `$UNKNOWN` symbol in a backward compatible way. Depending on
the semantic meaning of the enum, this may or may not be possible, and so
the backward compatibility of changes to enum symbols should be approached
with care.

Typerefs
--------

[Pegasus Typerefs](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates#typeref)
provide a lightweight alias to any other type.

They can be used for a variety of purposes. A few common uses:

(1) Provide a name for a union, map, or array so that it can be referenced by name. E.g.:

```json
{
  "name": "AnswerTypes",
  "namespace": "org.example",
  "type": "typeref",
  "ref": ["MutlipleChoice", "TextEntry"]
}

```

This will be generated as:

```scala
abstract class AnswerTypes
case class MutlipleChoiceMember(value: MutlipleChoice) extends AnswerTypes
case class TextEntryMember(value: TextEntry) extends AnswerTypes
```

And can be referred to from any other type using the name
`org.example.AnswerTypes`, e.g.:

```json
{
  "type": "record",
  "name": "Question",
  "namespace": "org.example",
  "fields": [
    { "name": "answerFormat", "type": "org.example.AnswerTypes" }
  ]
}
```

This is particularly useful because unions, maps and arrays cannot otherwise be
named directly like records and enums can.

(2) Provide additional clarity when using primitive types for specific purposes.

```json
{
  "name": "UnixTimestamp",
  "namespace": "org.example",
  "type": "typeref",
  "ref": "long"
}
```

No classes will be generated for this typeref. In Scala, typerefs to primitives
are simply bound to their reference types (unless the typref is defined as a
custom, see below for details).  E.g. `UnixTypestamp` will simply be bound
to `Long` in Scala.

Custom Types
------------

[Pegasus Custom Types](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates#custom-java-class-binding-for-primitive-types)
allow any Scala type to be bound to any pegasus primitive type.

For example, [Joda time](http://www.joda.org/joda-time/) has a convenient
`DateTime` class. If we wish to use this class in Scala to represent date times,
all we need to do is define a pegasus custom type that binds to it:

```json
{
  "name": "DateTime",
  "namespace": "org.example",
  "type": "typeref",
  "ref": "string",
  "doc": "ISO 8601 date-time.",
  "scala": {
    "class": "org.joda.time.DateTime",
    "coercerClass": "org.coursera.models.common.DateTimeCoercer"
  }
}

```

The coercer is responsible for converting the pegasus "referenced" type, in this
case `"string"` to the Joda `DateTime` class:

```scala
class DateTimeCoercer extends DirectCoercer[DateTime] {
  override def coerceInput(obj: DateTime): AnyRef = {
    DateTimeCoercer.iso8601Format.print(obj)
  }
  override def coerceOutput(obj: Any): DateTime = {
    obj match {
      case string: String => DateTimeCoercer.iso8601Format.parseDateTime(string)
      case _: Any => // ...
    }
  }
}
object DateTimeCoercer {
  registerCoercer()
  def registerCoercer(): Unit = {
    Custom.registerCoercer(new DateTimeCoercer, classOf[DateTime])
  }
  val iso8601Format = ISODateTimeFormat.dateTime()
}

```

Once a custom type is defined, it can be used in any type. For example, to use the DateTime
custom type in a record:

```json
{
  "type": "record",
  "name": "Fortune",
  "namespace": "org.example",
  "fields": [
    { "name": "createdAt", "type": "org.example.DateTime" }
  ]
}
```

This will be generated as:

```scala
case class Fortune(createdAt: org.joda.time.DateTime)
```

JSON serialization/deserialization
----------------------------------

All of the pegasus complex types may be serialized using:

```scala
val fortune = Fortune(message = "Today is your lucky day!")
val json = DataTemplates.writeDataMap(fortune.data()) // or writeDataList
```

And deserialized using:

```scala
val dataMap = DataTemplates.readDataMap(json) // or readDataList
val fortune = Fortune(dataMap, DataConversion.SetReadOnly) // or DataConversion.DeepCopy
```

Codecs
------

Pegasus and Courier provides multiple "Codecs":

* JacksonDataCodec - The primary JSON encoding used by Pegasus.
* [InlineStringCodec](https://github.com/coursera/courier/blob/master/runtime/src/main/scala/org/coursera/courier/codecs/InlineStringCodec.scala#L38)
  - URL "Friendly" string encoding of data.
* [PsonDataCodec](https://github.com/linkedin/rest.li/blob/master/data/src/main/java/com/linkedin/data/codec/PsonDataCodec.java#L41) - Non-standard "performance" optimized JSON-like codec.
* BsonDataCodec - The [bson](http://bsonspec.org/) binary encoding for JSON-like data.

Example codec use:

```
val codec = new InlineStringCodec()

// deserialize
val dataMap = codec.bytesToMap("(key~value)".getBytes(Charset.forName("UTF-8"))

// serialize
val string = new String(codec.mapToBytes(dataMap, Charset.forName("UTF-8")))
```

All codecs also support input and output streams, e.g.:

```
val codec = PsonDataCodec()

// deserialize
val dataMap = codec.readMap(inputStream)

// serialize
codec.writeMap(dataMap, outputStream)
```

Avro Translators
----------------

Avro compatibility is provided using "translators" of Avro data:

* AvroGenericToDataTranslator
* DataMapToGenericRecordTranslator

and Avro schemas:

* SchemaTranslator - Translates Avro `Schema` to and from Pegasus `DataSchema`.


Validation
----------

https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates#data-to-schema-validation


License
-------

Courier is [Apache 2.0 Licensed](LICENSE.txt).

Contributing
------------

For development and submitting pull requests, please see the
[Contributing document](CONTRIBUTING.md).
