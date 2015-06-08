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
