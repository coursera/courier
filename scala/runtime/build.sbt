name := "courier-runtime"

runtimeVersionSettings

junitTestSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Pegasus.data,
  ExternalDependencies.Coursera.courscala,
  ExternalDependencies.JUnit.junit,
  ExternalDependencies.Scalatest.scalatest) ++
  ExternalDependencies.ScalaParserCombinators.dependencies(scalaVersion.value)
