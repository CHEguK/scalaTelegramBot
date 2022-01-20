import cats.effect._
import modules.Bot
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.Slf4jLogger
import cats.effect.implicits._
import cats.syntax._



object Main extends IOApp {

  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  override def run(args: List[String]): IO[ExitCode] = {
    config.loader[IO].flatMap { cfg =>
      Logger[IO].info(s"Loaded config $cfg")
      for {
        _ <- new Bot[IO](cfg.token.value.value.value).startPolling()
      } yield ExitCode.Success
    }
  }
}
