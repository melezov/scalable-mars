package mars
package board

sealed trait Award

object Award {
  enum Tharsis extends Award {
    case Landlord
    case Banker
    case Scientist
    case Thermalist
    case Miner
  }

  enum Elysium extends Award {
    case Celebrity
    case Industrialist
    case DesertSettler
    case EstateDealer
    case Benefactor
  }

  enum Hellas extends Award {
    case Cultivator
    case Magnate
    case SpaceBaron
    case Excentric
    case Contractor
  }

  case object Venuphile extends Award
}
