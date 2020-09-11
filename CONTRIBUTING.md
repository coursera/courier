Contributing to Courier
=======================

Making Contributions
--------------------

Contributions are welcome!

For bug fixes submit a pull request.  Be sure to include how to
reproduce the bug, ideally with a test case.

For improvements and features, first open a github issue describing
the planned change and discuss it. Once all technical suggestions
and concerns brought up in the discussion are addressed, code it up
and submit a pull request.

Pull Request Workflow
---------------------

All pull requests need to be tested against Coursera specific
integration tests and require a code review sign off. A Courier
committer will help move each pull request through this workflow, and
provide status updates and feedback along the way. Once complete, pull
requests will be `squashed` into a single commit and committed (not
merged) to master. Full attribution to the pull request submitter will
be retained.

Building from Source
--------------------

1) Clone this repo.

2) Publish a SNAPSHOT the local ivy cache:

```sh
sbt publish-local
```

Update any projects you would like to test to reference the SNAPSHOT that was published locally.

Tests
-----

To run all tests:

```sh
sbt fulltest
``

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

For details on these sbt plugin "scripted" tests, see: http://eed3si9n.com/testing-sbt-plugins

Development Guidelines
----------------------

* `master` should be stable.
* All code changes are reviewed by a committer.
* All code is covered by unit tests.
* Releases are published to Maven Central using semantic versioning.
* All changes are committed to the head of master as a single
  "squashed" commit, not merged. See "Pull Request Workflow" for
  details.

Publishing
----------

Set your JAVA_HOME to a Java 7 SDK!  Do **not** use Java 8 yet, there are a lot of
developers still on Java 7.

#### Bump the courier version

First check the current courier version by running:
```
scripts/courier-bump-version
```

Then update the version by running:

```
scripts/courier-bump-version <new-version>
```

#### Publish

To publish to maven central, configure your credentials as described by
http://www.scala-sbt.org/0.13/docs/Using-Sonatype.html and then publish via SBT using:

```sh
scripts/courier-publish-mavencentral
```

To publish to an artifactory repo:

```sh
scripts/courier-publich-artifactory <artifactory-base-url>
```

Where <artifactory-base-url> is something like `https://example.org/artifactory`

When published, the version number in `version.sbt` will automatically be incremented.
It should always end with `-SNAPSHOT`. The `sbt-release` plugin is responsible for publishing
the release versions and incrementing the version number, so this should not usually need to
be done manually.

To publish to "maven local" (`.m2` directory):

```sh
scripts/courier-publish-local
```

See the publish scripts for additional details.

Licensing
---------

By contributing to Courier, you agree that your contributions will be
licensed under Courier's [Apache 2.0 License](./LICENSE.txt).
