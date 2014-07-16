name := "Scala"

version := "1.0"

scalaVersion := "2.11.0"

libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s" % "1.2.1.3"
)

resolvers ++= Seq(
    "Local Maven Repository"    at "file://"+Path.userHome.absolutePath+"/.m2/repository",
    "Typesafe Repo"             at "http://repo.typesafe.com/typesafe/releases/",
    "Sonatype Snapshots"        at "http://oss.sonatype.org/content/repositories/snapshots",
    "Sonatype Releases"         at "http://oss.sonatype.org/content/repositories/releases"
)