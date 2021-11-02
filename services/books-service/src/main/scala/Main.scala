package org.sbttest.booksservice

import config.{BookServiceConfig, Configuration, ServerConfig}
import repo.BookRepo

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.blaze.server.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  private val app = for {
    bookServiceConfig <- Configuration.get("bookService")[BookServiceConfig]
    serverConfig <- Configuration.get("server")[ServerConfig]

    dbClient = new DynamoDB(
      bookServiceConfig.dynamo.url,
      bookServiceConfig.dynamo.user,
      bookServiceConfig.dynamo.pass).client(bookServiceConfig.dynamo.port)

    dbRepo = new BookRepo(dbClient)
    booksRetrievalService = new BooksRetrievalService(dbRepo)
    booksRouter = new BooksHttpRouter(booksRetrievalService)


    server <- BlazeServerBuilder[IO](global)
      .bindHttp(serverConfig.port, serverConfig.host)
      .withHttpApp(booksRouter.httpApp)
      .resource
  } yield server

  override def run(args: List[String]): IO[ExitCode] = {
    app.use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
