

package org.coursera.courier.data

import javax.annotation.Generated

import com.linkedin.data.ByteString
import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.template.DataTemplate
import org.coursera.courier.companions.ArrayCompanion
import org.coursera.courier.templates.DataTemplates
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.courier.templates.ScalaArrayTemplate
import scala.collection.GenTraversable
import scala.collection.JavaConverters._
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable
import com.linkedin.data.template.Custom

@Generated(value = Array("DoubleArray"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class DoubleArray(private val dataList: DataList)
  extends IndexedSeq[Double]
  with Product
  with GenTraversable[Double]
  with DataTemplate[DataList]
  with ScalaArrayTemplate {

  override def length: Int = dataList.size()

  private[this] lazy val list = dataList.asScala.map(coerceInput)

  private[this] def coerceInput(any: AnyRef): Double = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Double])

  }

  override def apply(idx: Int): Double = list(idx)

  override def productElement(n: Int): Any = dataList.get(n)
  override def productArity: Int = dataList.size()

  override def schema(): DataSchema = DoubleArray.SCHEMA

  override def data(): DataList = dataList
  override def copy(): DataTemplate[DataList] = this
  override def copy(dataList: DataList, conversion: DataConversion): ScalaArrayTemplate =
    DoubleArray.build(dataList, conversion)
}

object DoubleArray extends ArrayCompanion[DoubleArray] {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"array","items":"double"}""").asInstanceOf[ArrayDataSchema]

  val empty = DoubleArray()

  def apply(elems: Double*): DoubleArray = {
    new DoubleArray(new DataList(elems.map(coerceOutput).toList.asJava))
  }

  def apply(collection: Traversable[Double]): DoubleArray = {
    new DoubleArray(new DataList(collection.map(coerceOutput).toList.asJava))
  }

  def build(dataList: DataList, conversion: DataConversion): DoubleArray = {
    new DoubleArray(DataTemplates.makeImmutable(dataList, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[DoubleArray, Double, DoubleArray] {
    def apply(from: DoubleArray) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: DoubleArray) extends mutable.Builder[Double, DoubleArray] {
    def this() = this(new DoubleArray(new DataList()))

    val elems = new DataList(initial.data())

    def +=(x: Double): this.type = {
      elems.add(coerceOutput(x))
      this
    }

    def clear() = {
      elems.clear()
    }

    def result() = {
      elems.makeReadOnly()
      new DoubleArray(elems)
    }
  }

  private def coerceOutput(value: Double): AnyRef = {

    DataTemplateUtil.coerceInput(Double.box(value), classOf[java.lang.Double], classOf[java.lang.Double])

  }

  implicit def wrap(traversable: Traversable[Double]): DoubleArray = {
    DoubleArray(traversable)
  }
}

