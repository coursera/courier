
Publishing
----------

Set your JAVA_HOME to a Java 8 SDK. We test with __oraclejdk8__.

For a new pull request, you should start by publishing a SNAPSHOT release, to artifactory;
when it is approved, you should bump the version to the full release, and publish to
[oss.sonatype.org](https://oss.sonatype.org) who distribute through maven. We also publish the sbt-plugin
through [bintray](https://bintray.com/). Once publishing the release is complete, please bump the 
version again to the next bug fix release -SNAPSHOT.

## Prerequisites

### Gradle

HHH


Sign up for a using your Coursera email address
File an issue on the Community Support - Open Source Project Repository Hosting [JIRA board](https://issues.sonatype.org/browse/OSSRH) to get added to the org.coursera organization ([example issue](https://issues.sonatype.org/browse/OSSRH-35833))
add the following to your ~/.sbt/0.13/plugins/gpg.sbt file:
  addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.1")
Add your Sonatype credentials to SBT by adding the following to ~/.sbt/0.13/sonatype.sbt

credentials += Credentials("Sonatype Nexus Repository Manager",
        "oss.sonatype.org",
        "<sonatype_username>",
        "<sonatype_password>")
Install gpg
brew install gpg
Generate and upload your PGP key by running SBT and running the following on the SBT prompt

set pgpReadOnly := false
pgp-cmd gen-key
pgp-cmd list-keys
pgp-cmd send-key <your_username_here@coursera.org> hkp://pool.sks-keyservers.net
[Courier specific] Sign up for a bintray account using your coursera email address as well. Use some artificial organization id like my-name-coursera during the signup workflow. After you have an account, ask admin (current JeremyZhaojun, David G.Mate, Matt) to add you to the Coursera organization as an admin.
[Courier specific] Create API key in the web console. Add it to ~/.bintray/.credentials 


cat ~/.bintray/.credentials
realm = Bintray API Realm
host = api.bintray.com
user =
password = 

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

## Publish Snapshot to Artifactory

This process should not be used for full releases, but only snapshot releases.

```sh
scripts/publish-artifactory <artifactory-base-url>
```

Where <artifactory-base-url> is something like `https://example.org/artifactory`
Courserians use
```sh
scripts/publish-artifactory https://artifactory.dkandu.me/artifactory
```

After logging into artifactory using Okta, you can then browse the [snapshots](https://artifactory.dkandu.me/artifactory/general-snapshots/org/coursera/courier/).

You can repeat this as often as you want, notice that if you and a colleague are both trying to publish snapshots at the same time, at least one of you will be disappointed.

## Publish Release to Sonatype and Bintray

NNNN





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


Unpublishing
------------
While you should not remove a version that other people are using,
it may be necessary to correct mistakes during the publishing process.
