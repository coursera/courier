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
object Courier extends Build {

  override lazy val settings = super.settings ++ Sonatype.Settings ++ Seq(
    organization := "org.coursera.courier",
    releaseProcess := Seq[ReleaseStep](
        checkSnapshotDependencies,
        inquireVersions,
        runClean,
        ReleaseStep(action = Command.process("fulltest", _)),
        setReleaseVersion,
        commitReleaseVersion,
        tagRelease,
        ReleaseStep(action = Command.process("courier-sbt-plugin:publishSigned", _)),
        ReleaseStep(action = Command.process("+courier-runtime:publishSigned", _)),
        setNextVersion,
        commitNextVersion,
        ReleaseStep(action = Command.process("courier-sbt-plugin:sonatypeReleaseAll", _)),
        ReleaseStep(action = Command.process("+courier-runtime:sonatypeReleaseAll", _)),
        pushChanges))

  //
  // Cross building
  //

  // Our scala build version story is, unfortunately, a bit hairy.
  // In order to keep it under control we primarily concern ourselves with these two below Scala
  // version numbers:

  lazy val sbtScalaVersion = "2.10.5" // the version of Scala used by the current sbt version.
  lazy val currentScalaVersion = "2.11.6" // The current scala version.

  // We can add other scala versions here as we want for cross building.
  lazy val crossBuildScalaVersions = Seq(sbtScalaVersion, currentScalaVersion)

  // Our plugin runs as part of SBT so must use the same Scala version that SBT currently uses.
  lazy val pluginVersionSettings = Seq(
    scalaVersion in ThisBuild := sbtScalaVersion,
    crossScalaVersions in ThisBuild := Seq(sbtScalaVersion))

  // We cross build our runtime for recent versions of Scala.
  lazy val runtimeVersionSettings = Seq(
    scalaVersion in ThisBuild := currentScalaVersion,
    crossScalaVersions in ThisBuild := crossBuildScalaVersions)

  // Strictly speaking, our generator only needs to be built for the SBT plugin Scala version.
  // But we also cross built it to the current Scala version so that our generator-test
  // project can depend on the generator and still run with the current Scala version, which
  // is more convenient because it allow us to do all testing and development in the current
  // Scala version.
  lazy val generatorVersionSettings = Seq(
    scalaVersion in ThisBuild := sbtScalaVersion,
    crossScalaVersions in ThisBuild := crossBuildScalaVersions)

  //
  // Projects
  //
  lazy val generator = Project(id = "courier-generator", base = file("generator"))
    .dependsOn(runtime)
    .aggregate(runtime)
    .settings(generatorVersionSettings)
    .settings(Twirl.settings: _*)
    .settings(
      libraryDependencies ++= Seq(
        ExternalDependencies.Pegasus.data,
        ExternalDependencies.Pegasus.generator,
        ExternalDependencies.ScalaLogging.scalaLoggingSlf4j,
        ExternalDependencies.JUnit.junit,
        ExternalDependencies.Scalatest.scalatest))

  lazy val runtime = Project(id = "courier-runtime", base = file("runtime"))
    .settings(runtimeVersionSettings)
    .settings(
      libraryDependencies += ExternalDependencies.Pegasus.data)

