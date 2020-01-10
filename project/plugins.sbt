
resolvers += Resolver.bintrayRepo("vladimirkorchik", "maven")

addSbtPlugin("com.vkorchik" %% "sbt-slick-generator" % "0.1")

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "42.2.9"
)
