import cats.effect._
import modules.Bot
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.Slf4jLogger
import domain.Scenario
import io.circe.yaml.parser

import scala.io.BufferedSource


object Main extends IOApp {

  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  val res: IO[BufferedSource] = IO(scala.io.Source.fromFile("/home/azdrogov/scalaTelegramBot/test.yaml"))

  override def run(args: List[String]): IO[ExitCode] = {
    config.loader[IO].flatMap { cfg =>
      Logger[IO].info(s"Loaded config $cfg")

      Resource.fromAutoCloseable(res).use { source =>
        for {
          scenario <- parser.parse(source.mkString).flatMap(_.as[Scenario])
          _ <- new Bot[IO](cfg.token.value.value.value).startPolling()
        } yield ExitCode.Success
      }
    }
  }
}