  lazy val generatorTest = Project(id = "courier-generator-test", base = file("generator-test"))
    .dependsOn(generator)
    .settings(packagedArtifacts := Map.empty) // do not publish
    .settings(runtimeVersionSettings)
    .settings(forkedVmCourierGeneratorSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        ExternalDependencies.ScalaLogging.scalaLoggingSlf4j,
        ExternalDependencies.JUnit.junit,
        ExternalDependencies.Scalatest.scalatest))

  lazy val courierSbtPlugin = Project(id = "courier-sbt-plugin", base = file("sbt-plugin"))
    .dependsOn(generator)
    .aggregate(generator)
    .settings(pluginVersionSettings)
    .settings(libraryDependencies += "com.github.eirslett" %% "sbt-slf4j" % "0.1")
    .settings(
      sbtPlugin := true,
      name := "courier-sbt-plugin")


  // This is a temporary project. It contains hand written files that exemplify the structure of
  // the Scala classes Courier should generate. Once the generator is stable, this project should
  // be deleted, with schemas that we can use for testing moved into appropriate test directories.
  lazy val spec = Project("spec", file("spec"))
    .settings(
      packagedArtifacts := Map.empty, // disable publishing for this project
      libraryDependencies ++= Seq(
        ExternalDependencies.Pegasus.data,
        ExternalDependencies.Pegasus.dataAvro,
        ExternalDependencies.JUnit.junit,
        ExternalDependencies.Scalatest.scalatest,
        ("joda-time" % "joda-time" % "2.0").withSources().withJavadoc()))

  lazy val root = Project(id = "courier", base = file("."))
    .aggregate(generator, runtime, courierSbtPlugin, generatorTest)
    .settings(packagedArtifacts := Map.empty) // disable publish for root aggregate module
    .settings(
      addCommandAlias("fulltest", ";compile;test;scripted"),
      addCommandAlias("fullpublish", ";courier-sbt-plugin:publish;+courier-runtime:publish"),
      addCommandAlias(
        "fullpublish-local", ";courier-sbt-plugin:publish-local;+courier-runtime:publish-local"))

  //
  // Dependencies
  //

  object ExternalDependencies {
    object Pegasus {
      val version = "2.6.0"
      val avroVersion = "1_6"
      val data = "com.linkedin.pegasus" % "data" % version
      val dataAvro = "com.linkedin.pegasus" % s"data-avro-$avroVersion" % version
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

  //
  // Helper tasks
  //

  // In order to be able to quickly test our generator, we use
  // this approach, which has has been taken directly from Sleipnir by Dmitriy Yefremov.
  lazy val forkedVmCourierGenerator = taskKey[Seq[File]](
    "Courier generator executed in a forked VM")

  val forkedVmCourierGeneratorSettings = Seq(
    forkedVmCourierGenerator in Compile := {
      val src = sourceDirectory.value / "main" / "pegasus"
      val dst = target.value / s"scala-${scalaBinaryVersion.value}" / "courier"
      val classpath = (dependencyClasspath in Runtime in generator).value.files
      streams.value.log.info("Generating .pdsc bindings...")
      val files = runForkedGenerator(src, dst, classpath, streams.value.log)
      streams.value.log.info(s"There are ${files.size} classes generated from .pdsc")
      files
    },
    sourceGenerators in Compile <+= (forkedVmCourierGenerator in Compile),
    unmanagedSourceDirectories in Compile +=
      target.value / s"scala-${scalaBinaryVersion.value}" / "courier",
    managedSourceDirectories in Compile +=
      target.value / s"scala-${scalaBinaryVersion.value}" / "courier",
    cleanFiles += target.value / s"scala-${scalaBinaryVersion.value}" / "courier"
  )

  def runForkedGenerator(src: File, dst: File, classpath: Seq[File], log: Logger): Seq[File] = {
    val mainClass = "org.coursera.courier.generator.ScalaDataTemplateGenerator"
    val args = Seq(dst.toString, src.toString)
    val jvmOptions = Seq.empty
    IO.withTemporaryFile("courier", "output") { tmpFile =>
      val outStream = new java.io.FileOutputStream(tmpFile)
      try {
        val exitValue = new Fork.ForkScala(mainClass)(
          None, jvmOptions, classpath, args, None, CustomOutput(outStream))
        val outputLines = scala.io.Source.fromFile(tmpFile).getLines().toSeq
        if (exitValue != 0) {
          outputLines.foreach(println)
          sys.error(s"Code generator failed with exit code: $exitValue")
        } else {
          outputLines.map(file(_)).toSeq
        }
      } finally {
        outStream.close()
      }
    }
  }
}


