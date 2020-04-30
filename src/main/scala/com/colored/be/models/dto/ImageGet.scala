package com.colored.be.models.dto

import java.time.LocalDateTime

import com.colored.be.models.ImageAccessLevel
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class ImageGet (
    id: Int,
    title: String,
    description: String,
    width: Option[Int],
    height: Option[Int],
    xs: Option[String],
    sm: Option[String],
    md: Option[String],
    lg: Option[String],
    original: String,
    user: UserDto,
    album: Option[AlbumDto],
    genre: Option[GenreDto],
    tags: List[TagDto],
    views: Int,
    accessLevel: ImageAccessLevel,
    likes: LikesList,
    comments: CommentsList,
    colors: List[ColorDto],
    metadata: Option[ImageMetadataDto],
    createdAt: LocalDateTime
)

object ImageGet {
  implicit val imageGetEncoder: Encoder[ImageGet] = deriveEncoder[ImageGet]
}
