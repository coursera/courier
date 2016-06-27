name := "courier-mock"

runtimeVersionSettings

packagedArtifacts := Map.empty // do not publish

libraryDependencies ++= Seq(
  ExternalDependencies.JodaTime.jodaTime,
  ExternalDependencies.JUnit.junit,
  ExternalDependencies.Scalatest.scalatest)

fork in Test := true

javaOptions in Test +=
  "-Dreferencesuite.srcdir=" + (sourceDirectory in referenceSuite).value.getAbsolutePath

// Test generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.generator.ScalaDataTemplateGenerator"

forkedVmCourierClasspath := (dependencyClasspath in Runtime in scalaGenerator).value.files

forkedVmSourceDirectory := (sourceDirectory in referenceSuite).value / "main" / "courier"

forkedVmCourierDest := target.value / s"scala-${scalaBinaryVersion.value}" / "courier"
