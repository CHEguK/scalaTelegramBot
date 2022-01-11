package config

import cats.effect._
import cats.syntax.all._
import ciris.ConfigValue
import config.data.AppConfig
import config.environments.AppEnvironment
import config.environments.AppEnvironment.{Prod, Test}
import eu.timepit.refined.types.string.NonEmptyString

import scala.sys.env

object loader {

  def apply[F[_]: Async: ContextShift]: F[AppConfig] =
    env("SC_APP_ENV")
      .as[AppEnvironment]
      .flatMap {
        case Test =>
          default()
        case Prod =>
          default()
      }
      .load[F]

  private def default(): ConfigValue[AppConfig] =
    (
      env("TELEGRAM_TOKEN").as[NonEmptyString].secret
    ).parMapN { (token) =>
      AppConfig(
        token
      )

    }
}
