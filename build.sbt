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

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.github.spullara.mustache.java" % "compiler" % "0.9.5",
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)

