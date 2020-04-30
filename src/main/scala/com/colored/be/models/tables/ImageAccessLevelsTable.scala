package com.colored.be.models.tables

import com.colored.be.models.ImageAccessLevel

case class ImageAccessLevelsTable (
    id: Int,
    level: ImageAccessLevel,
    description: String
)
