package mars
package board

import mars.board.Milestones.*
import zio.*

enum Milestone {
  case Terraformer
  case Mayor
  case Gardener
  case Builder
  case Planner
}

final class Milestones private(val claimed: IndexedSeq[(Milestone, Player.Color)]) {
  def claim(milestone: Milestone, color: Player.Color): IO[NoMilestonesAvailable.type, (Milestones, Int)] =
    MilestoneCosts.take(claimed.size).headOption match {
      case Some(cost) =>
        IO.succeed((Milestones(claimed :+ ((milestone, color))), cost))
      case _ => 
        IO.fail(NoMilestonesAvailable)
    }
}
object Milestones {
  final val MilestoneCosts = IndexedSeq(8, 8, 8)
  case object NoMilestonesAvailable extends MarsError
  val Start: Milestones = Milestones(IndexedSeq.empty)
}
