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
val metadataExtractorVersion = "2.13.0"
val fs2BlobstoreVersion = "0.7.0"
val awsS3SdkVersion = "2.12.0"
val rabbitMqClientVersion = "5.9.0"

lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1"

lazy val root = (project in file("."))
  .enablePlugins(DockerPlugin)
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "colored-be",
    libraryDependencies ++= Seq(
      "com.drewnoakes" % "metadata-extractor" % metadataExtractorVersion,
      "io.chrisdavenport" %% "log4cats-slf4j" % "1.0.1",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.github.fs2-blobstore" %% "core" % fs2BlobstoreVersion,
      "com.github.fs2-blobstore" %% "s3" % fs2BlobstoreVersion,
      "software.amazon.awssdk" % "s3" % awsS3SdkVersion,
      "org.typelevel" %% "cats-core" % catsVersion,
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
      "com.amazonaws" % "aws-java-sdk-s3" % "1.11.774",
      "com.rabbitmq" % "amqp-client" % rabbitMqClientVersion,
      scalaTest % Test
    )
  )
