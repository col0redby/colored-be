package com.colored.be

import java.io.File

import cats.effect.IOApp
import cats.effect.{ExitCode, IO}

object App extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    args match {
      case ::(name, ::(value, Nil)) if name == "--config-file" && exists(value) => HttpServer.create(new File(value))
      case Nil =>
        println("--config-file parameter must be specified, e.g. ~/colored-be/application-local.conf")
        IO.pure(ExitCode.Error)
    }
  }

  def exists(path: String): Boolean = new File(path).exists()
}
