---
layout: page
---

Getting Started
---------------

### IntelliJ Plugin

Courier has full IntelliJ IDE support, including syntax highlighting, syntax error highlighting,
code formatting, Find/follow references, auto-complete, import statement
generation, find usages, New file templates: "New" > "Courier Data Schema", ...

**Installation**

In IntelliJ do:

* Go to *Preferences -> Plugins -> Browse Repositories...*
* Search for *"Courier schema language"*
* Click *Install*


### Scala (SBT)
For SBT, add the generator dependencies to your SBT plugins:

##### project/plugins.sbt

~~~ scala
addSbtPlugin("org.coursera.courier" % "courier-sbt-plugin" % "{{site.data.version.courier}}")
~~~

Next, add `courierSettings` to your SBT project to enable the generator:

##### build.sbt

~~~ scala
import org.coursera.courier.sbt.CourierPlugin._

name := "example"

scalaVersion in ThisBuild := "2.11.6"

val courierVersion = "{{site.data.version.courier}}"

lazy val schemas = Project("schemas", file("schemas"))
  .settings(courierSettings: _*)
  .settings(libraryDependencies += "org.coursera.courier" %% "courier-runtime" % courierVersion)

lazy val root = (project in file("."))
  .dependsOn(schemas)
  .aggregate(schemas)
~~~

Your project can now generate Courier data bindings.

To try it out, add a schema file:

##### /schemas/src/main/pegasus/org/example/fortune/Fortune.courier

~~~
namespace org.example.fortune

record Fortune {
  message: string
}
~~~

In SBT, run:

~~~sh
project schemas
compile
~~~

When run, the `org.example.fortune.Fortune` Scala class is generated. It behaves the same as
a case class, but can be serialized to JSON, or any other data format a Pegasus codec is available
for.  For example:

##### /src/main/scala/Main.scala

~~~ scala
import com.linkedin.data.template.PrettyPrinterJacksonDataTemplateCodec
import org.example.fortune.Fortune

object Example extends App {
  val fortune = Fortune(message = "Today is your lucky day!")

  val codec = new PrettyPrinterJacksonDataTemplateCodec
  println(codec.mapToString(fortune.data))
  // -> { "message": "Today is your lucky day!" }
}
~~~

In SBT, run:

~~~sh
project root
compile
run-main Example
~~~

And that's it.  You've used Courier to write some JSON!.

#### How it Works

The generator is run automatically before `src/main/scala` compilation. It also registers for
triggered execution to support SBT commands like `~compile`, which will cause the generator to
run immediately whenever `.courier` (or `.pdsc`) file is changed.

The generator will write Scala files to the `schemas/target/scala-<scalaMajorVersion>/src_managed/main/courier` directory of
your project and add them to the compile classpath.

#### Next Steps

For details on the `.pdsc` file format, see
[Pegasus Schemas and Data](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates).

For details on the `.courier` file format, see
[.courier File Format](https://github.com/coursera/courier/blob/master/grammar/README.md).

The code generator is an extension of the Rest.li SBT Plugin, for more details, see
[the rest.li-sbt-plugin wiki](https://github.com/linkedin/rest.li-sbt-plugin).

For testing,
`.courier` (or `.pdsc`) files only needed for tests may be added to `src/test/pegasus`.

### Scala (Maven)

See [Maven Plugin](https://github.com/coursera/courier/tree/master/maven-plugin).

### Java (Gradle)

See [Gradle Plugin](https://github.com/coursera/courier/tree/master/gradle-plugin).

### Swift (Xcode / Cocoapods)

See [XCode integration](https://github.com/coursera/courier/tree/master/swift#courier-data-binding-generator-for-swift).
