package org.sbttest.booksservice

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import org.scanamo.generic.auto.genericDerivedFormat
import org.scanamo.{DynamoReadError, ScanamoAsync, Table}
import org.scanamo.syntax._
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


final case class Author(name: String)
final case class Book(name: String, genre: String, author: Author)

object Author{
  implicit val authorCodec: Codec[Author] = deriveCodec[Author]
}
object Book{
  implicit val bookCodec: Codec[Book] = deriveCodec[Book]
}

class BooksDatabase(db: DynamoDbAsyncClient) {
  private val scanamo = ScanamoAsync(db)
  private val books = Table[Book]("books")


  def getAll: Future[List[Either[DynamoReadError, Book]]] = scanamo.exec( books.scan() )
  def get(name: String): Future[Option[Either[DynamoReadError, Book]]] = scanamo.exec( books.get("name" === name))

}

object BooksDatabase {
  def apply(db: DynamoDbAsyncClient) = new BooksDatabase(db)
}
