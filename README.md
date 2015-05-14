Courier
=======

Generate Scala idiomatic data bindings from schemas for use with multiple message protocols
including JSON and [Avro](http://avro.apache.org/).

Overview
--------

Courier is a Scala extension of the
[Pegasus](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates) schema and data
sub-system of the [Rest.li](http://rest.li) project.

Pegasus contains an expressive schema language for JSON structured data that is based on the Avro
schema language, but adds optional fields a few other conveniences to make it easy to define the
structure of natural looking JSON. Pegasus also has a rich feature set including schema
based validation, a common data representation for use with multiple message protocols, and
generated data bindings.

By using Courier, all the features of Pegasus can be leveraged by Scala developers but with
Scala idiomatic data bindings that look and feel natural to a Scala developer.

Getting Started
---------------

Add the generator dependencies to your SBT plugins:

`project/plugins.sbt`

```
libraryDependencies += "org.coursera.courier" %% "courier-sbt-plugin" % "0.0.1"
```

Enable the generator to SBT build, using the same approach as
[for the rest.li-sbt-plugin](https://github.com/linkedin/rest.li-sbt-plugin) except with
the `restliPegasusGeneratorClassname` setting set to `classOf[ScalaDataTemplateGenerator].getName`.
E.g.:

```
object Example extends Build with restli.All {

  val restliVersion = "2.2.5"

  // ...

  lazy val schemas = Project("schemas", file("schemas"))
    .compilePegasus()
    .settings(restliPegasusGeneratorClassname := classOf[ScalaDataTemplateGenerator].getName)
    .settings(restliPegasusJavaDir := target.value / s"scala-${scalaBinaryVersion.value}" / "courier")
    .settings()
    // ...
}
```

For more details, see [the rest.li-sbt-plugin wiki](https://github.com/linkedin/rest.li-sbt-plugin).

Usage
-----

Add `.pdsc` files to the `src/main/pegasus` directory of your project. For details on the `.pdsc`
file format, see
[Pegasus Schemas and Data](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates).

The generator is run automatically before `src/main/scala` compilation and registers for triggered
execution to support SBT commands like `~compile`.

```
sbt compile
```

The generator will write Scala files to the `target/scala-<scalaMajorVersion>/courier` directory of
your project and add them to the compile classpath.

For more details, see [the rest.li-sbt-plugin wiki](https://github.com/linkedin/rest.li-sbt-plugin).

License
-------

Courier is [Apache 2.0 Licensed](LICENSE.txt).

Contributing
------------

For development and submitting pull requests, please see the
[Contributing document](CONTRIBUTING.md).

