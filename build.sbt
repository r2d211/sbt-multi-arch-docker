import sbtghplugin.PblshPlgnGHPckgsPlugin.ghPckgPomConsistency

name := "sbt-multi-arch-docker"
version := "0.0.0-alpha1"
ThisBuild / organization := "io.github.r2d211"
ThisBuild / sbtPlugin := true
ThisBuild / sbtVersion := "1.6.2"

enablePlugins(SbtPlugin)


resolvers += Resolver.githubPackages("r2d211")
resolvers += Resolver.mavenLocal
githubOwner := "r2d211"
githubRepository := "sbt-multi-arch-docker"
githubTokenSource := TokenSource.GitConfig("github.token") || TokenSource.Environment("GITHUB_TOKEN")

enablePlugins(PblshPlgnGHPckgsPlugin)


lazy val root = (project in file("."))
  .enablePlugins(UniversalPlugin)
  .settings(ghPckgPomConsistency)
