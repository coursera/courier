import CourierBuild._

name := "courier-runtime"

runtimeVersionSettings

junitTestSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Pegasus.data,
  ExternalDependencies.Coursera.courscala,
  ExternalDependencies.JUnit.junit,
  ExternalDependencies.Scalatest.scalatest,
  ExternalDependencies.ScalatestPlusJunit.scalatestPlusJunit) ++
  ExternalDependencies.ScalaParserCombinators.dependencies(scalaVersion.value)
