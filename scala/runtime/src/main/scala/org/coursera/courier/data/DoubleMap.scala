

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

@Generated(value = Array("DoubleMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class DoubleMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, Double)]
  with Map[String, Double]
  with immutable.MapLike[String, Double, immutable.Map[String, Double]]
  with DataTemplate[DataMap] {
  import DoubleMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): Double = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Double])

  }

  private[this] def coerceKeyInput(key: String): String = {

    def coerceKeyDataInput(any: AnyRef): String = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.String])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: String): Option[Double] = map.get(key)

  override def iterator: Iterator[(String, Double)] = map.iterator

  override def +[F >: Double](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case v: Double =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new DoubleMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: String): DoubleMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new DoubleMap(copy)
  }

  override def schema(): DataSchema = DoubleMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object DoubleMap extends MapCompanion[DoubleMap] {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"double"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""string"""")

  val empty = DoubleMap()

  def apply(elems: (String, Double)*): DoubleMap = {
    DoubleMap(elems.toMap)
  }

  def apply(map: Map[String, Double]): DoubleMap = {
    new DoubleMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def build(dataMap: DataMap, conversion: DataConversion): DoubleMap = {
    new DoubleMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[DoubleMap, (String, Double), DoubleMap] {
    def apply(from: DoubleMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: DoubleMap) extends mutable.Builder[(String, Double), DoubleMap] {
    def this() = this(new DoubleMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (String, Double)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new DoubleMap(entries)
    }
  }

  private def coerceOutput(value: Double): AnyRef = {

    DataTemplateUtil.coerceInput(Double.box(value), classOf[java.lang.Double], classOf[java.lang.Double])

  }

  private def coerceKeyOutput(key: String): String = {

    def coerceKeyDataOutput(value: String): AnyRef = {

      DataTemplateUtil.coerceInput(value, classOf[java.lang.String], classOf[java.lang.String])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[String, Double]): DoubleMap = {
    DoubleMap(map)
  }
}
