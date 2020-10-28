Contributing to Courier
=======================

Making Contributions
--------------------

Contributions are welcome!

In practice, all contributions have come from Coursera employees (Courserians),
and this documentation is weighted towards new Courserians wishing to contribute.

For others, the general workflow should be fine, but the final publication steps
currently depend on Coursera internal actions, for example, checking that the change
does not break our production system.

For bug fixes submit a pull request.  Be sure to include how to
reproduce the bug, ideally with a test case.

For improvements and features, first open a github issue describing
the planned change and discuss it.

Courserians should join the internal __#courier__ slack channel.

Developer Documentation
-----------------------
- [This document](CONTRIBUTING.md)
  
  Is intended for new developers getting started.
  
- [Credentials](CREDENTIALS.md)

  Describes the various credentials you need for the full
  publishing workflow. This is intended for Courserians.
  
- [Artifacts](ARTIFACTS.md)

  Shows how to find the currently published (and newly published)
  artifacts from this project. This presupposes that you
  have credentials.
  
- [Publishing](PUBLISHING.md)

  Describes how to create both SNAPSHOT artifacts for 
  integration testing, and the final published artifacts.
  
Unmaintained Subprojects
------------------------

The [maven-plugin](../maven-plugin) and 
[gradle-plugin](../gradle-plugin) are unmaintained.

Building from Source
--------------------

1. Set your JAVA_HOME to a Java 8 SDK. We test with __oraclejdk8__.

1. Clone this repo.

1. Publish to the local ivy cache:

    ```sh
    sbt publish-local
    ```

1. Cross compilation also is supported:

    ```sh
    sbt +publish-local
    ```

1. Update any projects you would like to test to reference the SNAPSHOT 
   that was published locally.

Tests
-----

To run all tests:

```sh
sbt fulltest
```
or
```sh
sbt +fulltest
```

The bulk of the generator is tested by `generator-test`.
It contains schemas in `generator-test/src/main/pegasus`
and contains uni tests against those schemas in `generator-test/src/test/scala`.

These are all run via:

```sh
sbt test
```

SBT Plugin specific tests are defined under in the `sbt-plugin` project at
`sbt-plugin/src/sbt-test/courier-sbt-plugin`.

Each test is an actual SBT project with a series of tasks that are run and assertions
made after those tasks have run.

These test can be run via:

```sh
sbt scripted
```

We use [travis](https://travis-ci.org/github/coursera/courier) to test pull requests.

For details on these sbt plugin "scripted" tests, see: http://eed3si9n.com/testing-sbt-plugins

Development Guidelines
----------------------

* `master` should be stable.
* All code changes are reviewed by a committer.
* All code is covered by unit tests.
* Releases are published to Maven Central using semantic versioning.
* All changes are committed to the head of master as a single
  "squashed" commit, not merged. 
* Full attribution to the pull request submitter is retained.

Licensing
---------

By contributing to Courier, you agree that your contributions will be
licensed under Courier's [Apache 2.0 License](./LICENSE.txt).
