/*
 Copyright 2015 Coursera Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

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
