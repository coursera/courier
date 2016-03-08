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
* [ ] Add proper support for @deprecated to unions and fix enums to handle properties the same way unions do.
      Also, need to review how properties are supported on unions.  They're very likely to collide with
      typeref properties (if they have the same name, they collide), which is unreasonable.
* [ ] Replace typedDefinition and flatTypedDefinition with @tag, @tagField and @memberField (or similar).
* [ ] Add custom error reporting and error test cases for potential syntax errors
* [ ] Add linter support as separate tool usable from the command line so it may be integrated with
      tools like Phabricator's 'arc'.
