package com.colored.be.models.dto

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class LikePost (
    imageId: Int,
    userId: Int
)

object LikePost {
  implicit val likePostDecoder: Decoder[LikePost] = deriveDecoder[LikePost]
}
