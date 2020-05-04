package com.colored.be.models.queue_requests

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class ResizeRequest (
  imageId: Int,
  originalBucket: String,
  originalKey: String,
  filename: String,
  targetBucket: String,
  targetKeyPrefix: String
)

object ResizeRequest {
  implicit val requestEncoder: Encoder[ResizeRequest] = deriveEncoder[ResizeRequest]
}
