name := """play-mustache"""

organization := "ch.wavein"

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.github.spullara.mustache.java" % "compiler" % "0.9.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

