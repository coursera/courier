import CourierBuild._

name := "courier-android-runtime"

plainJavaProjectSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Gson.gson)

// Target Java 8 bytecode for compatibility with infra-services (Java 8)
javacOptions ++= Seq("--release", "8")
