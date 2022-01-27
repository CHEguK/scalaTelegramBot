ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "scalaTelegramBot"
  )

libraryDependencies ++= Seq(
  "com.bot4s" %% "telegram-core" % "5.3.0",
  "org.typelevel" %% "cats-core" % "2.7.0",
  "org.typelevel" %% "cats-effect" % "3.3.4",
  "com.softwaremill.sttp.client3" %% "core" % "3.3.17",
  "com.softwaremill.sttp.client3" %% "async-http-client-backend-cats" % "3.3.17",
  "is.cir" %% "ciris" % "2.3.2",
  "is.cir" %% "ciris-enumeratum" % "2.3.2",
  "is.cir" %% "ciris-refined" % "2.3.2",
  "io.estatico" %% "newtype" % "0.4.4",
  "eu.timepit" %% "refined" % "0.9.28",
  "eu.timepit" %% "refined-cats" % "0.9.28",
  "com.beachape" %% "enumeratum" % "1.7.0",
  "io.circe" %% "circe-yaml" % "0.14.1",
  "org.typelevel" %% "log4cats-slf4j" % "2.2.0",
  "org.tpolecat" %% "doobie-core" % "1.0.0-RC1",
  "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC1",
  "org.tpolecat" %% "doobie-hikari" % "1.0.0-RC1",
  "dev.profunktor" %% "redis4cats-effects" % "1.0.0",
  "dev.profunktor" %% "redis4cats-log4cats" % "1.0.0",
  "dev.optics" %% "monocle-core"  % "3.1.0",
  "dev.optics" %% "monocle-macro" % "3.1.0"
)

scalacOptions += "-Ymacro-annotations"
