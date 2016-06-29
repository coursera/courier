package org.coursera.courier.mock

import scala.collection.immutable

trait EnumSymbolGenerator extends  StringValueGenerator {

  val symbols: immutable.Seq[String]

}

class CyclicEnumSymbolGenerator(val symbols: immutable.Seq[String]) extends EnumSymbolGenerator {

  private var previousIndex: Int = -1

  override def next(): String = {
    previousIndex = (previousIndex + 1) % symbols.size
    symbols(previousIndex)
  }

}