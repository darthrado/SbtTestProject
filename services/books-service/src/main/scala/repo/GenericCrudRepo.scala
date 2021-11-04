package org.sbttest.booksservice
package repo

import cats.effect.IO
import org.scanamo.query.UniqueKey
import org.scanamo.{DynamoFormat, DynamoReadError, ScanamoCats, Table}
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient

class GenericCrudRepo[A](val db: DynamoDbAsyncClient, val tableName: String)(implicit val tableFormat: DynamoFormat[A]) {
  private val scanamo = ScanamoCats[IO](db)
  private val table = Table[A](tableName)

  def getAll: IO[List[Either[DynamoReadError, A]]] = scanamo.exec( table.scan() )
  def get(key: UniqueKey[_]): IO[Option[Either[DynamoReadError, A]]] = scanamo.exec( table.get(key))
  def save(entity: A): IO[Unit] = scanamo.exec( table.put(entity) )
  def delete(key: UniqueKey[_]): IO[Unit] = scanamo.exec( table.delete(key))
}