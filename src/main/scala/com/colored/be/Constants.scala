package com.colored.be

import org.http4s.MediaType
import org.http4s.headers.`Content-Type`

object Constants {
  val eligibleContentTypes: Set[`Content-Type`] = Seq(
    MediaType.image.jpeg,
    MediaType.image.png,
    MediaType.image.bmp,
    MediaType.image.gif,
    MediaType.image.tiff
  ).map(`Content-Type`(_)).toSet
}
