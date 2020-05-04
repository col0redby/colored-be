package com.colored.be.models.dto

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class ImageMetadataDto (
    exposureTimeDescription: Option[String],
    exposureTimeInverse: Option[Int],
    isoDescription: Option[String],
    iso: Option[Int],
    apertureDescription: Option[String],
    aperture: Option[Float],
    gpsLatitudeDescription: Option[String],
    gpsLatitude: Option[Double],
    gpsLongitudeDescription: Option[String],
    gpsLongitude: Option[Double],
    gpsAltitudeDescription: Option[String],
    gpsAltitudeMeters: Option[Float]
)

object ImageMetadataDto {
  implicit val metadataDtoEncoder: Encoder[ImageMetadataDto] = deriveEncoder[ImageMetadataDto]
}
