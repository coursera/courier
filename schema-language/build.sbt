import CourierBuild._
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

Antlr4 / antlr4PackageName := Some("org.coursera.courier.grammar")
