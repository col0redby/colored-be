package com.colored.be.models.dto

import com.colored.be.models.ImageAccessLevel
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class ImageAccessLevelDto (
    id: Int,
    level: ImageAccessLevel
)

object ImageAccessLevelDto {
  implicit val accessLevelDtoEncoder: Encoder[ImageAccessLevelDto] = deriveEncoder[ImageAccessLevelDto]
}
