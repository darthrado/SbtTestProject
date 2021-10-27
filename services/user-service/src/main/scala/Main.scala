package org.sbttest.userservice

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.blaze.server.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {

    val appResource = for {
      server <- BlazeServerBuilder[IO](global)
        .bindHttp(8081, "localhost")
        .withHttpApp(HttpRouter.httpApp)
        .resource
    } yield server

    appResource.use(_ => IO.never)
      .as(ExitCode.Success)
  }
}