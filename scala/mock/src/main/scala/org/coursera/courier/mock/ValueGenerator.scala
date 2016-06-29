package org.coursera.courier.mock

import java.util

import com.linkedin.data.Data
import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import org.coursera.courier.templates.ScalaRecordTemplate

sealed trait ValueGenerator[K <: AnyRef] {

  def next(): K

}

class ConstantValueGenerator[K <: AnyRef](value: K) extends ValueGenerator[K] {
  // TODO amory: verify that value is of valid type
  //  require(Data.isAllowed(value), "Value must be of data type")
  def next(): K = value
}

sealed trait PrimitiveValueGenerator[K <: AnyRef] extends ValueGenerator[K]

sealed trait UnboxedValueGenerator[U <: AnyVal, K <: AnyRef]
  extends PrimitiveValueGenerator[K] {

  def nextUnboxed(): U

}

sealed trait StringKeyGenerator {

  def nextKey(): String

}

trait BooleanValueGenerator
  extends UnboxedValueGenerator[Boolean, java.lang.Boolean]
  with StringKeyGenerator {

  final override def next(): java.lang.Boolean = Boolean.box(nextUnboxed())

  final override def nextKey(): String = next().toString // TODO amory: check this

}

trait IntegerValueGenerator
  extends UnboxedValueGenerator[Int, Integer]
  with StringKeyGenerator {

  final override def next(): Integer = Int.box(nextUnboxed())

  final override def nextKey(): String = next().toString // TODO amory: check this

}

trait LongValueGenerator extends UnboxedValueGenerator[Long, java.lang.Long] {

  final override def next(): java.lang.Long = Long.box(nextUnboxed())

}

trait FloatValueGenerator extends UnboxedValueGenerator[Float, java.lang.Float] {

  final override def next(): java.lang.Float = Float.box(nextUnboxed())

}

trait DoubleValueGenerator extends UnboxedValueGenerator[Double, java.lang.Double] {

  final override def next(): java.lang.Double = Double.box(nextUnboxed())

}

trait StringValueGenerator extends PrimitiveValueGenerator[String] with StringKeyGenerator {

  final override def nextKey(): String = next()

}

trait RecordValueGenerator[R <: ScalaRecordTemplate]
  extends ValueGenerator[DataMap]
  with StringKeyGenerator {

  final override def next(): DataMap = nextRecord().data()

  final override def nextKey(): String = ???

  def nextRecord(): R

}

final class DataMapValueGenerator(
    fieldGenerators: Map[String, ValueGenerator[_ <: AnyRef]]) extends ValueGenerator[DataMap] {

  override def next(): DataMap = {
    val data = new DataMap()

    fieldGenerators.foreach { case (name, generator) =>
      data.put(name, generator.next())
    }

    data.makeReadOnly()
    data
  }

}

class ListValueGenerator[V <: ValueGenerator[_ <: AnyRef]](
    itemGenerator: V,
    listLength: Int)
  extends ValueGenerator[DataList] {

  final override def next(): DataList = {
    val list = new util.ArrayList[AnyRef](listLength)
    (1 to listLength).foreach(_ => list.add(itemGenerator.next()))
    new DataList(list)
  }

}

class MapValueGenerator[K <: StringKeyGenerator, V <: ValueGenerator[_ <: AnyRef]](
    keyGenerator: K,
    valueGenerator: V,
    listLength: Int)
  extends ValueGenerator[DataMap] {

  final override def next(): DataMap = {
    val data = new DataMap()
    (1 to listLength).foreach {_ =>
      data.put(keyGenerator.nextKey(), valueGenerator.next())
    }
    data.makeReadOnly()
    data
  }

}