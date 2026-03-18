import CourierBuild._

name := "courier-android-generator-test"

packagedArtifacts := Map.empty

plainJavaProjectSettings

junitTestSettings

// Test generator
forkedVmCourierGeneratorSettings

forkedVmCourierMainClass := "org.coursera.courier.AndroidGenerator"

forkedVmCourierClasspath := (LocalProject("androidGenerator") / Runtime / dependencyClasspath).value.files

forkedVmSourceDirectory := (LocalProject("referenceSuite") / sourceDirectory).value / "main" / "courier"

forkedVmCourierDest := target.value / s"scala-${scalaBinaryVersion.value}" / "courier"

Test / fork := true

Test / javaOptions += "-Dreferencesuite.srcdir=" + (LocalProject("referenceSuite") / sourceDirectory).value.getAbsolutePath

// `record` is a reserved type name in Java 14+ (JDK 17 parser treats it as restricted
// regardless of --release); exclude the generated record.java from compilation
Compile / sources ~= { _.filterNot(_.getName == "record.java") }

// Exclude courier-generated data model classes from JaCoCo — generated artifacts only.
jacocoExcludes := Seq("org.example.**", "org.coursera.**", "data.**", "WithoutNamespace*")
