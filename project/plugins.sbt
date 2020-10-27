resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

resolvers += "simplytyped" at "http://simplytyped.github.io/repo/releases"

addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.3.12")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.0")

addSbtPlugin("com.simplytyped" % "sbt-antlr4" % "0.7.7")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.1")

// 2.3 is last version that support 0.13
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.3")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

// sbt-bintray 0.6.0 does not support sbt 0.13
addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.6")
