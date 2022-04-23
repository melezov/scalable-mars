package mars

import zio.*
import zio.ZIO.debug
import zio.test.*
import zio.test.Assertion.*

import java.io.IOException

object GlobalParametersSpec extends DefaultRunnableSpec {
  def spec = suite("Synergies")(
    testM("Bonus ocean at 0 degrees") {
      val gp = GlobalParameters.Start
      val temperatureBump = UIO.succeed(Action.IncreaseTemperature)
      val `gp at -28` = gp.apply(temperatureBump)
      assertM(`gp at -28`.map(_._1.temperature.current))(equalTo(-24))

      val `gp at 0` = (-26 to 0 by 2).foldLeft(`gp at -28`){ case (gp, temperature) =>
        gp.flatMap(_._1.apply(temperatureBump))
      }
      assertM(`gp at 0`.map(_._1.temperature.current))(equalTo(0))
      assertM(`gp at 0`.map(_._2))(contains(ActionBonus.BonusOcean))
    }
  )
}
