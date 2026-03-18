import CourierBuild._

name := "courier-java-runtime"

plainJavaProjectSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Pegasus.data)

// Target Java 8 bytecode for compatibility with infra-services (Java 8)
javacOptions ++= Seq("--release", "8")
