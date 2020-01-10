import sbt._

object Dependencies {

  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"

  val scalatest = "org.scalatest" %% "scalatest" % "3.0.6" % Test
  val scalamock = "org.scalamock" %% "scalamock" % "4.1.0" % Test

  lazy val slick = Seq(
    "com.typesafe.slick" %% "slick" % "3.3.0",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0",
    "com.typesafe.slick" %% "slick-codegen" % "3.3.0"
  )

  val mysql = "mysql" % "mysql-connector-java" % "8.0.15"
  val postgres = "org.postgresql" % "postgresql" % "42.2.9"
  val h2 = "com.h2database" % "h2" % "1.4.197" % Test

}
