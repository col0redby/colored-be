package com.colored.be.routes

import cats.effect.IO

import org.http4s.dsl.io._
import org.http4s.HttpRoutes
import org.http4s.headers.`Content-Disposition`
import org.http4s.multipart.Multipart

import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.{
  GetUrlRequest,
  ObjectCannedACL
}

import blobstore.fs.FileStore
import blobstore.s3.{S3MetaInfo, S3Path, S3Store}

import com.colored.be.repository.ImagesRepository
import com.colored.be.Constants.eligibleContentTypes
import com.colored.be.config.Config

class ImagesRoutes(
    imagesRepository: ImagesRepository,
    s3: S3AsyncClient,
    s3Store: S3Store[IO],
    fileStore: FileStore[IO],
    config: Config
) {

  // Just to somehow emulate user
  val username = "nkharitonov"

  val routes = HttpRoutes.of[IO] {

    case req @ POST -> Root / "images" / "upload" =>
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
                    .fold(username + System.currentTimeMillis())(identity)
                )

                streamSize <- imagePart.body.compile.toList.map(_.length)

                path = S3Path(
                  config.s3.bucket,
                  s"$username/${config.s3.imageOriginalFolder}/$filename",
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
                TODO: handle errors.
                 */
                uploadResponse <- {
                  imagePart.body
                    .rechunkRandomly()
                    .through(s3Store.put(path))
                    .compile
                    .toList
                    .attempt
                }

                objectUrl <- Ok(
                  s3.utilities()
                    .getUrl(
                      GetUrlRequest
                        .builder()
                        .bucket(path.bucket)
                        .key(path.key)
                        .build()
                    )
                    .toString
                )
              } yield objectUrl
            case None =>
              UnsupportedMediaType(
                s"At least one part should contain one of the following " +
                  s"headers ${eligibleContentTypes.mkString(", ")}"
              )
          }
        }
      } yield response
  }
}
