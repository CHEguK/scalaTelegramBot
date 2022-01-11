package config

import enumeratum._
import enumeratum.EnumEntry.Lowercase

object environments {

  sealed abstract class AppEnvironment extends EnumEntry with Lowercase

  object AppEnvironment extends Enum[AppEnvironment] {
    final case object Test extends AppEnvironment
    final case object Prod extends AppEnvironment

    val values = findValues
  }
}
