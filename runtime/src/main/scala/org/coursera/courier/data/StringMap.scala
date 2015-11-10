

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

@Generated(value = Array("StringMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class StringMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, String)]
  with Map[String, String]
  with immutable.MapLike[String, String, immutable.Map[String, String]]
  with DataTemplate[DataMap] {
  import StringMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): String = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.String])

  }

  private[this] def coerceKeyInput(key: String): String = {

    def coerceKeyDataInput(any: AnyRef): String = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.String])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: String): Option[String] = map.get(key)

  override def iterator: Iterator[(String, String)] = map.iterator

  override def +[F >: String](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case v: String =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new StringMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: String): StringMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new StringMap(copy)
  }

  override def schema(): DataSchema = StringMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object StringMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"string"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""string"""")

  val empty = StringMap()

  def apply(elems: (String, String)*): StringMap = {
    StringMap(elems.toMap)
  }

  def apply(map: Map[String, String]): StringMap = {
    new StringMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): StringMap = {
    new StringMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[StringMap, (String, String), StringMap] {
    def apply(from: StringMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: StringMap) extends mutable.Builder[(String, String), StringMap] {
    def this() = this(new StringMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (String, String)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new StringMap(entries)
    }
  }

  private def coerceOutput(value: String): AnyRef = {

    DataTemplateUtil.coerceInput(value, classOf[java.lang.String], classOf[java.lang.String])

  }

  private def coerceKeyOutput(key: String): String = {

    def coerceKeyDataOutput(value: String): AnyRef = {

      DataTemplateUtil.coerceInput(value, classOf[java.lang.String], classOf[java.lang.String])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[String, String]): StringMap = {
    StringMap(map)
  }
}
