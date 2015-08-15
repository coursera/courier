

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

@Generated(value = Array("LongToFloatMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class LongToFloatMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Long, Float)]
  with Map[Long, Float]
  with immutable.MapLike[Long, Float, immutable.Map[Long, Float]]
  with DataTemplate[DataMap] {
  import LongToFloatMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): Float = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Float])

  }

  private[this] def coerceKeyInput(key: String): Long = {

    def coerceKeyDataInput(any: AnyRef): Long = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.Long])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Long): Option[Float] = map.get(key)

  override def iterator: Iterator[(Long, Float)] = map.iterator

  override def +[F >: Float](kv: (Long, F)): Map[Long, F] = {
    val (key, value) = kv
    value match {
      case v: Float =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new LongToFloatMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Long): LongToFloatMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new LongToFloatMap(copy)
  }

  override def schema(): DataSchema = LongToFloatMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object LongToFloatMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"float","keys":"long"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""long"""")

  val empty = LongToFloatMap()

  def apply(elems: (Long, Float)*): LongToFloatMap = {
    LongToFloatMap(elems.toMap)
  }

  def apply(map: Map[Long, Float]): LongToFloatMap = {
    new LongToFloatMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): LongToFloatMap = {
    new LongToFloatMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[LongToFloatMap, (Long, Float), LongToFloatMap] {
    def apply(from: LongToFloatMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: LongToFloatMap) extends mutable.Builder[(Long, Float), LongToFloatMap] {
    def this() = this(new LongToFloatMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Long, Float)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new LongToFloatMap(entries)
    }
  }

  private def coerceOutput(value: Float): AnyRef = {

    DataTemplateUtil.coerceInput(Float.box(value), classOf[java.lang.Float], classOf[java.lang.Float])

  }

  private def coerceKeyOutput(key: Long): String = {

    def coerceKeyDataOutput(value: Long): AnyRef = {

      DataTemplateUtil.coerceInput(Long.box(value), classOf[java.lang.Long], classOf[java.lang.Long])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[Long, Float]): LongToFloatMap = {
    LongToFloatMap(map)
  }
}
