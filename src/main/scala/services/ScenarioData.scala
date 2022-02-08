package services

import cats.MonadThrow
import cats.implicits._
import domain.{Answer, Point, Scenario}

trait ScenarioData[F[_]] {
  def getPointById(stepId: String): F[Point]
  def getAnswer(text: String, stepId: String): F[List[Answer]]
}

object ScenarioData {
  def make[F[_]: MonadThrow](scenario: F[Scenario]): ScenarioData[F] =
    new ScenarioData[F] {
      def getPointById(stepId: String): F[Point] =
        scenario.map(_.script.steps(stepId))

      def getAnswer(text: String, stepId: String): F[List[Answer]] = {
        getPointById(stepId).map { p =>
          p.answer.getOrElse(List(Answer("", "init")))
        }

      }
    }
}
