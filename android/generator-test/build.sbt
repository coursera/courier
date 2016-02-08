name := "courier-android-generator-test"

packagedArtifacts := Map.empty

plainJavaProjectSettings

junitTestSettings

// Test generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.AndroidGenerator"

forkedVmCourierClasspath := (dependencyClasspath in Runtime in androidGenerator).value.files

forkedVmSourceDirectory := (sourceDirectory in referenceSuite).value / "main" / "courier"

forkedVmCourierDest := target.value / s"scala-${scalaBinaryVersion.value}" / "courier"
