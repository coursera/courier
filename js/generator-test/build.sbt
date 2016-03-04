name := "courier-js-generator-test"

junitTestSettings

packagedArtifacts := Map.empty // do not publish

libraryDependencies ++= Seq()

// Test Generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.ReactGenerator"

forkedVmCourierClasspath := (dependencyClasspath in Runtime in jsGenerator).value.files

forkedVmSourceDirectory := (sourceDirectory in referenceSuite).value / "main" / "courier"

forkedVmCourierDest := file("js") / "testsuite" / "js" / "propTypes"

forkedVmAdditionalArgs := Seq("REQUIRED_FIELDS_MAY_BE_ABSENT", "EQUATABLE")

