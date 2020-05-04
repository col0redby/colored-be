package com.colored.be.models.tables

case class ImageMetadataTable(
    imageId: Int,
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
