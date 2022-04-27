package mars

import zio.*

final class Board private(
  globalParameters: GlobalParameters,
  tiles: Map[RowPos, Tile],
  generationMarker: Int,
) {
  private[this] def copy(
    globalParameters: GlobalParameters = this.globalParameters,
    tiles: Map[RowPos, Tile] = this.tiles,
    generationMarker: Int = this.generationMarker,
  ): Board = Board(globalParameters, tiles, generationMarker)

  def nextGeneration(): Board = {
    copy(generationMarker = generationMarker + 1)
  }

  private[this] lazy val uniqueTiles: Set[OwnedTile.Unique] =
    (tiles.values flatMap { tile =>
      tile.placedTile collect { case unique: OwnedTile.Unique => unique }
    }).toSet

  def placeTile(rowPos: RowPos, tile: PlacedTile): IO[MarsError, Board] =
    tile match {
      case uniqueTile: OwnedTile.Unique if uniqueTiles(uniqueTile) =>
        IO.fail(Board.Error.UniqueTileAlreadyPlaced)
      case _ =>
        tiles(rowPos).placeTile(tile) flatMap { placedTile =>
          val updatedTiles = tiles.updated(rowPos, placedTile)
          IO.succeed(copy(tiles = updatedTiles))
        }
    }
}

object Board {
  sealed trait Error extends MarsError
  object Error {
    case object UniqueTileAlreadyPlaced extends Error
  }

  val Tharsis: Board = Board(
    globalParameters = GlobalParameters.Start,
    tiles = (tile.BoardParser.Tharsis map { tile => tile.rowPos -> tile }).toMap,
    generationMarker = 1,
  )
}
