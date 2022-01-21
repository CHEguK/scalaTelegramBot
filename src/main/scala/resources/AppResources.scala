package resources

import config.data.{AppConfig, PostgreSQLConfig, RedisConfig}
import cats.effect._
import cats.implicits._
import eu.timepit.refined.auto._
import dev.profunktor.redis4cats.effect.MkRedis
import dev.profunktor.redis4cats.{Redis, RedisCommands}
import doobie._
import doobie.implicits._
import doobie.hikari._
import org.typelevel.log4cats.Logger


sealed abstract class AppResources[F[_]](
  val postgres: HikariTransactor[F],
  val redis: RedisCommands[F, String, String]
)

object AppResources {

  def make[F[_]: Async: Logger: MkRedis](
    cfg: AppConfig
  ): Resource[F, AppResources[F]] = {

    def checkRedisConnection(
      redis: RedisCommands[F, String, String]
    ): F[Unit] =
      redis.info.flatMap {
        _.get("redis_version").traverse_ { v =>
          Logger[F].info(s"Connected to Redis $v")
        }
      }

    def checkPostgresConnection(
      postgres: HikariTransactor[F]
    ): F[Unit] =
      sql"SELECT version();".query[String].unique.transact(postgres).flatMap { v =>
        Logger[F].info(s"Connected to $v")
      }

    def mkRedisResource(cfg: RedisConfig): Resource[F, RedisCommands[F, String, String]] =
      Redis[F].utf8(cfg.uri.value).evalTap(checkRedisConnection)

    def mkPostgreSqlResource(cfg: PostgreSQLConfig): Resource[F, HikariTransactor[F]] =
      (for {
        ce <- ExecutionContexts.fixedThreadPool[F](32) // our connect EC
        xa <- HikariTransactor.newHikariTransactor[F](
          cfg.host.value,
          cfg.url.value,
          cfg.user.value,
          cfg.password.value,
          ce
        )
      } yield xa
      ).evalTap(b => checkPostgresConnection(b))


    (
      mkPostgreSqlResource(cfg.postgreSQL),
      mkRedisResource(cfg.redis)
    ).parMapN(new AppResources(_, _) {})

  }

}
