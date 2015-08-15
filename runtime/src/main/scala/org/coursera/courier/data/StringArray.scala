

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

@Generated(value = Array("StringArray"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class StringArray(private val dataList: DataList)
  extends IndexedSeq[String]
  with Product
  with GenTraversable[String]
  with DataTemplate[DataList] {

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
}

object StringArray {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"array","items":"string"}""").asInstanceOf[ArrayDataSchema]

  val empty = StringArray()

  def apply(elems: String*): StringArray = {
    new StringArray(new DataList(elems.map(coerceOutput).toList.asJava))
  }

  def apply(collection: Traversable[String]): StringArray = {
    new StringArray(new DataList(collection.map(coerceOutput).toList.asJava))
  }

  def apply(dataList: DataList, conversion: DataConversion): StringArray = {
    new StringArray(DataTemplates.makeImmutable(dataList, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[StringArray, String, StringArray] {
    def apply(from: StringArray) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: StringArray) extends mutable.Builder[String, StringArray] {
    def this() = this(new StringArray(new DataList()))

    val elems = new DataList(initial.data())

    def +=(x: String): this.type = {
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

  implicit def wrap(traversable: Traversable[String]): StringArray = {
    StringArray(traversable)
  }
}

