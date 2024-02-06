import Dependencies._

ThisBuild / scalaVersion     := "2.13.12"
ThisBuild / version          := "1.0.0"
ThisBuild / organization     := "uk.co.flynnit"
ThisBuild / organizationName := "flynnit"

lazy val root = (project in file("."))
  .settings(
    name := "CheckoutMercator",
    libraryDependencies += catsCore,
    libraryDependencies += mockito % Test,
    libraryDependencies += scalatest % Test,
  )

