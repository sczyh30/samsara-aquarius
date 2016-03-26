name := """samsara-aquarius"""

version := "0.1.24"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "com.typesafe.slick" % "slick_2.11" % "3.1.1",
  "com.typesafe.play" % "play-slick_2.11" % "2.0.0",
  "mysql" % "mysql-connector-java" % "5.1.38",
  "com.typesafe.slick" %% "slick-codegen" % "3.1.1"
  //"com.typesafe.play" %% "play-slick-evolutions" % "1.1.1",
  //"org.postgresql" % "postgresql" % "9.4.1207.jre7"

)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator
