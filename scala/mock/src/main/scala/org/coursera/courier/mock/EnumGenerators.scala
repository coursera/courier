package org.coursera.courier.mock

import org.coursera.courier.templates.ScalaEnumTemplateSymbol


abstract class CyclicSymbolGenerator[K](symbols: Set[K]) {

  private var previousIndex: Int = -1
  private val symbolsList = symbols.toSeq

  def next(): K = {
    previousIndex = (previousIndex + 1) % symbols.size
    symbolsList(previousIndex)
  }

}

class CyclicEnumSymbolGenerator[K <: ScalaEnumTemplateSymbol](val symbols: Set[K])
  extends CyclicSymbolGenerator[K](symbols) with EnumSymbolGenerator[K]

class CyclicEnumStringGenerator(val symbols: Set[String])
  extends CyclicSymbolGenerator[String](symbols) with StringValueGenerator

