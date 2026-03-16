import CourierBuild._

name := "courier-grammar"

plainJavaProjectSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Pegasus.data,
  ExternalDependencies.ApacheCommons.lang)

dependencyOverrides += ExternalDependencies.ApacheCommons.io

junitTestSettings

Test / fork := true

Test / javaOptions += "-Dreferencesuite.srcdir=" + (LocalProject("referenceSuite") / sourceDirectory).value.getAbsolutePath

// ANTLR
Antlr4 / antlr4PackageName := Some("org.coursera.courier.grammar")
