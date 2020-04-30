package com.colored.be.repository

import cats.effect.IO
import com.colored.be.models.tables.GenresTable
import doobie.util.transactor.Transactor
import doobie.implicits._

class GenresRepository(transactor: Transactor[IO]) {

  def listGenres(): IO[Either[Throwable, List[GenresTable]]] = {
    sql"SELECT id, title FROM genres ORDER BY title"
      .query[GenresTable]
      .to[List]
      .transact(transactor)
      .attempt
  }

}
