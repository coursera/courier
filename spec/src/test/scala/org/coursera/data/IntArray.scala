package org.coursera.data

import com.linkedin.data.DataList
import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.DataTemplateUtil
import org.coursera.data.DataTemplates.DataConversion

import scala.collection.GenTraversable
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable
import scala.collection.JavaConverters._

sealed class IntArray(private val dataList: DataList)
  extends IndexedSeq[Int]
  with Product
  with GenTraversable[Int]
  with DataTemplate[DataList] {

  override def length: Int = dataList.size()

  // TODO(jbetz): Decide on caching policy for data template types. In general, we should not be
  // creating a new instance here on each lookup, but for primitive types, maybe it's acceptable?
  private[this] def lookup(idx: Int) = {
    dataList.get(idx).asInstanceOf[Integer].toInt
  }

  override def apply(idx: Int): Int = lookup(idx)

  override def productElement(n: Int): Any = dataList.get(n)
  override def productArity: Int = dataList.size()

  override def schema(): DataSchema = IntArray.SCHEMA

  override def data(): DataList = dataList
  override def copy(): DataTemplate[DataList] = this
}

object IntArray {
  private val SCHEMA = DataTemplateUtil.parseSchema(
    """
      |{ "type" : "array", "items" : "int" }
      |""".stripMargin
  ).asInstanceOf[ArrayDataSchema]

  val empty = IntArray()

  def apply(elems : scala.Int*) : IntArray = {
    new IntArray(new DataList(elems.map(Int.box).toList.asJava))
  }

  def apply(collection: Traversable[Int]): IntArray = {
    new IntArray(new DataList(collection.map(Int.box).toList.asJava))
  }

  def apply(dataList: DataList, conversion: DataConversion) : IntArray = {
    new IntArray(DataTemplates.makeImmutable(dataList, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[IntArray, scala.Int, IntArray] {
    def apply(from: IntArray) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: IntArray) extends mutable.Builder[Int, IntArray] {
    def this() = this(new IntArray(new DataList()))

    val elems = new DataList(initial.dataList)

    def +=(x: Int): this.type = {
      elems.add(Int.box(x))
      this
    }

    def clear() = {
      elems.clear()
    }

    def result() = {
      elems.setReadOnly()
      new IntArray(elems)
    }
  }
}

