name := "courier-generator-test"

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

forkedVmCourierMainClass := "org.coursera.courier.generator.TestScalaDataTemplateGenerator"

forkedVmCourierClasspath := (dependencyClasspath in Runtime in scalaGeneratorTestGenerator).value.files

forkedVmSourceDirectory := (sourceDirectory in referenceSuite).value / "main" / "courier"
forkedVmSourceDirectory := (sourceDirectory in testLib).value / "main" / "scala"

forkedVmCourierDest := target.value / s"scala-${scalaBinaryVersion.value}" / "courier"
