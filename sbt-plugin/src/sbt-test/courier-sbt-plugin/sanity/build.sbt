version := "0.1"

scalaVersion := "2.10.5"

org.coursera.courier.sbt.CourierPlugin.courierSettings

libraryDependencies += "org.coursera.courier" %% "courier-runtime" % System.getProperty("plugin.version")

libraryDependencies += "junit" % "junit" % "4.11" % "test"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.3" % "test"

