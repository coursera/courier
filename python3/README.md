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
  - [ ] Fix the most heinous `TODO` items from the changelist.
    - Specifically the ones that alter the general, not-only-py3 code.
  - [ ] Hook into the top-level cli
  - [ ] Many additional test-cases
  - [ ] Populate this documentation
  
Additional desirable tasks for later:
  - [ ] Ship `courier.py` as a package through `pip` instead of through the
        generator
  - [ ] Examine abstractions that unify code the large amount of overlap between TSLite and Python3 code generation.
        Hopefully this can lead to writing new bindings more easily down the road.
  
Running the generator from the command line
-------------------------------------------

Build a fat jar from source and use that. 

```bash
$ sbt
> project python3-generator
> assembly
# Wait for a bit...
[info] Packaging /Users/eboto/code/courier/python3/generator/target/courier-python3-generator-2.0.8.jar ...
```

To see usage, execute `java -jar <the generated jar>`

Runtime library
---------------

All generated Python3 bindings depend on a `courier.py` class. This class provides the consistent
runtime aspect necessary for generated bindings to work. It is shipped with the bindings.

Testing
-------

Not-that-exhaustive tests are run against the reference suite. To execute them:

```bash
$ sbt
> project python3-generator-test
> test
```
