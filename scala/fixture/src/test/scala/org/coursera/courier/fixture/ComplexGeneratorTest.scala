package org.coursera.courier.fixture

import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import org.coursera.courier.fixture.generator.DataMapValueGenerator
import org.coursera.courier.fixture.generator.IntegerRangeGenerator
import org.coursera.courier.fixture.generator.ListValueGenerator
import org.coursera.courier.fixture.generator.MapValueGenerator
import org.coursera.courier.fixture.generator.PrefixedStringGenerator
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitSuite

import scala.collection.JavaConverters._

class ComplexGeneratorTest extends JUnitSuite with AssertionsForJUnit {

  @Test
  def dataMapValueGenerator: Unit = {
    val generator = new DataMapValueGenerator(
      Map(
        "int" -> new IntegerRangeGenerator(),
        "string" -> new PrefixedStringGenerator("string")))

    val expected = new DataMap()
    expected.put("int", Int.box(0))
    expected.put("string", "string0")

    assertResult(expected)(generator.next())
  }

  @Test
  def mapValueGenerator: Unit = {
    val generator = new MapValueGenerator(
      keyGenerator = new PrefixedStringGenerator("key"),
      valueGenerator = new PrefixedStringGenerator("value"),
      listLength = 5)

    val expected = new DataMap()
    (0 to 4).foreach { i =>
      expected.put(s"key$i", s"value$i")
    }

    assertResult(expected)(generator.next())
  }

  @Test
  def listValueGenerator: Unit = {
    val generator = new ListValueGenerator(
      itemGenerator = new PrefixedStringGenerator("value"),
      listLength = 5)

    val expected = new DataList((0 to 4).map(i => s"value$i").toList.asJava)

    assertResult(expected)(generator.next())
  }
}
