resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

// scala-xml version conflict between sbt-bintray (dispatch→1.2.0) and scala-compiler (2.1.0)
libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always

resolvers += "simplytyped" at "https://simplytyped.github.io/repo/releases"

addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.5.1")

addSbtPlugin("com.github.sbt" % "sbt-release" % "1.4.0")

addSbtPlugin("com.simplytyped" % "sbt-antlr4" % "0.8.3")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.1.5")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.10.0")

addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.2.1")

addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.6.1")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.11")

addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.4.0")
