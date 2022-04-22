package mars

import zio.*

enum ActionBonus {
  case TRBump
  case BonusOcean
  case BonusHeatProduction
}

case class OceanTrack private(range: Range, current: Int) {
  def advance(): IO[ActionError, (OceanTrack, Seq[ActionBonus])] =
    if (current == range.last) {
      IO.fail(ActionError.OceansMaxed)
    }  else {
      IO.succeed(copy(current = current + range.step), Seq(ActionBonus.TRBump))
    }
}
object OceanTrack {
  val Steps: Range = 0 to 9 by 1
  val Start: OceanTrack = OceanTrack(Steps, Steps.head)
}

case class TemperatureTrack private(range: Range, current: Int) {
  def advance(): IO[ActionError, (TemperatureTrack, Seq[ActionBonus])] = {
    if (current == range.last) {
      IO.fail(ActionError.TemperatureMaxed)
    } else {
      (current + range.step) match {
        case nt @ TemperatureTrack.OceanSynergy =>
          IO.succeed(copy(current = nt), Seq(ActionBonus.TRBump, ActionBonus.BonusOcean))
        case nt if TemperatureTrack.HeatProductionSynergy(nt) =>
          IO.succeed(copy(current = nt), Seq(ActionBonus.TRBump, ActionBonus.BonusHeatProduction))
        case nt =>
          IO.succeed(copy(current = nt), Seq(ActionBonus.TRBump))
      }
    }
  }
}
object TemperatureTrack {
  val Steps: Range = -30 to 8 by 2
  val OceanSynergy = 0
  val HeatProductionSynergy = Set(-24, -20)
  val Start: TemperatureTrack = TemperatureTrack(Steps, Steps.head)
}

enum ActionError {
  case OceansMaxed
  case TemperatureMaxed
  case TileAlreadyPlaced
}

case class GlobalParameters private(
  temperature: TemperatureTrack,
  oceans: OceanTrack,
) {
  def apply(action: UIO[Action]): IO[ActionError, (GlobalParameters, Seq[ActionBonus])] =
    action flatMap {
      case Action.PlaceOcean(tile) =>
        oceans.advance() map { case (ot, sab) =>
          (copy(oceans = ot), sab)
        }
      case Action.IncreaseTemperature =>
        temperature.advance() map { case (tt, sab) =>
          (copy(temperature = tt), sab)
        }
      case _ => ???
    }
}

object GlobalParameters {
  val TemperatureSteps: Range = -30 to 8 by 2

  val Start: GlobalParameters = GlobalParameters(
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
case class Row(tiles: IndexedSeq[Tile])
case class Board(row: Int, rows: IndexedSeq[Row])

object Board {
  def apply(): Board = {
    """
         SS [SS] .. [C.][..]
       ..  S.  ..  ..  .. [CC]
     C.  ..  ..  ..  ..  ..  S.
   PT  P.  P.  P.  PP  P.  P. [PP]
 PP  PP  PP [PP][PP][PP] PP  PP  PP
   P.  PP  P.  P.  P. [P.][P.][P.]
     ..  ..  ..  ..  ..  P.  ..
       SS  ..  C.  C.  ..  T.
         S.  SS  ..  .. [TT]
"""
    ???
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

