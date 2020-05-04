package com.colored.be.models.dto

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class CommentPost(
    userId: Int,
    text: String
)

object CommentPost {
  implicit val commentPostDecoder: Decoder[CommentPost] = deriveDecoder[CommentPost]
}
