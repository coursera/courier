

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

@Generated(value = Array("BooleanToStringMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class BooleanToStringMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Boolean, String)]
  with Map[Boolean, String]
  with immutable.MapLike[Boolean, String, immutable.Map[Boolean, String]]
  with DataTemplate[DataMap] {
  import BooleanToStringMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): String = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.String])

  }

  private[this] def coerceKeyInput(key: String): Boolean = {

    def coerceKeyDataInput(any: AnyRef): Boolean = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.Boolean])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Boolean): Option[String] = map.get(key)

  override def iterator: Iterator[(Boolean, String)] = map.iterator

  override def +[F >: String](kv: (Boolean, F)): Map[Boolean, F] = {
    val (key, value) = kv
    value match {
      case v: String =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new BooleanToStringMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Boolean): BooleanToStringMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new BooleanToStringMap(copy)
  }

  override def schema(): DataSchema = BooleanToStringMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object BooleanToStringMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"string","keys":"boolean"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""boolean"""")

  val empty = BooleanToStringMap()

  def apply(elems: (Boolean, String)*): BooleanToStringMap = {
    BooleanToStringMap(elems.toMap)
  }

  def apply(map: Map[Boolean, String]): BooleanToStringMap = {
    new BooleanToStringMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): BooleanToStringMap = {
    new BooleanToStringMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[BooleanToStringMap, (Boolean, String), BooleanToStringMap] {
    def apply(from: BooleanToStringMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: BooleanToStringMap) extends mutable.Builder[(Boolean, String), BooleanToStringMap] {
    def this() = this(new BooleanToStringMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Boolean, String)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new BooleanToStringMap(entries)
    }
  }

  private def coerceOutput(value: String): AnyRef = {

    DataTemplateUtil.coerceInput(value, classOf[java.lang.String], classOf[java.lang.String])

  }

  private def coerceKeyOutput(key: Boolean): String = {

    def coerceKeyDataOutput(value: Boolean): AnyRef = {

      DataTemplateUtil.coerceInput(Boolean.box(value), classOf[java.lang.Boolean], classOf[java.lang.Boolean])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[Boolean, String]): BooleanToStringMap = {
    BooleanToStringMap(map)
  }
}
