package mars

import zio.*

enum ActionBonus {
  case IncreaseTerraformingRating
  case BonusOcean
  case BonusHeatProduction
  case BonusTemperature
}

enum ActionError {
  case OxygenMaxed
  case TemperatureMaxed
  case OceansMaxed
}

sealed trait Action extends Product with Serializable
object Action {
  final case class PlaceOcean(tile: Tile) extends Action
  case object IncreaseTemperature extends Action
  case object IncreaseOxygen extends Action
}
