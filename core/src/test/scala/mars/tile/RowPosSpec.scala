package mars
package tile

import zio.test.*

object RowPosSpec extends ZIOSpecDefault {
  def spec =
    suite("RowPos Spec")(
      test("Size sanity check") {
        assertTrue(RowPos.cache.size == 5 + 6 + 7 + 8 + 9 + 8 + 7 + 6 + 5)
      },
      test("Adjacency checks - Non Existent") {
        for (f <- RowPos.at(1, 6).isFailure) yield {
          assertTrue(f)
        }
      },
      test("Adjacency checks - NW corner") {
        for (nw <- RowPos.at(1, 1)) yield {
          assertTrue(nw.adjacencies.size == 3)
        }
      },
      test("Adjacency checks - Center") {
        for (nw <- RowPos.at(5, 5)) yield {
          assertTrue(nw.adjacencies.size == 6)
        }
      },
    )
}
