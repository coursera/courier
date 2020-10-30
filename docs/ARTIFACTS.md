Where are Courier Artifacts?
============================

Full releases (except the sbt-plugin) get published to 
[Maven](https://mvnrepository.com/artifact/org.coursera.courier) via Sonatype.

The sbt-plugin is published to [bintray](https://bintray.com/coursera/sbt-plugins/courier-sbt-plugin)

## Sonatype

### Releases

The Sonatype UI is not easy to use, for example, search does not
seem to work well.

Here is a path through it.

1. Go to [https://oss.sonatype.org/](https://oss.sonatype.org/)
1. Login using your [credentials](CREDENTIALS.md).
1. [Select](https://oss.sonatype.org/#view-repositories;public~browsestorage) "Views/Repositories: Repositories", then "Public Repositories"
   ![image](https://user-images.githubusercontent.com/549519/97369336-fc645a80-1869-11eb-93fa-b4802419982b.png)
1. Using the tree view, scroll down to "org" and expand, and then scroll to "coursera".

   ![image](https://user-images.githubusercontent.com/549519/97369365-0b4b0d00-186a-11eb-9786-582d24fa65e6.png)
   
   ![image](https://user-images.githubusercontent.com/549519/97369380-11d98480-186a-11eb-9361-80019c297234.png)

### Staging Area

During the publication process, artifacts are first transferred into
the staging area. It is possible to manually release them from the UI
as follows.

1. Go to [https://oss.sonatype.org/](https://oss.sonatype.org/)
1. Login using your [credentials](CREDENTIALS.md).
1. [Select](https://oss.sonatype.org/#stagingRepositories) "Build Promotion: Staging Repositories", then "Public Repositories"
![image](https://user-images.githubusercontent.com/549519/97369415-21f16400-186a-11eb-908e-ac7c0e01f3a8.png)
1. To promote this code to a release, you need to
  - Press the Close button
  - wait for the operation to complete
  - Press the Release button

## Bintray

The [bintray](https://bintray.com/coursera/sbt-plugins/courier-sbt-plugin) interface for sbt-plugin
is easy to use, whether or not you are logged in.


## Artifactory

To see courier snapshots in artifactory:
1. Log into artifactory using Okta
1. In your browser you can directly examine the [repo](https://artifactory.dkandu.me/artifactory/general-snapshots/org/coursera/courier/).
1. You can also navigate to the repo in the UI:
![image](https://user-images.githubusercontent.com/549519/97467332-52d0a800-1901-11eb-822b-eba2fa8f3dfe.png)
1. You can delete any old snapshots from the UI.

## Old Artifacts

You can also find artifacts for [courier-gradle-plugin](https://mvnrepository.com/artifact/org.coursera.courier/courier-gradle-plugin)
and [courier-maven-plugin](https://mvnrepository.com/artifact/org.coursera.courier/courier-maven-plugin).
Both of these are alpha versions that are no longer maintained.
