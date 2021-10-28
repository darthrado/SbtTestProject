package org.sbttest.userservice

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

final case class Book(name: String, genre: String)

object Book{
  implicit val bookCodec: Codec[Book] = deriveCodec[Book]
}

