package com.colored.be.models.dto

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class ImagePost (
    title: String,
    description: String,
    original: String,
    userId: Int,
    genreId: Int,
    accessLevelId: Int
)

object ImagePost {
  implicit val imagePostDecoder: Decoder[ImagePost] = deriveDecoder[ImagePost]
}
