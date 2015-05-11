import sbt._
import Keys._

// TODO(jbetz): Look into using bintray to manage all publishing
trait OverridablePublishSettings {
  private[this] val releaseKey = "sbt.override.publish.repos.release"
  private[this] val snapshotKey = "sbt.override.publish.repos.snapshot"
  private[this] val overrideReleaseRepo = Option(System.getProperty(releaseKey))
  private[this] val overrideSnapshotRepo = Option(System.getProperty(snapshotKey))

  val defaultPublishSettings: Seq[Def.Setting[_]]

  val overridePublishSettings = {
    assert(overrideReleaseRepo.isDefined == overrideSnapshotRepo.isDefined,
      s"If overriding publish repos, both $releaseKey and $snapshotKey must be provided")

    (overrideReleaseRepo, overrideSnapshotRepo) match {
      case (Some(release), Some(snapshot)) =>
        Seq(
          publishTo := Some(if (version.value.trim.endsWith("SNAPSHOT")) {
            println("version:" + version.value)
          "snapshots" at snapshot
        } else {
          "releases" at release
        })
      )
      case _: Any => defaultPublishSettings
    }
  }
}
