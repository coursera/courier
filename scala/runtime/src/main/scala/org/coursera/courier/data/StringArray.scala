

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

final class StringArray(private val dataList: DataList)
  extends IndexedSeq[String]
  with Product
  with DataTemplate[DataList]
  with ScalaArrayTemplate {

  override def length: Int = dataList.size()

  private[this] lazy val list = dataList.asScala.map(coerceInput)

  private[this] def coerceInput(any: AnyRef): String = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.String])

  }

  override def apply(idx: Int): String = list(idx)

  override def productElement(n: Int): Any = dataList.get(n)
  override def productArity: Int = dataList.size()

  override def schema(): DataSchema = StringArray.SCHEMA

  override def data(): DataList = dataList
  override def copy(): DataTemplate[DataList] = this
  override def clone(): DataTemplate[DataList] = copy()
  override def copy(dataList: DataList, conversion: DataConversion): ScalaArrayTemplate =
    StringArray.build(dataList, conversion)
}

object StringArray extends ArrayCompanion[StringArray] {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"array","items":"string"}""").asInstanceOf[ArrayDataSchema]

  val empty = StringArray()

  def apply(elems: String*): StringArray = {
    new StringArray(new DataList(elems.map(coerceOutput).toList.asJava))
  }

  def apply(collection: Iterable[String]): StringArray = {
    new StringArray(new DataList(collection.map(coerceOutput).toList.asJava))
  }

  def build(dataList: DataList, conversion: DataConversion): StringArray = {
    new StringArray(DataTemplates.makeImmutable(dataList, conversion))
  }

  def newBuilder = new DataBuilder()

  class DataBuilder(initial: StringArray) extends mutable.Builder[String, StringArray] {
    def this() = this(new StringArray(new DataList()))

    val elems = new DataList(initial.data())

    override def addOne(x: String): this.type = {
      elems.add(coerceOutput(x))
      this
    }

    def clear() = {
      elems.clear()
    }

    def result() = {
      elems.makeReadOnly()
      new StringArray(elems)
    }
  }

  private def coerceOutput(value: String): AnyRef = {

    DataTemplateUtil.coerceInput(value, classOf[java.lang.String], classOf[java.lang.String])

  }

  implicit def wrap(iterable: Iterable[String]): StringArray = {
    StringArray(iterable)
  }
}

