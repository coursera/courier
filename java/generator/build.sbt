import CourierBuild._

name := "courier-java-generator"

plainJavaProjectSettings

libraryDependencies ++= Seq(
  "com.sun.codemodel" % "codemodel" % "2.2",
  // javax.annotation.Generated was removed in Java 11; pegasus JavaDataTemplateGenerator
  // still references it. This restores it for the forked generator JVM.
  "javax.annotation" % "javax.annotation-api" % "1.3.2"
)


