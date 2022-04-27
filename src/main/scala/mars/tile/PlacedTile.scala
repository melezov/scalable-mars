package mars
package tile

sealed trait PlacedTile extends Product with Serializable

sealed trait OwnedTile extends PlacedTile
object OwnedTile {
  enum Generic extends OwnedTile {
    case City
    case Greenery
  }

  enum Unique extends OwnedTile {
    case Capital
    case CommercialDistrict
    case EcologicalZone
    case IndustrialCenter
    case LavaFlows
    case MiningArea
    case MiningRights
    case MoholeArea
    case NaturalPreserve
    case NuclearZone
    case RestrictedArea
  }
}

enum UnownedTile extends PlacedTile {
  case Ocean
}
