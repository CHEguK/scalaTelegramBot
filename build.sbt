ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "scalaTelegramBot"
  )

libraryDependencies ++= Seq(
  "com.bot4s" %% "telegram-core" % "5.3.0",
  "org.typelevel" %% "cats-effect" % "2.5.3",
  "org.typelevel" %% "cats-core" % "2.3.0",
  "com.softwaremill.sttp.client3" %% "core" % "3.3.18",
  "com.softwaremill.sttp.client3" %% "async-http-client-backend-cats-ce2" % "3.3.18",
  "is.cir" %% "ciris" % "2.3.1",
  "io.estatico" %% "newtype" % "0.4.4",
  "eu.timepit" %% "refined" % "0.9.28",
  "com.beachape" %% "enumeratum" % "1.7.0"
)
