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

import java.io.File

import sbt.Keys._
import sbt.Tests
import sbt.Keys.libraryDependencies
import sbt._
import Keys._

/**
  * SBT build helpers for Courier.
  * Project definitions have moved to the root build.sbt.
  */
object CourierBuild {

  //
  // Cross building
  //

  // SBT 1.x uses Scala 2.12; our cross-build target is 2.13.
  lazy val sbtScalaVersion = "2.12.19"   // Scala version used by SBT 1.x
  lazy val currentScalaVersion = "2.13.12" // Primary cross-build target

  // Our plugin runs as part of SBT so must use the Scala version that SBT uses (2.12).
  lazy val pluginVersionSettings: Seq[Setting[_]] = Seq(
    scalaVersion := sbtScalaVersion,
    crossScalaVersions := Seq(sbtScalaVersion)
  )

  // We cross build our runtime to 2.12 and 2.13.
  lazy val runtimeVersionSettings: Seq[Setting[_]] = Seq(
    scalaVersion := currentScalaVersion,
    crossScalaVersions := Seq(sbtScalaVersion, currentScalaVersion)
  )

  // Generator is cross-built to the SBT plugin Scala version and current Scala version.
  lazy val generatorVersionSettings: Seq[Setting[_]] = Seq(
    scalaVersion := sbtScalaVersion,
    crossScalaVersions := Seq(sbtScalaVersion, currentScalaVersion)
  )

  // Java project settings
  lazy val plainJavaProjectSettings: Seq[Setting[_]] = Seq(
    autoScalaLibrary := false,
    crossPaths := false
  )

