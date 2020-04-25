package com.colored.be.aws

import java.nio.file.Paths

import cats.effect.{IO, Resource}
import com.colored.be.config.Config
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.profiles.ProfileFile
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient

object AwsSdk {
  def s3AsyncClient(config: Config): Resource[IO, S3AsyncClient] = {
    Resource.liftF(
      IO.pure(
        S3AsyncClient
          .builder()
          .credentialsProvider(
            ProfileCredentialsProvider
              .builder()
              .profileFile(
                ProfileFile.aggregator()
                  .applyMutation(builder => builder.addFile(
                    ProfileFile.builder()
                      .content(Paths.get(config.aws.credentialsProvider.config))
                      .`type`(ProfileFile.Type.CONFIGURATION)
                      .build())
                  )
                  .applyMutation(builder => builder.addFile(
                    ProfileFile.builder()
                      .content(Paths.get(config.aws.credentialsProvider.credentials))
                      .`type`(ProfileFile.Type.CREDENTIALS)
                      .build())
                  )
                  .build()
              )
              .profileName(config.aws.credentialsProvider.profile)
              .build()
          )
          .region(Region.of(config.aws.credentialsProvider.region))
          .build()
      )
    )
  }
}
