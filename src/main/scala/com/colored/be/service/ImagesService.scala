package com.colored.be.service

import java.net.{URL, URLDecoder}

import blobstore.s3.{S3MetaInfo, S3Path, S3Store}
import cats.effect.IO
import com.colored.be.config.Config
import com.colored.be.models.User
import com.colored.be.models.dto._
import com.colored.be.repository.ImagesRepository
import software.amazon.awssdk.services.s3.model.{GetUrlRequest, ObjectCannedACL}
import fs2.Stream
import software.amazon.awssdk.services.s3.S3AsyncClient
import cats.implicits._
import com.colored.be.models.queue_requests._
import com.colored.be.models.tables.{ImageMetadataTable, UserView}
import com.rabbitmq.client.Channel
import io.circe.syntax._

import scala.util.Try

class ImagesService(
    imagesRepository: ImagesRepository,
    s3: S3AsyncClient,
    s3Store: S3Store[IO],
    rabbitChannel: Channel,
    config: Config
) {

  def uploadImage(
      stream: Stream[IO, Byte],
      headersFileName: Option[String],
      user: User = User.mock
  ): IO[Either[Throwable, String]] = {
    for {

      // Aws s3 sdk requires size to be present before streaming upload
      streamSize <- stream.compile.toList.map(_.length)

      filename = headersFileName.fold(
        user.username + System.currentTimeMillis()
      )(identity)
      bucket = config.aws.s3.bucket
      key = s"${user.username}/${config.aws.s3.imageOriginalFolder}/$filename"
      path = S3Path(
        bucket,
        key,
        Some(
          S3MetaInfo.const(
            constSize = Some(streamSize),
            constAcl = Some(ObjectCannedACL.PUBLIC_READ)
          )
        )
      )

      /*
      Rechunking input stream here just to make it working, because:
      Uploading work's fine for
        1. Http -> http4s -> Stream[IO, Byte] -> Local fs
        2. Local fs -> Stream[IO, Byte] -> StreamUnicastPublisher -> AsyncRequestBody -> s3
      But don't want to work with
        Http -> http4s -> Stream[IO, Byte] -> StreamUnicastPublisher -> AsyncRequestBody -> s3 chain
      there is a bytes losing when an aws sdk sending the first chunk of bytes. Don't know how to
      solve it now, need to dig deeper later ¯\_(ツ)_/¯.
       */
      uploadResponse <- {
        stream
          .rechunkRandomly()
          .through(s3Store.put(path))
          .compile
          .toList
          .attempt
      }

      objectUrl <- IO.pure {
        uploadResponse.map { _ =>
          s3.utilities()
            .getUrl(
              GetUrlRequest
                .builder()
                .bucket(path.bucket)
                .key(path.key)
                .build()
            )
            .toString
        }
      }
    } yield objectUrl
  }

  def saveImage(imagePost: ImagePost): IO[Either[Throwable, Int]] =
    for {
      saveResult <- imagesRepository.saveImage(imagePost)
      queueResult <- saveResult.fold(
        error => Either.left(error).pure[IO],
        imageId => sendColoriaRequests(imageId, imagePost)
      )
    } yield queueResult

  def listImages(): IO[Either[Throwable, List[ImageList]]] =
    imagesRepository.listImages

  def getImage(id: Int): IO[Either[Throwable, ImageGet]] = {
    imagesRepository
      .getImage(id)
      .map(_.map { getImageView =>
        ImageGet(
          getImageView.image.id,
          getImageView.image.title,
          getImageView.image.description,
          getImageView.image.width,
          getImageView.image.height,
          getImageView.image.xs,
          getImageView.image.sm,
          getImageView.image.md,
          getImageView.image.lg,
          getImageView.image.original,
          mapUserViewOnDto(getImageView.userView),
          getImageView.album.map(a => AlbumDto(a.id, a.title)),
          getImageView.genre.map(g => GenreDto(g.id, g.title)),
          getImageView.tags.map(t => TagDto(t.id, t.title)),
          getImageView.viewsCount,
          getImageView.accessLevel.level,
          LikesList(
            getImageView.likesView.length,
            getImageView.likesView.map { likeView =>
              LikeDto(
                mapUserViewOnDto(likeView.userView),
                likeView.like.createdAt
              )
            }
          ),
          CommentsList(
            getImageView.commentsView.length,
            getImageView.commentsView.map { commentView =>
              CommentDto(
                mapUserViewOnDto(commentView.userView),
                commentView.comment.text,
                commentView.comment.createdAt
              )
            }
          ),
          getImageView.colors.map(c => ColorDto(c.color, c.pct)),
          getImageView.metadata.map(mapMetadataOnDto),
          getImageView.image.createdAt
        )
      })
  }

  def updateImage(id: Int, image: ImagePut): IO[Either[Throwable, ImageGet]] =
    for {
      updateResult <- imagesRepository.updateImage(id, image)
      getResult <- updateResult.fold(
        e => Either.left[Throwable, ImageGet](e).pure[IO],
        _ => getImage(id)
      )
    } yield getResult

  def deleteImage(id: Int): IO[Either[Throwable, Int]] =
    imagesRepository.deleteImage(id)

  def listAccessLevels(): IO[Either[Throwable, List[ImageAccessLevelDto]]] =
    imagesRepository
      .listAccessLevels()
      .map(_.map(_.map(ial => ImageAccessLevelDto(ial.id, ial.level))))

  /*
  Likes
   */
  def like(likePost: LikePost): IO[Either[Throwable, LikesList]] =
    imagesRepository
      .like(likePost.imageId, likePost.userId)
      .map(
        _.map(likesView =>
          LikesList(
            likesView.length,
            likesView.map { likeView =>
              LikeDto(
                mapUserViewOnDto(likeView.userView),
                likeView.like.createdAt
              )
            }
          )
        )
      )

  def unlike(likePost: LikePost): IO[Either[Throwable, LikesList]] =
    imagesRepository
      .unlike(likePost.imageId, likePost.userId)
      .map(
        _.map(likesView =>
          LikesList(
            likesView.length,
            likesView.map { likeView =>
              LikeDto(
                mapUserViewOnDto(likeView.userView),
                likeView.like.createdAt
              )
            }
          )
        )
      )

  /*
  Comments
   */
  def saveComment(
      imageId: Int,
      commentPost: CommentPost
  ): IO[Either[Throwable, CommentsList]] =
    imagesRepository
      .saveComment(imageId, commentPost.userId, commentPost.text)
      .map(
        _.map(commentsView =>
          CommentsList(
            commentsView.length,
            commentsView.map { commentView =>
              CommentDto(
                mapUserViewOnDto(commentView.userView),
                commentView.comment.text,
                commentView.comment.createdAt
              )
            }
          )
        )
      )

  /*
   Coloria integration
   TODO: Use avast/scala-rabbitmq client
   TODO: Investigate how we can get rid of slicing and dicing
    original url to obtain bucket, key, filename
   */
  private def sendColoriaRequests(
      imageId: Int,
      imagePost: ImagePost
  ): IO[Either[Throwable, Int]] =
    IO {
      Try {
        val originalUrl =
          new URL(URLDecoder.decode(imagePost.original, "UTF-8"))
        val originalBucket = originalUrl.getHost.split('.').head
        val originalKey = originalUrl.getPath.drop(1)
        val filename = originalUrl.getPath.split("/").last
        val resizeRequest = ResizeRequest(
          imageId,
          originalBucket,
          originalKey,
          filename,
          originalBucket,
          originalKey.split("original").head.dropRight(1)
        )
        rabbitChannel.basicPublish(
          "",
          config.rabbitMQ.queues.resize.name,
          null,
          resizeRequest.asJson.noSpaces.getBytes()
        )

        val metadataRequest =
          MetadataRequest(imageId, originalBucket, originalKey)
        rabbitChannel.basicPublish(
          "",
          config.rabbitMQ.queues.metadata.name,
          null,
          metadataRequest.asJson.noSpaces.getBytes()
        )

        val colorsRequest = ColorsRequest(imageId, originalBucket, originalKey)
        rabbitChannel.basicPublish(
          "",
          config.rabbitMQ.queues.colors.name,
          null,
          colorsRequest.asJson.noSpaces.getBytes()
        )

        imageId
      }.toEither
    }

  // Mappers
  private def mapUserViewOnDto(u: UserView): UserDto = {
    UserDto(
      u.user.id,
      u.user.username,
      u.avatar.map(a =>
        UserAvatarDto(
          a.xs,
          a.sm,
          a.md
        )
      )
    )
  }

  private def mapMetadataOnDto(m: ImageMetadataTable): ImageMetadataDto = {
    ImageMetadataDto(
      m.exposureTimeDescription,
      m.exposureTimeInverse,
      m.isoDescription,
      m.iso,
      m.apertureDescription,
      m.aperture,
      m.gpsLatitudeDescription,
      m.gpsLatitude,
      m.gpsLongitudeDescription,
      m.gpsLongitude,
      m.gpsAltitudeDescription,
      m.gpsAltitudeMeters
    )
  }
}
