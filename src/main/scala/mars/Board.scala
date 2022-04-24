package mars

final case class Board(tiles: SortedSet[Tile]) {
  val coords: SortedMap[TileCoords, Tile] =
    SortedMap.empty ++ tiles.map(t => t.coords -> t)

  val adjacencies: SortedMap[TileCoords, SortedMap[TileAdjacency, Tile]] =
    SortedMap.empty ++ coords.view.mapValues { tile =>
      SortedMap.empty ++ TileAdjacency.values.toSeq
        .map { adj => adj -> coords.get(adj(tile.coords)) }
        .collect { case (k, Some(v)) => (k, v) }
    }
}

object Board {
  val EquatorRow = 5

  val Tharsis: Board = {
    val map = io.Source.fromResource("tharsis.txt")
    val tiles = for {
      (line, row) <- map.getLines() zip LazyList.from(1)
      (tile, pos) <- line.drop(math.abs((EquatorRow - row) * 2)).grouped(4) zip LazyList.from(1)
    } yield {
      Tile(
        coords = TileCoords(row = row, pos = pos),
        `type` = (tile.head, tile.drop(3).headOption.getOrElse(' ')) match {
          case ('(', ')') => TileType.Ocean
          case ('[', ']') => TileType.NoctisCity
          case (' ', ' ') => TileType.Land
        },
        placementBonuses = tile.slice(1, 3) flatMap {
          case 'C' => Seq(TilePlacementBonus.Card)
          case 'P' => Seq(TilePlacementBonus.Plant)
          case 'S' => Seq(TilePlacementBonus.Steel)
          case 'T' => Seq(TilePlacementBonus.Titanium)
          case '.' => Nil
        },
      )
    }
    new Board(SortedSet.empty ++ tiles)
  }
}
