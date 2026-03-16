import CourierBuild._

name := "courier-fixture-test"

runtimeVersionSettings

packagedArtifacts := Map.empty // do not publish

libraryDependencies ++= Seq(
  ExternalDependencies.JodaTime.jodaTime,
  ExternalDependencies.JUnit.junit,
  ExternalDependencies.Scalatest.scalatest,
  ExternalDependencies.ScalatestPlusJunit.scalatestPlusJunit)

Test / fork := true

Test / javaOptions +=
  "-Dreferencesuite.srcdir=" + (referenceSuite / sourceDirectory).value.getAbsolutePath

// Test generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.generator.ScalaDataTemplateGenerator"

forkedVmCourierClasspath := (scalaGenerator / Runtime / dependencyClasspath).value.files

forkedVmSourceDirectory := (testLib / sourceDirectory).value / "main" / "scala"
forkedVmSourceDirectory := (scalaFixture / sourceDirectory).value / "main" / "scala"

forkedVmCourierDest := target.value / s"scala-${scalaBinaryVersion.value}" / "courier"
