package com.colored.be.routes

import java.net.URL

import cats.effect.IO
import org.http4s.dsl.io._
import org.http4s.HttpRoutes
import org.http4s.headers.`Content-Disposition`
import org.http4s.multipart.Multipart
import com.colored.be.Constants.eligibleContentTypes
import com.colored.be.models.dto.{
  CommentPost,
  ImagePost,
  ImagePut,
  LikeDto,
  LikePost
}
import com.colored.be.models.exceptions.ImageNotFound
import com.colored.be.service.ImagesService

class ImagesRoutes(imagesService: ImagesService) {

  import org.http4s.circe.CirceEntityCodec._

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {

    case req @ POST -> Root / "api" / "v1" / "images" / "upload" =>
      for {
        multipart <- req.as[Multipart[IO]]
        response <- {
          multipart.parts.find(
            _.headers.exists(h => eligibleContentTypes.exists(h == _))
          ) match {
            case Some(imagePart) =>
              for {
                filename <- IO.pure(
                  imagePart.headers
                    .get(`Content-Disposition`)
                    .flatMap(_.parameters.get("filename"))
                )
                uploadResult <- imagesService.uploadImage(
                  imagePart.body,
                  filename
                )
                res <- uploadResult match {
                  case Left(error)  => InternalServerError(error.getMessage)
                  case Right(value) => Ok(value)
                }
              } yield res
            case None =>
              UnsupportedMediaType(
                s"At least one part should contain one of the following " +
                  s"headers ${eligibleContentTypes.mkString(", ")}"
              )
          }
        }
      } yield response

    case req @ POST -> Root / "api" / "v1" / "images" =>
      for {
        imagePost <- req.as[ImagePost]
        saveResult <- imagesService.saveImage(imagePost)
        res <- saveResult.fold(
          e => InternalServerError(e.getMessage),
          Created(_)
        )
      } yield res

    case GET -> Root / "api" / "v1" / "images" =>
      for {
        listResult <- imagesService.listImages()
        res <- listResult.fold(e => InternalServerError(e.getMessage), Ok(_))
      } yield res

    case GET -> Root / "api" / "v1" / "images" / IntVar(id) =>
      for {
        getResult <- imagesService.getImage(id)
        res <- getResult.fold({
          case e: ImageNotFound => NotFound()
          case e: Throwable     => InternalServerError(e.getMessage)
        }, Ok(_))
      } yield res

    case req @ PUT -> Root / "api" / "v1" / "images" / IntVar(id) =>
      for {
        imagePut <- req.as[ImagePut]
        updateResult <- imagesService.updateImage(id, imagePut)
        res <- updateResult.fold({
          case e: ImageNotFound => NotFound()
          case e: Throwable     => InternalServerError(e.getMessage)
        }, Ok(_))
      } yield res

    case DELETE -> Root / "api" / "v1" / "images" / IntVar(id) =>
      for {
        deleteResult <- imagesService.deleteImage(id)
        res <- deleteResult.fold(
          e => InternalServerError(e.getMessage),
          _ => NoContent()
        )
      } yield res

    /*
      Image access levels
     */
    case GET -> Root / "api" / "v1" / "images" / "access-levels" =>
      for {
        listResult <- imagesService.listAccessLevels()
        res <- listResult.fold(
          e => InternalServerError(e.getMessage),
          Ok(_)
        )
      } yield res

    /*
    Likes
     */
    case req @ POST -> Root / "api" / "v1" / "images" / "like" =>
      for {
        like <- req.as[LikePost]
        likeResult <- imagesService.like(like)
        res <- likeResult.fold(
          e => InternalServerError(e.getMessage),
          Created(_)
        )
      } yield res

    case req @ DELETE -> Root / "api" / "v1" / "images" / "unlike" =>
      for {
        unlike <- req.as[LikePost]
        unlikeResult <- imagesService.unlike(unlike)
        res <- unlikeResult.fold(
          e => InternalServerError(e.getMessage),
          Ok(_)
        )
      } yield res

    /*
    Comments
     */
    case req @ POST -> Root / "api" / "v1" / "images" / IntVar(id) / "comment" =>
      for {
        comment <- req.as[CommentPost]
        commentSaveResult <- imagesService.saveComment(id, comment)
        res <- commentSaveResult.fold(
          e => InternalServerError(e.getMessage),
          Created(_)
        )
      } yield res
  }
}
