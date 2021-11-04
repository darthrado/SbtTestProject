package org.sbttest.booksservice

import dto.Book
import repo.BookRepo

import cats.effect.IO
import org.scanamo.DynamoReadError

class BooksRetrievalService(booksDatabase: BookRepo) {
  def getAllBooks: IO[Either[DynamoReadError,List[Book]]] = booksDatabase.getAll.map(zip)

  def getBookByName(name: String): IO[Option[Either[DynamoReadError, Book]]] = booksDatabase.get(BookRepo.matchByName(name))

  def saveBook(book: Book): IO[Unit] = booksDatabase.save(book)

  def deleteBook(name: String): IO[Unit] = booksDatabase.delete(BookRepo.matchByName(name))

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
