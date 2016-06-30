package org.coursera.courier.mock

import java.util

import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.template.DirectCoercer
import org.coursera.courier.templates.ScalaRecordTemplate

import scala.collection.immutable

// TODO amory: scaladoc

sealed trait ValueGenerator[+K <: AnyRef] {

  def next(): K

}

trait RecordValueGenerator[K <: ScalaRecordTemplate] extends ValueGenerator[K]

class CoercedValueGenerator[K](
    values: Iterable[K],
    coercer: DirectCoercer[K]) extends ValueGenerator[AnyRef] {

  final override def next(): AnyRef = coercer.coerceInput(iterator.next())

  private[this] val iterator = values.iterator
}

final class ConstantValueGenerator[K <: AnyRef](value: K) extends ValueGenerator[K] {

  override def next(): K = value

}

final class CyclicGenerator[+K <: AnyRef] (generators: immutable.Seq[ValueGenerator[_ <: K]])
  extends ValueGenerator[K] {

  private var previousIndex: Int = -1

  override def next(): K = {
    previousIndex = (previousIndex + 1) % generators.size
    generators(previousIndex).next()
  }
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

final class ListValueGenerator[V <: ValueGenerator[_ <: AnyRef]](
    itemGenerator: V,
    listLength: Int)
  extends ValueGenerator[DataList] {

  override def next(): DataList = {
    val list = new util.ArrayList[AnyRef](listLength)
    (1 to listLength).foreach(_ => list.add(itemGenerator.next()))
    new DataList(list)
  }
}

final class MapValueGenerator[K <: StringKeyGenerator, V <: ValueGenerator[_ <: AnyRef]](
    keyGenerator: K,
    valueGenerator: V,
    listLength: Int)
  extends ValueGenerator[DataMap] {

  override def next(): DataMap = {
    val data = new DataMap()
    (1 to listLength).foreach {_ =>
      data.put(keyGenerator.nextKey(), valueGenerator.next())
    }
    data.makeReadOnly()
    data
  }
}

sealed trait PrimitiveValueGenerator[+K <: AnyRef] extends ValueGenerator[K]

sealed trait UnboxedValueGenerator[U <: AnyVal, K <: AnyRef]
  extends PrimitiveValueGenerator[K] with StringKeyGenerator {

  def nextUnboxed(): U

  final override def nextKey(): String = nextUnboxed().toString

}

sealed trait StringKeyGenerator {

  def nextKey(): String

}

trait BooleanValueGenerator
  extends UnboxedValueGenerator[Boolean, java.lang.Boolean] {

  final override def next(): java.lang.Boolean = Boolean.box(nextUnboxed())

}

trait IntegerValueGenerator
  extends UnboxedValueGenerator[Int, Integer] {

  final override def next(): Integer = Int.box(nextUnboxed())

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
