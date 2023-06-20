package mars
package action

trait Action extends Product with Serializable
/** System actions with no owner (player) */
object Action {
//  case class AddPlayers(colors: Set[Color]) extends Action
//  case object IncreaseTemperature extends Action
}

/** Base for all actions performed by a player */
trait PlayerAction extends Action { def color: Color }
object PlayerAction {
//  final case class ChooseCorporation(color: Color, corporation: Corporation, cards: Seq[String]) extends PlayerAction
  final case class PlaceOcean(color: Color, rowPos: RowPos) extends PlayerAction
  final case class PlaceGreenery(color: Color, rowPos: RowPos) extends PlayerAction
  final case class PlaceCity(color: Color, rowPos: RowPos) extends PlayerAction
}

/** Does not decrease remaining actions */
trait BonusAction extends Action

/** Meta-Actions for turn control */
sealed trait TurnOrderAction extends PlayerAction with BonusAction
object TurnOrderAction {
  final case class EndTurn(color: Color) extends TurnOrderAction
  final case class Pass(color: Color) extends TurnOrderAction
  final case class ProposeNextPlayerActivation(color: Color) extends TurnOrderAction
  final case class AcceptActivation(color: Color) extends TurnOrderAction
}

/** Player actions that are generated by the system, which may take away an action unless the action is also a BonusAction */
trait ForcedAction extends PlayerAction
object ForcedAction {
  final case class PlaceBonusOcean(color: Color, rowPos: RowPos) extends ForcedAction with BonusAction
}
