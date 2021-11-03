package org.sbttest.booksservice
package repo

import dto.Book

import org.scanamo.query.UniqueKey
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import org.scanamo.syntax._
import org.scanamo.generic.auto._

class BookRepo(db: DynamoDbAsyncClient) extends GenericCrudRepo[Book](db,"books")//(Book.bookFormat)

object BookRepo{
  def matchByName(name:String): UniqueKey[_] = "name" === name
}