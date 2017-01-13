

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

@Generated(value = Array("BytesMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:37 PDT 2015")
final class BytesMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, ByteString)]
  with Map[String, ByteString]
  with immutable.MapLike[String, ByteString, immutable.Map[String, ByteString]]
  with DataTemplate[DataMap] {
  import BytesMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): ByteString = {

    DataTemplateUtil.coerceOutput(any, classOf[com.linkedin.data.ByteString])

  }

  private[this] def coerceKeyInput(key: String): String = {

    def coerceKeyDataInput(any: AnyRef): String = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.String])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: String): Option[ByteString] = map.get(key)

  override def iterator: Iterator[(String, ByteString)] = map.iterator

  override def +[F >: ByteString](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case v: ByteString =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new BytesMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: String): BytesMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new BytesMap(copy)
  }

  override def schema(): DataSchema = BytesMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object BytesMap extends MapCompanion[BytesMap] {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"bytes"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""string"""")

  val empty = BytesMap()

  def apply(elems: (String, ByteString)*): BytesMap = {
    BytesMap(elems.toMap)
  }

  def apply(map: Map[String, ByteString]): BytesMap = {
    new BytesMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def build(dataMap: DataMap, conversion: DataConversion): BytesMap = {
    new BytesMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[BytesMap, (String, ByteString), BytesMap] {
    def apply(from: BytesMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: BytesMap) extends mutable.Builder[(String, ByteString), BytesMap] {
    def this() = this(new BytesMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (String, ByteString)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new BytesMap(entries)
    }
  }

  private def coerceOutput(value: ByteString): AnyRef = {

    DataTemplateUtil.coerceInput(value, classOf[com.linkedin.data.ByteString], classOf[com.linkedin.data.ByteString])

  }

  private def coerceKeyOutput(key: String): String = {

    def coerceKeyDataOutput(value: String): AnyRef = {

      DataTemplateUtil.coerceInput(value, classOf[java.lang.String], classOf[java.lang.String])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[String, ByteString]): BytesMap = {
    BytesMap(map)
  }
}
