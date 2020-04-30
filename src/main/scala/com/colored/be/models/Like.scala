package com.colored.be.models

import java.time.LocalDateTime

case class Like (
    id: Int,
    imageId: Int,
    userId: Int,
    createdAt: LocalDateTime
)
