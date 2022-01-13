package config

import cats.effect.Async
import cats.implicits._
import ciris._
import ciris.refined._
import config.data.{AppConfig, TelegramToken}
import config.environments.AppEnvironment
import config.environments.AppEnvironment.{Prod, Test}
import eu.timepit.refined.cats._
import eu.timepit.refined.types.string.NonEmptyString


object loader {

  def apply[F[_]: Async]: F[AppConfig] = {
    env("SC_APP_ENV")
      .as[AppEnvironment]
      .flatMap {
        case Test =>
          default[F]
        case Prod =>
          default[F]
      }
    default[F].load[F]
  }

  private def default[F[_]]: ConfigValue[F, AppConfig] = {
    env("TELEGRAM_TOKEN").as[NonEmptyString].secret.map(a => AppConfig(TelegramToken(a)))
  }

}
