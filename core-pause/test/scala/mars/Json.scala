package mars

import com.fasterxml.jackson.core.{JsonFactory, JsonGenerator}
import com.fasterxml.jackson.databind.node.{BooleanNode, IntNode, TextNode}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import mars.board.{GlobalParameter, GlobalParameters}
import mars.engine.impl.SeedTwister
import mars.game.GameStage
import mars.player.{Color, Mat, TurnState}

object Json {
  private[this] val mapper = new ObjectMapper()
    .registerModule(DefaultScalaModule)

  def bool(value: Boolean): JsonNode = BooleanNode.valueOf(value)
  def str(value: String): JsonNode = TextNode.valueOf(value)
  def int(value: Int): JsonNode = IntNode.valueOf(value)
  def obj(values: (String, JsonNode)*): JsonNode = {
    val node = mapper.createObjectNode()
    values foreach { case (key, value) =>
      node.set(key, value): Unit // remove and see Scala 3 explode gloriously
    }
    node
  }
  def arr(values: Iterable[JsonNode]): JsonNode = {
    val node = mapper.createArrayNode()
    values foreach { value => node.add(value) }
    node
  }

  def opt[T](value: Option[T])(f: T => JsonNode): JsonNode = value.map(f).getOrElse(mapper.nullNode())

  def js(seed: Seed): JsonNode = str(seed match { case st: SeedTwister => "0x%x" format st.seed })
  def js(rowPos: RowPos): JsonNode = arr(IndexedSeq(int(rowPos.row), int(rowPos.pos)))
  def js(color: Color): JsonNode = str(color.toString)
  def js(kind: Tile.Kind): JsonNode = str(kind.toString)
  def js(placementBonus: Tile.PlacementBonus): JsonNode = str(placementBonus.toString)
  def js(placedTile: PlacedTile): JsonNode = str(placedTile.toString)
  def js(awards: Awards): JsonNode = obj(awards.funded.toSeq map { case (award, color) => award.toString -> js(color) }: _*)
  def js(milestones: Milestones): JsonNode = obj(milestones.claimed.toSeq map { case (milestone, color) => milestone.toString -> js(color) }: _*)
  def js(corporation: Corporation): JsonNode = str(corporation.name)
  def js(gameStage: GameStage): JsonNode = str(gameStage.toString)

  def js(globalParameters: GlobalParameters): JsonNode = obj(
    "oxygen" -> int(globalParameters.oxygen.current),
    "temperature" -> int(globalParameters.temperature.current),
    "oceans" -> int(globalParameters.oceans.current),
  )

  def js(tile: Tile): JsonNode = obj(
    "rowPos" -> js(tile.rowPos),
    "kind" -> js(tile.kind),
    "placementBonuses" -> arr(tile.placementBonuses.map(js)),
    "placedTile" -> opt(tile.placedTile)(js),
  )

  def js(board: Board): JsonNode = obj(
    "globalParameters" -> js(board.globalParameters),
    "awards" -> js(board.awards),
    "milestones" -> js(board.milestones),
    "tiles" -> arr(board.tiles.values.map(js)),
    "generationMarker" -> int(board.generationMarker),
  )

  def js(ownedTile: (RowPos, OwnedTile)): JsonNode = obj(
    "rowPos" -> js(ownedTile._1),
    "tile" -> js(ownedTile._2),
  )

  def js(mat: Mat): JsonNode = {
    def ap(amount: Int, production: Int): JsonNode = obj(
      "amount" -> int(amount),
      "production" -> int(production),
    )
    obj(
      "megaCredits" -> ap(mat.megaCredits, mat.megaCreditProduction),
      "steel" -> ap(mat.steel, mat.steelProduction),
      "titanium" -> ap(mat.titanium, mat.titaniumProduction),
      "plants" -> ap(mat.plants, mat.plantProduction),
      "energy" -> ap(mat.energy, mat.energyProduction),
      "heat" -> ap(mat.heat, mat.heatProduction),
    )
  }

  def js(forcedAction: Class[ForcedAction]): JsonNode = forcedAction match {
    case x if x == classOf[ForcedAction.PlaceBonusOcean] => str(x.getSimpleName)
  }

  def js(turnState: TurnState): JsonNode = turnState match {
    case TurnState.Waiting | TurnState.ProposedActivation | TurnState.Passed => str(turnState.toString)
    case TurnState.Active(actionNumber, forcedActions) => obj(
      "actionNumber" -> int(actionNumber),
      "forcedActions" -> arr(forcedActions.map(js))
    )
  }

  def js(turnOrder: TurnOrder): JsonNode = obj(
    turnOrder.players.toSeq map { case (color, ts) =>
      color.toString -> js(ts)
    }: _*
  )

  def js(player: Player): JsonNode = obj(
    "color" -> js(player.color),
    "corporation" -> opt(player.corporation) { case (corporation, isActive) => obj(
      "name" -> js(corporation),
      "isActive" -> bool(isActive),
    )},
    "terraformRating" -> int(player.terraformRating),
    "mat" -> js(player.mat),
    "cards" -> arr(player.cards.map(str)),
    "tiles" -> arr(player.tiles.map(js)),
    "turnState" -> js(player.turnState),
  )

  def js(game: Game): JsonNode = obj(
    "seed" -> js(game.seed),
    "board" -> js(game.board),
    "players" -> arr(game.players.map(js)),
    // "turnOrder" -> js(game.turnOrder),
    "gameStage" -> js(game.gameStage),
  )

  def apply(game: Game): String =
    mapper.writeValueAsString(js(game))
}

