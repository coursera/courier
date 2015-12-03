

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

@Generated(value = Array("LongToByteStringMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class LongToByteStringMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Long, ByteString)]
  with Map[Long, ByteString]
  with immutable.MapLike[Long, ByteString, immutable.Map[Long, ByteString]]
  with DataTemplate[DataMap] {
  import LongToByteStringMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): ByteString = {

    DataTemplateUtil.coerceOutput(any, classOf[com.linkedin.data.ByteString])

  }

  private[this] def coerceKeyInput(key: String): Long = {

    def coerceKeyDataInput(any: AnyRef): Long = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.Long])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Long): Option[ByteString] = map.get(key)

  override def iterator: Iterator[(Long, ByteString)] = map.iterator

  override def +[F >: ByteString](kv: (Long, F)): Map[Long, F] = {
    val (key, value) = kv
    value match {
      case v: ByteString =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new LongToByteStringMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Long): LongToByteStringMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new LongToByteStringMap(copy)
  }

  override def schema(): DataSchema = LongToByteStringMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object LongToByteStringMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"bytes","keys":"long"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""long"""")

  val empty = LongToByteStringMap()

  def apply(elems: (Long, ByteString)*): LongToByteStringMap = {
    LongToByteStringMap(elems.toMap)
  }

  def apply(map: Map[Long, ByteString]): LongToByteStringMap = {
    new LongToByteStringMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): LongToByteStringMap = {
    new LongToByteStringMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[LongToByteStringMap, (Long, ByteString), LongToByteStringMap] {
    def apply(from: LongToByteStringMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: LongToByteStringMap) extends mutable.Builder[(Long, ByteString), LongToByteStringMap] {
    def this() = this(new LongToByteStringMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Long, ByteString)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new LongToByteStringMap(entries)
    }
  }

  private def coerceOutput(value: ByteString): AnyRef = {

    DataTemplateUtil.coerceInput(value, classOf[com.linkedin.data.ByteString], classOf[com.linkedin.data.ByteString])

  }

  private def coerceKeyOutput(key: Long): String = {

    def coerceKeyDataOutput(value: Long): AnyRef = {

      DataTemplateUtil.coerceInput(Long.box(value), classOf[java.lang.Long], classOf[java.lang.Long])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[Long, ByteString]): LongToByteStringMap = {
    LongToByteStringMap(map)
  }
}
