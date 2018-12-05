name := "bowling"

maintainer := "Jayesh Thakrar"

version := "1.0"

scalaVersion := "2.11.8"

val scalaTestVersion = "3.0.4"

enablePlugins(JavaAppPackaging)

libraryDependencies += "org.scalatest" %% "scalatest" % scalaTestVersion % Test
