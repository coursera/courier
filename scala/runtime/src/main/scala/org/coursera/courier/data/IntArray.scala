

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
import scala.collection.JavaConverters._
import scala.collection.mutable
import com.linkedin.data.template.Custom

@Generated(value = Array("IntArray"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class IntArray(private val dataList: DataList)
  extends IndexedSeq[Int]
  with Product
  with DataTemplate[DataList]
  with ScalaArrayTemplate {

  override def length: Int = dataList.size()

  private[this] lazy val list = dataList.asScala.map(coerceInput)

  private[this] def coerceInput(any: AnyRef): Int = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Integer])

  }

  override def apply(idx: Int): Int = list(idx)

  override def productElement(n: Int): Any = dataList.get(n)
  override def productArity: Int = dataList.size()

  override def schema(): DataSchema = IntArray.SCHEMA

  override def data(): DataList = dataList
  override def copy(): DataTemplate[DataList] = this
  override def copy(dataList: DataList, conversion: DataConversion): ScalaArrayTemplate =
    IntArray.build(dataList, conversion)
}

object IntArray extends ArrayCompanion[IntArray] {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"array","items":"int"}""").asInstanceOf[ArrayDataSchema]

  val empty = IntArray()

  def apply(elems: Int*): IntArray = {
    new IntArray(new DataList(elems.map(coerceOutput).toList.asJava))
  }

  def apply(collection: Iterable[Int]): IntArray = {
    new IntArray(new DataList(collection.map(coerceOutput).toList.asJava))
  }

  def build(dataList: DataList, conversion: DataConversion): IntArray = {
    new IntArray(DataTemplates.makeImmutable(dataList, conversion))
  }

  def newBuilder = new DataBuilder()

  class DataBuilder(initial: IntArray) extends mutable.Builder[Int, IntArray] {
    def this() = this(new IntArray(new DataList()))

    val elems = new DataList(initial.data())

    def +=(x: Int): this.type = {
      elems.add(coerceOutput(x))
      this
    }

    def clear() = {
      elems.clear()
    }

    def result() = {
      elems.makeReadOnly()
      new IntArray(elems)
    }
  }

  private def coerceOutput(value: Int): AnyRef = {

    DataTemplateUtil.coerceInput(Int.box(value), classOf[java.lang.Integer], classOf[java.lang.Integer])

  }

  implicit def wrap(iterable: Iterable[Int]): IntArray = {
    IntArray(iterable)
  }
}

