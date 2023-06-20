package mars
package board

final class Board private(
  val globalParameters: GlobalParameters,
  val tiles: Map[RowPos, Tile],
  val milestones: Milestones,
  val awards: Awards,
  val generationMarker: Int,
) {
  private[this] def copy(
    globalParameters: GlobalParameters = globalParameters,
    tiles: Map[RowPos, Tile] = tiles,
    milestones: Milestones = milestones,
    awards: Awards = awards,
    generationMarker: Int = generationMarker,
  ): Board = Board(globalParameters, tiles, milestones, awards, generationMarker)

  def nextGeneration(): Board =
    copy(generationMarker = generationMarker + 1)

  private[this] lazy val uniqueTiles: Set[OwnedTile.Unique] =
    (tiles.values flatMap { tile =>
      tile.placedTile collect { case unique: OwnedTile.Unique => unique }
    }).toSet

  private[this] def placeTile(rowPos: RowPos, tile: PlacedTile): IO[MarsErr, Board] =
    tile match {
      case uniqueTile: OwnedTile.Unique if uniqueTiles(uniqueTile) =>
        ZIO.fail(Board.Err.UniqueTileAlreadyPlaced)
      case _ =>
        tiles(rowPos).placeTile(tile) flatMap { placedTile =>
          val updatedTiles = tiles.updated(rowPos, placedTile)
          ZIO.succeed(copy(tiles = updatedTiles))
        }
    }

//  def processAction(action: Action): IO[MarsErr, (Board, Seq[ActionBonus])] = action match {
//    case Action.PlaceGreenery(color, rowPos) =>
//      placeTile(rowPos, OwnedTile.Generic.Greenery) flatMap { boardWithPlacement =>
//        boardWithPlacement.globalParameters.increaseOxygen()
//          .orElse(IO.succeed((boardWithPlacement.globalParameters, Nil)))
//          .map { case (gp, bonuses) => (boardWithPlacement.copy(globalParameters = gp), bonuses) }
//      }
//
//    case Action.PlaceOcean(color, rowPos) =>
//      placeTile(rowPos, UnownedTile.Ocean) flatMap { boardWithPlacement =>
//        boardWithPlacement.globalParameters.placeOcean()
//          .orElse(IO.succeed((boardWithPlacement.globalParameters, Nil)))
//          .map { case (gp, bonuses) => (boardWithPlacement.copy(globalParameters = gp), bonuses) }
//      }
//
//    case Action.IncreaseTemperature =>
//      globalParameters.increaseTemperature()
//        .orElse(IO.succeed((globalParameters, Nil)))
//        .map { case (gp, bonuses) => (copy(globalParameters = gp), bonuses) }
//  }
}

object Board {
  sealed trait Err extends MarsErr
  object Err {
    case object UniqueTileAlreadyPlaced extends Err
  }

//  val Tharsis: Board = Board(
//    globalParameters = GlobalParameters.start(false),
//    tiles = (tile.BoardParser.Tharsis map { tile => tile.rowPos -> tile }).toMap,
//    milestones = Milestones.Start,
//    awards = Awards.Start,
//    generationMarker = 1,
//  )
}
