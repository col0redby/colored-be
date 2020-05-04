package com.colored.be.models.dto

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class ImagePost (
    title: String,
    description: Option[String],
    original: String,
    userId: Int,
    albumId: Option[Int],
    genreId: Option[Int],
    accessLevelId: Int
)

object ImagePost {
  implicit val imagePostDecoder: Decoder[ImagePost] = deriveDecoder[ImagePost]
}
