package config

import scala.concurrent.duration._

import cats.effect.Async
import cats.implicits._
import ciris._
import ciris.refined._
import config.data.{AppConfig, PostgreSQLConfig, RedisConfig, RedisURI, SessionExpiration, TelegramToken}
import config.environments.AppEnvironment
import config.environments.AppEnvironment.{Prod, Test}
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import eu.timepit.refined.types.string.NonEmptyString


object loader {

  def apply[F[_]: Async]: F[AppConfig] = {
    env("SC_APP_ENV")
      .as[AppEnvironment]
      .flatMap {
        case Test =>
          default[F](
            RedisURI("redis://localhost"),
          )
        case Prod =>
          default[F](
            RedisURI("redis://localhost"),
          )
      }.load[F]
  }

  private def default[F[_]](
    redisUri: RedisURI,
  ): ConfigValue[F, AppConfig] = {
    (
      env("TELEGRAM_TOKEN").as[NonEmptyString].secret,
      env("POSTGRES_HOST").as[NonEmptyString],
      env("POSTGRES_URL").as[NonEmptyString],
      env("POSTGRES_USER").as[NonEmptyString],
      env("POSTGRES_PASSWORD").as[NonEmptyString].secret
    ).parMapN { (token, pg_host, pg_url, pg_user, pg_password) =>
      AppConfig(
        TelegramToken(token),
        PostgreSQLConfig(
          pg_host,
          pg_url,
          pg_user,
          pg_password
        ),
        RedisConfig(redisUri),
        SessionExpiration(24.hours)
      )
    }
  }

}
