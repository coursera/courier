import CourierBuild._
import org.coursera.courier.sbt.Sonatype
import play.twirl.sbt.SbtTwirl
import sbtassembly.AssemblyKeys._

// --- Global settings ---
ThisBuild / organization := "org.coursera.courier"
ThisBuild / scalaVersion := CourierBuild.currentScalaVersion

lazy val publishSettings: Seq[Setting[_]] =
  OverridablePublishSettings.settings(Sonatype.Settings)

// --- Project definitions ---

lazy val schemaLanguage =
  (project in file("schema-language"))
    .disablePlugins(bintray.BintrayPlugin)

lazy val generatorApi =
  (project in file("generator-api"))
    .dependsOn(schemaLanguage)
    .disablePlugins(bintray.BintrayPlugin)

lazy val referenceSuite =
  (project in file("reference-suite"))
    .disablePlugins(bintray.BintrayPlugin)

private val scalaDir = file("scala")

lazy val scalaGenerator =
  (project in scalaDir / "generator")
    .dependsOn(scalaRuntime, generatorApi, schemaLanguage)
    .enablePlugins(SbtTwirl)
    .disablePlugins(bintray.BintrayPlugin)

lazy val scalaRuntime =
  (project in scalaDir / "runtime")
    .disablePlugins(bintray.BintrayPlugin)

lazy val testLib =
  (project in scalaDir / "test-lib")
    .dependsOn(scalaGenerator)
    .disablePlugins(bintray.BintrayPlugin)

lazy val scalaGeneratorTestGenerator =
  (project in scalaDir / "generator-test-generator")
    .dependsOn(scalaGenerator)

lazy val scalaGeneratorTest =
  (project in scalaDir / "generator-test")
    .dependsOn(scalaGenerator, testLib, scalaGeneratorTestGenerator)
    .disablePlugins(bintray.BintrayPlugin)

lazy val scalaFixture =
  (project in scalaDir / "fixture")
    .dependsOn(scalaGenerator)
    .disablePlugins(bintray.BintrayPlugin)

lazy val scalaFixtureTest =
  (project in scalaDir / "fixture-test")
    .dependsOn(scalaGenerator, scalaFixture, testLib)
    .disablePlugins(bintray.BintrayPlugin)

private val javaDir = file("java")

lazy val javaGenerator =
  (project in javaDir / "generator")
    .dependsOn(generatorApi)
    .disablePlugins(bintray.BintrayPlugin)

lazy val javaGeneratorTest =
  (project in javaDir / "generator-test")
    .dependsOn(javaGenerator)
    .disablePlugins(bintray.BintrayPlugin)

lazy val javaRuntime =
  (project in javaDir / "runtime")
    .disablePlugins(bintray.BintrayPlugin)

private val androidDir = file("android")

lazy val androidGenerator =
  (project in androidDir / "generator")
    .dependsOn(generatorApi)
    .disablePlugins(bintray.BintrayPlugin)

lazy val androidGeneratorTest =
  (project in androidDir / "generator-test")
    .dependsOn(androidGenerator, androidRuntime)
    .disablePlugins(bintray.BintrayPlugin)

lazy val androidRuntime =
  (project in androidDir / "runtime")
    .disablePlugins(bintray.BintrayPlugin)

private val swiftDir = file("swift")

lazy val swiftGenerator =
  (project in swiftDir / "generator")
    .dependsOn(generatorApi)
    .disablePlugins(bintray.BintrayPlugin)

lazy val swiftGeneratorTest =
  (project in swiftDir / "generator-test")
    .dependsOn(swiftGenerator)
    .disablePlugins(bintray.BintrayPlugin)

private val typescriptLiteDir = file("typescript-lite")

lazy val typescriptLiteGenerator =
  (project in typescriptLiteDir / "generator")
    .dependsOn(generatorApi)
    .disablePlugins(bintray.BintrayPlugin)

lazy val typescriptLiteGeneratorTest =
  (project in typescriptLiteDir / "generator-test")
    .dependsOn(typescriptLiteGenerator)
    .disablePlugins(bintray.BintrayPlugin)

lazy val cli =
  (project in file("cli"))
    .dependsOn(javaGenerator, androidGenerator, scalaGenerator, typescriptLiteGenerator, swiftGenerator)
    .aggregate(javaGenerator, androidGenerator, scalaGenerator, typescriptLiteGenerator, swiftGenerator)
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

lazy val courierSbtPlugin =
  (project in file("sbt-plugin"))
    .dependsOn(scalaGenerator)
    .disablePlugins(xerial.sbt.Sonatype)
    .settings(
      scalaVersion := sbtScalaVersion
    )

// --- Root project ---

lazy val root = (project in file("."))
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
                    s";compile;+test;fullpublish-ivylocal;" +
                      s"project courier;++$sbtScalaVersion;scripted"),
    addCommandAlias("fullpublish",
                    publishCommands("publish", Some("publish"))),
    addCommandAlias("fullpublish-signed",
                    publishCommands("publish-signed", Some("publish-signed"))),
    addCommandAlias("fullpublish-ivylocal",
                    publishCommands("publish-local", Some("publish-local"))),
    addCommandAlias("fullpublish-mavenlocal", publishCommands("publishM2"))
  )
