name := "GCM Server"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.specs2" %% "specs2" % "2.3.3" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test"
)

scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-unchecked",
  "-feature",
  "-language:postfixOps",
  "-language:implicitConversions"
)

play.Project.playScalaSettings

