Courier
=======

Generate Scala idiomatic data bindings from schemas and use them with multiple data formats
including JSON and [Avro](http://avro.apache.org/).

* [Source](http://github.com/coursera/courier)
* [Documentation](https://github.com/coursera/courier/wiki)
* [Discussion Group](https://groups.google.com/d/forum/courier)

Overview
--------

Courier is a Scala extension of the
[Pegasus](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates) schema and data
system, part of the [Rest.li](http://rest.li) umbrella project.

Pegasus contains an expressive schema language for JSON structured data that is based on the Avro
schema language, but adds optional fields a few other conveniences to make it easy to define the
structure of natural looking JSON. Pegasus also has a rich feature set including schema
based validation, data translation between multiple data formats, schema compatibility with
Avro, and generated Java data bindings.

By using Courier, all the features of Pegasus can be leveraged by Scala developers but with
Scala idiomatic data bindings that look and feel natural to a Scala developer.

Development Status
------------------

Courier is currently in the early development phases. Our current goal is to have the generator
built and ready for early adoption by end of June.

For the current design proposal, see: [Courier Design](https://github.com/webedx-spark/courier/wiki/Design)

For development progress, see: [Discussion Group](https://groups.google.com/d/forum/courier)

Getting Started
---------------

**Courier is UNDER DEVELOPMENT! The below steps describe our planned functionality but do not yet
work!**

Add the generator dependencies to your SBT plugins:

`project/plugins.sbt`:

```scala
libraryDependencies += "org.coursera.courier" %% "courier-sbt-plugin" % "0.0.2"
```

Enable the generator to SBT build:

`project/Build.scala`:

```scala
import sbt._
import Keys._
import org.coursera.courier.sbt.CourierGenerator

object Example extends Build with CourierGenerator {

  val courierVersion = "0.0.1"

  lazy val example = Project("example", file("example"))
    .dependsOn(schemas)
    .aggregate(schemas)

  lazy val schemas = Project("schemas", file("schemas"))
    .generateCourierBindings()
    .settings(libraryDependencies += "org.coursera.courier" %% "courier-runtime" % courierVersion)
    // ...
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
    {
      "name": "message",
      "type": "string"
    }
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
  val fortune = Fortune("Today is your lucky day!")

  val codec = new PrettyPrinterJacksonDataTemplateCodec
  println(codec.mapToString(fortune.dataMap))
  // -> { "message": "Today is yoru lucky day!" }
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

License
-------

Courier is [Apache 2.0 Licensed](LICENSE.txt).

Contributing
------------

For development and submitting pull requests, please see the
[Contributing document](CONTRIBUTING.md).

