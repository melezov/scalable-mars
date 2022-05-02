package mars
package board

sealed trait Milestone

enum TharsisMilestone extends Milestone {
  case Terraformer
  case Mayor
  case Gardener
  case Builder
  case Planner
}
