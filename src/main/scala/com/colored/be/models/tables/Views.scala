package com.colored.be.models.tables

final case class GetImageView(
    image: ImagesTable,
    accessLevel: ImageAccessLevelsTable,
    userView: UserView,
    album: Option[AlbumsTable],
    genre: Option[GenresTable],
    tags: List[TagsTable],
    viewsCount: Int,
    likesView: List[LikeView],
    commentsView: List[CommentView],
    colors: List[ColorsTable],
    metadata: Option[ImageMetadataTable]
)

final case class SelectImageView(
    image: ImagesTable,
    accessLevel: ImageAccessLevelsTable,
    album: Option[AlbumsTable],
    genre: Option[GenresTable],
    metadata: Option[ImageMetadataTable]
)

final case class UserView(
    user: UsersTable,
    avatar: Option[UserAvatarsTable]
)

final case class LikeView(
    like: LikesTable,
    userView: UserView
)

final case class CommentView(
    comment: CommentsTable,
    userView: UserView
)
