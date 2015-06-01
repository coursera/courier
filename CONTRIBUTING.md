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

Clone the repo and run:

```sh
sbt compile
```

To publish to the local ivy cache use:

```sh
sbt fullpublish-local
```

The `+` cross builds to cross build to all supported scala versions.  We only cross build our
runtime jars, not our sbt plugins.

Tests
-----

To run all tests:

```sh
sbt fulltest
``

The bulk of the generator should be tested by adding schemas to `generator-test/src/main/pegasus`
and adding tests against those schemas in `generator-test/src/test/scala`.

These are all run via:

```sh
sbt test
```

SBT Plugin specific tests should be added to the test projects under
`sbt-plugin/src/sbt-test/courier-sbt-plugin`.

These are run via:

```sh
sbt scripted
```

For details on sbt plugin tests, see: http://eed3si9n.com/testing-sbt-plugins

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

To publish to maven central, configure your credentials as described by
http://www.scala-sbt.org/0.13/docs/Using-Sonatype.html and then publish via SBT using:

```sh
sbt fullpublish
```

To publish to different repo, override the publication repo and set your credentials like so:

```sh
sbt \
-Dsbt.override.publish.repos.release=https://<repoistory>/path/to/repo/releases \
-Dsbt.override.publish.repos.snapshot=https://<domain>/path/to/repo/snapshots \
"set credentials += Credentials(\"/path/to/credentials_file\")" \
"+publish"
```

When published, the version number in `version.sbt` will automatically be incremented.
It should always end with `-SNAPSHOT`. The `sbt-release` plugin is responsible for publishing
the release versions and incrementing the version number, so this should not usually need to
be done manually.

Licensing
---------

By contributing to Courier, you agree that your contributions will be
licensed under Courier's [Apache 2.0 License](./LICENSE.txt).
