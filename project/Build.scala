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
    Seq(organization := "org.coursera.courier")

  //
  // Cross building
  //

  // Our scala cross building story is, unfortunately, a bit hairy.
  // In order to keep it under control we primarily concern ourselves with these two below Scala
  // version numbers:

  lazy val sbtScalaVersion = "2.10.6" // the version of Scala used by the current sbt version.
  lazy val currentScalaVersion = "2.12.7" // The current scala version.

  // Our plugin runs as part of SBT so must use the same Scala version that SBT currently uses.
  //lazy val pluginVersionSettings = Seq(
    //scalaVersion in ThisBuild := sbtScalaVersion,
    //crossScalaVersions in ThisBuild := Seq(sbtScalaVersion))

  // We cross build our runtime to both versions.
  lazy val runtimeVersionSettings = Seq(
    scalaVersion in ThisBuild := currentScalaVersion,
    crossScalaVersions in ThisBuild := Seq(sbtScalaVersion,
                                           //"2.11.11",
                                           currentScalaVersion))

  // Strictly speaking, our generator only needs to be built for the SBT plugin Scala version.
  // But we also cross built it to the current Scala version so that our generator-test
  // project can depend on the generator and still run with the current Scala version, which
  // is more convenient because it allow us to do all testing and development in the current
  // Scala version.
  lazy val generatorVersionSettings = Seq(
    scalaVersion in ThisBuild := sbtScalaVersion,
    crossScalaVersions in ThisBuild := Seq(sbtScalaVersion,
                                           //"2.11.11",
                                           currentScalaVersion))

  // Java project settings
  lazy val plainJavaProjectSettings = Seq(
    autoScalaLibrary := false,
    crossPaths := false
  )

  // Test settings
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
    }
  )

  //
  // Projects
  //
  lazy val schemaLanguage =
    Project(id = "schema-language", base = file("schema-language"))
      .disablePlugins(bintray.BintrayPlugin)

  lazy val generatorApi =
    Project(id = "generator-api", base = file("generator-api"))
      .dependsOn(schemaLanguage)
      .disablePlugins(bintray.BintrayPlugin)

  lazy val referenceSuite =
    Project(id = "reference-suite", base = file("reference-suite"))
      .disablePlugins(bintray.BintrayPlugin)

  private[this] val scalaDir = file("scala")

  lazy val scalaGenerator =
    Project(id = "scala-generator", base = scalaDir / "generator")
      .dependsOn(scalaRuntime, generatorApi, schemaLanguage)
      .enablePlugins(SbtTwirl)
      .disablePlugins(bintray.BintrayPlugin)

  lazy val scalaRuntime =
    Project(id = "scala-runtime", base = scalaDir / "runtime")
      .disablePlugins(bintray.BintrayPlugin)

  lazy val testLib =
    Project(id = "scala-test-lib", base = scalaDir / "test-lib")
      .dependsOn(scalaGenerator)
      .disablePlugins(bintray.BintrayPlugin)

  lazy val scalaGeneratorTestGenerator =
    Project(id = "scala-generator-test-generator", base = scalaDir / "generator-test-generator")
      .dependsOn(scalaGenerator)

  lazy val scalaGeneratorTest =
    Project(id = "scala-generator-test", base = scalaDir / "generator-test")
      .dependsOn(scalaGenerator)
      .dependsOn(testLib)
      .dependsOn(scalaGeneratorTestGenerator)
      .disablePlugins(bintray.BintrayPlugin)

  lazy val scalaFixture =
    Project(id = "scala-fixture", base = scalaDir / "fixture")
      .dependsOn(scalaGenerator)
      .disablePlugins(bintray.BintrayPlugin)

  lazy val scalaFixtureTest =
    Project(id = "scala-fixture-test", base = scalaDir / "fixture-test")
      .dependsOn(scalaGenerator)
      .dependsOn(scalaFixture)
      .dependsOn(testLib)
      .disablePlugins(bintray.BintrayPlugin)

  private[this] val javaDir = file("java")

  lazy val javaGenerator =
    Project(id = "java-generator", base = javaDir / "generator")
      .dependsOn(generatorApi)
      .disablePlugins(bintray.BintrayPlugin)

  lazy val javaGeneratorTest =
    Project(id = "java-generator-test", base = javaDir / "generator-test")
      .dependsOn(javaGenerator)
      .disablePlugins(bintray.BintrayPlugin)

  lazy val javaRuntime =
    Project(id = "java-runtime", base = javaDir / "runtime")
      .disablePlugins(bintray.BintrayPlugin)

  private[this] val androidDir = file("android")

  lazy val androidGenerator =
    Project(id = "android-generator", base = androidDir / "generator")
      .dependsOn(generatorApi)
      .disablePlugins(bintray.BintrayPlugin)

  lazy val androidGeneratorTest =
    Project(id = "android-generator-test", base = androidDir / "generator-test")
      .dependsOn(androidGenerator, androidRuntime)
      .disablePlugins(bintray.BintrayPlugin)

  lazy val androidRuntime =
    Project(id = "android-runtime", base = androidDir / "runtime")
      .disablePlugins(bintray.BintrayPlugin)

  private[this] val swiftDir = file("swift")
  lazy val swiftGenerator =
    Project(id = "swift-generator", base = swiftDir / "generator")
      .dependsOn(generatorApi)
      .disablePlugins(bintray.BintrayPlugin)

  private[this] val typescriptLiteDir = file("typescript-lite")
  lazy val typescriptLiteGenerator =
    Project(id = "typescript-lite-generator",
            base = typescriptLiteDir / "generator")
      .dependsOn(generatorApi)
      .disablePlugins(bintray.BintrayPlugin)

  lazy val cli = Project(id = "courier-cli", base = file("cli"))
    .dependsOn(
      javaGenerator,
      androidGenerator,
      scalaGenerator,
      typescriptLiteGenerator,
      swiftGenerator
    )
    .aggregate(
      javaGenerator,
      androidGenerator,
      scalaGenerator,
      typescriptLiteGenerator,
      swiftGenerator
    )
    .settings(
      executableFile := {
        val exeFile = target.value / "courier"
        print(s"Writing executable file '$exeFile'...")
        IO.write(exeFile,
                 """#!/bin/bash
                            |exec java -jar $0 "$@"
                            |
                            |""".stripMargin)
        IO.append(exeFile, IO.readBytes(assembly.value))
        exeFile.setExecutable(true)
        println("written.")
        exeFile
      }
    )
    .disablePlugins(bintray.BintrayPlugin)

  lazy val typescriptLiteGeneratorTest =
    Project(id = "typescript-lite-generator-test",
            base = typescriptLiteDir / "generator-test")
      .dependsOn(typescriptLiteGenerator)
      .disablePlugins(bintray.BintrayPlugin)

  lazy val courierSbtPlugin =
    Project(id = "sbt-plugin", base = file("sbt-plugin"))
      .dependsOn(scalaGenerator)
      .disablePlugins(xerial.sbt.Sonatype)
      .disablePlugins(bintray.BintrayPlugin)
      .settings(
        scalaVersion := "2.12.7",
        sbtVersion in Global := "1.2.8",
        crossSbtVersions := Vector("0.13.17", "1.2.8"),
        sbtVersion in pluginCrossBuild := {
      scalaBinaryVersion.value match {
        case "2.10" => "0.13.17"
        //case "2.11" => "0.13.17"
        case "2.12" => "1.2.8"
      }
        },
        scalaCompilerBridgeSource := {
          val sv = appConfiguration.value.provider.id.version
          ("org.scala-sbt" % "compiler-interface" % sv % "component").sources
        }
      )

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

  // TODO(jbetz): Once SBT supports scala 2.11, we can enable .aggregate for all
  // project dependencies that have .dependsOn in the above projects, and then we will only
  // need to run `project courier-sbt-plugin;publish` here, which will dramatically simplify things.
  // Until then we have to be very explicit about publishing exactly what we want, mainly
  // to avoid build failures that would happen if we tried to publish the sbt plugin with scala
  // 2.11.
  def publishCommands(publishCommand: String,
                      sbtPluginCommand: Option[String] = None) = {
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
        baseCommand + s";++$sbtScalaVersion;project sbt-plugin;$sbtPluginCommand"
      }
      .getOrElse {
        baseCommand
      }
  }

  lazy val root = Project(id = "courier", base = file("."))
    .aggregate(
      scalaGenerator,
      schemaLanguage,
      scalaRuntime,
      courierSbtPlugin,
      testLib,
      scalaGeneratorTest,
      scalaFixture,
      scalaFixtureTest,
      androidGenerator,
      androidGeneratorTest,
      androidRuntime,
      swiftGenerator,
      typescriptLiteGenerator,
      typescriptLiteGeneratorTest,
      cli
    )
    .settings(runtimeVersionSettings)
    .settings(packagedArtifacts := Map.empty) // disable publish for root aggregate module
    .settings(
      // scripted attempts to publish what it needs, but because of the above mentioned cross
      // build issues, we have to manually publish what we need before we test here
      addCommandAlias(s"fulltest",
                      s";compile;test;fullpublish-ivylocal;" +
                        s"project courier;++$sbtScalaVersion;scripted"),
      addCommandAlias("fullpublish", publishCommands("publish")),
      addCommandAlias("fullpublish-signed",
                      publishCommands("publish-signed", Some("publish"))),
      addCommandAlias("fullpublish-ivylocal",
                      publishCommands("publish-local", Some("publish-local"))),
      addCommandAlias("fullpublish-mavenlocal", publishCommands("publishM2"))
    )

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
      val version = "0.10"
      val junitInterface = "com.novocode" % "junit-interface" % version % "test"
    }

    object Scalatest {
      val version = "3.0.4"
      val scalatest = "org.scalatest" %% "scalatest" % version % "test"
    }

    object ApacheCommons {
      val langVersion = "3.4"
      val lang = "org.apache.commons" % "commons-lang3" % langVersion

      val ioVersion = "2.4"
      val io = "commons-io" % "commons-io" % ioVersion
    }

    object Scalariform {
      val version = "0.2.6"
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

  // In order to be able to quickly test our generator, we use
  // this approach, which has has been taken directly from Sleipnir by Dmitriy Yefremov.
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

  val forkedVmCourierGeneratorSettings = Seq(
    // TODO: for android and swift, don't generate into a scala-x.xx dir
    forkedVmCourierGenerator in Compile := {
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
    sourceGenerators in Compile <+= (forkedVmCourierGenerator in Compile),
    unmanagedSourceDirectories in Compile +=
      target.value / s"scala-${scalaBinaryVersion.value}" / "courier",
    managedSourceDirectories in Compile +=
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
          Fork.java(None,
                    "-cp" +: classpath
                      .map(_.getAbsolutePath)
                      .mkString(java.io.File.pathSeparator) +:
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

  //
  // Other Commands
  //
  lazy val executableFile = taskKey[File](
    "Distributes the current version as an executable at cli/target/courier")
}
