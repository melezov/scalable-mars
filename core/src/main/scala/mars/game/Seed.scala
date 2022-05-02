package mars.game

import org.apache.commons.math3.random.MersenneTwister

import java.nio.charset.StandardCharsets.UTF_8
import java.security.{MessageDigest, SecureRandom}

final class Seed private(val seed: Long) {
  private[this] val mt = MersenneTwister(seed)
}

object Seed {
  private[this] val HexPattern = """0x([0-9a-fA-F]+)""".r
  private[this] val seeder: SecureRandom = SecureRandom()

  def random(): Seed = {
    Seed(seeder.nextLong())
  }

  def parse(value: String): Seed = {
    val seed = value match {
      case HexPattern(hex) =>
        java.lang.Long.parseLong(hex, 0x10)
      case unknownFormat =>
        val digest = MessageDigest.getInstance("SHA-1")
          .digest(unknownFormat.getBytes(UTF_8))
          .takeRight(java.lang.Long.BYTES)
        BigInt(digest).longValue
    }
    Seed(seed)
  }
}
