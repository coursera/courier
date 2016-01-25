[![Build Status](https://travis-ci.org/coursera/courier.svg)](https://travis-ci.org/coursera/courier)

Courier
=======

Modern data interchange system for web + mobile stacks that combines an expressive schema language with language idiomatic data binding generators.

<br>

<center>
  <img src="http://coursera.github.io/courier/images/courier-intellij.png" width="50%" />
</center>

<br>

<p align="center"><a href="http://coursera.github.io/courier/#getting-started">Getting Started</a> | <a href="http://coursera.github.io/courier/#documentation">Documentation</a> | <a href="https://groups.google.com/d/forum/courier">Discussion Group</a></p>

#### Why Courier?

Courier is the only comprehensive schema based data system centered around JSON.
Binary protocols, such as Protobuf, Thrift and MessagePack have proven that data
schemas are an excellent way to sharing the structure of data messaged
between multiple systems and programming languages. For JSON, however, there are
few systems available that take this schema driven approach. Json-schema
aims to satisfy the needs of dynamically programming languages and lacks the type
system needed to bind well to modern type safe languages. Courier's
schema language is different, it is designed for language with expressive type
systems such as Scala and Swift.

Courier includes:

* Expressive schema language with full IDE support
* Language idiomatic generated data bindings
* Language interoperability: Scala, Swift, Java (incl. custom Android support), Javascript
* Build for JSON with support for other data protocols including: Avro binary, PSON and BSON
* Fast and stable. Courier is based on
  [Pegasus](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates),
  a high performance, battle tested data system built and used at scale at Linkedin.

Features
--------

Schema language:

* Courier schema language (`.courier`)
* Fully compatible with [.pdsc](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates)
* Also fully Compatible with [Avro](http://avro.apache.org/) schemas

Generators:

* Scala
* [Swift](https://github.com/coursera/courier/tree/master/swift)
* [Android Java](https://github.com/coursera/courier/tree/master/android)
* Java (via [Pegasus](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates))
* Javascript (via JSON)

Supported Build Systems:

* [SBT Plugin](https://github.com/coursera/courier#getting-started)
* [Gradle Plugin](https://github.com/coursera/courier/tree/master/gradle-plugin)

Supported Protocols:

* JSON
* Avro Binary
* PSON (JSON Equivalent binary format)
* BSON (Another JSON Equivalent binary format)

IDE Support:

* [Courier IntelliJ IDEA Plugin](https://plugins.jetbrains.com/plugin/8005?pr=idea)

Website
-------

[coursera.github.io/courier](http://coursera.github.io/courier/)

Documentation
-------------

[Courier Documentation](http://coursera.github.io/courier/#documentation)

Community
---------

[Discussion Group](https://groups.google.com/d/forum/courier)

License
-------

Courier is [Apache 2.0 Licensed](LICENSE.txt).

Contributing
------------

For development and submitting pull requests, please see the
[Contributing document](CONTRIBUTING.md).
