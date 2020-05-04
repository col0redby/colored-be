package com.colored.be

import java.io.File
import java.nio.file.Paths

import blobstore.fs.FileStore
import blobstore.s3.S3Store
import cats.effect.{Blocker, ConcurrentEffect, ContextShift, ExitCode, IO, Resource, Timer}
import com.colored.be.aws.AwsSdk
import com.colored.be.config.Config
import com.colored.be.db.Database
import com.colored.be.repository.{GenresRepository, ImagesRepository}
import com.colored.be.routes.{GenresRoutes, ImagesRoutes}
import com.colored.be.service.{GenresService, ImagesService}
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._
import software.amazon.awssdk.services.s3.S3AsyncClient
import cats.implicits._
import com.rabbitmq.client.{Channel, Connection, ConnectionFactory}

object HttpServer {
  def create(configFile: File)(
      implicit contextShift: ContextShift[IO],
      concurrentEffect: ConcurrentEffect[IO],
      timer: Timer[IO]
  ): IO[ExitCode] = {
    resources(configFile).use(create)
  }

  private def resources(
      configFile: File
  )(implicit contextShift: ContextShift[IO]): Resource[IO, Resources] = {
    for {
      config <- Config.load(configFile)
      ec <- ExecutionContexts.fixedThreadPool[IO](
        config.database.threadPoolSize
      )
      blocker <- Blocker[IO]
      transactor <- Database.transactor(config.database, ec, blocker)
      s3AsyncClient <- AwsSdk.s3AsyncClient(config)
      s3Store <- Resource.liftF(S3Store[IO](s3AsyncClient))
      fileStore <- Resource.liftF(
        IO.pure(FileStore[IO](Paths.get("/"), blocker))
      )
      rabbitConnection <- Resource.fromAutoCloseable(IO.delay {
        val factory = new ConnectionFactory
        factory.setHost(config.rabbitMQ.connection.host)
        factory.setPort(config.rabbitMQ.connection.port)
        factory.newConnection()
      })
      rabbitChannel <- Resource.fromAutoCloseable(IO.delay {
        val channel = rabbitConnection.createChannel()
        channel
          .queueDeclare(config.rabbitMQ.queues.resize.name, false, false, false, null)
        channel
          .queueDeclare(config.rabbitMQ.queues.metadata.name, false, false, false, null)
        channel
          .queueDeclare(config.rabbitMQ.queues.colors.name, false, false, false, null)

        channel
      })
    } yield Resources(transactor, s3AsyncClient, s3Store, fileStore, rabbitConnection, rabbitChannel, config)
  }

  private def create(resources: Resources)(
      implicit concurrentEffect: ConcurrentEffect[IO],
      timer: Timer[IO]
  ): IO[ExitCode] = {
    val imagesRepository = new ImagesRepository(resources.transactor)
    val imagesService = new ImagesService(
      imagesRepository,
      resources.s3AsyncClient,
      resources.s3Store,
      resources.rabbitChannel,
      resources.config
    )
    val imagesRoutes = new ImagesRoutes(imagesService).routes

    val genresRepository = new GenresRepository(resources.transactor)
    val genresService = new GenresService(genresRepository)
    val genresRoutes = new GenresRoutes(genresService).routes

    val routes = imagesRoutes <+> genresRoutes

    BlazeServerBuilder[IO]
      .bindHttp(resources.config.server.port, resources.config.server.host)
      .withHttpApp(routes.orNotFound)
      .serve
      .compile
      .lastOrError
  }

  case class Resources(
      transactor: HikariTransactor[IO],
      s3AsyncClient: S3AsyncClient,
      s3Store: S3Store[IO],
      fileStore: FileStore[IO],
      rabbitConnection: Connection,
      rabbitChannel: Channel,
      config: Config
  )
}
