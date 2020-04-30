package com.colored.be.models

import java.time.LocalDateTime

case class Comment (
  id: Int,
  imageId: Int,
  userId: Int,
  createdAt: LocalDateTime,
  text: String
)
