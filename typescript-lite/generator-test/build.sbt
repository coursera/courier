name := "courier-typescript-lite-generator-test"

plainJavaProjectSettings

junitTestSettings

packagedArtifacts := Map.empty // do not publish

libraryDependencies ++= Seq(
  ExternalDependencies.JodaTime.jodaTime)

// Test Generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.TypeScriptLiteGenerator"

forkedVmCourierClasspath := (dependencyClasspath in Runtime in typescriptLiteGenerator).value.files

forkedVmSourceDirectory := (sourceDirectory in referenceSuite).value / "main" / "courier"

forkedVmCourierDest := file("typescript") / "testsuite" / "testsuiteTests" / "generated"

forkedVmAdditionalArgs := Seq("REQUIRED_FIELDS_MAY_BE_ABSENT", "EQUATABLE")
