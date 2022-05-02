package mars
package tile

import zio.*

enum Adjacency extends Ordered[Adjacency] {
  case NE, NW, E, W, SE, SW
  override def compare(that: Adjacency): Int = ordinal - that.ordinal
}
