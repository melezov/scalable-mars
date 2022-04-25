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
  def produce(terraformRating: Int): PlayerMat = copy(
    megacredits = megacredits + terraformRating + megacreditProduction,
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
  terraformRating: Int,
  mat: PlayerMat,
  tiles: SortedMap[TileCoords, PlayerTile],
  steelMultiplier: Int,
  titaniumMultiplier: Int,
)
object Player {
  val Start: Player = Player(
    terraformRating = 20,
    mat = PlayerMat.Start,
    tiles = SortedMap.empty,
    steelMultiplier = 2,
    titaniumMultiplier = 3,
  )
}