  // Test settings (without the Tests.Setup which referenced a project — see sub-project build.sbt)
  lazy val junitTestSettings: Seq[Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      ExternalDependencies.JUnit.junit,
      ExternalDependencies.JUnitInterface.junitInterface),
    Test / testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
  )

  //
  // Projects
  //

  //
  // Publishing
  //

  // TODO(jbetz): Figure out how to enable sbt-release for this build.
  // In order to use `sbt-release` we need to figure out how to cross build correctly with it.
  // For now, we can release using the `fullpublish*` aliases defined in the root project and
  // manually updating the version number before and after each release (removing -SNAPSHOT before),
  // adding it back afterward and bumping the version number.

  def publishCommands(publishCommand: String,
                      sbtPluginCommand: Option[String] = None): String = {
    // We do not cross build java projects:
    val baseCommand = s";project schema-language;$publishCommand" +
      s";project generator-api;$publishCommand" +
      s";project java-generator;$publishCommand" +
      s";project java-runtime;$publishCommand" +
      s";project android-generator;$publishCommand" +
      s";project android-runtime;$publishCommand" +
      s";project swift-generator;$publishCommand" +
      s";project typescript-lite-generator;$publishCommand" +
      s";++$sbtScalaVersion;project scala-generator;$publishCommand" +
      s";++$currentScalaVersion;project scala-generator;$publishCommand" +
      s";++$sbtScalaVersion;project scala-runtime;$publishCommand" +
      s";++$currentScalaVersion;project scala-runtime;$publishCommand" +
      s";++$sbtScalaVersion;project scala-fixture;$publishCommand" +
      s";++$currentScalaVersion;project scala-fixture;$publishCommand"
    sbtPluginCommand
      .map { sbtPluginCommand =>
        baseCommand + s";++$sbtScalaVersion;project sbt-plugin;$sbtPluginCommand" +
          s";++$currentScalaVersion;project sbt-plugin;$sbtPluginCommand"
      }
      .getOrElse {
        baseCommand
      }
  }

  //
  // Dependencies
  //

  object ExternalDependencies {
    object Pegasus {
      val version = "3.1.1"
      val avroVersion = "1_6"
      val data = "com.linkedin.pegasus" % "data" % version
      val dataAvro = "com.linkedin.pegasus" % s"data-avro-$avroVersion" % version
      val generator = ("com.linkedin.pegasus" % "generator" % version)
        .exclude("com.linkedin.pegasus", "r2-core")
    }

    object ScalaParserCombinators {
      val version = "1.1.2"

      def dependencies(scalaVersion: String) =
        CrossVersion.partialVersion(scalaVersion) match {
          case Some((2, scalaMajor)) if scalaMajor > 10 =>
            Seq(
              "org.scala-lang.modules" %% "scala-parser-combinators" % version)
          case _ =>
            Seq.empty[ModuleID]
        }
    }

    object JUnit {
      val version = "4.11"
      val junit = "junit" % "junit" % version % "test"
    }

    object JUnitInterface {
      val version = "0.11"
      val junitInterface = "com.github.sbt" % "junit-interface" % version % "test"
    }

    object Scalatest {
      val version = "3.2.19"
      val scalatest = "org.scalatest" %% "scalatest" % version % "test"
    }

    object ScalatestPlusJunit {
      val scalatestPlusJunit = "org.scalatestplus" %% "junit-4-13" % "3.2.19.0" % "test"
    }

    object ApacheCommons {
      val langVersion = "3.4"
      val lang = "org.apache.commons" % "commons-lang3" % langVersion

      val ioVersion = "2.4"
      val io = "commons-io" % "commons-io" % ioVersion
    }

    object Scalariform {
      val version = "0.2.10"
      val scalariform = "org.scalariform" %% "scalariform" % version
    }

    object Rythm {
      val version = "1.0.1"
      val rythmEngine = "org.rythmengine" % "rythm-engine" % version
    }

    object Gson {
      val version = "2.3.1"
      val gson = "com.google.code.gson" % "gson" % version
    }

    object JodaTime {
      val version = "2.9.9"
      val jodaTime = "joda-time" % "joda-time" % version
    }

    object Slf4j {
      val version = "1.7.12"
      val slf4jSimple = "org.slf4j" % "slf4j-simple" % version
    }

    object Coursera {
      val courscala = "org.coursera" %% "courscala" % "0.1.3"
    }
  }

  object Repos {
    val mavenCentralReleases =
      "releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
  }

  //
  // Test generator
  //

  lazy val forkedVmCourierGenerator =
    taskKey[Seq[File]]("Courier generator executed in a forked VM")
  lazy val forkedVmCourierDest = settingKey[File]("Generator target directory")

  lazy val forkedVmCourierMainClass =
    settingKey[String]("Main Generator class to execute.")

  lazy val forkedVmCourierClasspath =
    taskKey[Seq[File]]("Classpath to use when running the generator.")

  lazy val forkedVmSourceDirectory =
    settingKey[File]("directory containing .courier and .pdsc files")

  lazy val forkedVmAdditionalArgs =
    settingKey[Seq[String]]("Additional args to pass to the generator")

  val forkedVmCourierGeneratorSettings: Seq[Setting[_]] = Seq(
    Compile / forkedVmCourierGenerator := {
      val mainClass = forkedVmCourierMainClass.value
      val src = forkedVmSourceDirectory.value
      val dst = forkedVmCourierDest.value
      val classpath = forkedVmCourierClasspath.value
      val additionalArgs = forkedVmAdditionalArgs.value
      streams.value.log.info(s"Generating courier bindings for files in $src...")
      val files =
        runForkedGenerator(mainClass,
                           src,
                           dst,
                           classpath,
                           additionalArgs,
                           streams.value.log)
      streams.value.log
        .info(s"${files.size} classes generated from $src for $mainClass")
      files
    },
    forkedVmAdditionalArgs := Seq(),
    Compile / sourceGenerators += (Compile / forkedVmCourierGenerator).taskValue,
    Compile / unmanagedSourceDirectories +=
      target.value / s"scala-${scalaBinaryVersion.value}" / "courier",
    Compile / managedSourceDirectories +=
      target.value / s"scala-${scalaBinaryVersion.value}" / "courier",
    cleanFiles += target.value / s"scala-${scalaBinaryVersion.value}" / "courier"
  )

  def runForkedGenerator(mainClass: String,
                         src: File,
                         dst: File,
                         classpath: Seq[File],
                         additionalArgs: Seq[String],
                         log: Logger): Seq[File] = {
    IO.withTemporaryFile("courier", "output") { tmpFile =>
      val outStream = new java.io.FileOutputStream(tmpFile)
      try {
        val args = Seq(dst.toString, src.toString, src.toString) ++ additionalArgs
        val exitValue =
          Fork.java(
            ForkOptions().withOutputStrategy(CustomOutput(outStream)),
            "-cp" +:
              classpath
                .map(_.getAbsolutePath)
                .mkString(java.io.File.pathSeparator) +:
              mainClass +:
              args)
        val outputLines = scala.io.Source.fromFile(tmpFile).getLines().toSeq
        if (exitValue != 0) {
          outputLines.foreach(println)
          sys.error(s"Code generator failed with exit code: $exitValue")
        } else {
          outputLines.map(file)
        }
      } finally {
        outStream.close()
      }
    }
  }

  //
  // Other Commands
  //
  lazy val executableFile = taskKey[File](
    "Distributes the current version as an executable at cli/target/courier")
}
