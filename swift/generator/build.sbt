import CourierBuild._
import sbtassembly.AssemblyPlugin.autoImport._

name := "courier-swift-generator"

plainJavaProjectSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Rythm.rythmEngine,
  ExternalDependencies.Slf4j.slf4jSimple)

// Fat Jar
assembly / mainClass := Some("org.coursera.courier.SwiftGenerator")

assembly / assemblyPrependShellScript := Some(sbtassembly.AssemblyPlugin.defaultShellScript)

assembly / assemblyJarName := s"${name.value}-${version.value}.jar"
