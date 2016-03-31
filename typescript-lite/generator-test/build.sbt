import sbt.inc.Analysis

name := "courier-typescript-lite-generator-test"

packagedArtifacts := Map.empty // do not publish

libraryDependencies ++= Seq(
  ExternalDependencies.JodaTime.jodaTime)

autoScalaLibrary := false

crossPaths := false

// Test Generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.TypeScriptLiteGenerator"

forkedVmCourierClasspath := (dependencyClasspath in Runtime in typescriptLiteGenerator).value.files

forkedVmSourceDirectory := (sourceDirectory in referenceSuite).value / "main" / "courier"

forkedVmCourierDest := file("typescript-lite") / "testsuite" / "src" / "tslite-bindings"

forkedVmAdditionalArgs := Seq("STRICT")

(compile in Compile) := {
  (forkedVmCourierGenerator in Compile).value

  Analysis.Empty
}

lazy val npmTest = taskKey[Unit]("Executes NPM test")

npmTest in Test := {
  (compile in Compile).value

  val result = """./typescript-lite/testsuite/full-build.sh"""!

  if (result != 0) {
    throw new RuntimeException("NPM Build Failed")
  }
}

test in Test := (npmTest in Test).value
