package mars
package board

sealed abstract class GlobalParameter[T](steps: Range) {
  private final def step: Int = steps.step
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
    case object VenusMaxed extends Err
    case object VenusNotActive extends Err
  }

  final class OxygenTrack private(override val current: Int)
      extends GlobalParameter[OxygenTrack](OxygenTrack.Steps) {
    override def advance(): IO[Err, (OxygenTrack, Seq[ActionBonus])] =
      next match {
        case _ if isMaxed =>
          ZIO.fail(Err.OxygenMaxed)
        case no @ OxygenTrack.BonusTemperatureAt =>
          ZIO.succeed(OxygenTrack(no), Seq(ActionBonus.IncreaseTerraformRating, ActionBonus.IncreaseTemperature))
        case no =>
          ZIO.succeed(OxygenTrack(no), Seq(ActionBonus.IncreaseTerraformRating))
      }
  }
  object OxygenTrack {
    private val Steps: Range = 0 to 13 by 1
    private val BonusTemperatureAt = 8
    val Start: OxygenTrack = OxygenTrack(Steps.head)
  }

  final class TemperatureTrack private(override val current: Int)
      extends GlobalParameter[TemperatureTrack](TemperatureTrack.Steps) {
    override def advance(): IO[Err, (TemperatureTrack, Seq[ActionBonus])] =
      next match {
        case _ if isMaxed =>
          ZIO.fail(Err.TemperatureMaxed)
        case nt if TemperatureTrack.BonusHeatProductionAt(nt) =>
          ZIO.succeed(TemperatureTrack(nt), Seq(ActionBonus.IncreaseTerraformRating, ActionBonus.IncreaseHeatProduction))
        case nt @ TemperatureTrack.BonusOceanAt =>
          ZIO.succeed(TemperatureTrack(nt), Seq(ActionBonus.IncreaseTerraformRating, ActionBonus.PlaceOcean))
        case nt =>
          ZIO.succeed(TemperatureTrack(nt), Seq(ActionBonus.IncreaseTerraformRating))
      }
  }
  object TemperatureTrack {
    private val Steps = -30 to 8 by 2
    private val BonusOceanAt = 0
    private val BonusHeatProductionAt = Set(-24, -20)
    val Start: TemperatureTrack = TemperatureTrack(Steps.head)
  }

  final class OceanTiles private(override val current: Int)
      extends GlobalParameter[OceanTiles](OceanTiles.Steps) {
    override def advance(): IO[Err, (OceanTiles, Seq[ActionBonus])] =
      next match {
        case _ if isMaxed =>
          ZIO.fail(Err.OceansMaxed)
        case no =>
          ZIO.succeed(OceanTiles(no), Seq(ActionBonus.IncreaseTerraformRating))
      }
  }
  object OceanTiles {
    private val Steps = 0 to 9 by 1
    val Start: OceanTiles = OceanTiles(Steps.head)
  }

  final class VenusScale private(override val current: Int)
      extends GlobalParameter[VenusScale](VenusScale.Steps) {
    override def advance(): IO[Err, (VenusScale, Seq[ActionBonus])] =
      next match {
        case _ if isMaxed =>
          ZIO.fail(Err.VenusMaxed)
        case nv @ VenusScale.DrawCardBonusAt =>
          ZIO.succeed(VenusScale(nv), Seq(ActionBonus.IncreaseTerraformRating, ActionBonus.DrawCard))
        case nv @ VenusScale.TerraformingRatingBonusAt =>
          ZIO.succeed(VenusScale(nv), Seq(ActionBonus.IncreaseTerraformRating, ActionBonus.IncreaseTerraformRating))
        case nv =>
          ZIO.succeed(VenusScale(nv), Seq(ActionBonus.IncreaseTerraformRating))
      }
  }
  object VenusScale {
    private val Steps: Range = 0 to 30 by 2
    private val DrawCardBonusAt = 8
    private val TerraformingRatingBonusAt = 16
    val Start: VenusScale = VenusScale(Steps.head)
  }
}
