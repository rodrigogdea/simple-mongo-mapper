name := "simple-mongo-mapper"

version := "1.0"

scalaVersion := "2.11.1"

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.4.1" % "test" withSources(),
  "org.reactivemongo" %% "reactivemongo" % "0.10.5.akka23-SNAPSHOT" withSources()
)