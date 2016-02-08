name := "courier-runtime"

runtimeVersionSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Pegasus.data,
  ExternalDependencies.JUnit.junit,
  ExternalDependencies.Scalatest.scalatest) ++
  ExternalDependencies.ScalaParserCombinators.dependencies(scalaVersion.value)
