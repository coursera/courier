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

import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease.ReleaseStateTransformations._
import sbt.Keys.libraryDependencies
import sbt._
import Keys._
import twirl.sbt.TwirlPlugin._
import org.coursera.courier.sbt.Sonatype

/**
 * SBT project for Courier.
 */
object CourierBuild extends Build {

  override lazy val settings =
    super.settings ++ Sonatype.Settings ++ Seq(

    organization := "org.coursera.courier",
    releaseProcess := Seq[ReleaseStep](
        checkSnapshotDependencies,
        inquireVersions,
        runClean,
        runTest,
        setReleaseVersion,
        commitReleaseVersion,
        tagRelease,
        ReleaseStep(action = Command.process("publishSigned", _)),
        setNextVersion,
        commitNextVersion,
        ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
        pushChanges))

  lazy val pluginBuildSettings = Seq(
    scalaVersion in ThisProject := "2.10.5")

  lazy val runtimeBuildSettings = Seq(
    scalaVersion in ThisProject := "2.11.6",
    crossScalaVersions in ThisProject := Seq("2.10.5", "2.11.6"))

  lazy val generator = Project(id = "courier-generator", base = file("generator"))
    .settings(pluginBuildSettings)
    .dependsOn(runtime)
    .aggregate(runtime)
    .settings(Twirl.settings: _*)
    .settings(
      libraryDependencies ++= Seq(
        ExternalDependencies.Pegasus.data,
        ExternalDependencies.Pegasus.generator,
        ExternalDependencies.ScalaLogging.scalaLoggingSlf4j,
        ExternalDependencies.JUnit.junit,
        ExternalDependencies.Scalatest.scalatest))

  lazy val runtime = Project(id = "courier-runtime", base = file("runtime"))
    .settings(runtimeBuildSettings)
    .settings(
      libraryDependencies += ExternalDependencies.Pegasus.data)

  // This is a temporary project. It contains hand written files that exemplify the structure of
  // the Scala classes Courier should generate. Once the generator is stable, this project should
  // be deleted, with schemas that we can use for testing moved into appropriate test directories.
  lazy val spec = Project("spec", file("spec"))
    .settings(
      packagedArtifacts := Map.empty, // disable publishing for this project
      libraryDependencies ++= Seq(
        ExternalDependencies.Pegasus.data,
        ExternalDependencies.Pegasus.dataAvro16,
        ExternalDependencies.JUnit.junit,
        ExternalDependencies.Scalatest.scalatest,
        ("joda-time" % "joda-time" % "2.0").withSources().withJavadoc()))

  lazy val courierSbtPlugin = Project(id = "courier-sbt-plugin", base = file("sbt-plugin"))
    .settings(pluginBuildSettings)
    .dependsOn(generator)
    .aggregate(generator)
    .settings(
      sbtPlugin := true,
      scalaVersion in ThisProject := "2.10.5", // SBT is currently on Scala 2.10
      crossScalaVersions in ThisProject := Seq("2.10.5"),
      name := "courier-sbt-plugin")

  lazy val root = Project(id = "courier", base = file("."))
    .settings(packagedArtifacts := Map.empty) // disable publish for root aggregate module
    .aggregate(generator, runtime, courierSbtPlugin)

  object ExternalDependencies {
    object Pegasus {
      val version = "2.6.0"
      val avroVersion = "1_6"
      val data = "com.linkedin.pegasus" % "data" % version
      val dataAvro16 = "com.linkedin.pegasus" % s"data-avro-$avroVersion" % version
      val generator = "com.linkedin.pegasus" % "generator" % version
    }

    object ScalaLogging {
      val version = "2.1.2"
      val scalaLoggingSlf4j = "com.typesafe.scala-logging" %% "scala-logging-slf4j" % version
    }

    object JUnit {
      val version = "4.11"
      val junit = "junit" % "junit" % version % "test"
    }

    object Scalatest {
      val version = "2.2.3"
      val scalatest = "org.scalatest" %% "scalatest" % version % "test"
    }
  }

  object Repos {
    val mavenCentralReleases =
      "releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
  }
}


