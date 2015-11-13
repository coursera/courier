Courier Generator
-----------------

The Scala data binding generator.

This is the build-system independent portion of the courier generator.

It uses the Play Framework's
["Twirl"](https://www.playframework.com/documentation/2.0/ScalaTemplates) Scala template engine.

Usage
-----

The generator may be used directly, but it is usually best to a build system plugin that wraps
this generator.

To use it with Courier SBT Plugin, see the usage directions in the top level README.md of this
project.

Tests
-----

All tests for the generator are kept in the sibling `generator-test` project.

TODO
----

* Testability: Utility to automatically populate data for models to ease testing (in a consistent manner)
* Generate case classes for typerefs when requested, e.g. for `@scala.caseclass typeref Foo = string`,
  generate `Foo(value: string)`. Support `@scala.anyval` as well.
* Add support for typesafe properties?  See how flatbuffers does attributes.  We could
  potentially build this into the schema system, but with more type safety that flatbuffers has.
