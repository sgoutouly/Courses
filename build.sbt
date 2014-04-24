import play.Project._

name := "Courses"

version := "1.0-SNAPSHOT"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

libraryDependencies ++= Seq(
  cache,
  "org.mongodb" % "mongo-java-driver" % "2.11.3"
)     

playJavaSettings

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "1.8")
    sys.error("Java 8 is required for this project.")
}