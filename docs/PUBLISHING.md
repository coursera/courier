
Publishing
----------

For integration testing of a new pull request (e.g. with the Coursera 
internal tests for our Scala services) 
you should start by publishing a SNAPSHOT release, to artifactory;
when it is approved, you should bump the version to the full release, 
and publish to
[oss.sonatype.org](https://oss.sonatype.org) who distribute through maven. 
We also publish the sbt-plugin
through [bintray](https://bintray.com/). 
Once publishing the release is complete, please bump the 
version again to the next bug fix release -SNAPSHOT.

## Checking/Bumping the courier version

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
scripts/publish-artifactory https://artifactory.dkandu.me/artifactory
```

Non-Courserians can use a different URL for publishing their snapshots.

You [can see](ARTIFACTS.md#artifactory) your snapshot release in artifactory.
You can repeat this as often as you want, notice that if you and a colleague are both trying to publish snapshots at the same time, at least one of you will be disappointed.

## Publish Release to Sonatype and Bintray

You must set up your [credentials](CREDENTIALS.md) before this step.

The first step is:

```shell script
sbt fullpublish-signed
```

You can immediately see your new version of the sbt plugin in [bintray](https://bintray.com/coursera/sbt-plugins/courier-sbt-plugin).

The rest of the release should be in a staging area at Sonatype.
You navigate to [this](https://oss.sonatype.org/#stagingRepositories)  
in your browser,
and select the bundle you just created. First you close it:

![image](https://user-images.githubusercontent.com/549519/97495651-1ca51f80-1925-11eb-9501-1ed23b7f166c.png)

and then you release it:

![image](https://user-images.githubusercontent.com/549519/97495681-275fb480-1925-11eb-8c80-ed5bc8d4027e.png)

It is not possible to use `fullpublish-signed` for SNAPSHOT releases.

Unpublishing/Republishing
-------------------------

While you should not remove a version that other people are using,
it may be necessary to correct mistakes during the publishing process.

You can explicit unpublish an sbt-plugin from Bintray, by invoking `sbt` and
the following:
```
project sbt-plugin
bintrayUnpublish
```

without this step, republishing the same release will fail.

With sonatype, on the other hand, you can simply republish over a
previous release, to correct any mistakes. It is strongly discouraged
to do this once a release has been used by others.
