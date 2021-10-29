package org.sbttest.booksservice

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.dsl.io._
import Book.bookCodec

import org.http4s.circe.CirceEntityCodec.circeEntityEncoder

class BooksHttpRouter(booksRetrievalService: BooksRetrievalService) {
    implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global

    object QueryParamBookIdMather extends QueryParamDecoderMatcher[Int]("id")

    def booksRoute: HttpRoutes[IO] = HttpRoutes.of[IO] {
      case GET -> Root / "books" / name =>
        val serviceCall = booksRetrievalService.getBookByName(name)
        IO.fromFuture(IO(serviceCall)).flatMap {
          case None => NotFound()
          case Some(Left(error)) => InternalServerError()
          case Some(Right(value)) => Ok(value)
        }

        case GET -> Root / "books" =>
          val serviceCall = booksRetrievalService.getAllBooks
          IO.fromFuture(IO(serviceCall)).flatMap {
            case Right(value) => Ok(value)
            case Left(_) => InternalServerError()
          }

    }

    def httpApp: HttpApp[IO] = booksRoute.orNotFound

}
