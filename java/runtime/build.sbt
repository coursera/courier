import CourierBuild._

name := "courier-java-runtime"

plainJavaProjectSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Pegasus.data)


