package org.sbttest.booksservice

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.blaze.server.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val dbClient = LocalDynamoDB.client(8000)
    val db = BooksDatabase(dbClient)
    val brs = new BooksRetrievalService(db)
    val booksRouter = new BooksHttpRouter(brs)

    val appResource = for {

      server <- BlazeServerBuilder[IO](global)
        .bindHttp(8080, "localhost")
        .withHttpApp(booksRouter.httpApp)
        .resource
    } yield server

    appResource.use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
