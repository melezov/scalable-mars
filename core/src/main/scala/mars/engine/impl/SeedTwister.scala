package mars
package engine
package impl

import org.apache.commons.math3.random.MersenneTwister
import org.apache.commons.math3.util.MathArrays

import java.nio.charset.StandardCharsets.UTF_8
import java.security.{MessageDigest, SecureRandom}
import scala.collection.mutable
import scala.reflect.ClassTag

final class SeedTwister private(val seed: Long) extends Seed {
  override def shuffleColors(colors: Set[Color]): IndexedSeq[Color] = {
    val orderedColors = colors.toSeq.sortBy(_.ordinal)
    shuffle(SeedTwister.SubSeed.ColorShuffle, orderedColors)
  }

  private[this] def shuffle[T : ClassTag](subSeed: Int, elements: Seq[T]): IndexedSeq[T] = {
    val mt = MersenneTwister(seed + subSeed)
    val src = elements.toArray

    val indices = src.indices.toArray
    MathArrays.shuffle(indices, mt)

    val dst = new Array[T](src.length)
    (src zip indices) foreach { case (element, position) =>
      dst(position) = element
    }

    dst.toIndexedSeq
  }
}

object SeedTwister {
  object SubSeed {
    val ColorShuffle = 0
  }

  private[this] val HexPattern = """0x([0-9a-fA-F]+)""".r
  private[this] val seeder: SecureRandom = SecureRandom()

  def random(): SeedTwister = {
    SeedTwister(seeder.nextLong())
  }

  def parse(value: String): SeedTwister = {
    val seed = value match {
      case HexPattern(hex) =>
        java.lang.Long.parseLong(hex, 0x10)
      case unknownFormat =>
        val digest = MessageDigest.getInstance("SHA-1")
          .digest(unknownFormat.getBytes(UTF_8))
          .takeRight(java.lang.Long.BYTES)
        BigInt(digest).longValue
    }
    SeedTwister(seed)
  }
}
