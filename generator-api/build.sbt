import Courier.ExternalDependencies._

name := "courier-generator-api"

plainJavaProjectSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Pegasus.data,
  ExternalDependencies.Pegasus.generator)

junitTestSettings
