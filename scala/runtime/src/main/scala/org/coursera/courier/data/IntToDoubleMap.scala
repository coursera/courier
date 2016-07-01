

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

@Generated(value = Array("IntToDoubleMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class IntToDoubleMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Int, Double)]
  with Map[Int, Double]
  with immutable.MapLike[Int, Double, immutable.Map[Int, Double]]
  with DataTemplate[DataMap] {
  import IntToDoubleMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): Double = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Double])

  }

  private[this] def coerceKeyInput(key: String): Int = {

    def coerceKeyDataInput(any: AnyRef): Int = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.Integer])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Int): Option[Double] = map.get(key)

  override def iterator: Iterator[(Int, Double)] = map.iterator

  override def +[F >: Double](kv: (Int, F)): Map[Int, F] = {
    val (key, value) = kv
    value match {
      case v: Double =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new IntToDoubleMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Int): IntToDoubleMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new IntToDoubleMap(copy)
  }

  override def schema(): DataSchema = IntToDoubleMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object IntToDoubleMap extends MapCompanion[IntToDoubleMap] {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"double","keys":"int"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""int"""")

  val empty = IntToDoubleMap()

  def apply(elems: (Int, Double)*): IntToDoubleMap = {
    IntToDoubleMap(elems.toMap)
  }

  def apply(map: Map[Int, Double]): IntToDoubleMap = {
    new IntToDoubleMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def build(dataMap: DataMap, conversion: DataConversion): IntToDoubleMap = {
    new IntToDoubleMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[IntToDoubleMap, (Int, Double), IntToDoubleMap] {
    def apply(from: IntToDoubleMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: IntToDoubleMap) extends mutable.Builder[(Int, Double), IntToDoubleMap] {
    def this() = this(new IntToDoubleMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Int, Double)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new IntToDoubleMap(entries)
    }
  }

  private def coerceOutput(value: Double): AnyRef = {

    DataTemplateUtil.coerceInput(Double.box(value), classOf[java.lang.Double], classOf[java.lang.Double])

  }

  private def coerceKeyOutput(key: Int): String = {

    def coerceKeyDataOutput(value: Int): AnyRef = {

      DataTemplateUtil.coerceInput(Int.box(value), classOf[java.lang.Integer], classOf[java.lang.Integer])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[Int, Double]): IntToDoubleMap = {
    IntToDoubleMap(map)
  }
}
