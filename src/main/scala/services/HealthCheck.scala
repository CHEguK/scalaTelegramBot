package services

import cats.effect.implicits._
import cats.effect.kernel.Temporal
import cats.implicits._
import dev.profunktor.redis4cats.RedisCommands
import domain.healthcheck._
import doobie.hikari.HikariTransactor
import doobie.implicits.toSqlInterpolator
import doobie.implicits._

import scala.concurrent.duration.DurationInt


trait HealthCheck[F[_]] {
  def status: F[AppStatus]
}

object HealthCheck {
  def make[F[_]: Temporal](
    postgres: HikariTransactor[F],
    redis: RedisCommands[F, String, String]
  ): HealthCheck[F] =
    new HealthCheck[F] {

      val q: doobie.ConnectionIO[List[Int]] = sql"SELECT pid FROM pg_stat_activity;".query[Int].to[List]

      val postgresHealth: F[PostgresStatus] =
        q.transact(postgres)
          .map(_.nonEmpty)
          .timeout(1.second)
          .map(Status._Bool.reverseGet)
          .orElse(Status.Unreachable.pure[F].widen)
          .map(PostgresStatus.apply)

      val redisHealth: F[RedisStatus] =
        redis.ping
          .map(_.nonEmpty)
          .timeout(1.second)
          .map(Status._Bool.reverseGet)
          .orElse(Status.Unreachable.pure[F].widen)
          .map(RedisStatus.apply)


      val status: F[AppStatus] =
        (redisHealth, postgresHealth).parMapN(AppStatus.apply)
    }
}
