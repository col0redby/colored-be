package com.colored.be.models.queue_requests

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class MetadataRequest (
  imageId: Int,
  originalBucket: String,
  originalKey: String
)

object MetadataRequest {
  implicit val metadataRequestEncoder: Encoder[MetadataRequest] = deriveEncoder[MetadataRequest]
}
