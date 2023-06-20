package mars
package player

final class Mat private(
  val megaCredits: Int = 0,
  val megaCreditProduction: Int = 0,
  val steel: Int = 0,
  val steelProduction: Int = 0,
  val titanium: Int = 0,
  val titaniumProduction: Int = 0,
  val plants: Int = 0,
  val plantProduction: Int = 0,
  val energy: Int = 0,
  val energyProduction: Int = 0,
  val heat: Int = 0,
  val heatProduction: Int = 0,
) {
  private[this] def offset(
    deltaMegaCredits: Int = 0,
    deltaMegaCreditProduction: Int = 0,
    deltaSteel: Int = 0,
    deltaSteelProduction: Int = 0,
    deltaTitanium: Int = 0,
    deltaTitaniumProduction: Int = 0,
    deltaPlants: Int = 0,
    deltaPlantProduction: Int = 0,
    deltaEnergy: Int = 0,
    deltaEnergyProduction: Int = 0,
    deltaHeat: Int = 0,
    deltaHeatProduction: Int = 0,
  ): Mat = Mat(
    megaCredits = megaCredits + deltaMegaCredits,
    megaCreditProduction = megaCreditProduction + deltaMegaCreditProduction,
    steel = steel + deltaSteel,
    steelProduction = steelProduction + deltaSteelProduction,
    titanium = titanium + deltaTitanium,
    titaniumProduction = titaniumProduction + deltaTitaniumProduction,
    plants = plants + deltaPlants,
    plantProduction = plantProduction + deltaPlantProduction,
    energy = energy + deltaEnergy,
    energyProduction = energyProduction + deltaEnergyProduction,
    heat = heat + deltaHeat,
    heatProduction = heatProduction + deltaHeatProduction,
  )

  def consumeResources(
    megaCredits: Int = 0,
    steel: Int = 0,
    titanium: Int = 0,
    plants: Int = 0,
    energy: Int = 0,
    heat: Int = 0,
  ): IO[Mat.Err.InsufficientResources.type, Mat] =
    if (this.megaCredits < megaCredits ||
        this.steel < steel ||
        this.titanium < titanium ||
        this.plants < plants ||
        this.energy < energy ||
        this.heat < heat) { IO.fail(Mat.Err.InsufficientResources) } else {
      IO.succeed(offset(
        deltaMegaCredits = -megaCredits,
        deltaSteel = -steel,
        deltaTitanium = -titanium,
        deltaPlants = -plants,
        deltaEnergy = -energy,
        deltaHeat = -heat,
      ))
    }

  def reduceProduction(
    megaCreditProduction: Int = 0,
    steelProduction: Int = 0,
    titaniumProduction: Int = 0,
    plantProduction: Int = 0,
    energyProduction: Int = 0,
    heatProduction: Int = 0,
  ): IO[Mat.Err.InsufficientProduction.type, Mat] =
    if (this.megaCreditProduction - megaCreditProduction < Mat.MinimumMegaCreditProduction ||
        this.steelProduction - steelProduction < Mat.MinimumSteelProduction ||
        this.titaniumProduction - titaniumProduction < Mat.MinimumTitaniumProduction ||
        this.plantProduction - plantProduction < Mat.MinimumPlantProduction ||
        this.energyProduction - energyProduction < Mat.MinimumEnergyProduction ||
        this.heatProduction - heatProduction < Mat.MinimumHeatProduction) { IO.fail(Mat.Err.InsufficientProduction) } else {
      IO.succeed(offset(
        deltaMegaCreditProduction = -megaCreditProduction,
        deltaSteelProduction = -steelProduction,
        deltaTitaniumProduction = -titaniumProduction,
        deltaPlantProduction = -plantProduction,
        deltaEnergyProduction = -energyProduction,
        deltaHeatProduction = -heatProduction,
      ))
    }

  def produce(terraformRating: Int): Mat = offset(
    deltaMegaCredits = megaCreditProduction + terraformRating,
    deltaSteel = steelProduction,
    deltaTitanium = titaniumProduction,
    deltaPlants = plantProduction,
    deltaEnergy = energyProduction - energy,
    deltaHeat = heatProduction + energy,
  )
}

object Mat {
  sealed trait Err extends MarsErr
  object Err {
    case object InsufficientResources extends Err
    case object InsufficientProduction extends Err
  }

  val MinimumMegaCreditProduction: Int = -5
  val MinimumSteelProduction: Int = 0
  val MinimumTitaniumProduction: Int = 0
  val MinimumPlantProduction: Int = 0
  val MinimumEnergyProduction: Int = 0
  val MinimumHeatProduction: Int = 0

  val Start: Mat = Mat()
}
