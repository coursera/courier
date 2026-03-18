import CourierBuild._

name := "courier-grammar"

plainJavaProjectSettings

libraryDependencies ++= Seq(
  ExternalDependencies.Pegasus.data,
  ExternalDependencies.ApacheCommons.lang)

dependencyOverrides += ExternalDependencies.ApacheCommons.io

junitTestSettings

Test / fork := true

Test / javaOptions += "-Dreferencesuite.srcdir=" + (LocalProject("referenceSuite") / sourceDirectory).value.getAbsolutePath

// Target Java 8 bytecode for compatibility with infra-services (Java 8).
// This also satisfies JaCoCo's ASM (which can't process Java 25 class files).
javacOptions ++= Seq("--release", "8")

// Exclude ANTLR-generated parser/lexer classes from JaCoCo — generated artifacts,
// not hand-written code. Only ParseUtils, CourierSchemaParserFactory are hand-written.
jacocoExcludes := Seq(
  "org.coursera.courier.grammar.CourierParser*",
  "org.coursera.courier.grammar.CourierLexer*",
  "org.coursera.courier.grammar.CourierBaseListener*",
  "org.coursera.courier.grammar.CourierBaseVisitor*",
  "org.coursera.courier.grammar.CourierListener*",
  "org.coursera.courier.grammar.CourierVisitor*"
)

// ANTLR
Antlr4 / antlr4PackageName := Some("org.coursera.courier.grammar")
