package mars
package tile

import mars.tile.RowPos.*
import zio.*

final class RowPos private(val row: Int, val pos: Int) extends Ordered[RowPos] with Serializable {
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

  lazy val adjacencies: SortedMap[Adjacency, RowPos] = SortedMap(
    Adjacency.NE -> cache.get(row - 1, pos + (if (row <= EquatorRow) -1 else 0)),
    Adjacency.NW -> cache.get(row - 1, pos + (if (row <= EquatorRow)  0 else 1)),
    Adjacency.E  -> cache.get(row,     pos - 1),
    Adjacency.W  -> cache.get(row,     pos + 1),
    Adjacency.SE -> cache.get(row + 1, pos + (if (row >= EquatorRow) -1 else 0)),
    Adjacency.SW -> cache.get(row + 1, pos + (if (row >= EquatorRow)  0 else 1)),
  ) collect {
    case (adj, Some(trp)) => (adj, trp)
  }
}

object RowPos extends BoardParser {
  case class Error(row: Int, pos: Int) extends MarsError

  // attempt at a flyweight pattern
  private[tile] val cache: Map[(Int, Int), RowPos] =
    (parsePositions map { case rp @ (row, pos) =>
      rp -> RowPos(row, pos) // only actual construction of RowPos in the code
    }).toMap

  /** Main constructor for getting a RowPos */
  def at(row: Int, pos: Int): IO[Error, RowPos] =
    cache.get((row, pos)) match {
      case Some(trp) => IO.succeed(trp)
      case _ => IO.fail(Error(row, pos))
    }
}
