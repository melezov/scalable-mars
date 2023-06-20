package mars
package game

import mars.game.Game.Err

enum GameStage {
  case Setup         // selection of players
  case Prelude       // selection of corporations & cards
  case Terraforming  // main game body
  case LastPlacement // last greenery placement
  case Finished      // end of game

  def ifInStage[T](requiredStage: GameStage)(f: => IO[MarsErr, T]): IO[MarsErr, T] =
    if (this != requiredStage) {
      IO.fail(GameStage.Err.RequiresGameToBeInStage(requiredStage))
    } else {
      f
    }
}

object GameStage {
  sealed trait Err extends MarsErr
  object Err {
    final case class RequiresGameToBeInStage(gameStage: GameStage) extends Err
  }
}
