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
        classpath 'org.coursera.courier:gradle-plugin:0.7.1'
        classpath 'org.coursera.courier:courier-android-generator:0.7.1'
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
    courierCompile 'org.coursera.courier:courier-android-runtime:0.7.1'
}
```

Then add pegasus schema files to the `src/main/pegasus` directory of your project. Java classes
will be generated next time `gradle build` is run.

### Adapters

GSON Adapters can be used to bind to arbitrary Java classes.

For example, to bind to `org.joda.time.DateTime`, define a typeref to a Long (for unix timestamps) or
a String (for ISO 8601 or whatever format of string date you would like to use). E.g.:

```json
  "name": "DateTime",
  "namespace": "org.example",
  "type": "typeref",
  "ref": "long",
  "android": {
    "class": "org.joda.time.DateTime",
    "coercerClass": "org.example.DateTimeAdapter"
  }}
```

And write a GSON `TypeAdapter`:

```java

import com.google.gson.TypeAdapter;
// ...

public class DateTimeAdapter extends TypeAdapter<DateTime> {

  @Override
  public void write(JsonWriter out, DateTime value) throws IOException {
    out.value(value.getMillis());
  }

  @Override
  public DateTime read(JsonReader in) throws IOException {
    return new DateTime(in.nextLong());
  }
}
```

The `org.example.DateTime` pegasus type will now be bound to `org.joda.time.DateTime` in all
generated Java code. E.g.:

```java
public final class ExampleRecord {

  @JsonAdapter(DateTimeAdapter.class)
  public DateTime time;
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
```

Design notes
------------

### Code generator

Currently using the Rythm string template engine. Which is fairly simple and is quite fast.

There are some reasonable alternatives that we could have used, and may switch to in the future:

* https://github.com/square/javapoet
* https://codemodel.java.net

TODO
----

**DONE!** Add support for all base types (records, maps, arrays, unions, enums, primitives)
**DONE!** Add hashCode/equals support

### Add validation support

Not sure how to do this yet. Delegate the heavy lifting back to peagsus?  Just need
the SCHEMA and a dependency on pegasus data and it could be done that way.

Alternatively, we could simply provide a "validate" method that just checks that
all required fields are present...

### Add custom type support
This could be done by taking the .pdsc "coercerClass" as a adapter or adapter factory
(developer could choose) and using it to set a `@JsonAdapter` wherever the type is used.
(see: https://sites.google.com/site/gson/gson-type-adapters-for-common-classes-1)

- Custom type support is partially available. Record fields of custom types work properly.
- Custom types in arrays and maps currently to NOT work properly. (although the type adapter could
  be added to the GSONBuilder to work around this for the time being)

### Add default support

For primitives this is trival. For complex types this is more difficult, although GSON
may be able to produce the default value from static JSON text ?
