resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

resolvers += "simplytyped" at "http://simplytyped.github.io/repo/releases"
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.1.1")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.0")

addSbtPlugin("com.simplytyped" % "sbt-antlr4" % "0.7.7")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.1")

addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.2.1")
