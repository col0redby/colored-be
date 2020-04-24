package com.colored.be.config

import cats.effect.{Blocker, ContextShift, IO, Resource}
import com.typesafe.config.ConfigFactory
import pureconfig._
import pureconfig.generic.ProductHint
import pureconfig.module.catseffect.syntax._
import pureconfig.generic.auto._

case class ServerConfig(host: String, port: Int)

case class DatabaseConfig(
    driver: String,
    url: String,
    user: String,
    password: String,
    threadPoolSize: Int
)

final case class S3Config(
  bucket: String,
  imageOriginalFolder: String
)

case class Config(server: ServerConfig, database: DatabaseConfig, s3: S3Config)

object Config {

  implicit def hint[T]: ProductHint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))

  def load(
      configFile: String = "application.conf"
  )(implicit cs: ContextShift[IO]): Resource[IO, Config] = {
    Blocker[IO].flatMap { blocker =>
      Resource.liftF(
        ConfigSource
          .fromConfig(ConfigFactory.load(configFile))
          .loadF[IO, Config](blocker)
      )
    }
  }
}
