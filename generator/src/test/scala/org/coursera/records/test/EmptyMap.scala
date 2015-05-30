

package org.coursera.records.test

import javax.annotation.Generated

import com.linkedin.data.DataMap
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.DataTemplateUtil
import org.coursera.courier.data.DataTemplates
import org.coursera.courier.data.DataTemplates.DataConversion
import scala.collection.generic.CanBuildFrom
import scala.collection.immutable
import scala.collection.mutable
import scala.collection.JavaConverters._



@Generated(value = Array("EmptyMap"), comments="Courier Data Template.", date = "Sat May 30 10:07:51 PDT 2015")
final class EmptyMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, Empty)]
  with Map[String, Empty]
  with immutable.MapLike[String, Empty, immutable.Map[String, Empty]]
  with DataTemplate[DataMap] {

  // TODO(jbetz): Decide on caching policy for data template types. We should not be creating a
  // new instance here on each lookup.
  private[this] def lookup(key: String) = {
    Option(dataMap.getDataMap(key)).map(dataMap => Empty(dataMap, DataConversion.SetReadOnly))
  }

  override def get(key: String): Option[Empty] = lookup(key)

  override def iterator: Iterator[(String, Empty)] = new Iterator[(String, Empty)] {
    val underlying = dataMap.keySet().iterator()
    override def hasNext: Boolean = underlying.hasNext
    override def next(): (String, Empty) = {
      val key = underlying.next()
      key -> lookup(key).get
    }
  }

  override def +[F >: Empty](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case v: Empty =>
        val copy = dataMap.copy()
        copy.put(key, v.data())
        copy.setReadOnly()
        new EmptyMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: String): EmptyMap = {
    val copy = dataMap.copy()
    copy.remove(key)
    copy.setReadOnly()
    new EmptyMap(copy)
  }

  override def schema(): DataSchema = EmptyMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = {
    val copy = dataMap.copy()
    copy.setReadOnly()
    new EmptyMap(copy)
  }
}

object EmptyMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":{"type":"record","name":"Empty","namespace":"org.coursera.records.test","fields":[]}}""").asInstanceOf[MapDataSchema]

  val empty = EmptyMap()

  def apply(elems: (String, Empty)*): EmptyMap = {
    EmptyMap(elems.toMap)
  }

  def apply(map: Map[String, Empty]): EmptyMap = {
    new EmptyMap(new DataMap(map.mapValues(_.data()).asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): EmptyMap = {
    new EmptyMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[EmptyMap, (String, Empty), EmptyMap] {
    def apply(from: EmptyMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: EmptyMap) extends mutable.Builder[(String, Empty), EmptyMap] {
    def this() = this(new EmptyMap(new DataMap()))

    val entries = new DataMap(initial.dataMap)

    def +=(kv: (String, Empty)): this.type = {
      val (key, value) = kv
      entries.put(key, value.data())
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.setReadOnly()
      new EmptyMap(entries)
    }
  }
}

