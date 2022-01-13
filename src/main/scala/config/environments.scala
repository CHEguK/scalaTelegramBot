package config

import enumeratum._
import enumeratum.EnumEntry._


object environments {

  sealed trait AppEnvironment extends EnumEntry with Lowercase

  object AppEnvironment extends Enum[AppEnvironment] with CirisEnum[AppEnvironment] {
    final case object Test extends AppEnvironment
    final case object Prod extends AppEnvironment

    val values = findValues
  }
}
