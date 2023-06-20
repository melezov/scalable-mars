package mars

import mars.corporation.Corporation
import mars.engine.impl.SeedTwister

import scala.util.matching.Regex
import scala.jdk.CollectionConverters.*
import scala.reflect.ClassTag
import scala.util.Try

object Replay extends App {
  def parseFile(resource: String): Iterator[String] =
    scala.io.Source.fromResource(resource).getLines()

  object Patterns {
    val LineSplit: Regex = " *\\[([^]]+)] (.*)".r
    val RowPos: Regex = """\(\d, \d\)""".r

    val Seed: Regex = "Game seed: (.*)".r
    val AddPlayers: Regex = "Add players: (.*)".r
    val Corporation: Regex = "Corporation: (.*?), Cards: (.*)".r

    val EndTurn: Regex = "End turn".r
    val Pass: Regex = "Pass".r

    val PlaceOcean: Regex = s"Place ocean ($RowPos)".r
    val PlaceCity: Regex = s"Place city ($RowPos)".r
    val PlaceGreenery: Regex = s"Place greenery ($RowPos)".r
  }

  private[this] def split[T : ClassTag](str: String)(f: String => T): Seq[T] =
    str.split(", ").map(f).toSeq

  var game: IO[_, Game] = _

  def parseRowPos(rowPosStr: String): IO[mars.RowPos.Err, RowPos] = {
    val Seq(row, pos) = split(rowPosStr.tail.init){ _.toInt }
    RowPos.at(row, pos)
  }

  val runtime = zio.Runtime.default

  parseFile("test.log") foreach { line =>
    println("< " + line)
    val Patterns.LineSplit(color, cmd) = line
    val nextGame = if (color == "System") {
      cmd match {
        case Patterns.Seed(seed) =>
          IO.succeed(Game.create(SeedTwister.parse(seed)))
        case Patterns.AddPlayers(colors) =>
          val playerColors = split(colors) { Color.valueOf }.toSet
          game.flatMap(_.processAction(Action.AddPlayers(playerColors)))
        case x =>
          println("Unknown system action: " + x)
          game
      }
    } else {
      def player = Color.valueOf(color)
      cmd match {
        case Patterns.Corporation(corporationName, cardNames) =>
          val corporation = Corporation.all.find(_.name == corporationName)
            .getOrElse(sys.error("Unknown corporation: " + corporationName))
          val cards = cardNames.split(", *").toSeq.filter(_.nonEmpty)
          game.flatMap(_.processAction(PlayerAction.ChooseCorporation(player, corporation, cards)))

        case Patterns.EndTurn() =>
          game.flatMap(_.processAction(TurnOrderAction.EndTurn(player)))
        case Patterns.Pass() =>
          game.flatMap(_.processAction(TurnOrderAction.Pass(player)))

        case Patterns.PlaceGreenery(rowPosStr) =>
          parseRowPos(rowPosStr) flatMap { rowPos =>
            game.flatMap(_.processAction(PlayerAction.PlaceGreenery(player, rowPos)))
          }
        case Patterns.PlaceOcean(rowPosStr) =>
          parseRowPos(rowPosStr) flatMap { rowPos =>
            game.flatMap(_.processAction(PlayerAction.PlaceOcean(player, rowPos)))
          }
        case Patterns.PlaceCity(rowPosStr) =>
          parseRowPos(rowPosStr) flatMap { rowPos =>
            game.flatMap(_.processAction(PlayerAction.PlaceCity(player, rowPos)))
          }

        case e =>
          println("Unknown action: " + e)
          game
      }
    }

    println(Json(runtime.unsafeRun(nextGame)))
    game = nextGame
  }
}
