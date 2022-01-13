import modules.Bot
import cats.effect.{ExitCode, IO, IOApp}


object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    config.loader[IO].flatMap { cfg =>
      for {
        _ <- new Bot[IO](cfg.token.value.value.value).startPolling()
      } yield ExitCode.Success
    }
  }
}
