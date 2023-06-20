package mars
package board

final class Awards private(
  val awards: IndexedSeq[Award],
  val funded: ListMap[Award, Color],
) {
  private[this] def copy(
    awards: IndexedSeq[Award] = awards,
    funded: ListMap[Award, Color] = funded,
  ): Awards = Awards(awards, funded)

  def fund(color: Color, award: Award): IO[Awards.Err, (Awards, Int)] =
    Awards.Costs.drop(funded.size).headOption match {
      case Some(cost) =>
        if (funded.isDefinedAt(award)) {
          ZIO.fail(Awards.Err.AwardAlreadyFunded)
        } else if (!awards.contains(award)) {
          ZIO.fail(Awards.Err.UnsupportedAward)
        } else {
          ZIO.succeed(copy(funded = funded.updated(award, color)), cost)
        }
      case _ =>
        ZIO.fail(Awards.Err.AllAwardsAlreadyFunded)
    }
}

object Awards {
  sealed trait Err extends MarsErr
  object Err {
    case object AwardAlreadyFunded extends Err
    case object AllAwardsAlreadyFunded extends Err
    case object UnsupportedAward extends Err
  }

  final val Costs = IndexedSeq(8, 8, 8)
/*
  val Start: Awards = Awards(
    awards = TharsisAward.values.toIndexedSeq,
    funded = ListMap.empty,
  )
*/
}
