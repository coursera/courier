import sbtassembly.AssemblyPlugin.defaultShellScript

name := "courier-python3-generator"

plainJavaProjectSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Rythm.rythmEngine,
  ExternalDependencies.Slf4j.slf4jSimple,
  ExternalDependencies.Pegasus.dataAvro
)

// Fat Jar
mainClass in assembly := Some("org.coursera.courier.Python3Generator")

assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultShellScript))

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

