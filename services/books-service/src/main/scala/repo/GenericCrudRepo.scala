package org.sbttest.booksservice
package repo

import org.scanamo.query.UniqueKey
import org.scanamo.{DynamoFormat, DynamoReadError, ScanamoAsync, Table}
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GenericCrudRepo[A](val db: DynamoDbAsyncClient, val tableName: String)(implicit val tableFormat: DynamoFormat[A]) {
  private val scanamo = ScanamoAsync(db)
  private val table = Table[A](tableName)

  def getAll: Future[List[Either[DynamoReadError, A]]] = scanamo.exec( table.scan() )
  def get(key: UniqueKey[_]): Future[Option[Either[DynamoReadError, A]]] = scanamo.exec( table.get(key))
  def save(entity: A): Future[Unit] = scanamo.exec( table.put(entity) )
  def delete(key: UniqueKey[_]): Future[Unit] = scanamo.exec( table.delete(key))
}