package org.sbttest.userservice

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.dsl.io._
import org.http4s.implicits._


object HttpRouter {
  implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global

  def helloRoute: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name => Ok(s"Hello There $name")
  }

  def httpApp: HttpApp[IO] = helloRoute.orNotFound

}
