package mars
package engine

trait Seed {
  def shuffleColors(colors: Set[Color]): IndexedSeq[Color]
}
