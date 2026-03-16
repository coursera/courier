import CourierBuild._

name := "courier-android-generator-test"

packagedArtifacts := Map.empty

plainJavaProjectSettings

junitTestSettings

// Test generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.AndroidGenerator"

forkedVmCourierClasspath := (androidGenerator / Runtime / dependencyClasspath).value.files

forkedVmSourceDirectory := (referenceSuite / sourceDirectory).value / "main" / "courier"

forkedVmCourierDest := target.value / s"scala-${scalaBinaryVersion.value}" / "courier"
