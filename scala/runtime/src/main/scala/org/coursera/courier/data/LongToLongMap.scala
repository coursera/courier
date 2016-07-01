

package org.coursera.courier.data

import javax.annotation.Generated

import com.linkedin.data.ByteString
import com.linkedin.data.DataMap
import com.linkedin.data.DataList
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.DataTemplateUtil
import org.coursera.courier.companions.MapCompanion
import org.coursera.courier.templates.DataTemplates
import org.coursera.courier.templates.DataTemplates.DataConversion
import scala.collection.generic.CanBuildFrom
import scala.collection.immutable
import scala.collection.mutable
import scala.collection.JavaConverters._
import com.linkedin.data.template.Custom
import org.coursera.courier.codecs.InlineStringCodec

@Generated(value = Array("LongToLongMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class LongToLongMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Long, Long)]
  with Map[Long, Long]
  with immutable.MapLike[Long, Long, immutable.Map[Long, Long]]
  with DataTemplate[DataMap] {
  import LongToLongMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): Long = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Long])

  }

  private[this] def coerceKeyInput(key: String): Long = {

    def coerceKeyDataInput(any: AnyRef): Long = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.Long])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Long): Option[Long] = map.get(key)

  override def iterator: Iterator[(Long, Long)] = map.iterator

  override def +[F >: Long](kv: (Long, F)): Map[Long, F] = {
    val (key, value) = kv
    value match {
      case v: Long =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new LongToLongMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Long): LongToLongMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new LongToLongMap(copy)
  }

  override def schema(): DataSchema = LongToLongMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object LongToLongMap extends MapCompanion[LongToLongMap] {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"long","keys":"long"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""long"""")

  val empty = LongToLongMap()

  def apply(elems: (Long, Long)*): LongToLongMap = {
    LongToLongMap(elems.toMap)
  }

  def apply(map: Map[Long, Long]): LongToLongMap = {
    new LongToLongMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def build(dataMap: DataMap, conversion: DataConversion): LongToLongMap = {
    new LongToLongMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[LongToLongMap, (Long, Long), LongToLongMap] {
    def apply(from: LongToLongMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: LongToLongMap) extends mutable.Builder[(Long, Long), LongToLongMap] {
    def this() = this(new LongToLongMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Long, Long)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new LongToLongMap(entries)
    }
  }

  private def coerceOutput(value: Long): AnyRef = {

    DataTemplateUtil.coerceInput(Long.box(value), classOf[java.lang.Long], classOf[java.lang.Long])

  }

  private def coerceKeyOutput(key: Long): String = {

    def coerceKeyDataOutput(value: Long): AnyRef = {

      DataTemplateUtil.coerceInput(Long.box(value), classOf[java.lang.Long], classOf[java.lang.Long])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[Long, Long]): LongToLongMap = {
    LongToLongMap(map)
  }
}
