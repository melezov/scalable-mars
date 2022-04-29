package mars
package game

import mars.game.Game.*
import zio.*

final class Game(
  val board: Board,
  val players: IndexedSeq[Player],
  val currentPlayer: Int,
) {
  private[this] def copy(
    board: Board = board,
    players: IndexedSeq[Player] = players,
    currentPlayer: Int = currentPlayer,
  ): Game = Game(
    board = board,
    players = players,
    currentPlayer = currentPlayer,
  )

  def addPlayer(color: Player.Color): IO[Game.PlayerColorAlreadyTaken.type, Game] =
    players.find(_.color == color) match {
      case Some(_) =>
        IO.fail(PlayerColorAlreadyTaken)
      case _ =>
        IO.succeed(copy(players = players :+ Player.create(color)))
    }
}
object Game {
  sealed trait Error extends MarsError
  case object PlayerColorAlreadyTaken extends Error

  val Start: Game = Game(
    board = Board.Tharsis,
    players = IndexedSeq.empty,
    currentPlayer = 0,
  )
}
