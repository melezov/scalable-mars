package mars
package tile

import mars.tile.Latitude.{AboveEquator, BelowEquator, OnEquator}

enum Latitude extends Ordered[Latitude] {
  case AboveEquator
  case OnEquator
  case BelowEquator

  override def compare(that: Latitude): Int = ordinal - that.ordinal

  def aboveEquator: Boolean = this eq AboveEquator
  def onEquator: Boolean = this eq OnEquator
  def belowEquator: Boolean = this eq BelowEquator
}
