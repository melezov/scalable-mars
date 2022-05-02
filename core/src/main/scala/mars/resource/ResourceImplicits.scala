package mars
package resource

trait ResourceImplicits {
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
