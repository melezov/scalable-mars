package mars
package game

import mars.game.Game.*
import zio.*

import java.util.UUID

final class Game(
  val seed: Seed,
  val board: Board,
  val players: IndexedSeq[Player],
  val unlockedPlayers: IndexedSeq[Player.Color],
  val lifeCycle: LifeCycle,
) {
  private[this] def copy(
    seed: Seed = seed,
    board: Board = board,
    players: IndexedSeq[Player] = players,
    unlockedPlayers: IndexedSeq[Player.Color] = unlockedPlayers,
    lifeCycle: LifeCycle = lifeCycle,
  ): Game = Game(
    seed = seed,
    board = board,
    players = players,
    unlockedPlayers = unlockedPlayers,
    lifeCycle = lifeCycle,
  )

  private[this] def inSetup(f: => IO[Error, Game]): IO[Error, Game] =
    if (lifeCycle != LifeCycle.Setup) {
      IO.fail(Error.RequiresGameToBeInSetupPhase)
    } else {
      f
    }

  private[this] def addPlayer(color: Player.Color): IO[Error, Game] =
    inSetup {
      players.find(_.color == color) match {
        case Some(_) =>
          IO.fail(Error.PlayerColorAlreadyTaken)
        case _ =>
          IO.succeed(copy(players = players :+ Player.create(color)))
      }
    }

  private[this] def inPrelude(f: => IO[Error, Game]): IO[Error, Game] =
    if (lifeCycle != LifeCycle.Prelude) {
      IO.fail(Error.RequiresGameToBeInPreludePhase)
    } else {
      f
    }

  private[this] def enterPrelude(): IO[Error, Game] =
    inSetup {
      if (players.isEmpty) {
        IO.fail(Error.NoPlayersAdded)
      } else {
        UIO(copy(lifeCycle = LifeCycle.Prelude))
      }
    }

  private[this] def chooseCorporation(color: Player.Color, corporation: Corporation): IO[Error, Game] =
    inPrelude {
      players.zipWithIndex.find(_._1.color == color) match {
        case Some((player, index)) =>
          val corpPlayer = player.chooseCorporation(corporation)
          val corpPlayers = players.updated(index, corpPlayer)
          IO.succeed(copy(players = corpPlayers))
        case _ =>
          IO.fail(Error.PlayerWithColorDoesNotExist(color))
      }
    }

  private[this] def startGame(): IO[Error, Game] =
    inPrelude {
      if (players.isEmpty) {
        IO.fail(Error.NoPlayersAdded)
      } else {
        UIO(copy(lifeCycle = LifeCycle.Prelude))
      }
    }

  def processAction(action: Action): IO[MarsError, Game] = action match {
    case Action.AddPlayer(color) =>
      addPlayer(color)
    case Action.EnterPrelude =>
      enterPrelude()
    case Action.ChooseCorporation(color, corporation) =>
      chooseCorporation(color, corporation)
    case Action.StartGame =>
      UIO(copy(lifeCycle = LifeCycle.Started))
    case _ =>
      ???
  }
}
object Game {
  sealed trait Error extends MarsError
  object Error {
    final case class PlayerWithColorDoesNotExist(color: Player.Color) extends Error
    case object RequiresGameToBeInSetupPhase extends Error
    case object RequiresGameToBeInPreludePhase extends Error
    case object PlayerColorAlreadyTaken extends Error
    case object NoPlayersAdded extends Error
    final case class CannotPassThisForcedAction(forcedAction: ForcedAction) extends Error
  }

  def create(seed: Seed): Game = Game(
    seed = seed,
    board = Board.Tharsis,
    players = IndexedSeq.empty,
    unlockedPlayers = IndexedSeq.empty,
    lifeCycle = LifeCycle.Prelude,
  )
}
