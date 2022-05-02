package mars
package game

final class Game(
  val seed: Seed,
  val board: Board,
  val players: IndexedSeq[Player],
  val turnOrder: TurnOrder,
  val gameStage: GameStage,
) {
  private[this] def copy(
    seed: Seed = seed,
    board: Board = board,
    players: IndexedSeq[Player] = players,
    turnOrder: TurnOrder = turnOrder,
    gameStage: GameStage = gameStage,
  ): Game = Game(
    seed = seed,
    board = board,
    players = players,
    turnOrder = turnOrder,
    gameStage = gameStage,
  )

  private[this] def addPlayers(colors: Set[Color]): IO[MarsErr, Game] =
    gameStage.ifInStage(GameStage.Setup) {
      val shuffledColors = seed.shuffleColors(colors)
      val players = shuffledColors map Player.create
      IO.succeed(copy(
        players = players,
        gameStage = GameStage.Prelude,
      ))
    }

  private[this] def gameStartCheck(): Game =
    if (players.forall(_.corporation.isDefined)) {
      copy(gameStage = GameStage.Terraforming)
    } else {
      this
    }

  private[this] def chooseCorporation(color: Color, corporation: Corporation, cards: Seq[String]): IO[MarsErr, Game] =
    gameStage.ifInStage(GameStage.Prelude) {
      players.zipWithIndex.find(_._1.color == color) match {
        case Some((player, index)) =>
          if (player.corporation.isEmpty) {
            val corpPlayer = player.chooseCorporation(corporation, cards)
            val updatedPlayers = players.updated(index, corpPlayer)
            IO.succeed(copy(players = updatedPlayers).gameStartCheck())
          } else {
            IO.fail(Game.Err.PlayerAlreadySelectedCorporation)
          }
        case _ =>
          IO.fail(Game.Err.PlayerWithColorDoesNotExist)
      }
    }

//  private[this] def nextGen(): Game = {
//    val (previouslyFirst, other) = players.map(_.nextGen()).splitAt(1)
//    val newFirst = other.updated(0, other.head.activate())
//    copy(
//      board = board.nextGeneration(),
//      players = newFirst :+ previouslyFirst,
//    )
//  }

//  private[this] def endOfActionCalc(color: Color): Game =
//    if (players.forall(_.passed)) {
//      nextGen()
//    } else {
//      reduceActions(color)
//    }
//
//  private[this] def reduceActions(color: Color): Game = {
//    val player = players.find(_.color == color)
//      .getOrElse(sys.error("Should not happen - could not find current player"))
//
//    val reducedActionOnThisPlayer =
//      if (player.actionsRemaining == 1) {
//        activePlayers.take(index) ++ activePlayers.drop(index).tail
//      } else {
//        activePlayers.updated(index, (color, remainingActionsBeforeThis - 1))
//      }
////
////    val resetActionsOnEndOfTurn =
////      if (reducedActionOnThisPlayer.isEmpty) {
////        // end of turn, 1st player again has actions
////        IndexedSeq((players.head.color, Game.ActionsPerTurn))
////      } else {
////        reducedActionOnThisPlayer
////      }
////
////    copy(activePlayers = resetActionsOnEndOfTurn)
//  }
//
//  private[this] def withPlayer(color: Color)(f: Player => IO[Err, Game]): IO[Err, Game] =
//    if (gameStage != GameStage.Started) {
//      IO.fail(Err.RequiresGameToBeInStartedPhase)
//    } else {
//      players.find(_.color == color) match {
//        case Some(player) =>
//          if (activePlayers.contains(color)) {
//            f(player) map { _.endOfActionCalc(color) }
//          } else {
//            IO.fail(Err.PlayerIsNotAnActivePlayer)
//          }
//        case _ =>
//          IO.fail(Err.PlayerWithColorDoesNotExist)
//      }
//    }
//
//  def pass(color: Color): IO[Err, Game] =
//    withPlayer(color) { player =>
//      player.pass
//    }

  def processAction(action: Action): IO[MarsErr, Game] = action match {
    case t: TurnOrderAction =>
      turnOrder.processAction(t) map { to =>
        copy(turnOrder = to)
      }
    case Action.AddPlayers(colors) =>
      addPlayers(colors)
    case PlayerAction.ChooseCorporation(color, corporation, cards) =>
      chooseCorporation(color, corporation, cards)
    case _ =>
      ???
  }
}

object Game {
  sealed trait Err extends MarsErr
  object Err {
    case object PlayerWithColorDoesNotExist extends Err
    case object PlayerAlreadySelectedCorporation extends Err
//    case object PlayerColorAlreadyTaken extends Err
//    case object NoPlayersAdded extends Err
//    case object PlayerIsNotAnActivePlayer extends Err
  }

  def create(seed: Seed): Game = Game(
    seed = seed,
    board = Board.Tharsis,
    players = IndexedSeq.empty,
    turnOrder = TurnOrder.Start,
    gameStage = GameStage.Setup,
  )
}
