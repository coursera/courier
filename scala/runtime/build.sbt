name := "courier-runtime"

runtimeVersionSettings

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.6.2",
  "com.chuusai" %% "shapeless" % "2.3.2",
  ExternalDependencies.Pegasus.data,
  ExternalDependencies.Coursera.courscala,
  ExternalDependencies.JUnit.junit,
  ExternalDependencies.Scalatest.scalatest) ++
  ExternalDependencies.ScalaParserCombinators.dependencies(scalaVersion.value)
