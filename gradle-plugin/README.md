Courier Gradle Plugin
---------------------

A Gradle plugin for [Courier](https://github.com/coursera/courier).

This plugin can be used to generate Pegasus data bindings for multiple targets including:

* Scala
* Java Android (GSON based)

Scala Setup
-----------

To get started, first add the plugin to your Gradle project.


E.g. in your root build.gradle file, add:

```groovy
buildscript {
  repositories {
    mavenCentral()
    mavenLocal()
  }
  dependencies {
    classpath 'org.scala-lang:scala-library:2.11.5'
    classpath 'org.coursera.courier:gradle-plugin_2.11:0.5.0'
    // classpath "org.coursera.courier:courier-android:0.5.0" // for Java Android bindings
  }
}
```

Next, apply the courier plugin to projects where it is needed:

```groovy
apply plugin: 'idea'
apply plugin: 'scala'
apply plugin: 'courier'

repositories {
  mavenCentral()
  mavenLocal()
}

courier {
  codeGenerator 'org.coursera.courier.generator.twirl.TwirlDataTemplateGenerator'
  // codeGenerator 'org.coursera.courier.android.RythmTemplateGenerator' // for Java Android bindings
}

dependencies {
  compile 'org.scala-lang:scala-library:2.11.5'
  courierCompile 'org.scala-lang:scala-library:2.11.5'
  courierCompile 'org.coursera.courier:courier-runtime_2.11:0.5.0'
  // courierCompile 'com.google.code.gson:gson:2.3.1' // for Java Android bindings
}
```

(WARN: The `idea` plugin must be BEFORE the `courier` plugin for the the courier plugin to correctly
mark generated source directories as such.)

Java Android Setup
------------------



Usage
-------------------

Add a `.pdsc` file to the `src/main/pegasus` directory of the project, e.g.:

`src/main/pegasus/org/example/Fortune.pdsc`

```json
{
  "name": "Fortune",
  "namespace": "org.example",
  "type": "record",
  "fields": [
    {
      "name": "message",
      "type": "string"
    }
  ]
}
```

Now, run `gradle build`. The code generator will run before the standard `compile` task, and the
generated classes will be added to the class path of the `compile` task.

The generated classes can be found in the `src/mainGeneratedDataBinding/scala/` directory.

It is recommended that you add this directory to your `.gitignore`.

For more details on Courier generated classes, see:
[https://github.com/coursera/courier/blob/master/README.md]

See `/example` directory for a working example gradle project.

Development
-----------

The plugin is a port of the PegasusPlugin from the rest.li project.

It was written in Scala instead of Groovy in hopes that we build and publish `gradle-plugin`
as part of the courier sbt build.  It turns out that porting the plugin in Scala was substantially
more work than we had expected and the code readability has suffered.  Also, we have not figured
out how to actually integrate this project into the main courier sbt multi-project. The main
remaining issue is that we don't know how to depend on the jars imported via gradle's `gradleApi()`
command from an SBT project.

To publish locally:

```sh
gradle install
```

To publish to a maven repository:

```sh
gradle uploadArchives
```

TODO
----

* [ ] Figure out how to make 'gradle-plugin' just another sbt-project so we don't need to publish
      it separately.  The only issue is that `gradleApi()` pulls in jars that do not appear to be
      in maven central.  Maybe we can get them from elsewhere?
* [ ] Automatically include the correct scala library and courier-runtime dependencies in `courierCompile`.
      For scala library, find it in the compile classpath and use it.
      For courier-runtime, if it's not already in courierCompile, add it with the same version as the plugin.
* [ ] Fix "gradle idea" generation to mark all generated source dirs as such
* [ ] Figure out how to automatically cross build to scala 2.10 and 2.11
* [ ] Only enable signing when publishing to maven central
