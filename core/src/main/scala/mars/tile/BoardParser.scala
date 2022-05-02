package mars
package tile

trait BoardParser {
  final val EquatorRow = 5

  private[BoardParser] def parse[T](resource: String)(f: ((Int, Int), String) => T): Iterator[T] =
    for {
      (line, row) <- io.Source.fromResource(resource).getLines() zip LazyList.from(1)
      (tile, pos) <- line.drop(math.abs((EquatorRow - row) * 2)).grouped(4) zip LazyList.from(1)
    } yield {
      f((row, pos), tile)
    }

  private[this] final val GenericBoardResource = BoardParser.TharsisBoardResource
  protected def parsePositions: Iterator[(Int, Int)] =
    parse(GenericBoardResource) { case (rp, _) => rp }
}

object BoardParser extends BoardParser {
  private[this] final val TharsisBoardResource = "mars/board/tharsis.txt"

  val Tharsis: SortedSet[Tile] = {
    SortedSet.empty ++ parse(TharsisBoardResource) { case (rp, tile) =>
      Tile(
        rowPos = RowPos.cache(rp),
        kind = (tile.head, tile.drop(3).headOption.getOrElse(' ')) match {
          case (' ', ' ') => Tile.Kind.Land
          case ('(', ')') => Tile.Kind.Ocean
          case ('[', ']') => Tile.Kind.NoctisCity
        },
        placementBonuses = tile.slice(1, 3) flatMap {
          case 'C' => Seq(Tile.PlacementBonus.Card)
          case 'P' => Seq(Tile.PlacementBonus.Plant)
          case 'S' => Seq(Tile.PlacementBonus.Steel)
          case 'T' => Seq(Tile.PlacementBonus.Titanium)
          case '.' => Nil
        },
        placedTile = None,
      )
    }
  }
}
