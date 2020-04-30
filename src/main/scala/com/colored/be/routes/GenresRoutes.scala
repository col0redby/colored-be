package com.colored.be.routes

import cats.effect.IO
import com.colored.be.service.GenresService
import org.http4s.HttpRoutes
import org.http4s.dsl.impl.Root
import org.http4s.dsl.io._

class GenresRoutes(genresService: GenresService) {

  import org.http4s.circe.CirceEntityCodec._

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "api" / "v1" / "genres" =>
      for {
        listResult <- genresService.listGenres()
        res <- listResult.fold(
          e => InternalServerError(e.getMessage),
          Ok(_)
        )
      } yield res
  }
}
