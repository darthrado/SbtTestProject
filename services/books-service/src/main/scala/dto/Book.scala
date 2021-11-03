package org.sbttest.booksservice
package dto

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import org.scanamo.DynamoFormat
import org.scanamo.generic.semiauto.deriveDynamoFormat

final case class Author(name: String)
final case class Book(name: String, genre: String, author: Author)

object Author{
  implicit val authorCodec: Codec[Author] = deriveCodec[Author]
}
object Book{
  implicit val bookCodec: Codec[Book] = deriveCodec[Book]
}