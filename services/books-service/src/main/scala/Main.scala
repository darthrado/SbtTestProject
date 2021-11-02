package org.sbttest.booksservice

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.blaze.server.BlazeServerBuilder
import org.sbttest.booksservice.config.{BookServiceConfig, Configuration, ServerConfig}

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  private val app = for {
    bookServiceConfig <- Configuration.get("bookService")[BookServiceConfig]
    serverConfig <- Configuration.get("server")[ServerConfig]

    dbClient = new LocalDynamoDB(
      bookServiceConfig.dynamo.url,
      bookServiceConfig.dynamo.user,
      bookServiceConfig.dynamo.pass).client(bookServiceConfig.dynamo.port)
    db = BooksDatabase(dbClient)
    brs = new BooksRetrievalService(db)
    booksRouter = new BooksHttpRouter(brs)


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
