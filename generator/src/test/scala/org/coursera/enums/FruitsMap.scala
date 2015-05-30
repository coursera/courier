

package org.coursera.enums

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



@Generated(value = Array("FruitsMap"), comments="Courier Data Template.", date = "Sat May 30 14:26:52 PDT 2015")
final class FruitsMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, org.coursera.enums.Fruits.Fruits)]
  with Map[String, org.coursera.enums.Fruits.Fruits]
  with immutable.MapLike[String, org.coursera.enums.Fruits.Fruits, immutable.Map[String, org.coursera.enums.Fruits.Fruits]]
  with DataTemplate[DataMap] {
  import FruitsMap._

  // TODO(jbetz): Decide on caching policy for data template types. We should not be creating a
  // new instance here on each lookup.
  private[this] def lookup(key: String): Option[org.coursera.enums.Fruits.Fruits] = {
    
        Option(dataMap.get(key).asInstanceOf[String]).map(Fruits.fromString)
      
    
    
    
  }

  override def get(key: String): Option[org.coursera.enums.Fruits.Fruits] = lookup(key)

  override def iterator: Iterator[(String, org.coursera.enums.Fruits.Fruits)] = new Iterator[(String, org.coursera.enums.Fruits.Fruits)] {
    val underlying = dataMap.keySet().iterator()
    override def hasNext: Boolean = underlying.hasNext
    override def next(): (String, org.coursera.enums.Fruits.Fruits) = {
      val key = underlying.next()
      key -> lookup(key).get
    }
  }

  override def +[F >: org.coursera.enums.Fruits.Fruits](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case v: org.coursera.enums.Fruits.Fruits =>
        val copy = dataMap.copy()
        copy.put(key, coerceOutput(v))
        copy.setReadOnly()
        new FruitsMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: String): FruitsMap = {
    val copy = dataMap.copy()
    copy.remove(key)
    copy.setReadOnly()
    new FruitsMap(copy)
  }

  override def schema(): DataSchema = FruitsMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = {
    val copy = dataMap.copy()
    copy.setReadOnly()
    new FruitsMap(copy)
  }
}

object FruitsMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":{"type":"enum","name":"Fruits","namespace":"org.coursera.enums","symbols":["APPLE","BANANA","ORANGE","PINEAPPLE"],"symbolDocs":{"APPLE":"An Apple."}}}""").asInstanceOf[MapDataSchema]

  val empty = FruitsMap()

  def apply(elems: (String, org.coursera.enums.Fruits.Fruits)*): FruitsMap = {
    FruitsMap(elems.toMap)
  }

  def apply(map: Map[String, org.coursera.enums.Fruits.Fruits]): FruitsMap = {
    new FruitsMap(new DataMap(map.mapValues(coerceOutput).asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): FruitsMap = {
    new FruitsMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[FruitsMap, (String, org.coursera.enums.Fruits.Fruits), FruitsMap] {
    def apply(from: FruitsMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: FruitsMap) extends mutable.Builder[(String, org.coursera.enums.Fruits.Fruits), FruitsMap] {
    def this() = this(new FruitsMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (String, org.coursera.enums.Fruits.Fruits)): this.type = {
      val (key, value) = kv
      entries.put(key, coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.setReadOnly()
      new FruitsMap(entries)
    }
  }

  private def coerceOutput(value: org.coursera.enums.Fruits.Fruits): AnyRef = {
    
        value.toString
      
    
    
    
  }
}

