package mars
package tile
package impl

import zio.test.*

object BoardParserSpec extends ZIOSpecDefault {
  def spec =
    suite("BoardParser Spec")(
      test("Can reconstruct empty board from BoardParser.positions") {
        val expectedLines = io.Source.fromResource("mars/board/empty.txt").getLines()
        val expectedBody = expectedLines.mkString("\n", "\n", "\n")

        val eqRow = BoardParser.positions.find(_._3.onEquator).get._1

        val sb = new StringBuilder
        var lastRow = 0
        for ((row, pos, lat) <- BoardParser.positions) {
          if (row > lastRow) {
            sb += '\n' ++= s"  " * math.abs(row - eqRow)
            lastRow = row
          }
          sb ++= s" $row$pos "
        }

        val actualBody = (sb += '\n').toString.replaceAll(" +\n", "\n")
        assertTrue(actualBody == expectedBody)
      }
    )
}
