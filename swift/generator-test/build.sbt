import CourierBuild._

name := "courier-swift-generator-test"

plainJavaProjectSettings

junitTestSettings

packagedArtifacts := Map.empty // do not publish

libraryDependencies ++= Seq(
  ExternalDependencies.JodaTime.jodaTime)

// Test Generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.SwiftGenerator"

forkedVmCourierClasspath := (swiftGenerator / Runtime / dependencyClasspath).value.files

forkedVmSourceDirectory := (referenceSuite / sourceDirectory).value / "main" / "courier"

forkedVmCourierDest := file("swift") / "testsuite" / "testsuiteTests" / "generated"

forkedVmAdditionalArgs := Seq("REQUIRED_FIELDS_MAY_BE_ABSENT", "EQUATABLE")
