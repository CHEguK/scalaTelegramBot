package domain

import io.estatico.newtype.macros.newtype

object session {

  @newtype
  case class UserId(value: Long)

  @newtype
  case class LastPointId(value: String)

  case class Session(userId: UserId, lastPointId: LastPointId)
}
