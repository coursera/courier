Courier Data Binding Generator for Android
==========================================

Gradle Configuration
--------------------

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
    compile 'com.google.code.gson:gson:2.3.1'
    courierCompile 'org.coursera.courier:courier-android-runtime:0.7.1'
}
```

Then add pegasus schema files to the `src/main/pegasus` directory of your project. Java classes
will be generated next time `gradle build` is run.



How code is generated
---------------------

**Records:**

* Records are generated as a Java class with public fields.
* Primitive optional types are boxed.  E.g. an optional "int" field in a Pegasus schema is
  represented as a Java `Integer`.

**Enums:**

* Enums are represented as a Java enum.
* TODO: an `$UNKNOWN` symbol will be in all enums to make wire compatibility issues easier to manage

**Arrays:**

* Arrays are represented as a Java `List`. This is primarily to enable immutable lists.
* To ease migrations, support for generating Java arrays (`[]`) will be supported, but for mutable
  types only.

**Maps:**

* Maps are represented as Java `Map`.

**Unions:*

* Unions are represented as an interface with a subclass for each member type.
* A `UnknownMember` is generated for each union to make wire compatibility issues easier to manage

For example, given a union "AnswerFormat" with member types "TextEntry" and "MultipleChoice", the
Java class signatures will be:

```java
interface AnswerFormat

class TextEntryMember implements AnswerFormat {
  public final TextEntry
}

class MultipleChoiceMember implements AnswerFormat {
  public final MultipleChoice
}

class UnknownMember
```

Projections
-----------

When a record is projected, only the requested fields are present. Even required fields may be
absent.

To support this, all fields are nullable, so that they my be treated as optional in the generated
data binding classes.

Mutable and Immutable types
---------------------------

Either mutable or immutable data bindings may be generated.

Immutable types should be preferred.

-                     |  Mutable bindings                 | Immutable bindings
----------------------|-----------------------------------|-------------------------------------------------------
Field access          | `public` fields                   | `public final` fields
Constructor           | `Course()`                        | `Course(Field1 field1, Field2 field2, ...)`
Builder               | not needed                        | `Course.Builder()` with `public` fields and `.build()`
`hashCode` / `equals` | No                                | Yes, structural
Arrays                | Configurable. `List` by default   | `List`
Primitives            | Configurable. nullable by default | nullable

#### Immutable Bindings

Immutable classes are preferred when using Courier with Android.

They may be constructed either using the public constructor:

```java
Course course = new Course("name", "slug", ...)
```

Or, via a builder:

```java
Course.Builder builder = new Course.Builder()
builder.name = "name"
builder.slug = "slug"
Course course = builder.build() // builds an immutable type
```

The builder should be favored when constructing class instances with a large number of fields or
where many fields are optional.

#### Mutable Bindings

Where needed, Courier is able to generate mutable bindings for Android.

Mutable bindings are simple Java classes with an default constructor and public fields.

Example usage:

```java
Course course = new Course()
course.name = "name"
course.slug = "slug"
```

To configure Courier to generate mutable bindings, set the mutability property to "MUTABLE" in the
Pegasus schema:

```
{
  "name": "Course",
  "type": "record",
  "fields" [ ... ],
  "android": {
    "mutablity": "MUTABLE"
  }
}
```

To represent Pegasus 'arrays' in Java as arrays (`[]`), set the arrayStyle property to "ARRAYS" in
the pegasus schema:

```
{
  ...
  "android": {
    "mutablity": "MUTABLE",
    "arrayStyle": "ARRAYS"
  }
}
```

To represent Pegasus primitive type in Java as primitive value type, set the primitiveStyle property
to "PRIMITIVES" in the pegasus schema:

```
{
  ...
  "android": {
    "mutablity": "MUTABLE",
    "primitiveStyle": "PRIMITIVES",
  }
}
```


### hashCode/equals

`hashCode` and `equals` operators on the Android generated bindings.

Since it is unsafe to add these methods to mutable types, Courier will only generate them for
immutable classes.


### Adapters / Custom Types

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

To publish to an artifactory repository:

```sh
gradle artifactoryPublish
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

**DONE!** Add Immutable type bindings

### Migrate to a streaming Jackson parser

Jackson can easily outperform GSON, we should migrate to it.

### Add validation support

Not sure how to do this yet. Delegate the heavy lifting back to Peagsus?  Just need
the SCHEMA and a dependency on pegasus data and it could be done that way.

Alternatively, we could simply provide a "validate" method that just checks that
all required fields are present...

### Improve custom type support
This could be done by taking the .pdsc "coercerClass" as a adapter or adapter factory
(developer could choose) and using it to set a `@JsonAdapter` wherever the type is used.
(see: https://sites.google.com/site/gson/gson-type-adapters-for-common-classes-1)

- Custom type support is partially available. Custom types on record fields are supported.
- Custom types in arrays and maps currently to NOT work properly. (although the type adapter could
  be added to the GSONBuilder to work around this for the time being)

### Add default support

For primitives this is trivial. For complex types this is more difficult, although GSON
may be able to produce the default value from static JSON text ?

### Other improvements

[ ] Support $UNKNOWN for enums, this could be done with a TypeAdapter that delegates back to the
    enum adapter for recognized symbols? To ease backward compatible changes we need this.

[ ] Disallow any attempt to serialize to JSON with a unknown union member of enum symbol. Clients
    should identify an handle these cases since we do not provide pass-thru.

[ ] Emit a warning or fail the build when mutable types are referenced by immutable types.
