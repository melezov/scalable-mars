package mars
package corporation

abstract class Corporation(val name: String, val resources: Resources)

object Corporation {
  val all: IndexedSeq[Corporation] = IndexedSeq(
    BeginnerCorporation,
  )
}
