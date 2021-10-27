package org.sbttest.booksservice

class BooksRetrievalService(booksDatabase: BooksDatabase) {
  def getAllBooks: Iterable[Book] = booksDatabase.getAll
  def getBookByName(name: String): Option[Book] =
    booksDatabase.getAll.collectFirst( { case book if book.name == name => book } )
  def getBookById(id: Int): Option[Book] = booksDatabase.get(id)
}
