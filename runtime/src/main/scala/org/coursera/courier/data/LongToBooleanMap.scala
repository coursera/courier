

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

@Generated(value = Array("LongToBooleanMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class LongToBooleanMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Long, Boolean)]
  with Map[Long, Boolean]
  with immutable.MapLike[Long, Boolean, immutable.Map[Long, Boolean]]
  with DataTemplate[DataMap] {
  import LongToBooleanMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): Boolean = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Boolean])

  }

  private[this] def coerceKeyInput(key: String): Long = {

    def coerceKeyDataInput(any: AnyRef): Long = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.Long])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Long): Option[Boolean] = map.get(key)

  override def iterator: Iterator[(Long, Boolean)] = map.iterator

  override def +[F >: Boolean](kv: (Long, F)): Map[Long, F] = {
    val (key, value) = kv
    value match {
      case v: Boolean =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new LongToBooleanMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Long): LongToBooleanMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new LongToBooleanMap(copy)
  }

  override def schema(): DataSchema = LongToBooleanMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object LongToBooleanMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"boolean","keys":"long"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""long"""")

  val empty = LongToBooleanMap()

  def apply(elems: (Long, Boolean)*): LongToBooleanMap = {
    LongToBooleanMap(elems.toMap)
  }

  def apply(map: Map[Long, Boolean]): LongToBooleanMap = {
    new LongToBooleanMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): LongToBooleanMap = {
    new LongToBooleanMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[LongToBooleanMap, (Long, Boolean), LongToBooleanMap] {
    def apply(from: LongToBooleanMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: LongToBooleanMap) extends mutable.Builder[(Long, Boolean), LongToBooleanMap] {
    def this() = this(new LongToBooleanMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Long, Boolean)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new LongToBooleanMap(entries)
    }
  }

  private def coerceOutput(value: Boolean): AnyRef = {

    DataTemplateUtil.coerceInput(Boolean.box(value), classOf[java.lang.Boolean], classOf[java.lang.Boolean])

  }

  private def coerceKeyOutput(key: Long): String = {

    def coerceKeyDataOutput(value: Long): AnyRef = {

      DataTemplateUtil.coerceInput(Long.box(value), classOf[java.lang.Long], classOf[java.lang.Long])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[Long, Boolean]): LongToBooleanMap = {
    LongToBooleanMap(map)
  }
}
