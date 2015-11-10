

package org.coursera.courier.data

import javax.annotation.Generated

import com.linkedin.data.ByteString
import com.linkedin.data.DataMap
import com.linkedin.data.DataList
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.DataTemplateUtil
import org.coursera.courier.templates.DataTemplates
import org.coursera.courier.templates.DataTemplates.DataConversion
import scala.collection.generic.CanBuildFrom
import scala.collection.immutable
import scala.collection.mutable
import scala.collection.JavaConverters._
import com.linkedin.data.template.Custom
import org.coursera.courier.codecs.InlineStringCodec

@Generated(value = Array("LongMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class LongMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, Long)]
  with Map[String, Long]
  with immutable.MapLike[String, Long, immutable.Map[String, Long]]
  with DataTemplate[DataMap] {
  import LongMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): Long = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Long])

  }

  private[this] def coerceKeyInput(key: String): String = {

    def coerceKeyDataInput(any: AnyRef): String = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.String])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: String): Option[Long] = map.get(key)

  override def iterator: Iterator[(String, Long)] = map.iterator

  override def +[F >: Long](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case v: Long =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new LongMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: String): LongMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new LongMap(copy)
  }

  override def schema(): DataSchema = LongMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object LongMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"long"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""string"""")

  val empty = LongMap()

  def apply(elems: (String, Long)*): LongMap = {
    LongMap(elems.toMap)
  }

  def apply(map: Map[String, Long]): LongMap = {
    new LongMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): LongMap = {
    new LongMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[LongMap, (String, Long), LongMap] {
    def apply(from: LongMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: LongMap) extends mutable.Builder[(String, Long), LongMap] {
    def this() = this(new LongMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (String, Long)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new LongMap(entries)
    }
  }

  private def coerceOutput(value: Long): AnyRef = {

    DataTemplateUtil.coerceInput(Long.box(value), classOf[java.lang.Long], classOf[java.lang.Long])

  }

  private def coerceKeyOutput(key: String): String = {

    def coerceKeyDataOutput(value: String): AnyRef = {

      DataTemplateUtil.coerceInput(value, classOf[java.lang.String], classOf[java.lang.String])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[String, Long]): LongMap = {
    LongMap(map)
  }
}
