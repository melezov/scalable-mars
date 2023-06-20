package mars
package board

final class GlobalParameters private(
  val oxygen: GlobalParameter.OxygenTrack,
  val temperature: GlobalParameter.TemperatureTrack,
  val oceans: GlobalParameter.OceanTiles,
  val venus: Option[GlobalParameter.VenusScale],
) {
  private[this] def copy(
    oxygen: GlobalParameter.OxygenTrack = oxygen,
    temperature: GlobalParameter.TemperatureTrack = temperature,
    oceans: GlobalParameter.OceanTiles = oceans,
    venus: Option[GlobalParameter.VenusScale] = venus,
  ): GlobalParameters = GlobalParameters(oxygen, temperature, oceans, venus)

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

  def increaseVenus(): IO[GlobalParameter.Err, (GlobalParameters, Seq[ActionBonus])] =
    venus.fold(ZIO.fail(GlobalParameter.Err.VenusNotActive)) { v =>
      v.advance() map { case (newVenus, sab) =>
        (copy(venus = Some(newVenus)), sab)
      }
    }
  
  def areMaxed: Boolean = oxygen.isMaxed && temperature.isMaxed && oceans.isMaxed
}

object GlobalParameters {
  def start(venus: Boolean): GlobalParameters = GlobalParameters(
    GlobalParameter.OxygenTrack.Start,
    GlobalParameter.TemperatureTrack.Start,
    GlobalParameter.OceanTiles.Start,
    if (venus) Some(GlobalParameter.VenusScale.Start) else None,
  )
}
