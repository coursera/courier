package org.coursera.courier.fixture.generator

import java.util

import com.linkedin.data.ByteString
import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.DirectCoercer
import org.coursera.courier.codecs.InlineStringCodec
import org.coursera.courier.templates.ScalaEnumTemplateSymbol
import org.coursera.courier.templates.ScalaRecordTemplate

import scala.collection.immutable

// TODO amory: scaladoc
sealed trait PegasusCompatibleValueGenerator
sealed trait ValueGenerator[+K <: AnyRef] {

  def next(): K

  def nextKey(): String = {
    val data = next() match {
      case template: DataTemplate[_] => template.data().asInstanceOf[AnyRef]
      case other: AnyRef => other
    }
    InlineStringCodec.dataToString(data)
  }

  def map[V <: AnyRef](transform: K => V): ValueGenerator[V] = {
    val delegate = this
    new ValueGenerator[V] {
      override def next(): V = transform(delegate.next())
    }
  }
}

trait RecordValueGenerator[K <: ScalaRecordTemplate] extends ValueGenerator[K] {
  override def nextKey(): String = InlineStringCodec.dataToString(next().data())
}

trait EnumSymbolGenerator[K <: ScalaEnumTemplateSymbol]
  extends ValueGenerator[K]
  with PegasusCompatibleValueGenerator {
  val symbols: immutable.Iterable[K]
  override def nextKey(): String = next().toString()
}

trait CoercedValueGenerator[K] extends ValueGenerator[AnyRef] with PegasusCompatibleValueGenerator {

  val coercer: DirectCoercer[K]

  def nextValue(): K

  final override def next(): AnyRef = coercer.coerceInput(nextValue())
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
    fieldGenerators: Map[String, ValueGenerator[_ <: AnyRef]])
  extends ValueGenerator[DataMap]
  with PegasusCompatibleValueGenerator {

  override def next(): DataMap = {
    val data = new DataMap()

    fieldGenerators.foreach { case (name, generator) =>
      generator.next() match {
        case None => ()
        case Some(value: AnyRef) => data.put(name, value)
        case value: AnyRef => data.put(name, value)
      }
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

final class MapValueGenerator[V <: AnyRef](
    keyGenerator: ValueGenerator[_ <: AnyRef],
    valueGenerator: ValueGenerator[V],
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

sealed trait PrimitiveValueGenerator[+K <: AnyRef]
  extends ValueGenerator[K]
  with PegasusCompatibleValueGenerator

sealed trait UnboxedValueGenerator[U <: AnyVal, K <: AnyRef] extends PrimitiveValueGenerator[K] {
  def nextUnboxed(): U
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

trait StringValueGenerator extends PrimitiveValueGenerator[String] {

  final override def nextKey(): String = next()

}

trait BytesValueGenerator extends PrimitiveValueGenerator[ByteString] {

  final override def next(): ByteString = ByteString.copy(nextBytes())

  def nextBytes(): Array[Byte]
}

trait FixedBytesValueGenerator extends PrimitiveValueGenerator[ByteString] {

  final override def next(): ByteString = ByteString.copy(nextBytes())
  final override def nextKey(): String = nextBytes().toString

  def nextBytes(): Array[Byte]

}

