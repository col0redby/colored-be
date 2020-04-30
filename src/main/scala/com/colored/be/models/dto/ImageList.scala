package com.colored.be.models.dto

import com.colored.be.models.ImageAccessLevel
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class ImageList (
    id: Int,
    title: String,
    width: Option[Int],
    height: Option[Int],
    xs: Option[String],
    sm: Option[String],
    md: Option[String],
    lg: Option[String],
    original: String,
    accessLevel: ImageAccessLevel,
    likesCount: Int,
    commentsCount: Int,
    viewsCount: Int
)

object ImageList {
  implicit val imageListEncoder: Encoder[ImageList] = deriveEncoder[ImageList]
  implicit val imageListDecoder: Decoder[ImageList] = deriveDecoder[ImageList]
}
