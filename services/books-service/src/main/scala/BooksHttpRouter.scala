package org.sbttest.booksservice

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.dsl.io._
import Book.bookCodec

class BooksHttpRouter(booksRetrievalService: BooksRetrievalService) {
    implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global

    object QueryParamBookIdMather extends QueryParamDecoderMatcher[Int]("id")

    def booksRoute: HttpRoutes[IO] = HttpRoutes.of[IO] {
      case GET -> Root / "books" / name => Ok(booksRetrievalService.getBookByName(name))
      case GET -> Root / "books" :? QueryParamBookIdMather(bookId) => Ok(booksRetrievalService.getBookById(bookId))
      case GET -> Root / "books" => Ok(booksRetrievalService.getAllBooks)
    }

    def httpApp: HttpApp[IO] = booksRoute.orNotFound

}
