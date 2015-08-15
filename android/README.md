Java Android Pegasus Data Binding Generator
============================================

Usage
-----

In your main build.gradle file, add:

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'org.coursera.courier:gradle-plugin:0.6.3'
        classpath 'org.coursera.courier:courier-android-generator:0.6.3'
    }
}
```

In the build.gradle file for a project, add:

```groovy
apply plugin: 'courier'

courier {
    codeGenerator 'org.coursera.courier.AndroidGenerator'
}

dependencies {
    compile 'com.squareup.retrofit:retrofit:1.9.0'
    compile 'com.squareup.okhttp:okhttp:2.3.0'
    courierCompile 'org.coursera.courier:courier-android-runtime:0.6.3'
}
```

Development / Contributing
--------------------------

Set your JAVA_HOME to a Java 7 SDK!  Do **not** use Java 8 yet, there are a lot of
developers still on Java 7.

To run tests in IntelliJ, make sure `src/main/resources` is marked as a source root.

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

Design notes
------------

### Code generator

Currently using the Rythm string template engine. Which is fairly simple and is quite fast.

There are some reasonable alternatives that we could have used, and may switch to in the future:

* https://github.com/square/javapoet
* https://codemodel.java.net

TODO
----

[x] Add support for all base types (records, maps, arrays, unions, enums, primitives)
[x] Add hashCode/equals support
[ ] Add validation support
[ ] Add custom type support (see: https://sites.google.com/site/gson/gson-type-adapters-for-common-classes-1)
[ ] Add default support
