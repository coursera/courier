import CourierBuild._

name := "courier-generator"

generatorVersionSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Pegasus.data,
  ExternalDependencies.Pegasus.generator,
  ExternalDependencies.JUnit.junit,
  ExternalDependencies.Scalatest.scalatest,
  ExternalDependencies.ScalatestPlusJunit.scalatestPlusJunit,
  ExternalDependencies.Scalariform.scalariform,
  ExternalDependencies.ApacheCommons.lang)

dependencyOverrides += ExternalDependencies.ApacheCommons.io
