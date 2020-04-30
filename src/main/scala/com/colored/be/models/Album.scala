package com.colored.be.models

final case class Album(
    id: Int,
    title: String,
    description: String,
    cover: AlbumCover
)

final case class AlbumCover(
    albumId: Int,
    xs: String,
    sm: String,
    md: String
)
