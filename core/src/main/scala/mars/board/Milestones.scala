package mars
package board

final class Milestones private(
  val milestones: IndexedSeq[Milestone],
  val claimed: ListMap[Milestone, Color],
) {
  private[this] def copy(
    milestones: IndexedSeq[Milestone] = milestones,
    claimed: ListMap[Milestone, Color] = claimed,
  ): Milestones = Milestones(
    milestones = milestones,
    claimed = claimed,
  )

  def claim(color: Color, milestone: Milestone): IO[Milestones.Err, (Milestones, Int)] =
    Milestones.Costs.drop(claimed.size).headOption match {
      case Some(cost) =>
        if (claimed.isDefinedAt(milestone)) {
          IO.fail(Milestones.Err.MilestoneAlreadyClaimed)
        } else if (!milestones.contains(milestone)) {
          IO.fail(Milestones.Err.UnsupportedMilestone)
        } else {
          IO.succeed(copy(claimed = claimed.updated(milestone, color)), cost)
        }
      case _ =>
        IO.fail(Milestones.Err.AllMilestonesAlreadyClaimed)
    }
}

object Milestones {
  sealed trait Err extends MarsErr
  object Err {
    case object MilestoneAlreadyClaimed extends Err
    case object AllMilestonesAlreadyClaimed extends Err
    case object UnsupportedMilestone extends Err
  }

  final val Costs = IndexedSeq(8, 8, 8)
  val Start: Milestones = Milestones(
    milestones = TharsisMilestone.values.toIndexedSeq,
    claimed = ListMap.empty,
  )
}
