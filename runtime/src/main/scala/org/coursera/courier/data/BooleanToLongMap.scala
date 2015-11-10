

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

@Generated(value = Array("BooleanToLongMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class BooleanToLongMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Boolean, Long)]
  with Map[Boolean, Long]
  with immutable.MapLike[Boolean, Long, immutable.Map[Boolean, Long]]
  with DataTemplate[DataMap] {
  import BooleanToLongMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): Long = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Long])

  }

  private[this] def coerceKeyInput(key: String): Boolean = {

    def coerceKeyDataInput(any: AnyRef): Boolean = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.Boolean])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Boolean): Option[Long] = map.get(key)

  override def iterator: Iterator[(Boolean, Long)] = map.iterator

  override def +[F >: Long](kv: (Boolean, F)): Map[Boolean, F] = {
    val (key, value) = kv
    value match {
      case v: Long =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new BooleanToLongMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Boolean): BooleanToLongMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new BooleanToLongMap(copy)
  }

  override def schema(): DataSchema = BooleanToLongMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object BooleanToLongMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"long","keys":"boolean"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""boolean"""")

  val empty = BooleanToLongMap()

  def apply(elems: (Boolean, Long)*): BooleanToLongMap = {
    BooleanToLongMap(elems.toMap)
  }

  def apply(map: Map[Boolean, Long]): BooleanToLongMap = {
    new BooleanToLongMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): BooleanToLongMap = {
    new BooleanToLongMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[BooleanToLongMap, (Boolean, Long), BooleanToLongMap] {
    def apply(from: BooleanToLongMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: BooleanToLongMap) extends mutable.Builder[(Boolean, Long), BooleanToLongMap] {
    def this() = this(new BooleanToLongMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Boolean, Long)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new BooleanToLongMap(entries)
    }
  }

  private def coerceOutput(value: Long): AnyRef = {

    DataTemplateUtil.coerceInput(Long.box(value), classOf[java.lang.Long], classOf[java.lang.Long])

  }

  private def coerceKeyOutput(key: Boolean): String = {

    def coerceKeyDataOutput(value: Boolean): AnyRef = {

      DataTemplateUtil.coerceInput(Boolean.box(value), classOf[java.lang.Boolean], classOf[java.lang.Boolean])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[Boolean, Long]): BooleanToLongMap = {
    BooleanToLongMap(map)
  }
}
