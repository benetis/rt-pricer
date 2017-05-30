name := "rt-prices"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "net.ruippeixotog" %% "scala-scraper" % "2.0.0-RC2",
  "com.typesafe.akka" %% "akka-actor" % "2.5.2",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.2" % Test
)