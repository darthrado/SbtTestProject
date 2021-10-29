package org.sbttest.booksservice

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.dsl.io._
import Book.bookCodec

import org.http4s.circe.CirceEntityCodec.circeEntityEncoder

import scala.concurrent.Future

class BooksHttpRouter(booksRetrievalService: BooksRetrievalService) {
    implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global

    object QueryParamBookIdMather extends QueryParamDecoderMatcher[Int]("id")

    implicit class helperMethods[T](future: Future[T]){
      def toIO: IO[T] = IO.fromFuture(IO(future))
    }

    def booksRoute: HttpRoutes[IO] = HttpRoutes.of[IO] {
      case GET -> Root / "books" / name =>
        val serviceCall = booksRetrievalService.getBookByName(name)
        IO.fromFuture(IO(serviceCall)).flatMap {
          case None => NotFound()
          case Some(Left(_)) => InternalServerError()
          case Some(Right(value)) => Ok(value)
        }

      case GET -> Root / "books" =>
        booksRetrievalService.getAllBooks.toIO.flatMap {
          case Right(value) => Ok(value)
          case Left(_) => InternalServerError()
        }

      case req @ POST -> Root / "books" =>
        println("Hello there")
        booksRetrievalService.saveBook(req.body.as[Book]).toIO.flatMap(_ => Accepted())

      case req @ PUT -> Root / "books" / name =>
        booksRetrievalService.saveBook(req.body.as[Book]).toIO.flatMap(_ => Accepted())

      case DELETE -> Root / "books" / name => ???
    }

    def httpApp: HttpApp[IO] = booksRoute.orNotFound


}
