package com.colored.be.models.dto

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class TagDto (
    id: Int,
    title: String
)

object TagDto {
  implicit val tagDtoEncoder: Encoder[TagDto] = deriveEncoder[TagDto]
}
