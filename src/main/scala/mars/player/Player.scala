package mars
package player

final class Player private(
  val color: Player.Color,
  val terraformRating: Int,
  val mat: Mat,
  val tiles: SortedMap[RowPos, OwnedTile],
  val steelMultiplier: Int,
  val titaniumMultiplier: Int,
)

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
    steelMultiplier = 2,
    titaniumMultiplier = 3,
  )
}