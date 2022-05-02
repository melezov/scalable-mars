package mars

import mars.corporation.Corporation
import zio.{App as _, *}

import scala.util.matching.Regex
import scala.jdk.CollectionConverters.*
import scala.util.Try

object Replay extends App {
  def parseFile(resource: String): Iterator[String] =
    io.Source.fromResource(resource).getLines()

  object Patterns {
    val LineSplit: Regex = " *\\[([^]]+)] (.*)".r
    val RowPos: Regex = """\(\d, \d\)""".r

    val Seed: Regex = "Game seed: (.*)".r
    val AddPlayer: Regex = "Add player: (.*)".r
    val Corporation: Regex = "Corporation: (.*)".r
    val EnterPrelude: Regex = "Enter prelude".r
    val StartGame: Regex = "Start game".r

    val PlaceOcean: Regex = s"Place ocean ($RowPos)".r
    val PlaceCity: Regex = s"Place city ($RowPos)".r
    val PlaceGreenery: Regex = s"Place greenery ($RowPos)".r
  }

  var game: IO[_, Game] = _

  def parseRowPos(rowPosStr: String): IO[mars.RowPos.Error, RowPos] = {
    val Array(row, pos) = rowPosStr.tail.init.split(", *").map(_.toInt)
    RowPos.at(row, pos)
  }

  parseFile("test.log") foreach { line =>
    val Patterns.LineSplit(color, cmd) = line
    game = if (color == "System") {
      cmd match {
        case Patterns.Seed(seed) =>
          UIO.succeed(Game.create(Seed.parse(seed)))
        case Patterns.AddPlayer(color) =>
          game.flatMap(_.processAction(Action.AddPlayer(Player.Color.valueOf(color))))
        case Patterns.EnterPrelude() =>
          game.flatMap(_.processAction(Action.EnterPrelude))
        case Patterns.StartGame() =>
          game.flatMap(_.processAction(Action.StartGame))
        case x =>
          println("Unknown system action: " + x)
          game
      }
    } else {
      def player = Player.Color.valueOf(color)
      cmd match {
        case Patterns.Corporation(corporationName) =>
          val corporation = Corporation.all.find(_.name == corporationName)
            .getOrElse(sys.error("Unknown corporation: " + corporationName))
          game.flatMap(_.processAction(Action.ChooseCorporation(player, corporation)))
        case Patterns.PlaceOcean(rowPosStr) =>
          parseRowPos(rowPosStr) flatMap { rowPos =>
            game.flatMap(_.processAction(Action.PlaceOcean(rowPos)))
          }
        case e =>
          println("Unknown action: " + e)
          game
      }
    }
  }
}

