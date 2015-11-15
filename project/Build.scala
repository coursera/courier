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
import sbt.Keys.libraryDependencies
import sbt._
import Keys._
import org.coursera.courier.sbt.Sonatype
import play.twirl.sbt.SbtTwirl
import play.twirl.sbt.Import.TwirlKeys
import com.simplytyped.Antlr4Plugin._

/**
 * SBT project for Courier.
 */
object Courier extends Build with OverridablePublishSettings {

  override lazy val settings = super.settings ++ overridePublishSettings ++
      Seq(
        organization := "org.coursera.courier")

  //
  // Publishing
  //

  // TODO(jbetz): Figure out how to enable sbt-release for this build.
  // In order to use `sbt-release` we need to figure out how to cross build correctly with it.
  // For now, we can release using the `fullpublish*` aliases defined in the root project and
  // manually updating the version number before and after each release (removing -SNAPSHOT before),
  // adding it back afterward and bumping the version number.
  // OverrideablePublishSetiings allows the artifacts to be published to alternate repos.
  override def defaultPublishSettings: Seq[Def.Setting[_]] = Sonatype.Settings

  //
  // Cross building
  //

  // Our scala build version story is, unfortunately, a bit hairy.
  // In order to keep it under control we primarily concern ourselves with these two below Scala
  // version numbers:

  lazy val sbtScalaVersion = "2.10.5" // the version of Scala used by the current sbt version.
  lazy val currentScalaVersion = "2.11.6" // The current scala version.

  // Our plugin runs as part of SBT so must use the same Scala version that SBT currently uses.
  lazy val pluginVersionSettings = Seq(
    scalaVersion in ThisBuild := sbtScalaVersion,
    crossScalaVersions in ThisBuild := Seq(sbtScalaVersion))

  // We cross build our runtime for recent versions of Scala.
  lazy val runtimeVersionSettings = Seq(
    scalaVersion in ThisBuild := currentScalaVersion,
    crossScalaVersions in ThisBuild := Seq(sbtScalaVersion, currentScalaVersion))

  // Strictly speaking, our generator only needs to be built for the SBT plugin Scala version.
  // But we also cross built it to the current Scala version so that our generator-test
  // project can depend on the generator and still run with the current Scala version, which
  // is more convenient because it allow us to do all testing and development in the current
  // Scala version.
  lazy val generatorVersionSettings = Seq(
    scalaVersion in ThisBuild := sbtScalaVersion,
    crossScalaVersions in ThisBuild := Seq(sbtScalaVersion, currentScalaVersion))

  //
  // Projects
  //
  lazy val generator = Project(id = "courier-generator", base = file("generator"))
    .dependsOn(runtime, generatorApi, grammar)
    .settings(generatorVersionSettings)
    .enablePlugins(SbtTwirl)
    .settings(
      libraryDependencies ++= Seq(
        ExternalDependencies.Pegasus.data,
        ExternalDependencies.Pegasus.generator,
        ExternalDependencies.JUnit.junit,
        ExternalDependencies.Scalatest.scalatest,
        ExternalDependencies.Scalariform.scalariform,
        ExternalDependencies.ApacheCommons.lang),
      dependencyOverrides += ExternalDependencies.ApacheCommons.io)

  lazy val grammar = Project(id = "courier-grammar", base = file("grammar"))
    .settings(antlr4Settings)
    .settings(antlr4PackageName in Antlr4 := Some("org.coursera.courier.grammar"))
    .settings(
      autoScalaLibrary := false,
      crossPaths := false,
      libraryDependencies ++= Seq(
        ExternalDependencies.Pegasus.data,
        ExternalDependencies.JUnit.junit,
        ExternalDependencies.JUnitInterface.junitInterface,
        ExternalDependencies.ApacheCommons.lang),
      dependencyOverrides += ExternalDependencies.ApacheCommons.io,
      testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-q", "-v"),
      testOptions in Test += Tests.Setup { () =>
        System.setProperty("project.dir", baseDirectory.value.getAbsolutePath)
      })

  lazy val generatorApi = Project(id = "courier-generator-api", base = file("generator-api"))
    .dependsOn(grammar)
    .settings(
      autoScalaLibrary := false,
      crossPaths := false,
      libraryDependencies ++= Seq(
        ExternalDependencies.Pegasus.data,
        ExternalDependencies.Pegasus.generator,
        ExternalDependencies.JUnit.junit))

  lazy val runtime = Project(id = "courier-runtime", base = file("runtime"))
    .settings(runtimeVersionSettings)
    .settings(
      libraryDependencies ++= Seq(
        ExternalDependencies.Pegasus.data,
        ExternalDependencies.JUnit.junit,
        ExternalDependencies.Scalatest.scalatest))
    .settings(
      libraryDependencies ++=
        ExternalDependencies.ScalaParserCombinators.dependencies(scalaVersion.value))

