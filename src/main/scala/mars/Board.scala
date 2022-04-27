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
    tiles.values.flatMap(_.uniqueTile).toSet

  def placeTile(rowPos: RowPos, tile: PlacedTile): IO[Board.Error, Board] =
    tile match {
      case uniqueTile: OwnedTile.Unique if uniqueTiles(uniqueTile) =>
        IO.fail(Board.Error.UniqueTileAlreadyPlaced)
      case _ =>
        tiles(rowPos) match {
          case boardTile if boardTile.isPlaced =>
            IO.fail(Board.Error.TileRowPosAlreadyUsed)
          case boardTile =>
            boardTile.placeTile(tile) flatMap { placedTile =>
              val updatedTiles = tiles.updated(rowPos, placedTile)
              IO.succeed(copy(tiles = updatedTiles))
            }
        }
    }
}

object Board {
  sealed trait Error extends MarsError
  object Error {
    case object UniqueTileAlreadyPlaced extends Error
    case object TileRowPosAlreadyUsed extends Error
  }

  val Tharsis: Board = Board(
    globalParameters = GlobalParameters.Start,
    tiles = (tile.BoardParser.Tharsis map { tile => tile.rowPos -> tile }).toMap,
    generationMarker = 1,
  )
}
