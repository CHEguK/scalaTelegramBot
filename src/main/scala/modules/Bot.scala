package modules

import cats.effect.kernel.Sync
import cats.effect.{Async, Concurrent}
import cats.implicits._
import com.bot4s.telegram.Implicits.toOption
import com.bot4s.telegram.api.declarative.Commands
import com.bot4s.telegram.cats.{Polling, TelegramBot}
import com.bot4s.telegram.methods.SendMessage
import com.bot4s.telegram.models._
import org.asynchttpclient.Dsl.asyncHttpClient
import sttp.client3.asynchttpclient.cats.AsyncHttpClientCatsBackend

import scala.util.control.NoStackTrace

case object NoUserError extends NoStackTrace

class Bot[F[_]: Async: Concurrent](token: String, services: Services[F])
  extends TelegramBot[F](token, AsyncHttpClientCatsBackend.usingClient[F](asyncHttpClient()))
  with Polling[F]
  with Commands[F] {

  override def receiveMessage(msg: Message): F[Unit] = {
    for {
      user <- Async[F].fromOption(msg.from, NoUserError)
      ex <- services.temporalData.exist(user)
      temporalData = services.temporalData
      _ <- Sync[F].whenA(!ex)(temporalData.init(user))
      lastPoint <- temporalData.getLastPoint(user)
      answers <- services.scenario.getAnswer(msg.text.getOrElse(""), lastPoint.lastPointId.value)
      nextPoint <- services.scenario.getPointById(answers.head.next)
      _ <- services.temporalData.savePoint(user, nextPoint)
      _ <- request(SendMessage(msg.source, answers.head.text)).void
    } yield ()

  }
}
