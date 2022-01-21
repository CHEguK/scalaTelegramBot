package resources

import config.data.{AppConfig, PostgreSQLConfig}
import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.hikari._
import org.typelevel.log4cats.Logger


sealed abstract class AppResources[F[_]](
  val postgres: HikariTransactor[F]
)

object AppResources {

  def make[F[_]: Async: Logger](
    cfg: AppConfig
  ): Resource[F, AppResources[F]] = {

    def checkPostgresConnection(
      postgres: HikariTransactor[F]
    ): F[Unit] =
      sql"SELECT version();".query[String].unique.transact(postgres).flatMap { v =>
        Logger[F].info(s"Connected to $v")
      }

    def mkPostgreSqlResource(cfg: PostgreSQLConfig): Resource[F, HikariTransactor[F]] =
      (for {
        ce <- ExecutionContexts.fixedThreadPool[F](32) // our connect EC
        xa <- HikariTransactor.newHikariTransactor[F](
          cfg.host.value,
          cfg.url.value,
          cfg.user.value,
          cfg.password.value.value,
          ce
        )
      } yield xa
      ).evalTap(b => checkPostgresConnection(b))


    mkPostgreSqlResource(cfg.postgreSQL).map(new AppResources(_) {})

  }

}
