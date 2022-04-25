package mars

enum TileType {
  case Land
  case Ocean
  case NoctisCity
}

enum TilePlacementBonus {
  case Card
  case Plant
  case Steel
  case Titanium
}

final case class Tile(
  coords: TileCoords,
  `type`: TileType,
  placementBonuses: Seq[TilePlacementBonus],
) extends Ordered[Tile] {
  override def compare(that: Tile): Int = coords compare that.coords
}
