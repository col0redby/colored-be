package com.colored.be.models.tables

import java.time.LocalDateTime

case class LikesTable (
    id: Int,
    imageId: Int,
    userId: Int,
    createdAt: LocalDateTime
)
