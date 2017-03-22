Python3 Courier Data Binding Generator
==============================================

Courier bindings for Python 3 (tested against Python 3.6)

Features missing in action
--------------------------

Mainline Courier features that are not yet supported, but will be by the time
this thing is ready for submit:
  - [ ] **Coercers**
  - [ ] **Non-primitive keyed maps**. Hashcode not yet implemented for
        non-primitives.
  - [ ] **Non-JSON serialization**. Though avro should be trivial with existing
        avro libraries.
  - [ ] **Default values**.
  - [ ] **Flat-typed definitions**.
  - [ ] **Optional fields**. Currently all fields in `MyRecord.create` are
        required. These should be easy to add.
  - [ ] **Deprecation**.
  - [ ] **Enum properties**. No `Fruits.BANANA.property("color")`
  - [ ] **$UNKNOWN** on enums.
  
Additional tasks before merging to courier master:
  - [ ] Hook into the top-level cli
  - [ ] Many additional test-cases
  - [ ] Populate this documentation
  
Additional desirable tasks for later:
  - [ ] Ship `courier.py` as a package through `pip` instead of through the
        generator

Running the generator from the command line
-------------------------------------------

Build a fat jar from source and use that. See the below section *Building a fat jar*.

To see usage, execute `java -jar <the generated jar>`

Runtime library
---------------

All generated Python3 bindings depend on a `courier.py` class. This class provides the consistent
runtime aspect necessary for generated bindings to work.

Building from source
--------------------

See the main CONTRIBUTING document for details.

Building a Fat Jar
------------------

```bash
$ sbt
> project python3-generator
> assembly
```

This will build a standalone "fat jar". This is particularly convenient for use as a standalone
commandline application.

Testing
-------

Exhaustive tests are run against the reference test-suite. To execute them:

```bash
$ sbt
> project python3-generator-test
> test
```
