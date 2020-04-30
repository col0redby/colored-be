package com.colored.be.models.dto

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class GenreDto (
    id: Int,
    title: String
)

object GenreDto {
  implicit val genreDtoEncoder: Encoder[GenreDto] = deriveEncoder[GenreDto]
}