  lazy val generatorTest = Project(id = "courier-generator-test", base = file("generator-test"))
    .dependsOn(generator)
    .settings(packagedArtifacts := Map.empty) // do not publish
    .settings(runtimeVersionSettings)
    .settings(forkedVmCourierGeneratorSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        ExternalDependencies.JUnit.junit,
        ExternalDependencies.Scalatest.scalatest))

  lazy val courierSbtPlugin = Project(id = "courier-sbt-plugin", base = file("sbt-plugin"))
    .dependsOn(generator)
    .settings(pluginVersionSettings)
    //.settings(libraryDependencies += "com.github.eirslett" %% "sbt-slf4j" % "0.1")
    .settings(
      sbtPlugin := true,
      name := "courier-sbt-plugin")

  // TODO(jbetz): Once SBT supports scala 2.11, we can enable .aggregate for all
  // project dependencies that have .dependsOn in the above projects, and then we will only
  // need to run `project courier-sbt-plugin;publish` here, which will dramatically simplify things.
  // Until then we have to be very explicit about publishing exactly what we want, mainly
  // to avoid build failures that would happen if we tried to publish the sbt plugin with scala
  // 2.11.
  def publishCommands(publishCommand: String) =
    s";project courier-grammar;$publishCommand" + // java project, so we do not cross build
    s";project courier-generator-api;$publishCommand" + // java project, so we do not cross build
    s";++$sbtScalaVersion;project courier-generator;$publishCommand" +
    s";++$currentScalaVersion;project courier-generator;$publishCommand" +
    s";++$sbtScalaVersion;project courier-sbt-plugin;$publishCommand" +
    s";++$sbtScalaVersion;project courier-runtime;$publishCommand" +
    s";++$currentScalaVersion;project courier-runtime;$publishCommand"

  lazy val root = Project(id = "courier", base = file("."))
    .aggregate(generator, grammar, runtime, courierSbtPlugin, generatorTest)
    .settings(runtimeVersionSettings)
    .settings(packagedArtifacts := Map.empty) // disable publish for root aggregate module
    .settings(
      // scripted attempts to publish what it needs, but because of the above mentioned cross
      // build issues, we have to manually publish what we need before we test here
      addCommandAlias(s"fulltest", s";compile;test;fullpublish-ivylocal;" +
                      s"project courier;++$sbtScalaVersion;scripted"),
      addCommandAlias("fullpublish", publishCommands("publish")),
      addCommandAlias("fullpublish-signed", publishCommands("publish-signed")),
      addCommandAlias("fullpublish-ivylocal", publishCommands("publish-local")),
      addCommandAlias("fullpublish-mavenlocal", publishCommands("publishM2")))

  //
  // Dependencies
  //

  object ExternalDependencies {
    object Pegasus {
      val version = "2.6.0"
      val avroVersion = "1_6"
      val data = "com.linkedin.pegasus" % "data" % version
      val dataAvro = "com.linkedin.pegasus" % s"data-avro-$avroVersion" % version
      val generator = ("com.linkedin.pegasus" % "generator" % version)
        // Only used by the java code generator, which we do not use.
        .exclude("com.linkedin.pegasus", "r2-core")
        .exclude("com.sun.codemodel", "codemodel")
    }

    object ScalaParserCombinators {
      val version = "1.0.4"

      // this is part of scala-library in 2.10 and earlier
      def dependencies(scalaVersion: String) = CrossVersion.partialVersion(scalaVersion) match {
        case Some((2, scalaMajor)) if scalaMajor > 10 =>
          Seq("org.scala-lang.modules" %% "scala-parser-combinators" % version)
        case _ =>
          Seq.empty[ModuleID]
      }
    }

    object JUnit {
      val version = "4.11"
      val junit = "junit" % "junit" % version % "test"
    }

    object JUnitInterface {
      val version = "0.10"
      val junitInterface = "com.novocode" % "junit-interface" % version % "test"
    }

    object Scalatest {
      val version = "2.2.3"
      val scalatest = "org.scalatest" %% "scalatest" % version % "test"
    }

    object ApacheCommons {
      val langVersion = "3.4"
      val lang = "org.apache.commons" % "commons-lang3" % langVersion

      val ioVersion = "2.4"
      val io = "commons-io" % "commons-io" % ioVersion
    }

    object Scalariform {
      val version = "0.1.6"
      val scalariform = "org.scalariform" %% "scalariform" % version
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
  lazy val forkedVmCourierDest = settingKey[File]("Generator target directory")

  val forkedVmCourierGeneratorSettings = Seq(
    forkedVmCourierDest :=
      target.value / s"scala-${scalaBinaryVersion.value}" / "courier",

    forkedVmCourierGenerator in Compile := {
      val src = sourceDirectory.value / "main" / "pegasus"
      val dst = forkedVmCourierDest.value
      val classpath = (dependencyClasspath in Runtime in generator).value.files
      streams.value.log.info("Generating courier bindings...")
      val files = runForkedGenerator(src, dst, classpath, streams.value.log)
      streams.value.log.info(s"There are ${files.size} classes generated from courier bindings")
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


