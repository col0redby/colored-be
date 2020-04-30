package com.colored.be.models.dto

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class ImagePut (
  title: Option[String],
  description: Option[String],
  width: Option[Int],
  height: Option[Int],
  xs: Option[String],
  sm: Option[String],
  md: Option[String],
  lg: Option[String],
  original: Option[String],
  albumId: Option[Int],
  genreId: Option[Int],
  accessLevelId: Option[Int]
)

object ImagePut {
  implicit val imagePutDecoder: Decoder[ImagePut] = deriveDecoder[ImagePut]
}
