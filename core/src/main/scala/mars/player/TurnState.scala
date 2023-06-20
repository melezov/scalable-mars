package mars
package player

sealed trait TurnState extends Product with Serializable
object TurnState {
  case object Waiting extends TurnState
  case object ProposedActivation extends TurnState
  final case class Active(actionNumber: 1 | 2, forcedActions: Seq[ForcedAction]) extends TurnState
  case object Passed extends TurnState
}
