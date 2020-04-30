package com.colored.be.models

final case class Genre(
    id: Int,
    title: String,
    cover: GenreCover
)

final case class GenreCover(
    genreId: Int,
    xs: String,
    sm: String,
    md: String
)
