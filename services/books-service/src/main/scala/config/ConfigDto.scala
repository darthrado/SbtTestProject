package org.sbttest.booksservice
package config

import io.circe.Codec

case class BookServiceConfig(dynamo: DynamoConfig)
case class DynamoConfig(url: String, port: Int, user: String, pass: String)

case class ServerConfig(host: String, port: Int)