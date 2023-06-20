package mars
package resource

final class Resources(
  val resources: ListMap[Resource, Int],
) {
  def +(other: Resources): Resources = {
    Resources(other.resources.foldLeft(resources) { case (acc, (res, amount)) =>
      acc + ((res, acc.getOrElse(res, 0) + amount))
    })
  }

  // TODO: stuff like this should be moved to the logger / presenter module
  override def toString: String = {
    import Resource._
    val groups = (resources map {
      case (res, 1) => s"1 $res"
      case (res @ (MegaCredit | Plant | Animal | Microbe | Fighter), amount) => s"$amount ${res}s" // pluralable
      case (res, amount) => s"$amount ${res}"
    }).toIndexedSeq
    if (groups.size > 1) {
      groups.init.mkString(", ") + " and " + groups.last
    } else {
      groups.last
    }
  }
}
