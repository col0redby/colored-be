package com.colored.be.models.dto

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class UserAvatarDto (
  xs: Option[String],
  sm: Option[String],
  md: Option[String]
)

object UserAvatarDto {
  implicit val avatarDtoEncoder: Encoder[UserAvatarDto] = deriveEncoder[UserAvatarDto]
}
