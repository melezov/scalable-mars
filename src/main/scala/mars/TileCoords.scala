package mars

enum TileAdjacency extends Ordered[TileAdjacency] {
  case NE, NW, E, W, SE, SW

  override def compare(that: TileAdjacency): Int = ordinal - that.ordinal

  /** Does not perform validations, invalid coords will be matched against the board and not resolve to anything */
  def apply(coords: TileCoords): TileCoords = this match {
    case NE => TileCoords(row = coords.row - 1, pos = coords.pos + (if (coords.row <= Board.EquatorRow) -1 else 0))
    case NW => TileCoords(row = coords.row - 1, pos = coords.pos + (if (coords.row <= Board.EquatorRow) 0 else 1))
    case E  => TileCoords(row = coords.row,     pos = coords.pos - 1)
    case W  => TileCoords(row = coords.row,     pos = coords.pos + 1)
    case SE => TileCoords(row = coords.row + 1, pos = coords.pos + (if (coords.row >= Board.EquatorRow) -1 else 0))
    case SW => TileCoords(row = coords.row + 1, pos = coords.pos + (if (coords.row >= Board.EquatorRow) 0 else 1))
  }
}

final case class TileCoords(row: Int, pos: Int) extends Ordered[TileCoords] {
  override def compare(that: TileCoords): Int =
    (row - that.row) match {
      case 0 => pos - that.pos
      case x => x
    }
}
