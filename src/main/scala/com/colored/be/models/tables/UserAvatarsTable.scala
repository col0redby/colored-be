package com.colored.be.models.tables

case class UserAvatarsTable (
    userId: Int,
    xs: Option[String],
    sm: Option[String],
    md: Option[String]
)
