name := """samsara-aquarius"""

version := "0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,

  "com.typesafe.play" %% "play-slick" % "1.1.1",
  "org.postgresql" % "postgresql" % "9.4.1207.jre7"

)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator
