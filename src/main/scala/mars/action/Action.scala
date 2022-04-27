package mars
package action

import zio.*

enum ActionBonus {
  case IncreaseTerraformRating
  case BonusOcean
  case BonusHeatProduction
  case BonusTemperature
}

sealed trait Action extends Product with Serializable
object Action {
  final case class PlaceOcean(rowPos: RowPos) extends Action
  case object IncreaseTemperature extends Action
  case object IncreaseOxygen extends Action
}
