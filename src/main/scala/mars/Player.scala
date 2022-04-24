package mars

import zio.*

enum PlayerTile {
  case City
  case Capital
  case CommercialDistrict
  case EcologicalZone
  case Greenery
  case IndustrialCenter
  case LavaFlows
  case MiningArea
  case MiningRights
  case MoholeArea
  case NaturalPreserve
  case NuclearZone
  case RestrictedArea
}

case class PlayerMat private(
  megacredits: Int,
  megacreditProduction: Int,
  steel: Int,
  steelProduction: Int,
  titanium: Int,
  titaniumProduction: Int,
  plants: Int,
  plantProduction: Int,
  energy: Int,
  energyProduction: Int,
  heat: Int,
  heatProduction: Int,
) {
  def produce(terraformingRating: Int): PlayerMat = copy(
    megacredits = megacredits + terraformingRating + megacreditProduction,
    steel = steel + steelProduction,
    titanium = titanium + titaniumProduction,
    plants = plants + plantProduction,
    energy = energyProduction,
    heat = heat + energy + heatProduction,
  )
}
object PlayerMat {
  val Start: PlayerMat = PlayerMat(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
}

case class Player(
  terraformingRating: Int,
  mat: PlayerMat,
  tiles: SortedMap[TileCoords, PlayerTile],
)
object Player {
  val Start: Player = Player(
    terraformingRating = 20,
    mat = PlayerMat.Start,
    tiles = SortedMap.empty,
  )
}
