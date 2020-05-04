package com.colored.be.models.tables

import com.colored.be.models.ImageAccessLevel
import doobie.util.{Get, Put}

object Mappings {
  implicit val accessGet: Get[ImageAccessLevel] = Get[String].map(ImageAccessLevel.fromString)
  implicit val accessPut: Put[ImageAccessLevel] = Put[String].contramap(_.toString)
}
