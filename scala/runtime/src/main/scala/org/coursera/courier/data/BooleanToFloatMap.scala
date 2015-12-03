

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

@Generated(value = Array("BooleanToFloatMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class BooleanToFloatMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Boolean, Float)]
  with Map[Boolean, Float]
  with immutable.MapLike[Boolean, Float, immutable.Map[Boolean, Float]]
  with DataTemplate[DataMap] {
  import BooleanToFloatMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): Float = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Float])

  }

  private[this] def coerceKeyInput(key: String): Boolean = {

    def coerceKeyDataInput(any: AnyRef): Boolean = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.Boolean])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Boolean): Option[Float] = map.get(key)

  override def iterator: Iterator[(Boolean, Float)] = map.iterator

  override def +[F >: Float](kv: (Boolean, F)): Map[Boolean, F] = {
    val (key, value) = kv
    value match {
      case v: Float =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new BooleanToFloatMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Boolean): BooleanToFloatMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new BooleanToFloatMap(copy)
  }

  override def schema(): DataSchema = BooleanToFloatMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object BooleanToFloatMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"float","keys":"boolean"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""boolean"""")

  val empty = BooleanToFloatMap()

  def apply(elems: (Boolean, Float)*): BooleanToFloatMap = {
    BooleanToFloatMap(elems.toMap)
  }

  def apply(map: Map[Boolean, Float]): BooleanToFloatMap = {
    new BooleanToFloatMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): BooleanToFloatMap = {
    new BooleanToFloatMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[BooleanToFloatMap, (Boolean, Float), BooleanToFloatMap] {
    def apply(from: BooleanToFloatMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: BooleanToFloatMap) extends mutable.Builder[(Boolean, Float), BooleanToFloatMap] {
    def this() = this(new BooleanToFloatMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Boolean, Float)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new BooleanToFloatMap(entries)
    }
  }

  private def coerceOutput(value: Float): AnyRef = {

    DataTemplateUtil.coerceInput(Float.box(value), classOf[java.lang.Float], classOf[java.lang.Float])

  }

  private def coerceKeyOutput(key: Boolean): String = {

    def coerceKeyDataOutput(value: Boolean): AnyRef = {

      DataTemplateUtil.coerceInput(Boolean.box(value), classOf[java.lang.Boolean], classOf[java.lang.Boolean])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[Boolean, Float]): BooleanToFloatMap = {
    BooleanToFloatMap(map)
  }
}
