package mars
package action

import zio.*

enum ActionBonus {
  case IncreaseTerraformRating
  case BonusOcean
  case BonusHeatProduction
  case BonusTemperature
}

sealed trait Action extends Product with Serializable
object Action {
  case class AddPlayer(color: Player.Color) extends Action
  final case class UnlockNextPlayer(color: Player.Color) extends Action
  case object EnterPrelude extends Action
  case object StartGame extends Action
  case object Pass extends Action
  final case class PlaceOcean(rowPos: RowPos) extends Action
  case object IncreaseTemperature extends Action
  final case class PlaceGreenery(rowPos: RowPos) extends Action
  final case class PlaceCity(rowPos: RowPos) extends Action
  final case class ChooseCorporation(color: Player.Color, corporation: Corporation) extends Action
}

sealed abstract class ForcedAction(val canPass: Boolean) extends Action
object ForcedAction {
  final case class PlaceBonusOcean(rowPos: RowPos) extends ForcedAction(false)
}