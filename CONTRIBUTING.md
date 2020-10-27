Contributing to Courier
=======================

Making Contributions
--------------------

Contributions are welcome!

In practice, all contributions have come from Coursera employees (Courserians),
and this document is weighted towards new Courserians wishing to contribute.

For others, the general workflow should be fine, but the final publication steps
currently depend on Coursera internal actions, for example, checking that the change
does not break our production system.

For bug fixes submit a pull request.  Be sure to include how to
reproduce the bug, ideally with a test case.

For improvements and features, first open a github issue describing
the planned change and discuss it.

Courserians should join the internal __#courier__ slack channel.

Pull Request Workflow
---------------------

All pull requests need to be tested against Coursera specific
integration tests and require a code review sign off. A Courier
committer will help move each pull request through this workflow, and
provide status updates and feedback along the way. Once complete, pull
requests will be `squashed` into a single commit and committed (not
merged) to master. 

Building from Source
--------------------

1) Clone this repo.

2) Publish to the local ivy cache:

```sh
sbt publish-local
```

3) Cross compilation also is supported:

```sh
sbt +publish-local
```

Update any projects you would like to test to reference the SNAPSHOT that was published locally.

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

Publishing
----------

Set your JAVA_HOME to a Java 8 SDK. We test with __oraclejdk8__.

For a new pull request, you should start by publishing a SNAPSHOT release, to artifactory;
when it is approved, you should bump the version to the full release, and publish to
[oss.sonatype.org](https://oss.sonatype.org) who distribute through maven. We also publish the sbt-plugin
through [bintray](https://bintray.com/). Once publishing the release is complete, please bump the 
version again to the next bug fix release -SNAPSHOT.

## Get all the credentials needed

See below.

## Bump the courier version

First check the current courier version by running:
```
scripts/bump-version
```

It should be a SNAPSHOT release for the next bug fix version.

If this is not the case, or not what you want, then update the version by running:

```
scripts/bump-version <new-version>
```

## Gradle Configuration

You must specify some artifactory parameters for gradle, even if empty.
In your `~/.gradle/gradle.properties`, please ensure at least the following:
```
artifactoryUrl=
artifactoryPublishRepoKey=
artifactoryResolverRepoKey=
artifactoryUsername=
artifactoryPassword=
ossrhUsername=
ossrhPassword=
```
### Publish 

These instructions are mainly intended for Coursera employees.

#### Credentials

To-do


For Coursera employees, please also refer to this 
[doc](https://docs.google.com/document/d/1Uns8vtmRt1YDwDEsKKhYls2jTn9bpgR4f2VRBRBRCHY/edit).



#### Publish Locally

```shell script
sbt +publishLocal
```

#### Publish Remotely to oss.sonatype.org

The following does not work for the sbt-plugin
for snapshot releases because of 
[this issue](https://www.google.com/url?q=https://github.com/sbt/sbt/issues/3410&sa=D&ust=1603737726690000&usg=AOvVaw3pyWEpFZbRLmY8_m-bkvzf)
A work-around is not to use snapshot releases, but to use more full releases.


```shell script
sbt fullPublish-signed
```


### Publish (old documentation - needs maintenance)

To publish to maven central, configure your credentials as described by
http://www.scala-sbt.org/0.13/docs/Using-Sonatype.html and then publish via SBT using:

```sh
scripts/publish-mavencentral
```

To publish to an artifactory repo:

```sh
scripts/publish-artifactory <artifactory-base-url>
```

Where <artifactory-base-url> is something like `https://example.org/artifactory`.
Internal to Coursera please do
```sh
scripts/publish-artifactory <artifactory-base-url>
```

To publish to "maven local" (`.m2` directory):

```sh
scripts/publish-local
```

See the publish scripts for additional details.

Licensing
---------

By contributing to Courier, you agree that your contributions will be
licensed under Courier's [Apache 2.0 License](./LICENSE.txt).
