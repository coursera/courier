

package org.coursera.customtypes

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



@Generated(value = Array("CustomIntMap"), comments="Courier Data Template.", date = "Sat May 30 14:26:52 PDT 2015")
final class CustomIntMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, org.coursera.courier.generator.customtypes.CustomInt)]
  with Map[String, org.coursera.courier.generator.customtypes.CustomInt]
  with immutable.MapLike[String, org.coursera.courier.generator.customtypes.CustomInt, immutable.Map[String, org.coursera.courier.generator.customtypes.CustomInt]]
  with DataTemplate[DataMap] {
  import CustomIntMap._

  // TODO(jbetz): Decide on caching policy for data template types. We should not be creating a
  // new instance here on each lookup.
  private[this] def lookup(key: String): Option[org.coursera.courier.generator.customtypes.CustomInt] = {
    
        Option(dataMap.get(key)).map(value => DataTemplateUtil.coerceOutput(value, classOf[org.coursera.courier.generator.customtypes.CustomInt]))
      
    
    
    
  }

  override def get(key: String): Option[org.coursera.courier.generator.customtypes.CustomInt] = lookup(key)

  override def iterator: Iterator[(String, org.coursera.courier.generator.customtypes.CustomInt)] = new Iterator[(String, org.coursera.courier.generator.customtypes.CustomInt)] {
    val underlying = dataMap.keySet().iterator()
    override def hasNext: Boolean = underlying.hasNext
    override def next(): (String, org.coursera.courier.generator.customtypes.CustomInt) = {
      val key = underlying.next()
      key -> lookup(key).get
    }
  }

  override def +[F >: org.coursera.courier.generator.customtypes.CustomInt](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case v: org.coursera.courier.generator.customtypes.CustomInt =>
        val copy = dataMap.copy()
        copy.put(key, coerceOutput(v))
        copy.setReadOnly()
        new CustomIntMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: String): CustomIntMap = {
    val copy = dataMap.copy()
    copy.remove(key)
    copy.setReadOnly()
    new CustomIntMap(copy)
  }

  override def schema(): DataSchema = CustomIntMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = {
    val copy = dataMap.copy()
    copy.setReadOnly()
    new CustomIntMap(copy)
  }
}

object CustomIntMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":{"type":"typeref","name":"CustomInt","namespace":"org.coursera.customtypes","ref":"int","java":{"coercerClass":"org.coursera.courier.generator.customtypes.CustomIntCoercer","class":"org.coursera.courier.generator.customtypes.CustomInt"}}}""").asInstanceOf[MapDataSchema]

  val empty = CustomIntMap()

  def apply(elems: (String, org.coursera.courier.generator.customtypes.CustomInt)*): CustomIntMap = {
    CustomIntMap(elems.toMap)
  }

  def apply(map: Map[String, org.coursera.courier.generator.customtypes.CustomInt]): CustomIntMap = {
    new CustomIntMap(new DataMap(map.mapValues(coerceOutput).asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): CustomIntMap = {
    new CustomIntMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[CustomIntMap, (String, org.coursera.courier.generator.customtypes.CustomInt), CustomIntMap] {
    def apply(from: CustomIntMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: CustomIntMap) extends mutable.Builder[(String, org.coursera.courier.generator.customtypes.CustomInt), CustomIntMap] {
    def this() = this(new CustomIntMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (String, org.coursera.courier.generator.customtypes.CustomInt)): this.type = {
      val (key, value) = kv
      entries.put(key, coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.setReadOnly()
      new CustomIntMap(entries)
    }
  }

  private def coerceOutput(value: org.coursera.courier.generator.customtypes.CustomInt): AnyRef = {
    
        DataTemplateUtil.coerceInput(value, classOf[org.coursera.courier.generator.customtypes.CustomInt], classOf[Int])
      
    
    
    
  }
}

