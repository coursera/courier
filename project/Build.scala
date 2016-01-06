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
import sbtrelease.ReleasePlugin.autoImport._
import sbt.Keys.libraryDependencies
import sbt._
import Keys._
import org.coursera.courier.sbt.Sonatype
import play.twirl.sbt.SbtTwirl
import play.twirl.sbt.Import.TwirlKeys
import com.simplytyped.Antlr4Plugin._
import sbtassembly.AssemblyKeys._
import sbtassembly.AssemblyPlugin.defaultShellScript

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
  lazy val scalaGenerator = Project(id = "scala-generator", base = file("scala") / "generator")
    .dependsOn(scalaRuntime, scalaGeneratorApi, schemaLanguage)
    .settings(generatorVersionSettings)
    .enablePlugins(SbtTwirl)
    .settings(
      name := "courier-generator",
      libraryDependencies ++= Seq(
        ExternalDependencies.Pegasus.data,
        ExternalDependencies.Pegasus.generator,
        ExternalDependencies.JUnit.junit,
        ExternalDependencies.Scalatest.scalatest,
        ExternalDependencies.Scalariform.scalariform,
        ExternalDependencies.ApacheCommons.lang),
      dependencyOverrides += ExternalDependencies.ApacheCommons.io)

  lazy val schemaLanguage = Project(id = "schema-language", base = file("schema-language"))
    .settings(antlr4Settings)
    .settings(plainJavaProjectSettings)
    .settings(junitTestSettings)
    .settings(
      name := "courier-grammar",
      antlr4PackageName in Antlr4 := Some("org.coursera.courier.grammar"),
      libraryDependencies ++= Seq(
        ExternalDependencies.Pegasus.data,
        ExternalDependencies.ApacheCommons.lang),
      dependencyOverrides += ExternalDependencies.ApacheCommons.io)

  lazy val scalaGeneratorApi = Project(id = "generator-api", base = file("generator-api"))
    .dependsOn(schemaLanguage)
    .settings(plainJavaProjectSettings)
    .settings(junitTestSettings)
    .settings(
      name := "courier-generator-api",
      libraryDependencies ++= Seq(
        ExternalDependencies.Pegasus.data,
        ExternalDependencies.Pegasus.generator))

  lazy val scalaRuntime = Project(id = "scala-runtime", base = file("scala") / "runtime")
    .settings(runtimeVersionSettings)
    .settings(
      name := "courier-runtime",
      libraryDependencies ++= Seq(
        ExternalDependencies.Pegasus.data,
        ExternalDependencies.JUnit.junit,
        ExternalDependencies.Scalatest.scalatest)
        ++
        ExternalDependencies.ScalaParserCombinators.dependencies(scalaVersion.value))

  lazy val scalaGeneratorTest = Project(id = "scala-generator-test", base = file("scala") / "generator-test")
    .dependsOn(scalaGenerator)
    .settings(runtimeVersionSettings)
    .settings(forkedVmCourierGeneratorSettings)
    .settings(
      name := "courier-generator-test",
      forkedVmCourierMainClass := "org.coursera.courier.generator.ScalaDataTemplateGenerator",
      forkedVmCourierClasspath := (dependencyClasspath in Runtime in scalaGenerator).value.files,
      forkedVmSourceDirectory := (sourceDirectory in referenceSuite).value / "main" / "courier",
      forkedVmCourierDest :=
        target.value / s"scala-${scalaBinaryVersion.value}" / "courier",
      packagedArtifacts := Map.empty, // do not publish
      libraryDependencies ++= Seq(
        ExternalDependencies.JodaTime.jodaTime,
        ExternalDependencies.JUnit.junit,
        ExternalDependencies.Scalatest.scalatest),
      fork in Test := true,
      javaOptions in Test += // TODO(jbetz): figure out how to use testOptions in Test here
          "-Dreferencesuite.srcdir=" + (sourceDirectory in referenceSuite).value.getAbsolutePath)

  lazy val javaGenerator = Project(id = "java-generator", base = file("java") / "generator")
    .dependsOn(scalaGeneratorApi)
    .settings(plainJavaProjectSettings)
    .settings(
      name := "courier-java-generator",
      libraryDependencies ++= Seq("com.sun.codemodel" % "codemodel" % "2.2"))

  lazy val javaGeneratorTest = Project(id = "java-generator-test", base = file("java") / "generator-test")
    .dependsOn(javaGenerator)
    .settings(forkedVmCourierGeneratorSettings)
    .settings(junitTestSettings)
    .settings(plainJavaProjectSettings)
    .settings(
      name := "courier-java-generator-test",
      forkedVmCourierMainClass := "org.coursera.courier.JavaGenerator",
      forkedVmCourierClasspath := (dependencyClasspath in Runtime in javaGenerator).value.files,
      forkedVmSourceDirectory := (sourceDirectory in referenceSuite).value / "main" / "courier",
      forkedVmCourierDest :=
        target.value / s"scala-${scalaBinaryVersion.value}" / "courier",
      packagedArtifacts := Map.empty) // do not publish

  lazy val androidGenerator = Project(id = "android-generator", base = file("android") / "generator")
    .dependsOn(scalaGeneratorApi)
    .settings(plainJavaProjectSettings)
    .settings(
      name := "courier-android-generator",
      libraryDependencies ++= Seq(
        ExternalDependencies.Rythm.rythmEngine,
        ExternalDependencies.Gson.gson,
        ExternalDependencies.JodaTime.jodaTime))

  lazy val javaRuntime = Project(id = "java-runtime", base = file("java") / "runtime")
    .settings(plainJavaProjectSettings)
    .settings(
      name := "courier-java-runtime",
      libraryDependencies ++= Seq(
        ExternalDependencies.Pegasus.data))

  lazy val androidGeneratorTest = Project(id = "android-generator-test", base = file("android") / "generator-test")
    .dependsOn(androidGenerator, androidRuntime)
    .settings(forkedVmCourierGeneratorSettings)
    .settings(junitTestSettings)
    .settings(plainJavaProjectSettings)
    .settings(
      name := "courier-android-generator-test",
      forkedVmCourierMainClass := "org.coursera.courier.AndroidGenerator",
      forkedVmCourierClasspath := (dependencyClasspath in Runtime in androidGenerator).value.files,
      forkedVmSourceDirectory := (sourceDirectory in referenceSuite).value / "main" / "courier",
      forkedVmCourierDest :=
        target.value / s"scala-${scalaBinaryVersion.value}" / "courier",
      packagedArtifacts := Map.empty) // do not publish

  lazy val referenceSuite = Project(id = "reference-suite", base = file("reference-suite"))

  lazy val androidRuntime = Project(id = "android-runtime", base = file("android") / "runtime")
    .settings(plainJavaProjectSettings)
    .settings(
      name := "courier-android-runtime",
      libraryDependencies ++= Seq(
        ExternalDependencies.Gson.gson))

  lazy val swiftGenerator = Project(id = "swift-generator", base = file("swift") / "generator")
    .dependsOn(scalaGeneratorApi)
    .settings(plainJavaProjectSettings)
    .settings(
      name := "courier-swift-generator",
      mainClass in assembly := Some("org.coursera.courier.SwiftGenerator"),
      assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultShellScript)),
      assemblyJarName in assembly := s"${name.value}-${version.value}.jar",
      libraryDependencies ++= Seq(
        ExternalDependencies.Rythm.rythmEngine,
        ExternalDependencies.Slf4j.slf4jSimple))

  lazy val swiftGeneratorTest = Project(id = "swift-generator-test", base = file("swift") / "generator-test")
    .dependsOn(androidGenerator, androidRuntime)
    .settings(forkedVmCourierGeneratorSettings)
    .settings(junitTestSettings)
    .settings(plainJavaProjectSettings)
    .settings(
      name := "courier-swift-generator-test",
      forkedVmCourierMainClass := "org.coursera.courier.SwiftGenerator",
      forkedVmCourierClasspath := (dependencyClasspath in Runtime in swiftGenerator).value.files,
      forkedVmSourceDirectory := (sourceDirectory in referenceSuite).value / "main" / "courier",
      forkedVmCourierDest := file("swift") / "testsuite" / "testsuiteTests" / "generated",
      forkedVmAdditionalArgs := Seq("REQUIRED_FIELDS_MAY_BE_ABSENT", "EQUATABLE"),
      packagedArtifacts := Map.empty, // do not publish
      libraryDependencies ++= Seq(
        ExternalDependencies.JodaTime.jodaTime))

  lazy val courierSbtPlugin = Project(id = "sbt-plugin", base = file("sbt-plugin"))
    .dependsOn(scalaGenerator)
    .settings(pluginVersionSettings)
    .settings(
      sbtPlugin := true,
      name := "courier-sbt-plugin")

  lazy val plainJavaProjectSettings = Seq(
    autoScalaLibrary := false,
    crossPaths := false
  )

  lazy val junitTestSettings = Seq(
    libraryDependencies ++= Seq(
      ExternalDependencies.JUnit.junit,
      ExternalDependencies.JUnitInterface.junitInterface),
    // -q will hide output of successful tests
    testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-v"),
    testOptions in Test += Tests.Setup { () =>
      System.setProperty("project.dir", baseDirectory.value.getAbsolutePath)
      System.setProperty(
        "referencesuite.srcdir",
        (sourceDirectory in referenceSuite).value.getAbsolutePath)
    })

  // TODO(jbetz): Once SBT supports scala 2.11, we can enable .aggregate for all
  // project dependencies that have .dependsOn in the above projects, and then we will only
  // need to run `project courier-sbt-plugin;publish` here, which will dramatically simplify things.
  // Until then we have to be very explicit about publishing exactly what we want, mainly
  // to avoid build failures that would happen if we tried to publish the sbt plugin with scala
  // 2.11.
  def publishCommands(publishCommand: String) =
    // We do not cross build java projects:
    s";project schema-language;$publishCommand" +
    s";project generator-api;$publishCommand" +
    s";project java-generator;$publishCommand" +
    s";project java-runtime;$publishCommand" +
    s";project android-generator;$publishCommand" +
    s";project android-runtime;$publishCommand" +
    s";project swift-generator;$publishCommand" +
    s";++$sbtScalaVersion;project scala-generator;$publishCommand" +
    s";++$currentScalaVersion;project scala-generator;$publishCommand" +
    s";++$sbtScalaVersion;project sbt-plugin;$publishCommand" +
    s";++$sbtScalaVersion;project scala-runtime;$publishCommand" +
    s";++$currentScalaVersion;project scala-runtime;$publishCommand"

  lazy val root = Project(id = "courier", base = file("."))
    .aggregate(
      scalaGenerator,
      schemaLanguage,
      scalaRuntime,
      courierSbtPlugin,
      scalaGeneratorTest,
      androidGenerator,
      androidGeneratorTest,
      androidRuntime,
      swiftGenerator,
      swiftGeneratorTest)
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
      val version = "3.1.1"
      val avroVersion = "1_6"
      val data = "com.linkedin.pegasus" % "data" % version
      val dataAvro = "com.linkedin.pegasus" % s"data-avro-$avroVersion" % version
      val generator = ("com.linkedin.pegasus" % "generator" % version)
        // Only used by the java code generator, which we do not use.
        .exclude("com.linkedin.pegasus", "r2-core")
        //.exclude("com.sun.codemodel", "codemodel")
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

    object Rythm {
      val version = "1.0.1"
      val rythmEngine = "org.rythmengine" % "rythm-engine" % version
    }

    object Gson {
      val version = "2.3.1"
      val gson = "com.google.code.gson" % "gson" % version
    }

    object JodaTime {
      val version = "2.6"
      val jodaTime = "joda-time" % "joda-time" % version
    }

    object Slf4j {
      val version = "1.7.12"
      val slf4jSimple = "org.slf4j" % "slf4j-simple" % version
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

  lazy val forkedVmCourierMainClass = settingKey[String]("Main Generator class to execute.")

  lazy val forkedVmCourierClasspath = taskKey[Seq[File]](
    "Classpath to use when running the generator.")

  lazy val forkedVmSourceDirectory =
    settingKey[File]("directory containing .courier and .pdsc files")

  lazy val forkedVmAdditionalArgs =
    settingKey[Seq[String]]("Additional args to pass to the generator")

  val forkedVmCourierGeneratorSettings = Seq(

    // TODO: for android and swift, don't generate into a scala-x.xx dir
    forkedVmCourierGenerator in Compile := {
      val mainClass = forkedVmCourierMainClass.value
      val src = forkedVmSourceDirectory.value
      val dst = forkedVmCourierDest.value
      val classpath = forkedVmCourierClasspath.value
      val additionalArgs = forkedVmAdditionalArgs.value
      streams.value.log.info("Generating courier bindings...")
      val files =
        runForkedGenerator(mainClass, src, dst, classpath, additionalArgs, streams.value.log)
      streams.value.log.info(s"There are ${files.size} classes generated from courier bindings")
      files
    },
    forkedVmAdditionalArgs := Seq(),
    sourceGenerators in Compile <+= (forkedVmCourierGenerator in Compile),
    unmanagedSourceDirectories in Compile +=
      target.value / s"scala-${scalaBinaryVersion.value}" / "courier",
    managedSourceDirectories in Compile +=
      target.value / s"scala-${scalaBinaryVersion.value}" / "courier",
    cleanFiles += target.value / s"scala-${scalaBinaryVersion.value}" / "courier"
  )

  def runForkedGenerator(
      mainClass: String,
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
            None,
            "-cp" +: classpath.map(_.getAbsolutePath).mkString(java.io.File.pathSeparator) +:
              mainClass +:
              args,
            None,
            CustomOutput(outStream))
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
}


