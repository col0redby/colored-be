package com.colored.be.models.queue_requests

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class ColorsRequest (
  imageId: Int,
  originalBucket: String,
  originalKey: String
)

object ColorsRequest {
  implicit val metadataRequestEncoder: Encoder[ColorsRequest] = deriveEncoder[ColorsRequest]
}
