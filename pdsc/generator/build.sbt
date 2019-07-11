import sbtassembly.AssemblyPlugin.defaultShellScript

name := "courier-pdsc-generator"

plainJavaProjectSettings
junitTestSettings

// Fat Jar
mainClass in assembly := Some("org.coursera.courier.PdscGenerator")

assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultShellScript))

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

