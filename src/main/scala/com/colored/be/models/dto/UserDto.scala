package com.colored.be.models.dto

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class UserDto (
    id: Int,
    username: String,
    avatar: Option[UserAvatarDto]
)

object UserDto {
  implicit val userDtoEncoder: Encoder[UserDto] = deriveEncoder[UserDto]
}
