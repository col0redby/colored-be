package com.colored.be.models.tables

import java.time.LocalDateTime

case class LikesTable (

    imageId: Int,
    userId: Int,
    createdAt: LocalDateTime
)
