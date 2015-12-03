

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

@Generated(value = Array("BooleanArray"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class BooleanArray(private val dataList: DataList)
  extends IndexedSeq[Boolean]
  with Product
  with GenTraversable[Boolean]
  with DataTemplate[DataList] {

  override def length: Int = dataList.size()

  private[this] lazy val list = dataList.asScala.map(coerceInput)

  private[this] def coerceInput(any: AnyRef): Boolean = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Boolean])

  }

  override def apply(idx: Int): Boolean = list(idx)

  override def productElement(n: Int): Any = dataList.get(n)
  override def productArity: Int = dataList.size()

  override def schema(): DataSchema = BooleanArray.SCHEMA

  override def data(): DataList = dataList
  override def copy(): DataTemplate[DataList] = this
}

object BooleanArray {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"array","items":"boolean"}""").asInstanceOf[ArrayDataSchema]

  val empty = BooleanArray()

  def apply(elems: Boolean*): BooleanArray = {
    new BooleanArray(new DataList(elems.map(coerceOutput).toList.asJava))
  }

  def apply(collection: Traversable[Boolean]): BooleanArray = {
    new BooleanArray(new DataList(collection.map(coerceOutput).toList.asJava))
  }

  def apply(dataList: DataList, conversion: DataConversion): BooleanArray = {
    new BooleanArray(DataTemplates.makeImmutable(dataList, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[BooleanArray, Boolean, BooleanArray] {
    def apply(from: BooleanArray) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: BooleanArray) extends mutable.Builder[Boolean, BooleanArray] {
    def this() = this(new BooleanArray(new DataList()))

    val elems = new DataList(initial.data())

    def +=(x: Boolean): this.type = {
      elems.add(coerceOutput(x))
      this
    }

    def clear() = {
      elems.clear()
    }

    def result() = {
      elems.makeReadOnly()
      new BooleanArray(elems)
    }
  }

  private def coerceOutput(value: Boolean): AnyRef = {

    DataTemplateUtil.coerceInput(Boolean.box(value), classOf[java.lang.Boolean], classOf[java.lang.Boolean])

  }

  implicit def wrap(traversable: Traversable[Boolean]): BooleanArray = {
    BooleanArray(traversable)
  }
}

