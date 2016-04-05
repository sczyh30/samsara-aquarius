name := """samsara-aquarius"""

version := "0.1.58"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  filters,
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "com.typesafe.slick" % "slick_2.11" % "3.1.1",
  "com.typesafe.play" % "play-slick_2.11" % "2.0.0",
  "mysql" % "mysql-connector-java" % "5.1.38",
  "com.typesafe.slick" %% "slick-codegen" % "3.1.1",
  "org.scala-lang.modules" % "scala-async_2.11" % "0.9.5",
  "org.apache.commons" % "commons-lang3" % "3.4",
  "com.chuusai" % "shapeless_2.11" % "2.3.0"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator
