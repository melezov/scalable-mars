package mars
package tile

import mars.tile.RowPos.*

final class RowPos private(val row: Int, val pos: Int, val latitude: Latitude) extends Ordered[RowPos] with Serializable {
  override val hashCode: Int =
    row * 100 + pos

  override def equals(that: Any): Boolean =
     that match {
       case t: RowPos => row == t.row && pos == t.pos
       case _ => false
     }

  override def compare(that: RowPos): Int =
    row - that.row match {
      case 0 => pos - that.pos
      case x => x
    }
    
  override def toString: String = s"[$row,$pos]"

  lazy val adjacencies: SortedMap[Adjacency, RowPos] = SortedMap(
    Adjacency.NW -> cache.get((row - 1, pos - (if (latitude.belowEquator) 0 else 1))),
    Adjacency.NE -> cache.get((row - 1, pos + (if (latitude.belowEquator) 1 else 0))),
    Adjacency.W  -> cache.get((row,     pos - 1)),
    Adjacency.E  -> cache.get((row,     pos + 1)),
    Adjacency.SW -> cache.get((row + 1, pos - (if (latitude.aboveEquator) 0 else 1))),
    Adjacency.SE -> cache.get((row + 1, pos + (if (latitude.aboveEquator) 1 else 0))),
  ) collect {
    case (adj, Some(trp)) => (adj, trp)
  }
}

object RowPos {
  sealed trait Err extends MarsErr
  object Err {
    case object RowPosErr extends Err
  }

  // attempt at a flyweight pattern
  private[tile] val cache: Map[(Int, Int), RowPos] =
    (impl.BoardParser.positions map { case (row, pos, latitude) =>
      (row, pos) -> RowPos(row, pos, latitude) // only actual construction of RowPos in the code
    }).toMap

  /** Main constructor for getting a RowPos */
  def at(row: Int, pos: Int): IO[Err, RowPos] =
    cache.get((row, pos)) match {
      case Some(trp) => ZIO.succeed(trp)
      case _ => ZIO.fail(Err.RowPosErr)
    }
}
