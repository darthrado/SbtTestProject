package org.sbttest.booksservice
package config

import cats.effect.{IO, Resource}
import io.circe.Decoder
import io.circe.config.parser

object Configuration {
  def get[A](path: String)(implicit decoder: Decoder[A]): Resource[IO, A] = Resource.eval(parser.decodePathF[IO, A](path))
}
