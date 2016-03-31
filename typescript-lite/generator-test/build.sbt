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

forkedVmCourierDest := file("typescript-lite") / "testsuite" / "src" / "expected-successes" / "tslite-bindings"

forkedVmAdditionalArgs := Seq("STRICT")


lazy val npmTest = taskKey[Unit]("Executes NPM test")

npmTest in Test := {
  (forkedVmCourierGenerator in Compile).value

  val result = """./typescript-lite/testsuite/full-build.sh"""!

  if (result != 0) {
    throw new RuntimeException("NPM Build Failed")
  }
}

test in Test := (npmTest in Test).value
