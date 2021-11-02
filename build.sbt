import sbt.internal.util.ManagedLogger
import sbtide.Keys.idePackagePrefix

Global / onChangedBuildSource := ReloadOnSourceChanges

name := "SbtTest"

version := "0.1"

scalaVersion := "2.13.6"

idePackagePrefix := Some("org.sbttest")

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.6.0",
    "org.typelevel" %% "cats-effect" % "3.1.1",

    "com.typesafe" % "config" % "1.4.1",

    "io.circe" %% "circe-core" % "0.14.1",
    "io.circe" %% "circe-generic" % "0.14.1",
    "io.circe" %% "circe-parser" % "0.14.1",
    "io.circe" %% "circe-generic-extras" % "0.14.0",
    "io.circe" %% "circe-config" % "0.8.0",

    "org.http4s" %% "http4s-dsl" % "0.23.0-RC1",
    "org.http4s" %% "http4s-blaze-server" % "0.23.0-RC1",
    "org.http4s" %% "http4s-blaze-client" % "0.23.0-RC1",
    "org.http4s" %% "http4s-circe" % "0.23.0-RC1",

    "ch.qos.logback" % "logback-classic" % "1.2.3",

    "org.scalatest" %% "scalatest" % "3.2.7" % Test,
    "org.typelevel" %% "cats-laws" % "2.6.0" % Test,
    "org.typelevel" %% "discipline-scalatest" % "2.1.5" % Test,
    "org.typelevel" %% "cats-effect-testing-scalatest" % "1.1.1" % Test,

    "org.scanamo" %% "scanamo" % "1.0.0-M17"
  )

)



lazy val userService = (project in file("services/user-service"))
  .settings(
    name := "user-service",
    idePackagePrefix := Some("org.sbttest.userservice"),
    commonSettings
)

lazy val booksService = (project in file("services/books-service"))
  .settings (
    name:= "books-service",
    idePackagePrefix := Some("org.sbttest.booksservice"),
    commonSettings
  )


lazy val root =  project.in (file(".")).aggregate(userService,booksService)
