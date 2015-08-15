

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

@Generated(value = Array("BytesArray"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class BytesArray(private val dataList: DataList)
  extends IndexedSeq[ByteString]
  with Product
  with GenTraversable[ByteString]
  with DataTemplate[DataList] {

  override def length: Int = dataList.size()

  private[this] lazy val list = dataList.asScala.map(coerceInput)

  private[this] def coerceInput(any: AnyRef): ByteString = {

    DataTemplateUtil.coerceOutput(any, classOf[com.linkedin.data.ByteString])

  }

  override def apply(idx: Int): ByteString = list(idx)

  override def productElement(n: Int): Any = dataList.get(n)
  override def productArity: Int = dataList.size()

  override def schema(): DataSchema = BytesArray.SCHEMA

  override def data(): DataList = dataList
  override def copy(): DataTemplate[DataList] = this
}

object BytesArray {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"array","items":"bytes"}""").asInstanceOf[ArrayDataSchema]

  val empty = BytesArray()

  def apply(elems: ByteString*): BytesArray = {
    new BytesArray(new DataList(elems.map(coerceOutput).toList.asJava))
  }

  def apply(collection: Traversable[ByteString]): BytesArray = {
    new BytesArray(new DataList(collection.map(coerceOutput).toList.asJava))
  }

  def apply(dataList: DataList, conversion: DataConversion): BytesArray = {
    new BytesArray(DataTemplates.makeImmutable(dataList, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[BytesArray, ByteString, BytesArray] {
    def apply(from: BytesArray) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: BytesArray) extends mutable.Builder[ByteString, BytesArray] {
    def this() = this(new BytesArray(new DataList()))

    val elems = new DataList(initial.data())

    def +=(x: ByteString): this.type = {
      elems.add(coerceOutput(x))
      this
    }

    def clear() = {
      elems.clear()
    }

    def result() = {
      elems.makeReadOnly()
      new BytesArray(elems)
    }
  }

  private def coerceOutput(value: ByteString): AnyRef = {

    DataTemplateUtil.coerceInput(value, classOf[com.linkedin.data.ByteString], classOf[com.linkedin.data.ByteString])

  }

  implicit def wrap(traversable: Traversable[ByteString]): BytesArray = {
    BytesArray(traversable)
  }
}

