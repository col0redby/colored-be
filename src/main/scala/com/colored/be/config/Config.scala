package com.colored.be.config

import java.io.File

import cats.effect.{Blocker, ContextShift, IO, Resource}
import com.typesafe.config.ConfigFactory
import pureconfig._
import pureconfig.generic.ProductHint
import pureconfig.module.catseffect.syntax._
import pureconfig.generic.auto._

final case class ServerConfig(host: String, port: Int)

final case class DatabaseConfig(
    driver: String,
    url: String,
    user: String,
    password: String,
    threadPoolSize: Int
)

final case class Aws(
    s3: S3Config,
    credentialsProvider: CredentialsProvider
)

final case class S3Config(
    bucket: String,
    imageOriginalFolder: String
)

final case class CredentialsProvider(
    profile: String,
    region: String,
    config: String,
    credentials: String
)

case class RabbitMQConfig(
    connection: RabbitMQConnection,
    queues: Queues
)

case class RabbitMQConnection(
    host: String,
    port: Int
)

case class Queues(
    resize: Queue,
    metadata: Queue,
    colors: Queue
)

case class Queue(
    name: String
)

case class Config(
    server: ServerConfig,
    database: DatabaseConfig,
    aws: Aws,
    rabbitMQ: RabbitMQConfig
)

object Config {

  implicit def hint[T]: ProductHint[T] =
    ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))

  def load(
      configFile: File
  )(implicit cs: ContextShift[IO]): Resource[IO, Config] = {
    Blocker[IO].flatMap { blocker =>
      Resource.liftF(
        ConfigSource
          .fromConfig(ConfigFactory.parseFile(configFile))
          .loadF[IO, Config](blocker)
      )
    }
  }
}
