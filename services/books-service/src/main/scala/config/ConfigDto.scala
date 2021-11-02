package org.sbttest.booksservice
package config

import io.circe.Codec

case class BookServiceConfig(dynamo: DynamoConfig)
case class DynamoConfig(url: String, port: Int, user: String, pass: String)

case class ServerConfig(host: String, port: Int)

object ConfigDto{
  import io.circe.generic.semiauto._

  implicit val bookServiceConfigCodec: Codec[BookServiceConfig] = deriveCodec
  implicit val dynamoConfigCodec: Codec[DynamoConfig] = deriveCodec
  implicit val serverConfigCodec: Codec[ServerConfig] = deriveCodec
}