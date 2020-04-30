package com.colored.be.models.dto

import java.time.LocalDateTime

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class LikesList (
    count: Int,
    list: List[LikeDto]
)

case class LikeDto (
    user: UserDto,
    createdAt: LocalDateTime
)

object LikesList {
  implicit val likesListEncoder: Encoder[LikesList] = deriveEncoder[LikesList]
}

object LikeDto {
  implicit val likeDtoEncoder: Encoder[LikeDto] = deriveEncoder[LikeDto]
}
