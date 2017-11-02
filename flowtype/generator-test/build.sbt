import sbt.inc.Analysis

name := "courier-flowtype-generator-test"

packagedArtifacts := Map.empty // do not publish

libraryDependencies ++= Seq(
  ExternalDependencies.JodaTime.jodaTime)

autoScalaLibrary := false

crossPaths := false

// Test Generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.flowtypeGenerator"

forkedVmCourierClasspath := (dependencyClasspath in Runtime in flowtypeGenerator).value.files

forkedVmSourceDirectory := (sourceDirectory in referenceSuite).value / "main" / "courier"

forkedVmCourierDest := file("flowtype") / "testsuite" / "src" / "flowtype-bindings"

forkedVmAdditionalArgs := Seq("STRICT")

(compile in Compile) := {
  (forkedVmCourierGenerator in Compile).value

  Analysis.Empty
}

lazy val npmTest = taskKey[Unit]("Executes NPM test")

npmTest in Test := {
  (compile in Compile).value

  val result = """./flowtype/testsuite/full-build.sh"""!

  if (result != 0) {
    throw new RuntimeException("NPM Build Failed")
  }
}

test in Test := (npmTest in Test).value
