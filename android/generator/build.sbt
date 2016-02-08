name := "courier-android-generator"

plainJavaProjectSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Rythm.rythmEngine,
  ExternalDependencies.Gson.gson,
  ExternalDependencies.JodaTime.jodaTime)
