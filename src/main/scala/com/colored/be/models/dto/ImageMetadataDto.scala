package com.colored.be.models.dto

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class ImageMetadataDto (
    exposureTimeDescription: String,
    exposureTimeInverse: Int,
    isoDescription: String,
    iso: Int,
    apertureDescription: String,
    aperture: Float,
    gpsLatitudeDescription: String,
    gpsLatitude: Double,
    gpsLongitudeDescription: String,
    gpsLongitude: Double,
    gpsAltitudeDescription: String,
    gpsAltitudeMeters: Float
)

object ImageMetadataDto {
  implicit val metadataDtoEncoder: Encoder[ImageMetadataDto] = deriveEncoder[ImageMetadataDto]
}
