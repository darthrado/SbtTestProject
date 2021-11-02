package org.sbttest.booksservice

import org.sbttest.booksservice.repo.{Book, BookRepo}
import org.scanamo.DynamoReadError

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, blocking}

class BooksRetrievalService(booksDatabase: BookRepo) {
  def getAllBooks: Future[Either[DynamoReadError,List[Book]]] = booksDatabase.getAll.map(zip)

  def getBookByName(name: String): Future[Option[Either[DynamoReadError, Book]]] = booksDatabase.get(BookRepo.matchByName(name))

  def saveBook(book: Book): Future[Unit] = booksDatabase.save(book)

  def deleteBook(name: String): Future[Unit] = booksDatabase.delete(BookRepo.matchByName(name))

  private val emptyListOfBooks: Either[DynamoReadError,List[Book]] = Right(List.empty)

  private def zip(a: List[Either[DynamoReadError,Book]]) =
    a.foldRight(emptyListOfBooks)(
      (elem,acc) => (acc,elem) match {
        case (Right(list),Right(value)) => Right(value :: list)
        case (Left(error),_) => Left(error)
        case (_,Left(error)) => Left(error)
      }
  )
}
