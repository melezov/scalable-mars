package mars

import zio.*
import zio.ZIO.debug
import zio.test.*
import zio.test.Assertion.*

import java.io.IOException

object BoardSpec extends DefaultRunnableSpec {
  def spec = suite("Adjacencies")(
    test("Noctis City inspection") {
      val board = Board.Tharsis
      val nc = board.tiles.find(_.`type` == TileType.NoctisCity).get
      assert(nc.coords)(equalTo(TileCoords(row = 5, pos = 3))) &&
      assert(nc.placementBonuses)(equalTo(Seq(TilePlacementBonus.Plant, TilePlacementBonus.Plant))) &&
      assert(board.adjacencies(nc.coords).view.mapValues(_.placementBonuses).toSeq)(equalTo(Seq(
        TileAdjacency.NE -> Seq(TilePlacementBonus.Plant),
        TileAdjacency.NW -> Seq(TilePlacementBonus.Plant),
        TileAdjacency.E  -> Seq(TilePlacementBonus.Plant, TilePlacementBonus.Plant),
        TileAdjacency.W  -> Seq(TilePlacementBonus.Plant, TilePlacementBonus.Plant),
        TileAdjacency.SE -> Seq(TilePlacementBonus.Plant, TilePlacementBonus.Plant),
        TileAdjacency.SW -> Seq(TilePlacementBonus.Plant),
      )))
    }
  )
}
