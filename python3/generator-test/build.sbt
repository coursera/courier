import sbt.inc.Analysis

name := "courier-python3-generator-test"

packagedArtifacts := Map.empty // do not publish

libraryDependencies ++= Seq(
  ExternalDependencies.JodaTime.jodaTime)

autoScalaLibrary := false

crossPaths := false

// Test Generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.Python3Generator"

forkedVmCourierClasspath := (dependencyClasspath in Runtime in python3Generator).value.files

forkedVmSourceDirectory := (sourceDirectory in referenceSuite).value / "main" / "courier"

forkedVmCourierDest := file("python3") / "testsuite" / "src" / "py3bindings"

forkedVmAdditionalArgs := Seq("STRICT")

(compile in Compile) := {
  (forkedVmCourierGenerator in Compile).value

  Analysis.Empty
}

lazy val py3Test = taskKey[Unit]("Executes python unittest")

py3Test in Test := {
  val result = """./python3/testsuite/full-build.sh"""!

  if (result != 0) {
    throw new RuntimeException("Python3 Build Failed")
  }
}

test in Test := (py3Test in Test).value
