package org.sbttest.booksservice
package config

import cats.effect.{IO, Resource}
import io.circe.config.parser
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec

object Configuration {
  def get[A](path: String) = Resource.eval(parser.decodePathF[IO, A](path))
}
