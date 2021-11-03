package org.sbttest.booksservice
package repo

import dto.Book
import org.scanamo.syntax._
import org.scanamo.{DynamoReadError, ScanamoAsync, Table}
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import org.scanamo.generic.auto._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BooksDatabaseOldVersion(db: DynamoDbAsyncClient) {
  private val scanamo = ScanamoAsync(db)
  private val books = Table[Book]("books")


  def getAll: Future[List[Either[DynamoReadError, Book]]] = scanamo.exec( books.scan() )
  def get(name: String): Future[Option[Either[DynamoReadError, Book]]] = scanamo.exec( books.get("name" === name))
  def save(book: Book): Future[Unit] = scanamo.exec( books.put(book) )
  def delete(name: String): Future[Unit] = scanamo.exec( books.delete("name" === name))

}

object BooksDatabaseOldVersion {
  def apply(db: DynamoDbAsyncClient) = new BooksDatabaseOldVersion(db)
}
