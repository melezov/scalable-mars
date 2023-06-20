package mars
package tile

enum Adjacency extends Ordered[Adjacency] {
  case NW
  case NE
  case W
  case E
  case SW
  case SE

  override def compare(that: Adjacency): Int = ordinal - that.ordinal
}
