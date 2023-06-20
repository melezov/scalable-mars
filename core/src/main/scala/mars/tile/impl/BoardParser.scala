package mars
package tile
package impl

private[tile] object BoardParser {
  def parse[T](resource: String)(f: (Int, Int, Latitude, String) => T): IndexedSeq[T] = {
    val rows = io.Source.fromResource(resource).getLines().toIndexedSeq
    val equatorRow = rows.length / 2

    rows.zipWithIndex.flatMap { case (line, row) =>
      val equatorDistance = row - equatorRow
      val latitude = Latitude.values(math.signum(equatorDistance) + 1)

      val lineOffset = math.abs(equatorDistance) * 2
      val (offset, tiles) = line.splitAt(lineOffset)
      require(offset.forall(_ == ' '), s"Invalid row offset at row #${row + 1}, expected all spaces, got: '$offset'")

      val tweakedTiles = tiles.length % 4 match {
        case 0 => tiles
        case 3 => tiles + ' ' // allow superfluous whitespaces to be trimmed at end of lines
        case _ => sys.error(s"Invalid pattern for last tile in line: '$tiles'")
      }

      tweakedTiles.grouped(4).zipWithIndex map { (tile, pos) =>
        f(row + 1, pos + 1, latitude, tile)
      }
    }
  }

  private[this] final val EmptyBoardResource = "mars/board/empty.txt"

  val positions: IndexedSeq[(Int, Int, Latitude)] =
    parse(EmptyBoardResource) { case (row, pos, latitude, tile) =>
      require(s" $row$pos " == tile, s"Could not parse empty tile, expecting ' $row$pos ', but got: '$tile'")
      (row, pos, latitude)
    }
}


//  private[this] final val TharsisBoardResource = "mars/board/tharsis.txt"
//  private[this] final val ElysiumBoardResource = "mars/board/elysium.txt"
//  private[this] final val HellasBoardResource = "mars/board/hellas.txt"
//
//  val Tharsis: SortedSet[Tile] = {
//    SortedSet.empty ++ parse(TharsisBoardResource) { case (rp, tile) =>
//      Tile(
//        rowPos = RowPos.cache(rp),
//        kind = (tile.head, tile.drop(3).headOption.getOrElse(' ')) match {
//          case (' ', ' ') => Tile.Kind.Land
//          case ('(', ')') => Tile.Kind.Ocean
//          case ('[', ']') => Tile.Kind.NoctisCity
//        },
//        placementBonuses = tile.slice(1, 3) flatMap {
//          case 'C' => Seq(Tile.PlacementBonus.Card)
//          case 'P' => Seq(Tile.PlacementBonus.Plant)
//          case 'S' => Seq(Tile.PlacementBonus.Steel)
//          case 'T' => Seq(Tile.PlacementBonus.Titanium)
//          case '.' => Nil
//        },
//        placedTile = None,
//      )
//    }
//  }
