import CourierBuild._

name := "courier-test-lib"

runtimeVersionSettings

packagedArtifacts := Map.empty // do not publish

libraryDependencies ++= Seq(
  ExternalDependencies.JodaTime.jodaTime,
  ExternalDependencies.JUnit.junit,
  ExternalDependencies.Scalatest.scalatest,
  ExternalDependencies.ScalatestPlusJunit.scalatestPlusJunit)

Test / fork := true

Test / javaOptions +=
  "-Dreferencesuite.srcdir=" + (LocalProject("referenceSuite") / sourceDirectory).value.getAbsolutePath

// Test generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.generator.TestScalaDataTemplateGenerator"

forkedVmCourierClasspath := (LocalProject("scalaGeneratorTestGenerator") / Runtime / dependencyClasspath).value.files

forkedVmSourceDirectory := (LocalProject("referenceSuite") / sourceDirectory).value / "main" / "courier"

forkedVmCourierDest := target.value / s"scala-${scalaBinaryVersion.value}" / "courier"
