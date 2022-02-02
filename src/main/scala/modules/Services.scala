package modules

import cats.effect.kernel.Temporal
import config.data.SessionExpiration
import dev.profunktor.redis4cats.RedisCommands
import doobie.hikari.HikariTransactor
import services.{HealthCheck, TemporalData}


object Services {
  def make[F[_]: Temporal](
    redis: RedisCommands[F, String, String],
    postgres: HikariTransactor[F],
    sessionExpiration: SessionExpiration
  ): Services[F] = {
    new Services[F](
      //persistData = PersistData.make[F](postgres),
      temporalData = TemporalData.make[F](redis, sessionExpiration),
      healthCheck = HealthCheck.make[F](postgres, redis)
    ) {}
  }
}

sealed abstract class Services[F[_]] private (
  //val persistData: PersistData[F],
  val temporalData: TemporalData[F],
  val healthCheck: HealthCheck[F]
)
