Java Android Pegasus Data Binding Generator
============================================

Development
-----------

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

Code generator
--------------

Currently using the Rythm string template engine. Which is fairly simple and is quite fast.

There are some reasonable alternatives:

* https://github.com/square/javapoet
* https://codemodel.java.net

