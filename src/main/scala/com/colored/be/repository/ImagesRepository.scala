package com.colored.be.repository

import cats.effect.IO
import com.colored.be.models.dto.{ImageList, ImagePost, ImagePut}
import doobie.free.connection.ConnectionIO
import doobie.util.transactor.Transactor
import doobie.implicits._
import doobie.implicits.javatime._
import cats.implicits._
import com.colored.be.models.ImageAccessLevel
import com.colored.be.models.exceptions.ImageNotFound
import com.colored.be.models.tables.{
  AlbumsTable,
  ColorsTable,
  CommentsTable,
  GenresTable,
  ImageAccessLevelsTable,
  ImageMetadataTable,
  ImagesTable,
  LikesTable,
  TagsTable,
  UserAvatarsTable,
  UsersTable
}
import doobie.free.connection
import doobie.util.fragment.Fragment

import scala.collection.mutable.ArrayBuffer

class ImagesRepository(transactor: Transactor[IO]) {

  import com.colored.be.models.dao.Mappings._

  def saveImage(image: ImagePost): IO[Either[Throwable, Int]] = {
    for {
      id <- insertImage(image).update
        .withUniqueGeneratedKeys[Int]("id")
        .transact(transactor)
        .attempt
    } yield id
  }

  def listImages: IO[Either[Throwable, List[ImageList]]] = {
    selectImages()
      .query[ImagesTable]
      .to[List]
      .flatMap(
        _.traverse(image =>
          for {
            likesCount <- selectLikesCount(image.id).query[Int].unique
            commentsCount <- selectCommentsCount(image.id).query[Int].unique
            viewsCount <- selectViewsCount(image.id).query[Int].unique
          } yield ImageList(
            image.id,
            image.title,
            image.width,
            image.height,
            image.xs,
            image.sm,
            image.md,
            image.lg,
            image.original,
            ImageAccessLevel.Public,
            likesCount,
            commentsCount,
            viewsCount
          )
        )
      )
      .transact(transactor)
      .attempt
  }

  type GetImageType = (
      ImagesTable,
      ImageAccessLevelsTable,
      (UsersTable, Option[UserAvatarsTable]),
      Option[AlbumsTable],
      Option[GenresTable],
      List[TagsTable],
      Int,
      List[(LikesTable, UsersTable, Option[UserAvatarsTable])],
      List[(CommentsTable, UsersTable, Option[UserAvatarsTable])],
      List[ColorsTable],
      Option[ImageMetadataTable]
  )

  type SelectImageType = (
      ImagesTable,
      ImageAccessLevelsTable,
      Option[AlbumsTable],
      Option[GenresTable],
      Option[ImageMetadataTable]
  )

  def getImage(id: Int): IO[Either[Throwable, GetImageType]] = {
    (for {
      imageOption <- selectImage(id).query[SelectImageType].option
      result <- imageOption.fold(
        Either
          .left[Throwable, GetImageType](new ImageNotFound)
          .pure[ConnectionIO]
      ) {
        case (image, accessLevel, album, genre, metadata) =>
          for {
            user <- selectUser(image.userId)
              .query[(UsersTable, Option[UserAvatarsTable])]
              .unique
            tags <- selectTags(image.id).query[TagsTable].to[List]
            views <- selectViewsCount(image.id).query[Int].unique
            likes <- selectLikes(image.id)
              .query[(LikesTable, UsersTable, Option[UserAvatarsTable])]
              .to[List]
            comments <- selectComments(image.id)
              .query[(CommentsTable, UsersTable, Option[UserAvatarsTable])]
              .to[List]
            colors <- selectColors(image.id).query[ColorsTable].to[List]
          } yield Right(
            (
              image,
              accessLevel,
              user,
              album,
              genre,
              tags,
              views,
              likes,
              comments,
              colors,
              metadata
            )
          )
      }
    } yield result).transact(transactor).attempt.map(_.flatten)
  }

  def updateImage(id: Int, image: ImagePut): IO[Either[Throwable, Int]] = {
    updateImageFr(id, image).update.run
      .map {
        case affectedRows if affectedRows == 0 =>
          Either
            .left[Throwable, Int](new ImageNotFound)
        case affectedRows if affectedRows > 0 =>
          Either.right[Throwable, Int](affectedRows)
      }
      .transact(transactor)
      .attempt
      .map(_.flatten)
  }

  def deleteImage(id: Int): IO[Either[Throwable, Int]] =
    deleteImageFr(id).update.run.transact(transactor).attempt

  def listAccessLevels(): IO[Either[Throwable, List[ImageAccessLevelsTable]]] =
    selectAccessLevels.query[ImageAccessLevelsTable].to[List].transact(transactor).attempt


  /*
  Private help methods that returns sql fragments
   */
  private def insertImage(image: ImagePost) =
    sql"""
         |INSERT INTO images (title, description, original, user_id, genre_id, access_level_id)
         |VALUES (${image.title}, ${image.description}, ${image.original}, ${image.userId}, ${image.genreId}, ${image.accessLevelId})
         |""".stripMargin

  private def selectImages() =
    sql"""
         |SELECT id, title, description, width, height, xs, sm, md, lg, original, user_id, album_id,
         |        genre_id, access_level_id, created_at, updated_at
         |FROM images
         |""".stripMargin

  private def selectLikesCount(imageId: Int) =
    sql"SELECT COUNT(*) FROM likes WHERE image_id = $imageId"

