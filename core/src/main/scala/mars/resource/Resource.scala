package mars
package resource

sealed trait Pluralable
enum Resource {
  case MegaCredit extends Resource with Pluralable
  case Steel
  case Titanium
  case Plant extends Resource with Pluralable
  case Energy
  case Heat
  case Animal extends Resource with Pluralable
  case Microbe extends Resource with Pluralable
  case Fighter extends Resource with Pluralable
  case Science
}

final class Resources(
  val resources: ListMap[Resource, Int],
) {
  def +(other: Resources): Resources = {
    Resources(other.resources.foldLeft(resources) { case (acc, (res, amount)) =>
      acc + ((res, acc.getOrElse(res, 0) + amount))
    })
  }

  override def toString: String = {
    val res = (resources map {
      case (res, 1) => s"1 $res"
      case (res: Resource with Pluralable, amount) => s"$amount ${res}s"
      case (res, amount) => s"$amount ${res}"
    }).toIndexedSeq
    if (res.size > 1) {
      res.init.mkString(", ") + " and " + res.last
    } else {
      res.last
    }
  }
}

trait ResourceConversions {
  extension(amount: Int) {
    private[this] def resource(resource: Resource): Resources =
      Resources(ListMap(resource -> amount))
    def MegaCredit: Resources = resource(Resource.MegaCredit)
    def Steel: Resources = resource(Resource.Steel)
    def Titanium: Resources = resource(Resource.Titanium)
    def Plant: Resources = resource(Resource.Plant)
    def Energy: Resources = resource(Resource.Energy)
    def Heat: Resources = resource(Resource.Heat)
    def Animal: Resources = resource(Resource.Animal)
    def Microbe: Resources = resource(Resource.Microbe)
    def Fighter: Resources = resource(Resource.Fighter)
    def Science: Resources = resource(Resource.Science)

    def MegaCredits: Resources = MegaCredit
    def Plants: Resources = Plant
    def Animals: Resources = Animal
    def Microbes: Resources = Microbe
  }
}
