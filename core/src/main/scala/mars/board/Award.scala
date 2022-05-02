package mars
package board

import mars.board.Awards.*
import zio.*

enum Award {
  case Landlord
  case Banker
  case Scientist
  case Thermalist
  case Miner
}

final class Awards private(val funded: IndexedSeq[(Award, Player.Color)]) {
  def claim(award: Award, color: Player.Color): IO[NoAwardsAvailable.type, (Awards, Int)] =
    AwardCosts.take(funded.size).headOption match {
      case Some(cost) =>
        IO.succeed((Awards(funded :+ ((award, color))), cost))
      case _ =>
        IO.fail(NoAwardsAvailable)
    }
}
object Awards {
  final val AwardCosts = IndexedSeq(8, 14, 20)
  case object NoAwardsAvailable extends MarsError
  val Start: Awards = Awards(IndexedSeq.empty)
}
