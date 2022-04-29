package mars
package player

import mars.action.ForcedAction

final class Player private(
  val color: Player.Color,
  val terraformRating: Int,
  val mat: Mat,
  val tiles: SortedMap[RowPos, OwnedTile],
  val forcedActions: Seq[ForcedAction],
) {
  private[this] def copy(
    color: Player.Color = color,
    terraformRating: Int = terraformRating,
    mat: Mat = mat,
    tiles: SortedMap[RowPos, OwnedTile] = tiles,
    forcedActions: Seq[ForcedAction] = forcedActions,
  ): Player = Player(
    color = color,
    terraformRating = terraformRating,
    mat = mat,
    tiles = tiles,
    forcedActions = forcedActions,
  )

  def enqueueForcedAction(forcedAction: ForcedAction): Player =
    copy(forcedActions = forcedAction +: forcedActions)
}

object Player {
  enum Color {
    case Black
    case Blue
    case Green
    case Red
    case Yellow
  }

  def create(color: Color): Player = Player(
    color = color,
    terraformRating = 20,
    mat = Mat.Start,
    tiles = SortedMap.empty,
    forcedActions = Nil,
  )
}
