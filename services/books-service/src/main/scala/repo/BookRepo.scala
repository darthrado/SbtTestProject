package org.sbttest.booksservice
package repo

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import org.scanamo.query.{KeyEquals, UniqueKey}
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import org.scanamo.syntax._

final case class Author(name: String)
final case class Book(name: String, genre: String, author: Author)

object Author{
  implicit val authorCodec: Codec[Author] = deriveCodec[Author]
}
object Book{
  implicit val bookCodec: Codec[Book] = deriveCodec[Book]
}

class BookRepo(db: DynamoDbAsyncClient) extends GenericCrudRepo[Book](db,"books")

object BookRepo{
  def matchByName(name:String): UniqueKey[_] = "name" === name
}