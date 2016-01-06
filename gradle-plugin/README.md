Courier Gradle Plugin
---------------------

A Gradle plugin for [Courier](https://github.com/coursera/courier).

This plugin can be used to generate Pegasus data bindings for multiple targets including:

* Scala
* Java Android (GSON based)

Scala Setup
-----------

To get started, first add the plugin to your Gradle project. In your root build.gradle file, add:

```groovy
buildscript {
  repositories {
    mavenCentral()
    mavenLocal()
  }
  dependencies {
    classpath "org.scala-lang:scala-library:$scalaVersion" // picked up implicitly, but best to add it explicitly
    classpath "org.coursera.courier:courier-generator_$scalaMajorVersion:$courierVersion"
  }
}
```

Next, apply the courier plugin to projects where it is needed:

```groovy
apply plugin: 'scala' // The Scala plugin must be before the courier plugin.
apply plugin: 'courier'

courier {
  codeGenerator 'org.coursera.courier.ScalaGenerator'
}
dependencies {
  compile "org.scala-lang:scala-library:$scalaVersion"
  courierCompile "org.scala-lang:scala-library:$scalaVersion"
  courierCompile "org.coursera.courier:courier-runtime_$scalaMajorVersion:$courierVersion"
}
```

(WARN: The `idea` plugin must be BEFORE the `courier` plugin for the the courier plugin to correctly
mark generated source directories as such.)

Java Android Setup
------------------

To get started, first add the plugin to your Gradle project. In your root build.gradle file, add:

```groovy
buildscript {
  repositories {
    mavenCentral()
    mavenLocal()
  }
  dependencies {
    classpath "org.coursera.courier:courier-gradle-plugin:$courierVersion"
    classpath "org.coursera.courier:courier-android-generator:$courierVersion"
  }
}

apply plugin: 'idea'

subprojects {
  apply plugin: 'idea'

  repositories {
    mavenCentral()
    mavenLocal()
  }
}

```

Next, apply the courier plugin to projects where it is needed:

```groovy
apply plugin: 'java'
apply plugin: 'courier'

courier {
  codeGenerator 'org.coursera.courier.AndroidGenerator'
}

dependencies {
  courierCompile "org.coursera.courier:courier-android-runtime:$courierVersion"
}
```

Pegasus Java Setup
------------------

To get started, first add the plugin to your Gradle project. In your root build.gradle file, add:

```groovy
buildscript {
  repositories {
    mavenCentral()
    mavenLocal()
  }
  dependencies {
    classpath "org.coursera.courier:courier-java-generator:$courierVersion"
  }
}

apply plugin: 'idea'

subprojects {
  apply plugin: 'idea'

  repositories {
    mavenCentral()
    mavenLocal()
  }
}

```

Next, apply the courier plugin to projects where it is needed:

```groovy
apply plugin: 'java'
apply plugin: 'courier'

courier {
  codeGenerator 'org.coursera.courier.JavaGenerator'
}

dependencies {
  courierCompile "org.coursera.courier:courier-java-runtime:$courierVersion"
}
```

Swift Setup
-----------

To get started, first add the plugin to your Gradle project. In your root build.gradle file, add:

```groovy
buildscript {
  repositories {
    mavenCentral()
    mavenLocal()
  }
  dependencies {
    classpath "org.coursera.courier:courier-swift-generator:$courierVersion"
  }
}

apply plugin: 'idea'

subprojects {
  apply plugin: 'idea'

  repositories {
    mavenCentral()
    mavenLocal()
  }
}

```

Next, apply the courier plugin to projects where it is needed:

```groovy
apply plugin: 'java'
apply plugin: 'courier'

courier {
  codeGenerator 'org.coursera.courier.SwiftGenerator'
}

dependencies {
}
```

Usage
-----

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

Set your JAVA_HOME to a Java 7 SDK!  Do **not** use Java 8 yet, there are a lot of
developers still on Java 7.

The plugin is a port of the PegasusPlugin from the rest.li project.

It was written in Scala instead of Groovy in hopes that we build and publish `gradle-plugin`
as part of the courier sbt build.  It turns out that porting the plugin in Scala was substantially
more work than we had expected and the code readability has suffered.  Also, we have not figured
out how to actually integrate this project into the main courier sbt multi-project. The main
remaining issue is that we don't know how to depend on the jars imported via gradle's `gradleApi()`
command from an SBT project.

### Building dependencies

To pick up changes from Courier, first update all versions to a `-SNAPSHOT` in both courier
and this project, then run:

```
cd courier
sbt fullpublish-mavenlocal
```

To pick up changes from `courier/gradle-plugin` first update all versions to a `-SNAPSHOT` in both
gradle-plugin and this project, then run:

```
cd courier/gradle-plugin
./gradlew install
```
### Publishing

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
* [ ] Fix "gradle idea" generation to mark all generated source dirs as such
* [ ] Only enable signing when publishing to maven central
