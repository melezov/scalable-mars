package mars
package tile

sealed class Tile(
  val rowPos: RowPos,
  val kind: Tile.Kind,
  val placementBonuses: Seq[Tile.PlacementBonus],
  val placedTile: Option[PlacedTile],
) extends Ordered[Tile] {
  private[this] def copy(
    rowPos: RowPos = this.rowPos,
    kind: Tile.Kind = this.kind,
    placementBonuses: Seq[Tile.PlacementBonus] = this.placementBonuses,
    placedTile: Option[PlacedTile] = this.placedTile,
  ): Tile = Tile(rowPos, kind, placementBonuses, placedTile)

  def isPlaced: Boolean = placedTile.isDefined
  override def compare(that: Tile): Int = rowPos compare that.rowPos

  def placeTile(placedTile: PlacedTile): IO[Tile.Err, Tile] =
    this.placedTile match {
      case Some(_) => ZIO.fail(Tile.Err.RowPosAlreadyPlaced)
      case _ => ZIO.succeed(copy(placedTile = Some(placedTile)))
    }
}

object Tile {
  sealed trait Err extends MarsErr
  object Err {
    case object RowPosAlreadyPlaced extends Err
  }

  enum Kind {
    case Land
    case Ocean
    case NoctisCity
  }

  enum PlacementBonus {
    case Card
    case Plant
    case Steel
    case Titanium
  }
}