  private def selectCommentsCount(imageId: Int) =
    sql"SELECT COUNT(*) FROM comments WHERE image_id = $imageId"

  private def selectImage(id: Int) =
    sql"""
         |SELECT
         |       -- image
         |       i.id, i.title, i.description, width, height, xs, sm, md, lg, original, user_id, album_id, genre_id, access_level_id, created_at, updated_at,
         |       -- image access level
         |       ial.id, ial.level, ial.description,
         |       -- album
         |       a.id, a.title, a.description,
         |       -- genre
         |       g.id, g.title,
         |        -- metadata
         |       im.image_id, im.exposure_time_description, im.exposure_time_inverse, im.iso_description, im.iso, im.aperture_desctiprion, im.aperture, im.gps_latitude_description, im.gps_latitude, im.gps_longitude_description, im.gps_longitude, im.gps_altitude_description, im.gps_altitude_meters
         |FROM images i
         |    JOIN images_access_levels ial ON i.access_level_id = ial.id
         |    LEFT OUTER JOIN albums a ON i.album_id = a.id
         |    LEFT OUTER JOIN genres g ON i.genre_id = g.id
         |    LEFT OUTER JOIN image_metadata im ON i.id = im.image_id
         |WHERE i.id = $id
         |""".stripMargin

  private def selectUser(userId: Int) =
    sql"""
         |SELECT
         |    -- user
         |    id, username, email, country, birthday,
         |    -- user avatar
         |    user_id, xs, sm, md
         |FROM users u
         |    LEFT OUTER JOIN user_avatars ua ON u.id = ua.user_id
         |WHERE u.id = $userId
         |""".stripMargin

  private def selectTags(imageId: Int) =
    sql"""
         |SELECT t.id, title FROM image_tags it
         |    JOIN tags t ON it.tag_id = t.id
         |WHERE image_id = $imageId
         |""".stripMargin

  private def selectViewsCount(imageId: Int) =
    sql"SELECT COUNT(*) FROM views WHERE image_id = $imageId"

  private def selectLikes(imageId: Int) =
    sql"""
         |SELECT
         |    -- likes
         |    l.id, image_id, l.user_id, created_at,
         |    -- user
         |    u.id, u.username, u.email, u.country, u.birthday,
         |    -- user avatar
         |    ua.user_id, ua.xs, ua.sm, ua.md
         |FROM likes l
         |        JOIN users u ON l.user_id = u.id
         |        LEFT OUTER JOIN user_avatars ua ON u.id = ua.user_id
         |WHERE image_id = $imageId
         |ORDER BY l.created_at DESC
         |LIMIT 5
         |""".stripMargin

  private def selectComments(imageId: Int) =
    sql"""
         |SELECT
         |    -- comments
         |    c.id, image_id, c.user_id, created_at, text,
         |    -- user
         |    u.id, u.username, u.email, u.country, u.birthday,
         |    -- user avatar
         |    ua.user_id, ua.xs, ua.sm, ua.md
         |FROM comments c
         |    JOIN users u ON c.user_id = u.id
         |    LEFT OUTER JOIN user_avatars ua ON u.id = ua.user_id
         |WHERE image_id = $imageId
         |ORDER BY c.created_at DESC
         |""".stripMargin

  private def selectColors(imageId: Int) =
    sql"""
         |SELECT id, image_id, pct, color FROM image_colors
         |WHERE image_id = $imageId
         |ORDER BY pct DESC
         |""".stripMargin

  private def deleteImageFr(id: Int) =
    sql"DELETE FROM images WHERE id = $id"

  private def updateImageFr(id: Int, image: ImagePut) = {
    val fieldsToUpdate = buildFieldsToUpdate(image)
    Fragment.const(s"UPDATE images SET $fieldsToUpdate WHERE id = $id")
  }

  private def buildFieldsToUpdate(image: ImagePut): String = {
    val buffer = ArrayBuffer.empty[String]
    val withTitle =
      image.title.fold(buffer)(t => buffer.append(s"title = '$t'"))
    val withDescription = image.description.fold(withTitle)(d =>
      buffer.append(s"description = '$d'")
    )
    val withWidth =
      image.width.fold(withDescription)(w => buffer.append(s"width = $w"))
    val withHeigth =
      image.height.fold(withWidth)(h => buffer.append(s"height = $h"))
    val withXs =
      image.xs.fold(withHeigth)(xs => buffer.append(s"xs = '$xs'"))
    val withSm = image.sm.fold(withXs)(sm => buffer.append(s"xs = '$sm'"))
    val withMd = image.md.fold(withSm)(md => buffer.append(s"md = '$md'"))
    val withLg = image.lg.fold(withMd)(lg => buffer.append(s"lg = '$lg'"))
    val withOriginal =
      image.original.fold(withLg)(o => buffer.append(s"original = '$o'"))
    val withAlbumId =
      image.albumId.fold(withOriginal)(ai => buffer.append(s"album_id = $ai"))
    val withGenreId =
      image.genreId.fold(withAlbumId)(gi => buffer.append(s"genre_id = $gi"))
    val withAccessLevelId = image.accessLevelId.fold(withGenreId)(ali =>
      buffer.append(s"acccess_level_id = $ali")
    )

    withAccessLevelId.mkString(", ")
  }

  def selectAccessLevels =
    sql"SELECT id, level, description FROM images_access_levels"
}
