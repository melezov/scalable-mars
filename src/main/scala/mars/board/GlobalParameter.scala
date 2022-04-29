package mars
package board

import mars.board.GlobalParameter.*
import zio.*

sealed abstract class GlobalParameter[T](steps: Range) {
  protected final def step: Int = steps.step
  protected def next: Int = current + step
  final def isMaxed: Boolean = current == steps.last

  def current: Int
  def advance(): IO[GlobalParameter.Error, (T, Seq[ActionBonus])]
}
object GlobalParameter {
  sealed trait Error extends MarsError

  final class OxygenTrack private(override val current: Int)
      extends GlobalParameter[OxygenTrack](OxygenTrack.Steps) {
    override def advance(): IO[OxygenTrack.OxygenMaxed.type, (OxygenTrack, Seq[ActionBonus])] =
      next match {
        case _ if isMaxed =>
          IO.fail(OxygenTrack.OxygenMaxed)
        case no @ OxygenTrack.TemperatureSynergy =>
          IO.succeed(OxygenTrack(no), Seq(ActionBonus.IncreaseTerraformRating, ActionBonus.BonusTemperature))
        case no =>
          IO.succeed(OxygenTrack(no), Seq(ActionBonus.IncreaseTerraformRating))
      }
  }
  object OxygenTrack {
    case object OxygenMaxed extends GlobalParameter.Error
    private val Steps: Range = 0 to 13 by 1
    private val TemperatureSynergy = 8
    val Start: OxygenTrack = OxygenTrack(Steps.head)
  }

  final class TemperatureTrack private(override val current: Int)
      extends GlobalParameter[TemperatureTrack](TemperatureTrack.Steps) {
    override def advance(): IO[TemperatureTrack.TemperatureMaxed.type, (TemperatureTrack, Seq[ActionBonus])] =
      next match {
        case _ if isMaxed =>
          IO.fail(TemperatureTrack.TemperatureMaxed)
        case nt @ TemperatureTrack.OceanSynergy =>
          IO.succeed(TemperatureTrack(nt), Seq(ActionBonus.IncreaseTerraformRating, ActionBonus.BonusOcean))
        case nt if TemperatureTrack.HeatProductionSynergy(nt) =>
          IO.succeed(TemperatureTrack(nt), Seq(ActionBonus.IncreaseTerraformRating, ActionBonus.BonusHeatProduction))
        case nt =>
          IO.succeed(TemperatureTrack(nt), Seq(ActionBonus.IncreaseTerraformRating))
      }
  }
  object TemperatureTrack {
    case object TemperatureMaxed extends GlobalParameter.Error
    private val Steps: Range = -30 to 8 by 2
    private val OceanSynergy = 0
    private val HeatProductionSynergy = Set(-24, -20)
    val Start: TemperatureTrack = TemperatureTrack(Steps.head)
  }

  final class OceanTiles private(override val current: Int)
      extends GlobalParameter[OceanTiles](OceanTiles.Steps) {
    override def advance(): IO[OceanTiles.OceansMaxed.type, (OceanTiles, Seq[ActionBonus])] =
      next match {
        case _ if isMaxed =>
          IO.fail(OceanTiles.OceansMaxed)
        case no =>
          IO.succeed(OceanTiles(no), Seq(ActionBonus.IncreaseTerraformRating))
      }
  }
  object OceanTiles {
    case object OceansMaxed extends GlobalParameter.Error
    private val Steps: Range = 0 to 9 by 1
    val Start: OceanTiles = OceanTiles(Steps.head)
  }
}
final class GlobalParameters private(
  val oxygen: OxygenTrack,
  val temperature: TemperatureTrack,
  val oceans: OceanTiles,
) {
  private[this] def copy(
    oxygen: OxygenTrack = oxygen,
    temperature: TemperatureTrack = temperature,
    oceans: OceanTiles = oceans,
  ): GlobalParameters = GlobalParameters(
    oxygen = oxygen,
    temperature = temperature,
    oceans = oceans,
  )

  def increaseOxygen(): IO[GlobalParameter.Error, (GlobalParameters, Seq[ActionBonus])] =
    oxygen.advance() map { case (newOxygen, sab) =>
      (copy(oxygen = newOxygen), sab)
    }

  def increaseTemperature(): IO[GlobalParameter.Error, (GlobalParameters, Seq[ActionBonus])] =
    temperature.advance() map { case (newTemperature, sab) =>
      (copy(temperature = newTemperature), sab)
    }

  def placeOcean(): IO[GlobalParameter.Error, (GlobalParameters, Seq[ActionBonus])] =
    oceans.advance() map { case (newOceans, sab) =>
      (copy(oceans = newOceans), sab)
    }

  def areMaxed: Boolean = oxygen.isMaxed && temperature.isMaxed && oceans.isMaxed
}
object GlobalParameters {
  val Start: GlobalParameters = GlobalParameters(
    oxygen = OxygenTrack.Start,
    temperature = TemperatureTrack.Start,
    oceans = OceanTiles.Start,
  )
}
