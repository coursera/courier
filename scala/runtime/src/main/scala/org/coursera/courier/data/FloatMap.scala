

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

@Generated(value = Array("FloatMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class FloatMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, Float)]
  with Map[String, Float]
  with immutable.MapLike[String, Float, immutable.Map[String, Float]]
  with DataTemplate[DataMap] {
  import FloatMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): Float = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Float])

  }

  private[this] def coerceKeyInput(key: String): String = {

    def coerceKeyDataInput(any: AnyRef): String = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.String])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: String): Option[Float] = map.get(key)

  override def iterator: Iterator[(String, Float)] = map.iterator

  override def +[F >: Float](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case v: Float =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new FloatMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: String): FloatMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new FloatMap(copy)
  }

  override def schema(): DataSchema = FloatMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object FloatMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"float"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""string"""")

  val empty = FloatMap()

  def apply(elems: (String, Float)*): FloatMap = {
    FloatMap(elems.toMap)
  }

  def apply(map: Map[String, Float]): FloatMap = {
    new FloatMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): FloatMap = {
    new FloatMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[FloatMap, (String, Float), FloatMap] {
    def apply(from: FloatMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: FloatMap) extends mutable.Builder[(String, Float), FloatMap] {
    def this() = this(new FloatMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (String, Float)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new FloatMap(entries)
    }
  }

  private def coerceOutput(value: Float): AnyRef = {

    DataTemplateUtil.coerceInput(Float.box(value), classOf[java.lang.Float], classOf[java.lang.Float])

  }

  private def coerceKeyOutput(key: String): String = {

    def coerceKeyDataOutput(value: String): AnyRef = {

      DataTemplateUtil.coerceInput(value, classOf[java.lang.String], classOf[java.lang.String])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[String, Float]): FloatMap = {
    FloatMap(map)
  }
}
