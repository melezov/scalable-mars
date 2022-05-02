package mars
package board

import zio.*

final class Board private(
  val globalParameters: GlobalParameters,
  val tiles: Map[RowPos, Tile],
  val generationMarker: Int,
) {
  private[this] def copy(
    globalParameters: GlobalParameters = this.globalParameters,
    tiles: Map[RowPos, Tile] = this.tiles,
    generationMarker: Int = this.generationMarker,
  ): Board = Board(
    globalParameters = globalParameters,
    tiles = tiles,
    generationMarker = generationMarker,
  )

  def nextGeneration(): Board = {
    copy(generationMarker = generationMarker + 1)
  }

  private[this] lazy val uniqueTiles: Set[OwnedTile.Unique] =
    (tiles.values flatMap { tile =>
      tile.placedTile collect { case unique: OwnedTile.Unique => unique }
    }).toSet

  private[this] def placeTile(rowPos: RowPos, tile: PlacedTile): IO[MarsError, Board] =
    tile match {
      case uniqueTile: OwnedTile.Unique if uniqueTiles(uniqueTile) =>
        IO.fail(Board.Error.UniqueTileAlreadyPlaced)
      case _ =>
        tiles(rowPos).placeTile(tile) flatMap { placedTile =>
          val updatedTiles = tiles.updated(rowPos, placedTile)
          IO.succeed(copy(tiles = updatedTiles))
        }
    }

  def processAction(action: Action): IO[MarsError, (Board, Seq[ActionBonus])] = action match {
    case Action.PlaceGreenery(rowPos) =>
      placeTile(rowPos, OwnedTile.Generic.Greenery) flatMap { boardWithPlacement =>
        boardWithPlacement.globalParameters.increaseOxygen()
          .orElse(IO.succeed((boardWithPlacement.globalParameters, Nil)))
          .map { case (gp, bonuses) => (boardWithPlacement.copy(globalParameters = gp), bonuses) }
      }

    case Action.PlaceOcean(rowPos) =>
      placeTile(rowPos, UnownedTile.Ocean) flatMap { boardWithPlacement =>
        boardWithPlacement.globalParameters.placeOcean()
          .orElse(IO.succeed((boardWithPlacement.globalParameters, Nil)))
          .map { case (gp, bonuses) => (boardWithPlacement.copy(globalParameters = gp), bonuses) }
      }

    case Action.IncreaseTemperature =>
      globalParameters.increaseTemperature()
        .orElse(IO.succeed((globalParameters, Nil)))
        .map { case (gp, bonuses) => (copy(globalParameters = gp), bonuses) }
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
