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
  "-Dreferencesuite.srcdir=" + (LocalProject("referenceSuite") / sourceDirectory).value.getAbsolutePath

// Test generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.generator.ScalaDataTemplateGenerator"

forkedVmCourierClasspath := (LocalProject("scalaGenerator") / Runtime / dependencyClasspath).value.files

forkedVmSourceDirectory := (LocalProject("scalaFixture") / sourceDirectory).value / "main" / "scala"

forkedVmCourierDest := target.value / s"scala-${scalaBinaryVersion.value}" / "courier"
