package com.colored.be.repository

import cats.effect.IO
import doobie.util.transactor.Transactor
import doobie.implicits._

class ImagesRepository(transactor: Transactor[IO]) {

//  def createImage(image: Image): IO[Image] = {
//    for {
//      id <- sql"INSERT INTO images (title, width, height) values (${image.title}, ${image.width}, ${image.height})"
//        .update
//        .withUniqueGeneratedKeys[Int]("id")
//        .transact(transactor)
//      image <- sql"SELECT id, title, width, height FROM images WHERE id = $id"
//        .query[Image]
//        .unique
//        .transact(transactor)
//    } yield image
//  }
//
//  def getImages: IO[List[Image]] = {
//    sql"SELECT id, title, width, height FROM images"
//      .query[Image]
//      .to[List]
//      .transact(transactor)
//  }
//
//  def getImage(id: Int): IO[Either[ImageApiError, Image]] = {
//    sql"SELECT id, title, width, height FROM images WHERE id = $id"
//      .query[Image]
//      .option
//      .transact(transactor)
//      .map {
//        case Some(value) => Right(value)
//        case None => Left(ImageNotFound())
//      }
//  }
//
//  def updateImage(id: Int, image: Image): IO[Either[ImageApiError, Image]] = {
//    for {
//      affectedRows <- sql"UPDATE images SET title = ${image.title}, width = ${image.width}, height = ${image.height} WHERE id = $id"
//        .update
//        .run
//        .transact(transactor)
//      image <- if (affectedRows > 0) {
//        sql"SELECT id, title, width, height FROM images WHERE id = $id"
//          .query[Image]
//          .unique
//          .transact(transactor)
//      } map (Right(_))
//      else IO.pure(Left(ImageNotFound()))
//    } yield image
//  }
//
//  def deleteImage(id: Int): IO[Either[ImageApiError, Unit]] = {
//    for {
//      affectedRows <- sql"DELETE FROM images WHERE id = $id".update.run
//        .transact(transactor)
//      image <- if (affectedRows > 0) IO.pure(Right())
//      else IO.pure(Left(ImageNotFound()))
//    } yield image
//  }
}
