package mars
package board

sealed abstract class GlobalParameter[T](steps: Range) {
  protected final def step: Int = steps.step
  protected def next: Int = current + step
  final def isMaxed: Boolean = current == steps.last

  def current: Int
  def advance(): IO[GlobalParameter.Err, (T, Seq[ActionBonus])]
}
object GlobalParameter {
  sealed trait Err extends MarsErr
  object Err {
    case object OxygenMaxed extends Err
    case object TemperatureMaxed extends Err
    case object OceansMaxed extends Err
  }

  final class OxygenTrack private(override val current: Int)
      extends GlobalParameter[OxygenTrack](OxygenTrack.Steps) {
    override def advance(): IO[Err, (OxygenTrack, Seq[ActionBonus])] =
      next match {
        case _ if isMaxed =>
          IO.fail(Err.OxygenMaxed)
        case no @ OxygenTrack.TemperatureSynergy =>
          IO.succeed(OxygenTrack(no), Seq(ActionBonus.IncreaseTerraformRating, ActionBonus.BonusTemperature))
        case no =>
          IO.succeed(OxygenTrack(no), Seq(ActionBonus.IncreaseTerraformRating))
      }
  }
  object OxygenTrack {
    private val Steps: Range = 0 to 13 by 1
    private val TemperatureSynergy = 8
    val Start: OxygenTrack = OxygenTrack(Steps.head)
  }

  final class TemperatureTrack private(override val current: Int)
      extends GlobalParameter[TemperatureTrack](TemperatureTrack.Steps) {
    override def advance(): IO[Err, (TemperatureTrack, Seq[ActionBonus])] =
      next match {
        case _ if isMaxed =>
          IO.fail(Err.TemperatureMaxed)
        case nt @ TemperatureTrack.OceanSynergy =>
          IO.succeed(TemperatureTrack(nt), Seq(ActionBonus.IncreaseTerraformRating, ActionBonus.BonusOcean))
        case nt if TemperatureTrack.HeatProductionSynergy(nt) =>
          IO.succeed(TemperatureTrack(nt), Seq(ActionBonus.IncreaseTerraformRating, ActionBonus.BonusHeatProduction))
        case nt =>
          IO.succeed(TemperatureTrack(nt), Seq(ActionBonus.IncreaseTerraformRating))
      }
  }
  object TemperatureTrack {
    private val Steps: Range = -30 to 8 by 2
    private val OceanSynergy = 0
    private val HeatProductionSynergy = Set(-24, -20)
    val Start: TemperatureTrack = TemperatureTrack(Steps.head)
  }

  final class OceanTiles private(override val current: Int)
      extends GlobalParameter[OceanTiles](OceanTiles.Steps) {
    override def advance(): IO[Err, (OceanTiles, Seq[ActionBonus])] =
      next match {
        case _ if isMaxed =>
          IO.fail(Err.OceansMaxed)
        case no =>
          IO.succeed(OceanTiles(no), Seq(ActionBonus.IncreaseTerraformRating))
      }
  }
  object OceanTiles {
    private val Steps: Range = 0 to 9 by 1
    val Start: OceanTiles = OceanTiles(Steps.head)
  }
}
