package org.sbttest.booksservice
package config

import cats.effect.{IO, Resource}
import io.circe.Codec
import io.circe.config.parser

object Configuration {
  def get[A](path: String)(implicit decoder: Codec[A]): Resource[IO, A] = Resource.eval(parser.decodePathF[IO, A](path))
}
