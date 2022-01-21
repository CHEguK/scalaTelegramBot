import cats.effect._
import config.data.AppConfig
import modules.Bot
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.Slf4jLogger
import domain.Scenario
import io.circe.yaml.parser
import resources.AppResources

import scala.io.BufferedSource


object Main extends IOApp {

  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  val sourceIO: IO[BufferedSource] = IO(scala.io.Source.fromFile("/home/azdrogov/scalaTelegramBot/test.yaml"))

  override def run(args: List[String]): IO[ExitCode] = {
    config.loader[IO].flatMap { cfg =>
      Logger[IO].info(s"Loaded config $cfg")
      AppResources
        .make[IO](cfg)
        .evalMap { res =>
          IO(println(res.postgres))
          IO(ExitCode.Success)
/*          Resource.fromAutoCloseable(sourceIO).use { source =>
            for {
              scenario <- parser.parse(source.mkString).flatMap(_.as[Scenario])
              _ <- new Bot[IO](cfg.token.value.value.value).startPolling()
            } yield ExitCode.Success
          }*/
        }.useForever
    }
  }
}
