package mars
package player

import mars.corporation.BeginnerCorporation

final class Player private(
  val color: Color,
  val corporation: Option[(Corporation, Boolean)], // Some = isSelected, true = isActive
  val terraformRating: Int,
  val mat: Mat,
  val cards: Seq[String],
  val tiles: SortedMap[RowPos, OwnedTile],
  val turnState: TurnState,
) {
  private[this] def copy(
   Color: Color = color,
    corporation: Option[(Corporation, Boolean)] = corporation,
    terraformRating: Int = terraformRating,
    mat: Mat = mat,
    cards: Seq[String] = cards,
    tiles: SortedMap[RowPos, OwnedTile] = tiles,
   turnState: TurnState = turnState,
  ): Player = Player(
    color = color,
    corporation = corporation,
    terraformRating = terraformRating,
    mat = mat,
    cards = cards,
    tiles = tiles,
    turnState = turnState,
  )

  def chooseCorporation(corporation: Corporation, cards: Seq[String]): Player =
    copy(
      corporation = Some((corporation, false)),
      cards = cards,
    )

//  def enqueueForcedAction(forcedAction: ForcedAction): Player =
//    copy(forcedActions = forcedAction +: forcedActions)
//
//  def pass(): IO[Player.Err.PlayerAlreadyPassed.type, Player] =
//    if (passed) {
//      IO.fail(Player.Err.PlayerAlreadyPassed)
//    } else {
//      IO.succeed(copy(actionsRemaining = 0, passed = true))
//    }
//
//  def reduceAction(): IO[Player.Err.PlayerHasNoRemainingActions.type, Player] =
//    if (actionsRemaining <= 0) {
//      IO.fail(Player.Err.PlayerHasNoRemainingActions)
//    } else {
//      IO.succeed(copy(actionsRemaining = actionsRemaining - 1))
//    }
//
//  def nextGen(): Player =
//    copy(passed = false)
//
//  def activate(): Player =
//    copy(actionsRemaining = Game.ActionsPerTurn)
}

object Player {
//  sealed trait Err extends MarsErr
//  object Err {
//    case object PlayerAlreadyPassed extends Err
//    case object PlayerHasNoRemainingActions extends Err
//  }

  def create(color: Color): Player = Player(
    color = color,
    corporation = None,
    terraformRating = 20,
    mat = Mat.Start,
    cards = Seq.empty,
    tiles = SortedMap.empty,
    turnState = TurnState.Waiting,
  )
}
