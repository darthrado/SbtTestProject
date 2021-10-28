package org.sbttest.userservice

import cats.effect.IO
import org.http4s.Method.GET
import org.http4s.Request
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.client.Client
import org.http4s.implicits.http4sLiteralsSyntax

final case class BookError(e: Throwable) extends RuntimeException

class BookClient(client: Client[IO]) {

  def getBooks: IO[Iterable[Book]] =
    client.expect[Iterable[Book]](Request[IO](method = GET, uri = uri"http://localhost:8080/books"))
      .handleErrorWith(t => IO.raiseError(BookError(t)))

}
