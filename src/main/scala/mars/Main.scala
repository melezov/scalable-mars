package mars

import zio.*

enum ActionBonus {
  case TRBump
  case BonusOcean
  case BonusHeatProduction
  case BonusTemperature
}

enum ActionError {
  case OxygenMaxed
  case TemperatureMaxed
  case OceansMaxed
}

sealed abstract class GlobalParameter[T] {
  def current: Int
  def advance(): IO[ActionError, (T, Seq[ActionBonus])]
}

case class OxygenTrack private(current: Int) extends GlobalParameter[OxygenTrack] {
  override def advance(): IO[ActionError, (OxygenTrack, Seq[ActionBonus])] =
    (current + OxygenTrack.Steps.step) match {
      case no if no > OxygenTrack.Steps.last =>
        IO.fail(ActionError.OceansMaxed)
      case no @ OxygenTrack.TemperatureSynergy =>
        IO.succeed(copy(current = no), Seq(ActionBonus.TRBump, ActionBonus.BonusTemperature))
      case no =>
        IO.succeed(copy(current = no), Seq(ActionBonus.TRBump))
    }
}
object OxygenTrack {
  private val Steps: Range = 0 to 13 by 1
  private val TemperatureSynergy = 8
  val Start: OxygenTrack = OxygenTrack(Steps.head)
}

case class TemperatureTrack private(current: Int) extends GlobalParameter[TemperatureTrack] {
  override def advance(): IO[ActionError, (TemperatureTrack, Seq[ActionBonus])] =
    (current + TemperatureTrack.Steps.step) match {
      case nt if nt > TemperatureTrack.Steps.last =>
        IO.fail(ActionError.TemperatureMaxed)
      case nt @ TemperatureTrack.OceanSynergy =>
        IO.succeed(copy(current = nt), Seq(ActionBonus.TRBump, ActionBonus.BonusOcean))
      case nt if TemperatureTrack.HeatProductionSynergy(nt) =>
        IO.succeed(copy(current = nt), Seq(ActionBonus.TRBump, ActionBonus.BonusHeatProduction))
      case nt =>
        IO.succeed(copy(current = nt), Seq(ActionBonus.TRBump))
    }
}
object TemperatureTrack {
  private val Steps: Range = -30 to 8 by 2
  private val OceanSynergy = 0
  private val HeatProductionSynergy = Set(-24, -20)
  val Start: TemperatureTrack = TemperatureTrack(Steps.head)
}

case class OceanTrack private(current: Int) extends GlobalParameter[OceanTrack] {
  override def advance(): IO[ActionError, (OceanTrack, Seq[ActionBonus])] =
    (current + OceanTrack.Steps.step) match {
      case no if no > OceanTrack.Steps.last =>
        IO.fail(ActionError.OceansMaxed)
      case no =>
        IO.succeed(copy(current = no), Seq(ActionBonus.TRBump))
    }
}
object OceanTrack {
  private val Steps: Range = 0 to 9 by 1
  val Start: OceanTrack = OceanTrack(Steps.head)
}

case class GlobalParameters private(
  oxygen: OxygenTrack,
  temperature: TemperatureTrack,
  oceans: OceanTrack,
) {
  def apply(action: UIO[Action]): IO[ActionError, (GlobalParameters, Seq[ActionBonus])] =
    action flatMap {
      case Action.IncreaseOxygen =>
        oxygen.advance() map { case (ot, sab) =>
          (copy(oxygen = ot), sab)
        }
      case Action.IncreaseTemperature =>
        temperature.advance() map { case (tt, sab) =>
          (copy(temperature = tt), sab)
        }
      case Action.PlaceOcean(tile) =>
        oceans.advance() map { case (ot, sab) =>
          (copy(oceans = ot), sab)
        }
    }
}

object GlobalParameters {
  val Start: GlobalParameters = GlobalParameters(
    oxygen = OxygenTrack.Start,
    temperature = TemperatureTrack.Start,
    oceans = OceanTrack.Start,
  )
}

sealed trait Action
object Action {
  case class PlaceOcean(tile: Tile) extends Action
  case object IncreaseTemperature extends Action
  case object IncreaseOxygen extends Action
}

case class Tile(row: Int, column: Int)
case class Board(tiles: Set[Tile])

object Board {
  def apply(): Board = {
    new Board(Set.empty)
  }
}

object Main extends App {
  val gp = GlobalParameters.Start
  val ac = UIO.succeed(Action.IncreaseTemperature)
  val x = gp.apply(ac)

  val runtime = Runtime.default
  val state = runtime.unsafeRun(x)
  println(state)
}
