package modules

import cats.effect.{Async, Concurrent}
import cats.implicits.toFunctorOps
import com.bot4s.telegram.api.declarative.Commands
import com.bot4s.telegram.cats.{Polling, TelegramBot}
import com.bot4s.telegram.methods._
import com.bot4s.telegram.models._
import org.asynchttpclient.Dsl.asyncHttpClient
import sttp.client3.asynchttpclient.cats.AsyncHttpClientCatsBackend


class Bot[F[_]: Async: Concurrent](token: String)
  extends TelegramBot[F](token, AsyncHttpClientCatsBackend.usingClient[F](asyncHttpClient()))
  with Polling[F]
  with Commands[F] {

  override def receiveMessage(msg: Message): F[Unit] = {
    msg.text.fold(unit) { text =>
      request(SendMessage(msg.source, text.reverse)).void
    }
  }
}
