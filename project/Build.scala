import sbt.Keys.libraryDependencies
import sbt._
import Keys._

/**
 * SBT project for Courier.
 */
object CourierBuild extends Build with OverridablePublishSettings {

  // TODO(jbetz): Use sbt-release plugin
  val courierVersion = "0.0.2"

  override lazy val settings = super.settings ++ overridePublishSettings ++ Seq(
    organization := "org.coursera.courier",
    scalaVersion in ThisBuild := "2.11.6",
    // TODO(jbetz): If scala 2.9 cross builds with no issues, consider adding it?
    crossScalaVersions := Seq("2.10.5", "2.11.6"),
    version := courierVersion)

  lazy val generator = Project(id = "courier-generator", base = file("generator"))
    .settings(
      libraryDependencies += ExternalDependencies.Pegasus.data,
      libraryDependencies += ExternalDependencies.Pegasus.generator,
      libraryDependencies += ExternalDependencies.Treehugger.treeHugger,
      libraryDependencies += ExternalDependencies.ScalaLogging.scalaLoggingSlf4j)

  lazy val runtime = Project(id = "courier-runtime", base = file("runtime"))
    .settings(
      libraryDependencies += ExternalDependencies.Pegasus.data)

  // This is a temporary project. It contains hand written files that exemplify the sturcture of
  // the Scala classes Courier should generate. Once the generator is stable, this project should
  // be deleted, with schemas that we can use for testing moved into appropriate test directories.
  lazy val spec = Project("spec", file("spec"))
    .settings(packagedArtifacts := Map.empty) // disable publishing for this project
    .settings(libraryDependencies += ("com.linkedin.pegasus" % "data" % "2.2.5").withSources().withJavadoc())
    .settings(libraryDependencies += ("com.linkedin.pegasus" % "data-avro-1_6" % "2.2.5").withSources().withJavadoc())
    .settings(libraryDependencies += ("junit" % "junit" % "4.11" % "test").withSources().withJavadoc())
    .settings(libraryDependencies += ("org.scalatest" %% "scalatest" % "2.2.3" % "test").withSources().withJavadoc())
    .settings(libraryDependencies += ("joda-time" % "joda-time" % "2.0").withSources().withJavadoc())

  lazy val root = Project(id = "courier", base = file("."))
    .settings(packagedArtifacts := Map.empty) // disable publish for root aggregate module
    .aggregate(generator)
    .aggregate(runtime)

  lazy val defaultPublishSettings = PublishSettings.mavenCentral

  object ExternalDependencies {
    object Pegasus {
      val version = "2.2.5"
      val data = "com.linkedin.pegasus" % "data" % version
      val generator = "com.linkedin.pegasus" % "generator" % version
    }

    object Treehugger {
      val version = "0.4.1"
      val treeHugger = "com.eed3si9n" %% "treehugger" % version
    }

    object ScalaLogging {
      val version = "2.1.2"
      val scalaLoggingSlf4j = "com.typesafe.scala-logging" %% "scala-logging-slf4j" % version
    }
  }

  object Repos {
    val mavenCentralReleases =
      "releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
  }

  object PublishSettings {
    val mavenCentral = Seq(
      publishMavenStyle := true,
      publishTo := {
        val nexus = "https://oss.sonatype.org"
        if (version.value.trim.endsWith("SNAPSHOT")) {
          Some("snapshots" at s"$nexus/content/repositories/snapshots")
        } else {
          Some("releases" at s"$nexus/service/local/staging/deploy/maven2")
        }
      },
      publishArtifact in Test := false,
      pomIncludeRepository := { _ => false },
      pomExtra :=
        <url>http://github.com/coursera/courier</url>
          <licenses>
            <license>
              <name>Apache 2.0</name>
              <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
              <distribution>repo</distribution>
            </license>
          </licenses>
          <scm>
            <url>git@github.com:coursera/courier.git</url>
            <connection>scm:git:git@github.com:coursera/courier.git</connection>
          </scm>
          <developers>
            <developer>
              <id>jpbetz</id>
              <name>Joe Betz</name>
            </developer>
            <developer>
              <id>Daniel Chia</id>
              <name>danchia</name>
            </developer>
            <developer>
              <id>Nick Dellamaggiore</id>
              <name>nick</name>
            </developer>
            <developer>
              <id>Josh Newman</id>
              <name>josh</name>
            </developer>
            <developer>
              <id>saeta</id>
              <name>Brennan Saeta</name>
            </developer>
          </developers>
    )
  }
}


