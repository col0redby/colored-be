package com.colored.be

import cats.effect._

import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._

import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts

import java.nio.file.Paths
import software.amazon.awssdk.services.s3.S3AsyncClient

import blobstore.fs.FileStore
import blobstore.s3.S3Store

import com.colored.be.config.Config
import com.colored.be.db.Database
import com.colored.be.repository.ImagesRepository
import com.colored.be.routes.ImagesRoutes

object HttpServer {
  def create(configFile: String = "application.conf")(
      implicit contextShift: ContextShift[IO],
      concurrentEffect: ConcurrentEffect[IO],
      timer: Timer[IO]
  ): IO[ExitCode] = {
    resources(configFile).use(create)
  }

  private def resources(
      configFile: String
  )(implicit contextShift: ContextShift[IO]): Resource[IO, Resources] = {
    for {
      config <- Config.load(configFile)
      ec <- ExecutionContexts.fixedThreadPool[IO](
        config.database.threadPoolSize
      )
      blocker <- Blocker[IO]
      transactor <- Database.transactor(config.database, ec, blocker)
      s3AsyncClient <- Resource.liftF(IO.pure(S3AsyncClient.builder().build()))
      s3Store <- Resource.liftF(S3Store[IO](s3AsyncClient))
      fileStore <- Resource.liftF(
        IO.pure(FileStore[IO](Paths.get("/"), blocker))
      )
    } yield Resources(transactor, s3AsyncClient, s3Store, fileStore, config)
  }

  private def create(resources: Resources)(
      implicit concurrentEffect: ConcurrentEffect[IO],
      timer: Timer[IO]
  ): IO[ExitCode] = {
    val repository = new ImagesRepository(resources.transactor)
    BlazeServerBuilder[IO]
      .bindHttp(resources.config.server.port, resources.config.server.host)
      .withHttpApp(
        new ImagesRoutes(
          repository,
          resources.s3AsyncClient,
          resources.s3Store,
          resources.fileStore,
          resources.config
        ).routes.orNotFound
      )
      .serve
      .compile
      .lastOrError
  }

  case class Resources(
      transactor: HikariTransactor[IO],
      s3AsyncClient: S3AsyncClient,
      s3Store: S3Store[IO],
      fileStore: FileStore[IO],
      config: Config
  )
}
