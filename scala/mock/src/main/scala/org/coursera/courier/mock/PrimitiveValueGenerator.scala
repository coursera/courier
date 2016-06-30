package org.coursera.courier.mock

import scala.collection.immutable

/**
 * Generates an alternating Boolean series `true`, `false`, `true`, `false`, ...
 */
class TrueFalseValueGenerator extends BooleanValueGenerator {
  var previous = false

  override def nextUnboxed(): Boolean = {
    previous = !previous
    previous
  }
}

/**
 * Generates a series of [[Int]] from `min` to `max`.
 *
 * @param min Minimum value in the generated range.
 * @param max Maximum value in the generated range.
 */
class IntegerRangeGenerator(
    min: Int = 0,
    max: Int = 1000) extends IntegerValueGenerator {
  require(min < max, s"Min $min must be less than max $max.")

  override def nextUnboxed(): Int = iterator.next()

  private[this] val iterator = (min to max).iterator
}

/**
 * Generates a series of [[Long]] from `min` to `max`.
 *
 * @param min Minimum value in the generated range.
 * @param max Maximum value in the generated range.
 */
class LongRangeGenerator(
    min: Long = 0,
    max: Long = 1000L) extends LongValueGenerator {
  require(min < max, s"Min $min must be less than max $max.")

  override def nextUnboxed(): Long = iterator.next()

  private[this] val iterator = (min to max).iterator
}


/**
 * Generates a series of [[Double]] values that fill the  interval [`min`, `max`].
 *
 * If `intGenerator` generates a series like 0, 1, 2, ...., values from the generator will start
 * with bounds `min` and `max` and then fill the interior of the interval a progressively refining
 * mesh values.
 *
 * @param min The minimum value to be generated.
 * @param max The maximum value to be generated.
 * @param intGenerator An integer generator whose results will be converted into [[Double]] values.
 */
class SpanningDoubleValueGenerator(
    min: Double = 0.0,
    max: Double = 1.0,
    intGenerator: IntegerValueGenerator = new IntegerRangeGenerator())
  extends DoubleValueGenerator {
  require(min < max, s"Min $min must be less than max $max.")

  override def nextUnboxed(): Double = {
    val i = intGenerator.nextUnboxed()
    val u = if (i < 2) {
      // Generate 0.0 and 1.0 as the first two elements
      i.toFloat
    } else {
      // Generate 1/2, 1/4, 3/4, 1/8, 3/8, 5/8, 7/8 ... for remainder of series
      val denom = 1 << (log2(i - 1) + 1)
      val num = ((i - 1) % (denom >> 1)) * 2 + 1.0
      num / denom
    }
    project(u)
  }

  /**
   * Base-2 logarithm floor
   */
  private[this] def log2(n: Int) = {
    if(n <= 0) throw new IllegalArgumentException()
    31 - Integer.numberOfLeadingZeros(n)
  }

  /**
   * Projects a value from the interval [0.0, 1.0] onto the interval [min, max].
   */
  private[this] def project(unit: Double): Double = {
    min + (max - min) * unit
  }
}

/**
 * See [[DoubleValueGenerator]], to which this generator delegates.
 *
 * @param min The minimum value to be generated.
 * @param max The maximum value to be generated.
 * @param intGenerator An integer generator whose results will be converted into [[Float]] values.
 */
class SpanningFloatValueGenerator(
    min: Float = 0.0f,
    max: Float = 1.0f,
    intGenerator: IntegerValueGenerator = new IntegerRangeGenerator())
  extends FloatValueGenerator {
  require(min < max, s"Min $min must be less than max $max.")

  override def nextUnboxed(): Float = doubleSequence.nextUnboxed().toFloat

  private[this] val doubleSequence = new SpanningDoubleValueGenerator(min, max, intGenerator)
}

/**
 * Generates a series of [[String]] values.
 *
 * @param prefix Prefix for generated strings.
 * @param intGenerator Generator whose results be appended to `prefix` to generate values.
 */
class PrefixedStringGenerator(
    prefix: String,
    intGenerator: IntegerValueGenerator = new IntegerRangeGenerator())
  extends StringValueGenerator {

  override def next(): String = s"$prefix${intGenerator.nextUnboxed()}"
}

/**
 * Generates a sequence of fixed-length strings with prefix `prefix` and an integer-value suffix.
 * Inserts zeros between the prefix and integer string if the result would otherwise be fewer
 * than `length` characters and truncates `prefix` if the result would otherwise be longer
 * than `length`.
 *
 * @param prefix Prefix for generated strings.
 * @param length Length of generated strings.
 * @param intGenerator Generator whose results be appended to `prefix` to generate values.
 */
class FixedLengthStringGenerator(
    prefix: String,
    length: Int,
    intGenerator: IntegerValueGenerator = new IntegerRangeGenerator())
  extends StringValueGenerator {

  require(length > 0, "Provided length must be greater than 0.")

  override def next(): String = {
    val nextIntString = intGenerator.nextUnboxed().toString
    val paddingLength = length - nextIntString.length - prefix.length
    if (paddingLength >= 0) {
      prefix + "0"*paddingLength + nextIntString
    } else {
      prefix.dropRight(-paddingLength) + nextIntString
    }
  }
}

class StringBytesValueGenerator(
    prefix: String,
    intGenerator: IntegerValueGenerator = new IntegerRangeGenerator())
  extends BytesValueGenerator {

  override def nextBytes(): Array[Byte] = s"$prefix${intGenerator.nextUnboxed()}".getBytes
}

class IntegerRangeFixedBytesGenerator(
    length: Int,
    intGenerator: IntegerValueGenerator = new IntegerRangeGenerator())
  extends FixedBytesValueGenerator {

  override def nextBytes(): Array[Byte] = {
    val bytes = intGenerator.next().toString.getBytes
    if (bytes.size < length) {
      bytes.padTo(length, Byte.MaxValue)
    } else {
      bytes.take(length)
    }
  }
}