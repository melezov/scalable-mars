package mars
package board

sealed trait Award

enum TharsisAward extends Award {
  case Landlord
  case Banker
  case Scientist
  case Thermalist
  case Miner
}
