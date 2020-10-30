import com.typesafe.sbt.GitVersioning

name := """play-mustache"""

organization := "ch.wavein"

lazy val root = (project in file("."))
  .settings(
    bintrayRepository := "maven",
    bintrayOrganization := Some("waveinch"),
    publishMavenStyle := true,
    licenses += ("Apache-2.0", url("http://www.opensource.org/licenses/apache2.0.php")),
    git.useGitDescribe := true
  ).enablePlugins(
  PlayScala,
  GitVersioning
)

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  "com.github.spullara.mustache.java" % "compiler" % "0.9.7",
  "com.github.spullara.mustache.java" % "scala-extensions-2.13" % "0.9.7",
  "org.scalatest" %% "scalatest" % "3.2.0" % Test
)

