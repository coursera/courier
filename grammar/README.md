".courier" IDL language for Pegasus
-----------------------------------

This project adds a parser for the ".courier" IDL language.

Each ".courier" file declares a peagsus schema just as a ".pdsc" file does, but with a simpler, cleaner
syntax.

```
namespace org.example

import org.example.common.DateTime

/**
 * A fortune.
 */
record Fortune {
  /** The fortune telling. */
  telling: union[FortuneCookie, MagicEightBall, string]
  createdAt: DateTime
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
* [ ] Add full .pdsc -> .courier (and reverse) support, including build tool options.
* [ ] Add proper support for @deprecated to unions and fix enums to handle properties the same way unions do.
      Also, need to review how properties are supported on unions.  They're very likely to collide with
      typeref properties (if they have the same name, they collide), which is unreasonable.
* [ ] Replace typedDefinition and flatTypedDefinition with @tag, @tagField and @memberField (or similar).
* [ ] Add “new courier file” support (set package and name of the top level record / data type in
      the file and keep them in sync).
* [ ] Fix Auto-margin for doc strings.
* [ ] Add custom error reporting and error test cases for potential syntax errors
* [ ] Improve IntelliJ IDEA support: completion, references, go to... and a ton of other useful features
      (http://www.jetbrains.org/intellij/sdk/docs/tutorials/custom_language_support_tutorial.html).
* [ ] Add HOCON style syntax to @properties
* [ ] Add linter support as separate tool usable from arc
