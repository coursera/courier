

package org.coursera.records.test

import javax.annotation.Generated

import com.linkedin.data.ByteString
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
import com.linkedin.data.template.Custom



@Generated(value = Array("EmptyMap"), comments="Courier Data Template.", date = "Sun May 31 11:22:32 PDT 2015")
final class EmptyMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, org.coursera.records.test.Empty)]
  with Map[String, org.coursera.records.test.Empty]
  with immutable.MapLike[String, org.coursera.records.test.Empty, immutable.Map[String, org.coursera.records.test.Empty]]
  with DataTemplate[DataMap] {
  import EmptyMap._

  // TODO(jbetz): Decide on caching policy for data template types. We should not be creating a
  // new instance here on each lookup.
  private[this] def lookup(key: String): Option[org.coursera.records.test.Empty] = {
    
        Option(dataMap.getDataMap(key)).map(dataMap => org.coursera.records.test.Empty(dataMap, DataConversion.SetReadOnly))
      
    
    
    
  }

  override def get(key: String): Option[org.coursera.records.test.Empty] = lookup(key)

  override def iterator: Iterator[(String, org.coursera.records.test.Empty)] = new Iterator[(String, org.coursera.records.test.Empty)] {
    val underlying = dataMap.keySet().iterator()
    override def hasNext: Boolean = underlying.hasNext
    override def next(): (String, org.coursera.records.test.Empty) = {
      val key = underlying.next()
      key -> lookup(key).get
    }
  }

  override def +[F >: org.coursera.records.test.Empty](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case v: org.coursera.records.test.Empty =>
        val copy = dataMap.copy()
        copy.put(key, coerceOutput(v))
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

  def apply(elems: (String, org.coursera.records.test.Empty)*): EmptyMap = {
    EmptyMap(elems.toMap)
  }

  def apply(map: Map[String, org.coursera.records.test.Empty]): EmptyMap = {
    new EmptyMap(new DataMap(map.mapValues(coerceOutput).asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): EmptyMap = {
    new EmptyMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[EmptyMap, (String, org.coursera.records.test.Empty), EmptyMap] {
    def apply(from: EmptyMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: EmptyMap) extends mutable.Builder[(String, org.coursera.records.test.Empty), EmptyMap] {
    def this() = this(new EmptyMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (String, org.coursera.records.test.Empty)): this.type = {
      val (key, value) = kv
      entries.put(key, coerceOutput(value))
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

  private def coerceOutput(value: org.coursera.records.test.Empty): AnyRef = {
    
        value.data()
      
    
    
    
  }
}

