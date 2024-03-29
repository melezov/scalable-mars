package mars
package engine

final class TurnOrder private(val players: ListMap[Color, TurnState]) {
  def processAction(action: TurnOrderAction): IO[TurnOrder.Err, TurnOrder] =
    players.get(action.color) match {
      case Some (ts) => process (action, ts)
      case _ => ZIO.fail(TurnOrder.Err.InvalidPlayerColor)
    }

  private[this] def process(action: TurnOrderAction, ts: TurnState): IO[TurnOrder.Err, TurnOrder] = (action, ts) match {
    case (TurnOrderAction.EndTurn(color), TurnState.Active(1, _)) => ZIO.fail(TurnOrder.Err.PlayerCannotEndTurnAsFirstAction)
    case (TurnOrderAction.EndTurn(color), TurnState.Active(2, Nil)) => ZIO.succeed(TurnOrder(players.updated(color, TurnState.Waiting)))
    case (TurnOrderAction.EndTurn(color), TurnState.Active(2, _)) => ZIO.fail(TurnOrder.Err.PlayerCannotEndTurnWithForcedActions)

    case (TurnOrderAction.Pass(color), TurnState.Active(1, forcedActions)) =>
      if (forcedActions.exists { case _: BonusAction => true; case _ => false }) {
        ZIO.fail(TurnOrder.Err.PlayerCannotPassWithForcedBonusActions)
      } else {
        ZIO.succeed(TurnOrder(players.updated(color, TurnState.Passed)))
      }
    case (TurnOrderAction.Pass(color), TurnState.Active(2, _)) => ZIO.fail(TurnOrder.Err.PlayerCannotPassAsSecondAction)

    case (TurnOrderAction.ProposeNextPlayerActivation(color), TurnState.Active(_, _)) =>
      val before = players.takeWhile { case (otherColor, _) => otherColor != color }
      val others = players.drop(before.size) ++ before
      val nonPassed = others filter { case (_, ts) => ts != TurnState.Passed }
      nonPassed.headOption match {
        case Some((_, TurnState.ProposedActivation)) =>
          ZIO.fail(TurnOrder.Err.ProposedActivationAlreadySent)
        case Some((otherColor, TurnState.Waiting)) =>
          ZIO.succeed(TurnOrder(players.updated(otherColor, TurnState.ProposedActivation)))
        case _ =>
          ZIO.fail(TurnOrder.Err.NoTargetsForProposedActivation)
      }

    case (TurnOrderAction.AcceptActivation(color), TurnState.ProposedActivation) =>
      ZIO.succeed(TurnOrder(players.updated(color, TurnState.Active(1, Nil))))

    case _ =>
      ZIO.fail(TurnOrder.Err.InvalidPlayerState)
  }
}

object TurnOrder {
  sealed trait Err extends MarsErr
  object Err {
    case object InvalidPlayerColor extends Err

    case object PlayerCannotEndTurnAsFirstAction extends Err
    case object PlayerCannotEndTurnWithForcedActions extends Err

    case object PlayerAlreadyPassed extends Err
    case object PlayerCannotPassAsSecondAction extends Err
    case object PlayerCannotPassWithForcedBonusActions extends Err

    case object ProposedActivationAlreadySent extends Err
    case object NoTargetsForProposedActivation extends Err

    case object PlayerAlreadyActive extends Err

    case object InvalidPlayerState extends Err
  }

  val Start: TurnOrder = TurnOrder(ListMap.empty)
}
