package mars

/*

import GlobalParameters.{OxygenSteps, TemperatureSteps}

enum TileType:
  case Land, Ocean

case class Tile(row: Int, column: Int)
case class Row(tiles: IndexedSeq[Tile])
case class Board(row: Int, rows: IndexedSeq[Row])

sealed trait ActionBonus
object ActionBonus {

}

case class GlobalParameter private(range: Range, current: Int) {
  require(range.contains(current))
  def advance(): (GlobalParameter, ActionBonus) = copy(current = current + range.step)
}
object GlobalParameter {
  def apply(range: Range): GlobalParameter = GlobalParameter(range, range.head)
}

case class GlobalParameters private(
  oxygen: GlobalParameter,
  temperature: GlobalParameter,
  oceans: GlobalParameter,
) {
  def apply(action: Action): (GlobalParameters, ActionBonus) = action match {
    case Action.PlaceOcean(tile) =>
      copy(oceans = oceans.advance())
    case Action.IncreaseTemperature =>
      copy(temperature = temperature.advance())
    case Action.IncreaseOxygen =>
      copy(oxygen = oxygen.advance())
    case Action.PlaceGreenery(tile) =>

    case _ =>
      this
  }
}

object GlobalParameters {
  val OxygenSteps: Range = 0 to 13
  val TemperatureSteps: Range = -30 to 8 by 2
  val OceanSteps: Range = 0 to 9 by 1

  val Start: GlobalParameters = GlobalParameters(
    oxygen = GlobalParameter(OxygenSteps),
    temperature = GlobalParameter(TemperatureSteps),
    oceans = GlobalParameter(OceanSteps),
  )
}

sealed trait Action
object Action {
  case class PlaceOcean(tile: Tile) extends Action
  case class PlaceGreenery(tile: Tile) extends Action
  case class PlaceCity(tile: Tile) extends Action
  case object IncreaseTemperature extends Action
  case object IncreaseOxygen extends Action
}

object Board {
  def apply(): Board =
    val x =
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
    null

  def main(args: Array[String]): Unit =
    println("HULLO")
}

*/

object Main extends App {
  println("HULLO")
}
