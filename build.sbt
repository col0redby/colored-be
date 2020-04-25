import Dependencies._
import com.typesafe.sbt.packager.docker.Cmd

ThisBuild / scalaVersion := "2.13.1"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.colored"
ThisBuild / organizationName := "colored"
ThisBuild / maintainer := "colof00by@gmail.com"

val catsVersion = "2.1.0"
val circeVersion = "0.13.0"
val doobieVersion = "0.8.8"
val pureConfigVersion = "0.12.3"
val http4sVersion = "0.21.1"

lazy val root = (project in file("."))
  .enablePlugins(DockerPlugin)
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "colored-be",
    libraryDependencies ++= Seq(
      "com.drewnoakes" % "metadata-extractor" % "2.13.0",
      "io.chrisdavenport" %% "log4cats-slf4j" % "1.0.1",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.github.fs2-blobstore" %% "core" % "0.7.0",
      "com.github.fs2-blobstore" %% "s3" % "0.7.0",
      "software.amazon.awssdk" % "s3" % "2.12.0",
      "org.typelevel" %% "cats-core" % "2.1.0",
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-generic-extras" % circeVersion,
      "io.circe" %% "circe-optics" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-h2" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
      "mysql" % "mysql-connector-java" % "8.0.19",
      "com.github.pureconfig" %% "pureconfig" % pureConfigVersion,
      "com.github.pureconfig" %% "pureconfig-cats-effect" % pureConfigVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      scalaTest % Test
    )
  )
