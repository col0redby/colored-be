package com.colored.be.service

import cats.effect.IO
import com.colored.be.models.dto.GenreDto
import com.colored.be.repository.GenresRepository

class GenresService(genresRepository: GenresRepository) {

  def listGenres(): IO[Either[Throwable, List[GenreDto]]] = {
    genresRepository.listGenres().map(_.map(_.map(g => GenreDto(g.id, g.title))))
  }

}
