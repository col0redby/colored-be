package com.colored.be.models

import io.circe.{Decoder, Encoder, Json}

final case class Image(
    id: Int,
    title: String,
    description: String,
    width: Int,
    height: Int,
    xs: String,
    sm: String,
    md: String,
    lg: String,
    original: String,
    userId: Int,
    user: User,
    album: Album,
    genre: Genre,
    accessLevel: ImageAccessLevel,
    likes: List[Like],
    comments: List[Comment]
)

sealed trait ImageAccessLevel

object ImageAccessLevel {
  case object Public extends ImageAccessLevel {
    override def toString: String = Literals.public
  }
  case object Private extends ImageAccessLevel {
    override def toString: String = Literals.`private`
  }

  object Literals {
    val public = "public"
    val `private` = "private"
  }

  def fromString(str: String): ImageAccessLevel = str match {
    case Literals.public    => Public
    case Literals.`private` => Private
  }

  implicit val accessEncoder: Encoder[ImageAccessLevel] =
    Encoder.instance {
      case ImageAccessLevel.Public  => Json.fromString(Literals.public)
      case ImageAccessLevel.Private => Json.fromString(Literals.`private`)
    }

  implicit val accessDecoder: Decoder[ImageAccessLevel] =
    Decoder.decodeString.map {
      case Literals.public => Public
      case Literals.`private` => Private
    }
}
