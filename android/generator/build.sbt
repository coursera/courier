import CourierBuild._

name := "courier-android-generator"

plainJavaProjectSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Rythm.rythmEngine,
  ExternalDependencies.Gson.gson,
  ExternalDependencies.JodaTime.jodaTime)

// Target Java 8 bytecode for compatibility with infra-services (Java 8)
javacOptions ++= Seq("--release", "8")
