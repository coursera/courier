

package org.coursera.courier.data

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
import scala.collection.JavaConverters._
import scala.collection.mutable
import com.linkedin.data.template.Custom

final class BooleanArray(private val dataList: DataList)
  extends IndexedSeq[Boolean]
  with Product
  with DataTemplate[DataList]
  with ScalaArrayTemplate {

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
  override def clone(): DataTemplate[DataList] = copy()
  override def copy(dataList: DataList, conversion: DataConversion): ScalaArrayTemplate =
    BooleanArray.build(dataList, conversion)
}

object BooleanArray extends ArrayCompanion[BooleanArray] {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"array","items":"boolean"}""").asInstanceOf[ArrayDataSchema]

  val empty = BooleanArray()

  def apply(elems: Boolean*): BooleanArray = {
    new BooleanArray(new DataList(elems.map(coerceOutput).toList.asJava))
  }

  def apply(collection: Iterable[Boolean]): BooleanArray = {
    new BooleanArray(new DataList(collection.map(coerceOutput).toList.asJava))
  }

  def build(dataList: DataList, conversion: DataConversion): BooleanArray = {
    new BooleanArray(DataTemplates.makeImmutable(dataList, conversion))
  }

  def newBuilder = new DataBuilder()

  class DataBuilder(initial: BooleanArray) extends mutable.Builder[Boolean, BooleanArray] {
    def this() = this(new BooleanArray(new DataList()))

    val elems = new DataList(initial.data())

    override def addOne(x: Boolean): this.type = {
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

  implicit def wrap(iterable: Iterable[Boolean]): BooleanArray = {
    BooleanArray(iterable)
  }
}

