package org.coursera.courier.fixture

import org.coursera.courier.fixture.generator.FixedBytesValueGenerator
import org.coursera.courier.fixture.generator.FixedLengthStringGenerator
import org.coursera.courier.fixture.generator.IntegerRangeFixedBytesGenerator
import org.coursera.courier.fixture.generator.SpanningDoubleValueGenerator
import org.coursera.fixed.Fixed8
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitSuite

class PrimitiveGeneratorTest extends JUnitSuite with AssertionsForJUnit {

  @Test
  def DoubleValueGenerator_Oto1(): Unit = {
    val generator = new SpanningDoubleValueGenerator(0.0, 1.0)
    val numerator =   List(0, 1, 1, 1, 3, 1, 3, 5, 7)
    val denominator = List(1, 1, 2, 4, 4, 8, 8, 8, 8)
    val expected = numerator.zip(denominator).map { case (num, denom) => num.toDouble / denom }
    assertResult(expected)(expected.map(_ => generator.next()))
  }

  @Test
  def FixedLengthStringGenerator_ShortSuffix_Pads(): Unit = {
    val generator = new FixedLengthStringGenerator("prefix", 10)
    val expected = (0 to 9).map(i => s"prefix000$i") :+ "prefix0010"
    assertResult(expected)(expected.map(_ => generator.next()))
  }

  @Test
  def FixedLengthStringGenerator_LongSuffix_Truncates(): Unit = {
    val generator = new FixedLengthStringGenerator("prefix", 6)
    val expected = (0 to 9).map(i => s"prefi$i") :+ "pref10"
    assertResult(expected)(expected.map(_ => generator.next()))
  }


  @Test
  def FixedBytes(): Unit = {
    val generator = new IntegerRangeFixedBytesGenerator(8).map(Fixed8(_))
    val results = (0 to 10).map(_ => generator.next().data())
    assertResult(results.size)(results.toSet.size)
  }

}
