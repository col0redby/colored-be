package com.colored.be.models.exceptions

sealed abstract class ColoredException (msg: String) extends Exception

case class ImageNotFound() extends ColoredException("Image not found.")
