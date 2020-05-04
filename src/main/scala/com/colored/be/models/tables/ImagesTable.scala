package com.colored.be.models.tables

import java.time.LocalDateTime

import com.colored.be.models.ImageAccessLevel

case class ImagesTable(
  id: Int,
  title: String,
  description: Option[String],
  width: Option[Int],
  height: Option[Int],
  xs: Option[String],
  sm: Option[String],
  md: Option[String],
  lg: Option[String],
  original: String,
  userId: Int,
  albumId: Option[Int],
  genreId: Option[Int],
  accessLevelId: Int,
  createdAt: LocalDateTime,
  updatedAt: LocalDateTime
)
