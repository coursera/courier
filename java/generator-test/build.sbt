name := "courier-java-generator-test"

plainJavaProjectSettings

packagedArtifacts := Map.empty // Do not publish

junitTestSettings

// Test generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.JavaGenerator"

forkedVmCourierClasspath := (dependencyClasspath in Runtime in javaGenerator).value.files

forkedVmSourceDirectory := (sourceDirectory in referenceSuite).value / "main" / "courier"

forkedVmCourierDest := target.value / s"scala-${scalaBinaryVersion.value}" / "courier"
