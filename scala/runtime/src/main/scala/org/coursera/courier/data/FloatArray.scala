

package org.coursera.courier.data

import javax.annotation.Generated

import com.linkedin.data.ByteString
import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.template.DataTemplate
import org.coursera.courier.templates.DataTemplates
import org.coursera.courier.templates.DataTemplates.DataConversion
import scala.collection.GenTraversable
import scala.collection.JavaConverters._
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable
import com.linkedin.data.template.Custom

@Generated(value = Array("FloatArray"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class FloatArray(private val dataList: DataList)
  extends IndexedSeq[Float]
  with Product
  with GenTraversable[Float]
  with DataTemplate[DataList] {

  override def length: Int = dataList.size()

  private[this] lazy val list = dataList.asScala.map(coerceInput)

  private[this] def coerceInput(any: AnyRef): Float = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Float])

  }

  override def apply(idx: Int): Float = list(idx)

  override def productElement(n: Int): Any = dataList.get(n)
  override def productArity: Int = dataList.size()

  override def schema(): DataSchema = FloatArray.SCHEMA

  override def data(): DataList = dataList
  override def copy(): DataTemplate[DataList] = this
}

object FloatArray {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"array","items":"float"}""").asInstanceOf[ArrayDataSchema]

  val empty = FloatArray()

  def apply(elems: Float*): FloatArray = {
    new FloatArray(new DataList(elems.map(coerceOutput).toList.asJava))
  }

  def apply(collection: Traversable[Float]): FloatArray = {
    new FloatArray(new DataList(collection.map(coerceOutput).toList.asJava))
  }

  def apply(dataList: DataList, conversion: DataConversion): FloatArray = {
    new FloatArray(DataTemplates.makeImmutable(dataList, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[FloatArray, Float, FloatArray] {
    def apply(from: FloatArray) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: FloatArray) extends mutable.Builder[Float, FloatArray] {
    def this() = this(new FloatArray(new DataList()))

    val elems = new DataList(initial.data())

    def +=(x: Float): this.type = {
      elems.add(coerceOutput(x))
      this
    }

    def clear() = {
      elems.clear()
    }

    def result() = {
      elems.makeReadOnly()
      new FloatArray(elems)
    }
  }

  private def coerceOutput(value: Float): AnyRef = {

    DataTemplateUtil.coerceInput(Float.box(value), classOf[java.lang.Float], classOf[java.lang.Float])

  }

  implicit def wrap(traversable: Traversable[Float]): FloatArray = {
    FloatArray(traversable)
  }
}

