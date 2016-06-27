package org.coursera.courier.fixture

import org.coursera.courier.fixture.generator.CyclicEnumSymbolGenerator
import org.coursera.maps.Toggle
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitSuite

class EnumGeneratorTest extends JUnitSuite with AssertionsForJUnit {

  @Test
  def toggle(): Unit = {
    val generator: CyclicEnumSymbolGenerator[Toggle] = new CyclicEnumSymbolGenerator(Toggle)
    val symbols = (0 to 100).map(_ => generator.next())
    assertResult(Toggle.symbols - Toggle.withName("$UNKNOWN"))(symbols.toSet)
  }

}
