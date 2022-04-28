package mars

import mars.board.Board

class Game {

  // board
  // players
  // current player


  val board = Board.Tharsis
  val actions = Seq(
    Action.IncreaseTemperature,
    Action.IncreaseTemperature,
    Action.IncreaseTemperature,
    Action.IncreaseTemperature
  )

  actions.foldLeft(board){ case (b, a) => b.processAction(a).}


}
