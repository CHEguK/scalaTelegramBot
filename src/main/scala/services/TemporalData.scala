package services

import cats.MonadThrow
import cats.effect._
import cats.implicits._
import com.bot4s.telegram.models.User
import config.data.SessionExpiration
import dev.profunktor.redis4cats.RedisCommands
import domain.Point
import domain.session.{LastPointId, Session, UserId}

trait TemporalData[F[_]] {
  def init(user: User): F[Unit]
  def checkInitSession(user: User): F[Boolean]
  def savePoint(user: User, step: Point): F[Unit]
  def getLastPoint(user: User): F[Session]
  def exist(user: User): F[Boolean]
}


object TemporalData {
  def make[F[_]: MonadThrow](
    redis: RedisCommands[F, String, String],
    exp: SessionExpiration
  ): TemporalData[F] =
    new TemporalData[F] {
      def checkInitSession(user: User): F[Boolean] =
        redis.exists(user.id.toString)

      def savePoint(user: User, step: Point): F[Unit] =
        redis.hSet(user.id.show, "lastStep", step.toString) *>
          redis.expire(user.id.show, exp.value).void

      def getLastPoint(user: User): F[Session] = {
        redis.hGet(user.id.toString, "lastStep")
          .map(a => Session(UserId(user.id.show.toLong), LastPointId(a.getOrElse(""))))
      }

      def init(user: User): F[Unit] =
        redis.hSet(user.id.show, "lastStep", "init") *>
          redis.expire(user.id.show, exp.value).void

      def exist(user: User): F[Boolean] =
        redis.exists(user.id.show)
    }
}