name := "courier-swift-generator-test"

plainJavaProjectSettings

junitTestSettings

packagedArtifacts := Map.empty // do not publish

libraryDependencies ++= Seq(
  ExternalDependencies.JodaTime.jodaTime)

// Test Generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.SwiftGenerator"

forkedVmCourierClasspath := (dependencyClasspath in Runtime in swiftGenerator).value.files

forkedVmSourceDirectory := (sourceDirectory in referenceSuite).value / "main" / "courier"

forkedVmCourierDest := file("swift") / "testsuite" / "testsuiteTests" / "generated"

forkedVmAdditionalArgs := Seq("REQUIRED_FIELDS_MAY_BE_ABSENT", "EQUATABLE")
