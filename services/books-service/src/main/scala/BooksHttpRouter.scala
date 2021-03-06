package org.sbttest.booksservice

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import io.circe.generic.auto._
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.dsl.io._
import dto.Book

import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder

import scala.concurrent.Future

class BooksHttpRouter(booksRetrievalService: BooksRetrievalService) {
    implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global

    implicit class helperMethods[T](future: Future[T]){
      def toIO: IO[T] = IO.fromFuture(IO(future))
    }

    def booksRoute: HttpRoutes[IO] = HttpRoutes.of[IO] {
      case GET -> Root / "books" / name => booksRetrievalService.getBookByName(name).flatMap {
          case None => NotFound()
          case Some(Left(_)) => InternalServerError()
          case Some(Right(value)) => Ok(value)
        }

      case GET -> Root / "books" =>
        booksRetrievalService.getAllBooks.flatMap {
          case Right(value) => Ok(value)
          case Left(_) => InternalServerError()
        }

      case req @ POST -> Root / "books" =>
        req.as[Book].flatMap {
          booksRetrievalService.saveBook(_).flatMap(_ => Accepted())
        }

      case req @ PUT -> Root / "books" / name =>
        req.as[Book].flatMap {
          booksRetrievalService.saveBook(_).flatMap(_ => Accepted())
        }

      case DELETE -> Root / "books" / name =>
        booksRetrievalService.deleteBook(name).flatMap(_ => Accepted())
    }

    def httpApp: HttpApp[IO] = booksRoute.orNotFound

}
