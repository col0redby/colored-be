package com.colored.be.models.dto

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class AlbumDto (
  id: Int,
  title: String
)

object AlbumDto {
  implicit val albumDtoEncoder: Encoder[AlbumDto] = deriveEncoder[AlbumDto]
}
