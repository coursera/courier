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

// `record` is a reserved type name in Java 14+; exclude the generated record.java
Compile / sources ~= { _.filterNot(_.getName == "record.java") }

// Exclude all courier-generated data model classes from JaCoCo.
// Every class in this project is a generated artifact — there are no hand-written test classes.
jacocoExcludes := Seq("org.example.**", "org.coursera.**", "data.**", "WithoutNamespace*")
