import Dependencies._
import sbt.Test
import sbtslickgenerator.SlickGeneratorPlugin.autoImport.SG

val commonSettings = {
  val buildSettings = Seq(
    organization in ThisBuild := "com.du4",
    name := "slick_dao",
    version := "0.1",
    scalaVersion := "2.12.8",
    Test / parallelExecution := false,
  )

  buildSettings ++ Seq(
    libraryDependencies ++= Seq(
      scalaLogging,
      logback,
      scalatest,
      scalamock,
      postgres
    )
  )
}

def simpleProject(name: String) = Project(name, file(name)).settings(commonSettings)

lazy val mainDao =
  simpleProject("main-dao")
    .settings(
      libraryDependencies ++= slick ++ Seq(h2)
    )
    .dependsOn(tables)

lazy val tables = {
  lazy val generatorSettings = Seq(
    SG.jdbcProfile := "slick.jdbc.PostgresProfile",
    SG.jdbcDriver := "org.postgresql.Driver",
    SG.url := "jdbc:postgresql://localhost:5432/mydb",
    SG.username := Some("john"),
    SG.password := Some("pwd0123456789"),
    SG.outputDir := sourceManaged.value / "main",
    SG.pkg := "com.du4.tables.slick.generated",
    SG.filename := "Tables.scala",
    SG.tableTypes := Some(Seq("TABLE", "VIEW")),
    Compile / sourceGenerators += SG.generate.taskValue // register automatic code generation on every compile, remove for only manual use
  )

  simpleProject("tables")
    .enablePlugins(SlickGeneratorPlugin)
    .settings(generatorSettings)
    .settings(
      mappings in(Compile, packageSrc) := (managedSources in Compile).value map (s => (s, s.getName))
    )
    .settings(
      libraryDependencies ++= slick ++ Seq(postgres)
    )
}
