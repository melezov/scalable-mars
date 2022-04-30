package mars.game

enum LifeCycle {
  case Setup // selection of players
  case Prelude // selection of corporations & cards
  case Started // main game body
  case Finished // last greenery placement
}
