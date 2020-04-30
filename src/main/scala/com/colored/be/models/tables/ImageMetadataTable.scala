package com.colored.be.models.tables

case class ImageMetadataTable(
    id: Int,
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
