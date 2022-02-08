package modules

import cats.effect.kernel.Temporal
import config.data.SessionExpiration
import dev.profunktor.redis4cats.RedisCommands
import domain.Scenario
import doobie.hikari.HikariTransactor
import services.{HealthCheck, ScenarioData, TemporalData}


object Services {
  def make[F[_]: Temporal](
    redis: RedisCommands[F, String, String],
    postgres: HikariTransactor[F],
    sessionExpiration: SessionExpiration,
    scenario: F[Scenario]
  ): Services[F] = {
    new Services[F](
      //persistData = PersistData.make[F](postgres),
      scenario = ScenarioData.make[F](scenario),
      temporalData = TemporalData.make[F](redis, sessionExpiration),
      healthCheck = HealthCheck.make[F](postgres, redis)
    ) {}
  }
}

sealed abstract class Services[F[_]] private (
  //val persistData: PersistData[F],
  val scenario: ScenarioData[F],
  val temporalData: TemporalData[F],
  val healthCheck: HealthCheck[F]
)
