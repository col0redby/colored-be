package com.colored.be.models.dto

import java.time.LocalDateTime

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class CommentsList (
    count: Int,
    list: List[CommentDto]
)

case class CommentDto (
  user: UserDto,
  text: String,
  createdAt: LocalDateTime
)

object CommentsList {
  implicit val commentsListEncoder: Encoder[CommentsList] = deriveEncoder[CommentsList]
}

object CommentDto {
  implicit val commentDtoEncoder: Encoder[CommentDto] = deriveEncoder[CommentDto]
}
