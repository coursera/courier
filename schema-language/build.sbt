import com.simplytyped.Antlr4Plugin._

name := "courier-grammar"

plainJavaProjectSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Pegasus.data,
  ExternalDependencies.ApacheCommons.lang)

dependencyOverrides += ExternalDependencies.ApacheCommons.io

junitTestSettings

// ANTLR
antlr4Settings

antlr4PackageName in Antlr4 := Some("org.coursera.courier.grammar")
