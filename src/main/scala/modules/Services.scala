package modules

import dev.profunktor.redis4cats.RedisCommands
import doobie.hikari.HikariTransactor


object Services {
  def make[F[_]](
    redis: RedisCommands[F, String, String],
    postgres: HikariTransactor[F]
  ): Services[F] = {
    new Services[F](
      history = ScriptHistory.make[F](redis),
      persistData = PersistData.make[F](postgres),
      temporalData = TemporalData.make[F](redis),
      healthCheck = HealthCheck.make[F](postgres, redis)
    ) {}
  }
}

sealed abstract class Services[F[_]] private (
  val history: ScriptHistory[F],
  val persistData: PersistData[F],
  val temporalData: TemporalData[F],
  val healthCheck: HealthCheck[F]
)
