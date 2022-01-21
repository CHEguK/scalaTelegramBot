package domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.generic.auto._

case class Command(id: String, text: String, to: String)

object Command {
  implicit val commandDecoder: Decoder[Command] = deriveDecoder[Command]
  implicit val commandEncoder: Encoder[Command] = deriveEncoder[Command]
}

case class Commands(
  rest: Command,
  comeback: Command,
  skip: Command
)

object Commands {
  implicit val commandsDecoder: Decoder[Commands] = deriveDecoder[Commands]
  implicit val commandsEncoder: Encoder[Commands] = deriveEncoder[Commands]
}

case class PossibleMessage(text: String)
case class Answer(text: String, next: String)
case class Point(id: String, name: String, message: List[PossibleMessage], answer: Option[List[Answer]])
case class Script(
  init: Point,
  upload_photo: Point,
  uploaded: Point,
  goodbye: Point
)

case class Scenario(commands: Commands, script: Script)

object Scenario {
  implicit val scenarioDecoder: Decoder[Scenario] = deriveDecoder[Scenario]
  implicit val scenarioEncoder: Encoder[Scenario] = deriveEncoder[Scenario]
}




