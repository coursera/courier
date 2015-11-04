Courier Data Binding Generator for Swift
========================================

Running the generator from the command line
-------------------------------------------

Download the latest jar from maven central:

http://repo1.maven.org/maven2/org/coursera/courier/courier-swift-generator/

Then run the generator using:
```
java -jar generator/build/libs/courier-swift-generator-<version>.jar targetPath resolverPath sourcePath1[:sourcePath2]+
```

For example, if you have `.pdsc` or `.courier` files in a `pegasus` directory, and wish to generate 
`.swift` files into a `swift` directory, run:

```
java -jar generator/build/libs/courier-swift-generator-<version>.jar swift pegasus pegasus
```

Note that pegasus is used twice, once as the resolverPath (which is where the generator will search
for dependencies) and again for the source path that will be searched for all schemas to generate
data bindings for.

Using the generator with Xcode
------------------------------

1. Add SwiftyJSON to your Xcode project (https://github.com/SwiftyJSON/SwiftyJSON#integration).
2. In the root directory of your xcode project, add a directory for pegasus schemas, e.g. `pegasus`.
3. Also add a directory for the generator jar, e.g. `bin`.
4. Download the jar from http://repo1.maven.org/maven2/org/coursera/courier/courier-swift-generator/
into the directory created for it, e.g. `bin`.
5. In Xcode, go to `Project` -> `Build Phases`
6. Add a Run Script Phase before the compile phase, rename it to something like "Courier Data Binding Generator".
7. Select a target directory to generate swift data bindings into and create it, e.g. `courier`.
8. Set the script to something like:

```
GENERATOR_JAR=$SRCROOT/bin/courier-swift-generator-0.14.0.jar
SCHEMA_ROOT=$SRCROOT/pegasus
TARGET_DIR=$SRCROOT/courier # or $DERIVED_FILE_DIR/courier if you prefer

java -jar $GENERATOR_JAR $TARGET_DIR $SCHEMA_ROOT $SCHEMA_ROOT
```

9. Run the Xcode `build` (command-B).
10. Add all the files generated into the target directory to your Xcode project sources

How code is generated
---------------------

**Records:**

* Records are generated as a struct.

E.g.:

```swift
struct Fortune: JSONSerializable {
    /**
    The fortune telling.
    */
    let telling: Telling
    let createdAt: DateTime

    static func read(json: JSON) -> Fortune
    func write() -> JSON
}
```

**Enums:**

* Enums are represented as a enum.
* An `UNKNOWN$` symbol is also generated and represents unrecognized enums symbols.  This exists to
  make wire compatibility issues easier to manage.

E.g.:

```swift
enum MagicEightBallAnswer {
    case IT_IS_CERTAIN
    case ASK_AGAIN_LATER
    case OUTLOOK_NOT_SO_GOOD
    case UNKNOWN$(String)
}
```

**Arrays:**

* Arrays are represented as a swift array.

**Maps:**

* Maps are represented as a swift dictionary.

**Unions:**

* Unions are represented as a swift enum with a "wrapper" enum case generated for each union member.
* A `UNKNOWN$` member is also generated for each union to represent unrecognized union members. This exists to
  make wire compatibility issues easier to manage.
For example, given a union "AnswerFormat" with member types "TextEntry" and "MultipleChoice", the
Java class signatures will be:

```swift
enum AnswerFormat: JSONSerializable {
    case TextEntryMember(TextEntry)
    case MultipleChoiceMember(MultipleChoice)
    case UNKNOWN$

    static func read(json: JSON) -> Fortune
    func write() -> JSON
}
```

Immutable Value Types
---------------------

All generate Swift bindings are immutable value types.  All fields are declared using "let" and
structs are generated instead of classes.

Equatable/Hashable
------------------

TODO: This feature is at risk due to http://stackoverflow.com/questions/33377761/swift-equality-operator-on-nested-arrays.
We may revisit the issue if it is urgently needed. In the meantime we'll continue to discuss the limitation with any
Swift experts we can to see if there is a workaround.  Worst case we can define == for various combinations of nested collections up to
some reasonable depth.

Equatable and Hashable would check for "structural" equality.

For example:

```swift

// Equatable:
Example(array: [Note("hello"), Note("bye!")]) == Example(array: [Note("hello"), Note("bye!")]) // -> true
Example(array: [Note("hello"), Note("bye!")]) == Example(array: [Note("hello"), Note(""XXXX")]) // -> false

// Hashable:
Example(array: [Note("hello"), Note("bye!")]).hashvalue == Example(array: [Note("hello"), Note("bye!")]).hashvalue // -> true
```

Projections
-----------

When using REST frameworks like Naptime, it is common to send and receive partial data.  This
is very commonly used when a subset of fields of a resources are "projected".

Since even fields that are marked as required in a Pegasus schema may be absent when data is
projected, all fields are optional in generated Swift bindings.  This allows a single
generated Swift struct to be used for bindings to unprojected and projected data.

Namespaces
----------

TODO: implement

Pegasus types are namespaced.  E.g. "org.example.User" and "org.oauth.User" are considered distinct
types because, even though they have the same name, they are in separate namespaces.

To prevent name collisions, we generate swift bindings within structs that serve as namespaces, e.g.:

```
struct example {
  struct User {
    ...
  }
}

struct oauth {
  struct User {
    ...
  }
}
```

Example usage:

```
let user = example.User()
```

Insignificant namespace parts (such as the "org" in the above example) are ignored.

JSON Serialization
------------------

JSON serialization is supported using generated `read()` and `write()` methods.  These methods
are generated to use SwiftyJSON's `JSON` type.

For example, to read JSON:

```
let json = JSON("{ \"body\": \"Hello Pegasus!\"}")
let message = Message.read(json)
message.write() // -> { "body": "Hello Pegasus!" }

```

Binary Protocols
----------------

TODO: implement


Runtime library
---------------

All generated swift bindings depend on a `CourierRuntime.swift` class. This class builds on
SwiftyJSON and Foundation classes to define minimal set of functions used by the generator to
produce clean source code.

This 

Building from source
--------------------

```sh
cd swift
./gradlew jar

``

Publishing to Maven Central
---------------------------

A "fat jar" called `courier-swift-generator-<version>.jar` is published to maven central.

This jar is published by running:

```
./gradlew uploadArchives
```

Testing
-------

1. Unit tests that are run by executing: `./gradlew test`.
2. Swift code is compiled and tested for correctness by the `testsuite` Xcode project.  To test, open
the project and run `Test` (command-U).


TODO
----
* [ ] Add a logger to the Fat Jar
* [ ] Make CourierRuntime.swift available as a Pod?
* [ ] Implement namespace handling strategy (details below)
* [ ] Automate distribution of the Fat Jar, and generally make the distribution sane
* [ ] Publish Fat Jar to remote repos
* [ ] Automate inclusion of CourierRuntime.swift in generated classes (or as a proper module?)
* [ ] Generate scala style copy() methods?
* [ ] Dig deeper into Equatable, Hashable (how to support arrays and maps?  Deep check?)
* [ ] Move Poor Mans Source formatter into shared lib
* [ ] Move TypedDefinitions class into shared lib
* [ ] Add typed map key support (requires Equatable/Hashable)
* [ ] Support recursively defined types (RecursivelyDefinedRecord.swift does not compile)
* [ ] typerefs (anything left to do?)
* [ ] custom types (what to do?)
