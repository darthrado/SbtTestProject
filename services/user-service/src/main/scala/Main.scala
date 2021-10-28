package org.sbttest.userservice

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.blaze.server.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {

    val appResource = for {
      client <- BlazeClientBuilder[IO](global).resource

      bookClient = new BookClient(client)
      httpRouter = new HttpRouter(bookClient)

      server <- BlazeServerBuilder[IO](global)
        .bindHttp(8081, "localhost")
        .withHttpApp(httpRouter.httpApp)
        .resource
    } yield (client,server)

    appResource.use(_ => IO.never)
      .as(ExitCode.Success)
  }
}