package mars
package board

sealed trait Milestone

object Milestone {
  enum Tharsis extends Milestone {
    case Terraformer
    case Mayor
    case Gardener
    case Builder
    case Planner
  }

  enum Elysium extends Milestone {
    case Generalist
    case Specialist
    case Ecologist
    case Tycoon
    case Legend
  }

  enum Hellas extends Milestone {
    case Diversifier
    case Tactician
    case PolarExplorer
    case Energizer
    case RimSettler
  }

  case object Hoverlord extends Milestone
}
