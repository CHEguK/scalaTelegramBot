package config

import ciris._
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype

object data {

  @newtype case class TelegramToken(value: Secret[NonEmptyString])

  case class AppConfig(
    token: TelegramToken,
    postgreSQL: PostgreSQLConfig,
    redis: RedisConfig
  )

  case class PostgreSQLConfig(
    host: NonEmptyString,
    url: NonEmptyString,
    user: NonEmptyString,
    password: Secret[NonEmptyString]
  )

  @newtype case class RedisURI(value: NonEmptyString)
  @newtype case class RedisConfig(uri: RedisURI)
}
