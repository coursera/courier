import sbtassembly.AssemblyPlugin.defaultShellScript

name := "courier-flowtype-generator"

plainJavaProjectSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Rythm.rythmEngine,
  ExternalDependencies.Slf4j.slf4jSimple)

// Fat Jar
mainClass in assembly := Some("org.coursera.courier.flowtypeGenerator")

assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultShellScript))

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

