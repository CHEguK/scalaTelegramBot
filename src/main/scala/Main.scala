import modules.Bot
import cats.effect.{ExitCode, IO, IOApp}
import com.bot4s.telegram.cats.TelegramBot

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- new Bot()
    } yield ExitCode.Success
}
