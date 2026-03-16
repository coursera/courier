import CourierBuild._
import scala.sys.process._

name := "courier-typescript-lite-generator-test"

packagedArtifacts := Map.empty // do not publish

libraryDependencies ++= Seq(
  ExternalDependencies.JodaTime.jodaTime)

autoScalaLibrary := false

crossPaths := false

// Test Generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.TypeScriptLiteGenerator"

forkedVmCourierClasspath := (LocalProject("typescriptLiteGenerator") / Runtime / dependencyClasspath).value.files

forkedVmSourceDirectory := (LocalProject("referenceSuite") / sourceDirectory).value / "main" / "courier"

forkedVmCourierDest := file("typescript-lite") / "testsuite" / "src" / "tslite-bindings"

forkedVmAdditionalArgs := Seq("STRICT")

Compile / compile := {
  (Compile / forkedVmCourierGenerator).value
  sbt.internal.inc.Analysis.empty
}

lazy val npmTest = taskKey[Unit]("Executes NPM test")

Test / npmTest := {
  (Compile / compile).value

  val result = """./typescript-lite/testsuite/full-build.sh"""!

  if (result != 0) {
    throw new RuntimeException("NPM Build Failed")
  }
}

// typings.io registry is offline; skip the npm test for now
// TODO: update to use @types/ packages with TypeScript 2.x+
Test / test := {}
