package org.sbttest.booksservice

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec


final case class Author(name: String)
final case class Book(name: String, genre: String, author: Author)

object Author{
  implicit val authorCodec: Codec[Author] = deriveCodec[Author]
}
object Book{
  implicit val bookCodec: Codec[Book] = deriveCodec[Book]
}

class BooksDatabase {
  private val db = Map(
    1 -> Book("Game of Thrones", "fantasy",Author("George Martin")),
    2 -> Book("Sherlock Holmes", "mystery", Author("Conan Doyle")),
    3 -> Book("Harry Potter", "fantasy", Author("J.K. Rowling")),
    4 -> Book("Lord of the Rings", "fantasy", Author("J.R.R. Tolkien"))
  )

  def getAll: Iterable[Book] = db.values
  def get(id: Int): Option[Book] = db.get(id)

}

object BooksDatabase {
  def apply = new BooksDatabase
}
