import CourierBuild._
import play.twirl.sbt.SbtTwirl

// --- Global settings ---
ThisBuild / organization := "org.coursera.courier"
ThisBuild / scalaVersion := CourierBuild.currentScalaVersion

lazy val publishSettings: Seq[Setting[_]] =
  OverridablePublishSettings.settings(org.coursera.courier.sbt.Sonatype.Settings)

// --- Directory aliases (plain val, not private — required by .sbt syntax) ---
val scalaDir = file("scala")
val javaDir = file("java")
val androidDir = file("android")
val swiftDir = file("swift")
val typescriptLiteDir = file("typescript-lite")

// --- Project definitions ---

lazy val schemaLanguage =
  (project in file("schema-language"))
    .enablePlugins(com.simplytyped.Antlr4Plugin)
    .disablePlugins(BintrayPlugin)

lazy val generatorApi =
  (project in file("generator-api"))
    .dependsOn(schemaLanguage)
    .disablePlugins(BintrayPlugin)

lazy val referenceSuite =
  (project in file("reference-suite"))
    .disablePlugins(BintrayPlugin)

lazy val scalaGenerator =
  (project in scalaDir / "generator")
    .dependsOn(scalaRuntime, generatorApi, schemaLanguage)
    .enablePlugins(SbtTwirl)
    .disablePlugins(BintrayPlugin)

lazy val scalaRuntime =
  (project in scalaDir / "runtime")
    .disablePlugins(BintrayPlugin)

lazy val testLib =
  (project in scalaDir / "test-lib")
    .dependsOn(scalaGenerator)
    .disablePlugins(BintrayPlugin)

lazy val scalaGeneratorTestGenerator =
  (project in scalaDir / "generator-test-generator")
    .dependsOn(scalaGenerator)

lazy val scalaGeneratorTest =
  (project in scalaDir / "generator-test")
    .dependsOn(scalaGenerator, testLib, scalaGeneratorTestGenerator)
    .disablePlugins(BintrayPlugin)

lazy val scalaFixture =
  (project in scalaDir / "fixture")
    .dependsOn(scalaGenerator)
    .disablePlugins(BintrayPlugin)

lazy val scalaFixtureTest =
  (project in scalaDir / "fixture-test")
    .dependsOn(scalaGenerator, scalaFixture, testLib)
    .disablePlugins(BintrayPlugin)

lazy val javaGenerator =
  (project in javaDir / "generator")
    .dependsOn(generatorApi)
    .disablePlugins(BintrayPlugin)

lazy val javaGeneratorTest =
  (project in javaDir / "generator-test")
    .dependsOn(javaGenerator)
    .disablePlugins(BintrayPlugin)

lazy val javaRuntime =
  (project in javaDir / "runtime")
    .disablePlugins(BintrayPlugin)

lazy val androidGenerator =
  (project in androidDir / "generator")
    .dependsOn(generatorApi)
    .disablePlugins(BintrayPlugin)

lazy val androidGeneratorTest =
  (project in androidDir / "generator-test")
    .dependsOn(androidGenerator, androidRuntime)
    .disablePlugins(BintrayPlugin)

lazy val androidRuntime =
  (project in androidDir / "runtime")
    .disablePlugins(BintrayPlugin)

lazy val swiftGenerator =
  (project in swiftDir / "generator")
    .dependsOn(generatorApi)
    .disablePlugins(BintrayPlugin)

lazy val swiftGeneratorTest =
  (project in swiftDir / "generator-test")
    .dependsOn(swiftGenerator)
    .disablePlugins(BintrayPlugin)

lazy val typescriptLiteGenerator =
  (project in typescriptLiteDir / "generator")
    .dependsOn(generatorApi)
    .disablePlugins(BintrayPlugin)

lazy val typescriptLiteGeneratorTest =
  (project in typescriptLiteDir / "generator-test")
    .dependsOn(typescriptLiteGenerator)
    .disablePlugins(BintrayPlugin)

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
    .disablePlugins(BintrayPlugin)

lazy val courierSbtPlugin =
  (project in file("sbt-plugin"))
    .dependsOn(scalaGenerator)
    .enablePlugins(ScriptedPlugin)
    .disablePlugins(xerial.sbt.Sonatype)
    .settings(
      scalaVersion := sbtScalaVersion,
      // SBT plugin is tested via scripted, not regular test.
      // Skip during aggregate test until 2.12 cross-build is fully set up.
      Test / test := {}
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
  .settings(packagedArtifacts := Map.empty)
  .settings(
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
