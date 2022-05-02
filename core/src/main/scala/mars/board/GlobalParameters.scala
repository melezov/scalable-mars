package mars
package board

final class GlobalParameters private(
  val oxygen: GlobalParameter.OxygenTrack,
  val temperature: GlobalParameter.TemperatureTrack,
  val oceans: GlobalParameter.OceanTiles,
) {
  private[this] def copy(
    oxygen: GlobalParameter.OxygenTrack = oxygen,
    temperature: GlobalParameter.TemperatureTrack = temperature,
    oceans: GlobalParameter.OceanTiles = oceans,
  ): GlobalParameters = GlobalParameters(
    oxygen = oxygen,
    temperature = temperature,
    oceans = oceans,
  )

  def increaseOxygen(): IO[GlobalParameter.Err, (GlobalParameters, Seq[ActionBonus])] =
    oxygen.advance() map { case (newOxygen, sab) =>
      (copy(oxygen = newOxygen), sab)
    }

  def increaseTemperature(): IO[GlobalParameter.Err, (GlobalParameters, Seq[ActionBonus])] =
    temperature.advance() map { case (newTemperature, sab) =>
      (copy(temperature = newTemperature), sab)
    }

  def placeOcean(): IO[GlobalParameter.Err, (GlobalParameters, Seq[ActionBonus])] =
    oceans.advance() map { case (newOceans, sab) =>
      (copy(oceans = newOceans), sab)
    }

  def areMaxed: Boolean = oxygen.isMaxed && temperature.isMaxed && oceans.isMaxed
}

object GlobalParameters {
  val Start: GlobalParameters = GlobalParameters(
    oxygen = GlobalParameter.OxygenTrack.Start,
    temperature = GlobalParameter.TemperatureTrack.Start,
    oceans = GlobalParameter.OceanTiles.Start,
  )
}
