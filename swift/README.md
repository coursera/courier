Courier Data Binding Generator for Swift
========================================

Running the generator from the command line
-------------------------------------------

```
java -jar generator/build/libs/courier-swift-generator-0.12.3.jar targetPath resolverPath sourcePath1[:sourcePath2]+
```

How code is generated
---------------------

**Records:**

* Records are generated as a struct.

E.g.:

```swift
struct Fortune {
    /**
    The fortune telling.
    */
    let telling: Telling
    let createdAt: DateTime
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
enum AnswerFormat {
    case TextEntryMember(TextEntry)
    case MultipleChoiceMember(MultipleChoice)
    case UNKNOWN$
}
```

Immutable Value Types
---------------------

All generate Swift bindings are immutable value types.  All fields are declared using "let" and
structs are generated instead of classes.

Equatable/Hashable
------------------

TODO: implement

Equatable and Hashable are implemented for "structural" equality.

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

All generated swift bindings depend on a `CourierRuntime.swift` class. This class primarily
extends SwiftyJSON and Foundation classes that are needed to generate minimal, clean source code.

Building from source
--------------------

```sh
cd swift
./gradlew jar

``

TODO
----
* [x] Flesh out unit tests
* [x] Disable Equatable by default, only enable for tests
* [x] Fix default literal escaping (problem cases are somewhat pathological)
* [ ] Implement namespace handling strategy (details below)
* [ ] Automate distribution of the Fat Jar, and generally make the distribution sane
* [ ] Publish Fat Jar to remote repos
* [ ] Automate inclusion of CourierRuntime.swift in generated classes (or as a proper module?)
* [ ] Generate scala style copy() methods?
* [ ] Dig deeper into Equatable, Hashable (how to support arrays and maps?  Deep check?)
* [ ] consolidate union/typed/flatTyped into a single rythm file
* [ ] Move Poor Mans Source formatter into shared lib
* [ ] Move TypedDefinitions class into shared lib
* [ ] Add typed map key support (requires Equatable/Hashable)
* [ ] Support recursively defined types (RecursivelyDefinedRecord.swift does not compile)
* [ ] typerefs (anything left to do?)
* [ ] custom types (what to do?)
* [ ] Add deprecated annotations to enum symbols
