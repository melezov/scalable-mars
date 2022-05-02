package mars
package tile

enum Adjacency extends Ordered[Adjacency] {
  case NE
  case NW
  case E
  case W
  case SE
  case SW

  override def compare(that: Adjacency): Int = ordinal - that.ordinal
}
