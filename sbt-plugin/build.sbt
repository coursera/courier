name := "courier-sbt-plugin"

sbtPlugin := true

pluginVersionSettings

licenses += ("Apache-2", url("https://opensource.org/licenses/Apache-2.0"))

description := "Data interchange for the modern web + mobile stack. http://coursera.github.io/courier/"

/*
 Bintray does not support maven style, nor SNAPSHOT
 */
publishMavenStyle := version.value.trim.endsWith("SNAPSHOT")

bintrayRepository := "sbt-plugins"

bintrayOrganization := Some("coursera")
