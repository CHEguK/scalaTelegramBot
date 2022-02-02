import cats.effect._
import config.data.AppConfig
import modules.{Bot, Services}
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.Slf4jLogger
import domain.Scenario
import io.circe.yaml.parser
import resources.AppResources
import dev.profunktor.redis4cats.log4cats._
import eu.timepit.refined.auto._

import scala.io.BufferedSource


object Main extends IOApp {

  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  val sourceIO: IO[BufferedSource] = IO(scala.io.Source.fromFile("/home/azdrogov/scalaTelegramBot/test.yaml"))

  override def run(args: List[String]): IO[ExitCode] = {
    config.loader[IO].flatMap { cfg =>
      Logger[IO].info(s"Loaded config $cfg")
      Resource.fromAutoCloseable(sourceIO).use { source =>
        AppResources
          .make[IO](cfg)
          .evalMap { res =>
            val scenario = Sync[IO].fromEither(parser.parse(source.mkString).flatMap(_.as[Scenario]))
            val services = Services.make[IO](res.redis, res.postgres, cfg.sessionExpiration)
            new Bot[IO](cfg.token.value.value, services, scenario).startPolling()
          }.useForever
      }
    }
  }
}
