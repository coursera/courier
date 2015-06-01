resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

addSbtPlugin("io.spray" % "sbt-twirl" % "0.7.0")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.5.0")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.0")
