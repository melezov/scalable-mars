package mars
package player

import mars.action.ForcedAction
import mars.corporation.BeginnerCorporation

final class Player private(
  val color: Player.Color,
  val corporation: Option[(Corporation, Boolean)], // Some = isSelected, true = isActive
  val terraformRating: Int,
  val mat: Mat,
  val tiles: SortedMap[RowPos, OwnedTile],
  val forcedActions: Seq[ForcedAction],
) {
  private[this] def copy(
    color: Player.Color = color,
    corporation: Option[(Corporation, Boolean)] = corporation,
    terraformRating: Int = terraformRating,
    mat: Mat = mat,
    tiles: SortedMap[RowPos, OwnedTile] = tiles,
    forcedActions: Seq[ForcedAction] = forcedActions,
  ): Player = Player(
    color = color,
    corporation = corporation,
    terraformRating = terraformRating,
    mat = mat,
    tiles = tiles,
    forcedActions = forcedActions,
  )

  def chooseCorporation(corporation: Corporation): Player =
    copy(corporation = Some((corporation, false)))

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
    corporation = None,
    terraformRating = 20,
    mat = Mat.Start,
    tiles = SortedMap.empty,
    forcedActions = Nil,
  )
}
