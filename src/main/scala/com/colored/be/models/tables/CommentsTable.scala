package com.colored.be.models.tables

import java.time.LocalDateTime

case class CommentsTable (
    id: Int,
    imageId: Int,
    userId: Int,
    createdAt: LocalDateTime,
    text: String
)
