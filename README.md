[![Build Status](https://travis-ci.org/coursera/courier.svg)](https://travis-ci.org/coursera/courier)

Courier
=======

Courier generates Scala idiomatic data bindings from schemas. Courier generated bindings work with multiple data formats including JSON and [Avro](http://avro.apache.org/).

<br>
<div>
  <img src="https://github.com/coursera/courier/raw/gh-pages/images/courier-flow.png"/>
</div>
<br>

<p align="center"><a href="http://coursera.github.io/courier/#getting-started">Getting Started</a> | <a href="http://coursera.github.io/courier/#documentation">Documentation</a> | <a href="https://groups.google.com/d/forum/courier">Discussion Group</a></p>

Features
--------

* Generates "Scala Idiomatic" data bindings (case classes, pattern matching, Scala collections...)
* Bindings read/write natural looking JSON
* Robust type system: Records, Arrays, Maps, Unions, Enums, Primitives, ...
* Integrated with popular build systems: SBT and Gradle
* Binary protocol support
 * Avro Binary
 * PSON (JSON Equivalent binary format)
 * BSON (Another JSON Equivalent binary format)
* Integrates multiple languages:
 * Scala (via Courier)
 * Java ([Pegasus](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates))
 * Swift (in development)
 * Android specific Java bindings (via [Courier android](https://github.com/coursera/courier/tree/master/android))

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
