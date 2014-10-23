name := """Courses"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  cache,
  "org.mongodb" % "mongo-java-driver" % "2.11.3",
  "uk.co.panaxiom" %% "play-jongo" % "0.7.1-jongo1.0"
)
