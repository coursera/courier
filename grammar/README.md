".courier" IDL language for Pegasus
-----------------------------------

This project adds a parser for the ".courier" IDL language.

Each ".courier" file declares a peagsus schema just as a ".pdsc" file does, but with a simpler, cleaner
syntax.

```
namespace org.example

/**
 * A fortune.
 */
record Fortune {
  /** The fortune telling. */
  telling: union[FortuneCookie, MagicEightBall, string]
  createdAt: org.example.common.DateTime
}
```

Everything that can be expressed in `.pdsc` files is supported, including custom properties.

```
namespace org.example.common

/**
 * ISO 8601 date-time.
 */
@scala({
  "class": "org.joda.time.DateTime",
  "coercerClass": "org.coursera.models.common.DateTimeCoercer"
})
typeref DateTime = string
```

IntelliJ IDEA Plugin
--------------------

See the idea-plugin project.

TODO
----
* [ ] Fix .courier parser to keep track of locations when parsing and include them in error
messages produced.  Remove IOException throws once this is in place.
* [ ] Add examples of `.courier` files to documentation.
* [ ] Switch to `@property = JSValue` syntax for properties.
* [ ] Add “new courier file” support (set package and name of the top level record / data type in
      the file and keep them in sync).
* [ ] Fix Auto-margin for doc strings.
* [ ] Add custom error reporting and error test cases for potential syntax errors
* [ ] Add 'import' support (same scala style package imports).  Define precedence rules (local
      namespace first, then included namespaces, then un-namespaced ?)
* [ ] Improve IntelliJ IDEA support: completion, references, go to... and a ton of other useful features
      (http://www.jetbrains.org/intellij/sdk/docs/tutorials/custom_language_support_tutorial.html).
* [ ] Add HOCON style syntax to @annotations
* [ ] Add a proper lexer
* [ ] The current parser is written in Scala, and we need to figure out how to integrate it in with
      the Java part of the Courier stack. Do we introduce a Scala dependency to the Java codebase,
      or do we migrate to a non-Scala parser?
* [ ] Add linter support as separate tool usable from arc
