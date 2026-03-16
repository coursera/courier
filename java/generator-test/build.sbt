import CourierBuild._

name := "courier-java-generator-test"

plainJavaProjectSettings

packagedArtifacts := Map.empty // Do not publish

junitTestSettings

// Test generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.JavaGenerator"

forkedVmCourierClasspath := (LocalProject("javaGenerator") / Runtime / dependencyClasspath).value.files

forkedVmSourceDirectory := (LocalProject("referenceSuite") / sourceDirectory).value / "main" / "courier"

forkedVmCourierDest := target.value / s"scala-${scalaBinaryVersion.value}" / "courier"
