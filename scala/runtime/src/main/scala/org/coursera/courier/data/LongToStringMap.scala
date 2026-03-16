

package org.coursera.courier.data

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
import scala.collection.immutable
import scala.collection.mutable
import scala.collection.JavaConverters._
import com.linkedin.data.template.Custom
import org.coursera.courier.codecs.InlineStringCodec

final class LongToStringMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Long, String)]
  with Map[Long, String]
  with DataTemplate[DataMap] {
  import LongToStringMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): String = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.String])

  }

  private[this] def coerceKeyInput(key: String): Long = {

    def coerceKeyDataInput(any: AnyRef): Long = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.Long])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Long): Option[String] = map.get(key)

  override def iterator: Iterator[(Long, String)] = map.iterator

  override def updated[V1 >: String](key: Long, value: V1): Map[Long, V1] = this + (key -> value)

  override def +[F >: String](kv: (Long, F)): Map[Long, F] = {
    val (key, value) = kv
    value match {
      case v: String =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new LongToStringMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }


  override def removed(key: Long): LongToStringMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new LongToStringMap(copy)
  }

  override def schema(): DataSchema = LongToStringMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
  override def clone(): DataTemplate[DataMap] = copy()
}

object LongToStringMap extends MapCompanion[LongToStringMap] {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"string","keys":"long"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""long"""")

  val empty = LongToStringMap()

  def apply(elems: (Long, String)*): LongToStringMap = {
    LongToStringMap(elems.toMap)
  }

  def apply(map: Map[Long, String]): LongToStringMap = {
    new LongToStringMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def build(dataMap: DataMap, conversion: DataConversion): LongToStringMap = {
    new LongToStringMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  class DataBuilder(initial: LongToStringMap) extends mutable.Builder[(Long, String), LongToStringMap] {
    def this() = this(new LongToStringMap(new DataMap()))

    val entries = new DataMap(initial.data())

    override def addOne(kv: (Long, String)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new LongToStringMap(entries)
    }
  }

  private def coerceOutput(value: String): AnyRef = {

    DataTemplateUtil.coerceInput(value, classOf[java.lang.String], classOf[java.lang.String])

  }

  private def coerceKeyOutput(key: Long): String = {

    def coerceKeyDataOutput(value: Long): AnyRef = {

      DataTemplateUtil.coerceInput(Long.box(value), classOf[java.lang.Long], classOf[java.lang.Long])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[Long, String]): LongToStringMap = {
    LongToStringMap(map)
  }
}
