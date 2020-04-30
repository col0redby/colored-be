package com.colored.be.models.dto

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class ColorDto (
    color: String,
    pct: Float
)

object ColorDto {
  implicit val colorDtoEncoder: Encoder[ColorDto] = deriveEncoder[ColorDto]
}
