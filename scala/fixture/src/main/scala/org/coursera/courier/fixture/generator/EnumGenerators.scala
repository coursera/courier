package org.coursera.courier.fixture.generator

import org.coursera.courier.templates.ScalaEnumTemplate
import org.coursera.courier.templates.ScalaEnumTemplateSymbol

class CyclicEnumSymbolGenerator[K <: ScalaEnumTemplateSymbol](companion: ScalaEnumTemplate[K])
    extends EnumSymbolGenerator[K] {

  val symbols = companion.symbols - companion.withName("$UNKNOWN")

  private var previousIndex: Int = -1
  private val symbolsList = symbols.toSeq

  def next(): K = {
    previousIndex = (previousIndex + 1) % symbols.size
    symbolsList(previousIndex)
  }

}

class CyclicEnumStringGenerator(val symbols: Set[String]) extends StringValueGenerator {

  private var previousIndex: Int = -1
  private val symbolsList = symbols.toSeq

  def next(): String = {
    previousIndex = (previousIndex + 1) % symbols.size
    symbolsList(previousIndex)
  }

}

