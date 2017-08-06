import sbt.Keys.version

lazy val root = (project in file(".")).
  settings(
    name := "rt-prices",
    version := "1.0",
    scalaVersion := "2.11.8",

    mainClass in Compile := Some("rt.Main")
  )

libraryDependencies ++= Seq(
  "net.ruippeixotog" %% "scala-scraper" % "2.0.0-RC2",
  "com.typesafe.akka" %% "akka-actor" % "2.5.2",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.2" % Test,
  "io.lemonlabs" %% "scala-uri" % "0.4.16",
  "org.sorm-framework" % "sorm" % "0.3.21",
  "mysql" % "mysql-connector-java" % "5.1.24",
  "ch.qos.logback" % "logback-classic" % "1.1.3"
)

enablePlugins(JavaAppPackaging)
enablePlugins(UniversalPlugin)