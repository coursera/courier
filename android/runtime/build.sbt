import CourierBuild._

name := "courier-android-runtime"

plainJavaProjectSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Gson.gson)


