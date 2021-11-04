package org.sbttest.booksservice
package repo

import dto.Book

import cats.effect.IO
import org.scanamo.syntax._
import org.scanamo.{DynamoReadError, ScanamoCats, Table}
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import org.scanamo.generic.auto._

import scala.concurrent.Future

class BooksDatabaseOldVersion(db: DynamoDbAsyncClient) {
  private val scanamo = ScanamoCats[IO](db)
  private val books = Table[Book]("books")

  implicit class helperMethods[T](future: Future[T]){
    def toIO: IO[T] = IO.fromFuture(IO(future))
  }

  def getAll: IO[List[Either[DynamoReadError, Book]]] = scanamo.exec( books.scan() )
  def get(name: String): IO[Option[Either[DynamoReadError, Book]]] = scanamo.exec( books.get("name" === name))
  def save(book: Book): IO[Unit] = scanamo.exec( books.put(book) )
  def delete(name: String): IO[Unit] = scanamo.exec( books.delete("name" === name))

}

object BooksDatabaseOldVersion {
  def apply(db: DynamoDbAsyncClient) = new BooksDatabaseOldVersion(db)
}
