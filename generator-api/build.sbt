import CourierBuild._

name := "courier-generator-api"

plainJavaProjectSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Pegasus.data,
  ExternalDependencies.Pegasus.generator)

junitTestSettings

// Target Java 8 bytecode for compatibility with infra-services (Java 8)
javacOptions ++= Seq("--release", "8")
