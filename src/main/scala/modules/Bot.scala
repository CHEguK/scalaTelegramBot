package modules

import cats.effect.{Async, Concurrent}
import cats.implicits.toFunctorOps
import com.bot4s.telegram.api.declarative.Commands
import com.bot4s.telegram.cats.{Polling, TelegramBot}
import com.bot4s.telegram.methods._
import com.bot4s.telegram.models._
import domain.Scenario
import io.circe.yaml.parser
import org.asynchttpclient.Dsl.asyncHttpClient
import sttp.client3.asynchttpclient.cats.AsyncHttpClientCatsBackend

import scala.io.BufferedSource


class Bot[F[_]: Async: Concurrent](token: String, services: Services[F], scenarioIO:  F[Scenario])
  extends TelegramBot[F](token, AsyncHttpClientCatsBackend.usingClient[F](asyncHttpClient()))
  with Polling[F]
  with Commands[F] {

  override def receiveMessage(msg: Message): F[Unit] = {

    for {
      scenario <- scenarioIO
      script = scenario.script
      text = msg.text
/*      lastPoint = redis
      nextPointId = lastStep.nextPoint(text)
      nextPoint = script.steps(nextPoint)
      nextPoint.message
      _ = redis.saveLastPoint(nextPoint)*/

    } yield ()






/*    msg.text.fold(unit) { text =>
      request(SendMessage(msg.source, text.reverse)).void
    }*/
  }
}
