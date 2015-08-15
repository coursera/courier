

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

@Generated(value = Array("LongArray"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class LongArray(private val dataList: DataList)
  extends IndexedSeq[Long]
  with Product
  with GenTraversable[Long]
  with DataTemplate[DataList] {

  override def length: Int = dataList.size()

  private[this] lazy val list = dataList.asScala.map(coerceInput)

  private[this] def coerceInput(any: AnyRef): Long = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Long])

  }

  override def apply(idx: Int): Long = list(idx)

  override def productElement(n: Int): Any = dataList.get(n)
  override def productArity: Int = dataList.size()

  override def schema(): DataSchema = LongArray.SCHEMA

  override def data(): DataList = dataList
  override def copy(): DataTemplate[DataList] = this
}

object LongArray {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"array","items":"long"}""").asInstanceOf[ArrayDataSchema]

  val empty = LongArray()

  def apply(elems: Long*): LongArray = {
    new LongArray(new DataList(elems.map(coerceOutput).toList.asJava))
  }

  def apply(collection: Traversable[Long]): LongArray = {
    new LongArray(new DataList(collection.map(coerceOutput).toList.asJava))
  }

  def apply(dataList: DataList, conversion: DataConversion): LongArray = {
    new LongArray(DataTemplates.makeImmutable(dataList, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[LongArray, Long, LongArray] {
    def apply(from: LongArray) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: LongArray) extends mutable.Builder[Long, LongArray] {
    def this() = this(new LongArray(new DataList()))

    val elems = new DataList(initial.data())

    def +=(x: Long): this.type = {
      elems.add(coerceOutput(x))
      this
    }

    def clear() = {
      elems.clear()
    }

    def result() = {
      elems.makeReadOnly()
      new LongArray(elems)
    }
  }

  private def coerceOutput(value: Long): AnyRef = {

    DataTemplateUtil.coerceInput(Long.box(value), classOf[java.lang.Long], classOf[java.lang.Long])

  }

  implicit def wrap(traversable: Traversable[Long]): LongArray = {
    LongArray(traversable)
  }
}

