

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

final class BooleanMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, Boolean)]
  with Map[String, Boolean]
  with DataTemplate[DataMap] {
  import BooleanMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): Boolean = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Boolean])

  }

  private[this] def coerceKeyInput(key: String): String = {

    def coerceKeyDataInput(any: AnyRef): String = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.String])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: String): Option[Boolean] = map.get(key)

  override def iterator: Iterator[(String, Boolean)] = map.iterator

  override def updated[V1 >: Boolean](key: String, value: V1): Map[String, V1] = this + (key -> value)

  override def +[F >: Boolean](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case v: Boolean =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new BooleanMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }


  override def removed(key: String): BooleanMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new BooleanMap(copy)
  }

  override def schema(): DataSchema = BooleanMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
  override def clone(): DataTemplate[DataMap] = copy()
}

object BooleanMap extends MapCompanion[BooleanMap] {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"boolean"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""string"""")

  val empty = BooleanMap()

  def apply(elems: (String, Boolean)*): BooleanMap = {
    BooleanMap(elems.toMap)
  }

  def apply(map: Map[String, Boolean]): BooleanMap = {
    new BooleanMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def build(dataMap: DataMap, conversion: DataConversion): BooleanMap = {
    new BooleanMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  class DataBuilder(initial: BooleanMap) extends mutable.Builder[(String, Boolean), BooleanMap] {
    def this() = this(new BooleanMap(new DataMap()))

    val entries = new DataMap(initial.data())

    override def addOne(kv: (String, Boolean)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new BooleanMap(entries)
    }
  }

  private def coerceOutput(value: Boolean): AnyRef = {

    DataTemplateUtil.coerceInput(Boolean.box(value), classOf[java.lang.Boolean], classOf[java.lang.Boolean])

  }

  private def coerceKeyOutput(key: String): String = {

    def coerceKeyDataOutput(value: String): AnyRef = {

      DataTemplateUtil.coerceInput(value, classOf[java.lang.String], classOf[java.lang.String])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[String, Boolean]): BooleanMap = {
    BooleanMap(map)
  }
}
