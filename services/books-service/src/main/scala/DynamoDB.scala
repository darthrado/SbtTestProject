package org.sbttest.booksservice

import software.amazon.awssdk.auth.credentials.{ AwsBasicCredentials, StaticCredentialsProvider }
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb._
import software.amazon.awssdk.services.dynamodb.model._

import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.compat.java8.DurationConverters._

import java.net.URI
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient

//Copied from: scanamo test kit
class DynamoDB(val url: String, val username: String, val password: String) {
  def client(port: Int = 8042): DynamoDbAsyncClient =
    DynamoDbAsyncClient.builder
      .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(username, password)))
      .endpointOverride(URI.create(s"$url:$port"))
      .overrideConfiguration(
        ClientOverrideConfiguration.builder
          .apiCallAttemptTimeout(5.seconds.toJava)
          .apiCallTimeout(5.seconds.toJava)
          .build
      )
      .httpClient(NettyNioAsyncHttpClient.builder.build)
      .region(Region.EU_WEST_1)
      .build

  def syncClient(port: Int = 8042): DynamoDbClient =
    DynamoDbClient.builder
      .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(username, password)))
      .endpointOverride(URI.create(s"$url:$port"))
      .overrideConfiguration(
        ClientOverrideConfiguration.builder
          .apiCallAttemptTimeout(5.seconds.toJava)
          .apiCallTimeout(5.seconds.toJava)
          .build
      )
      .region(Region.EU_WEST_1)
      .build

}