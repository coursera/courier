

package org.coursera.records.test



import javax.annotation.Generated

import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.DataTemplateUtil
import org.coursera.courier.data.DataTemplates
import org.coursera.courier.data.DataTemplates.DataConversion

import scala.collection.GenTraversable
import scala.collection.JavaConverters._
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable

@Generated(value = Array("EmptyArray"), comments="Courier Data Template.", date = "Fri May 29 21:44:23 PDT 2015")
final class EmptyArray(private val dataList: DataList)
  extends IndexedSeq[Empty]
  with Product
  with GenTraversable[Empty]
  with DataTemplate[DataList] {

  override def length: Int = dataList.size()

  
  private[this] def lookup(idx: Int) = {
    Empty(dataList.get(idx).asInstanceOf[DataMap], DataTemplates.DataConversion.SetReadOnly)
  }

  override def apply(idx: Int): Empty = lookup(idx)

  override def productElement(n: Int): Any = dataList.get(n)
  override def productArity: Int = dataList.size()

  override def schema(): DataSchema = EmptyArray.SCHEMA

  override def data(): DataList = dataList
  override def copy(): DataTemplate[DataList] = this
}

object EmptyArray {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"array","items":{"type":"record","name":"Empty","namespace":"org.coursera.records.test","fields":[]}}""").asInstanceOf[ArrayDataSchema]

  val empty = EmptyArray()

  def apply(elems: Empty*): EmptyArray = {
    new EmptyArray(new DataList(elems.map(_.data()).toList.asJava))
  }

  def apply(collection: Traversable[Empty]): EmptyArray = {
    new EmptyArray(new DataList(collection.map(_.data()).toList.asJava))
  }

  def apply(dataList: DataList, conversion: DataConversion) : EmptyArray = {
    new EmptyArray(DataTemplates.makeImmutable(dataList, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[EmptyArray, Empty, EmptyArray] {
    def apply(from: EmptyArray) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: EmptyArray) extends mutable.Builder[Empty, EmptyArray] {
    def this() = this(new EmptyArray(new DataList()))

    val elems = new DataList(initial.dataList)

    def +=(x: Empty): this.type = {
      elems.add(x.data())
      this
    }

    def clear() = {
      elems.clear()
    }

    def result() = {
      elems.setReadOnly()
      new EmptyArray(elems)
    }
  }
}


