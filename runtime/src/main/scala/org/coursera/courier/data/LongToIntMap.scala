

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

@Generated(value = Array("LongToIntMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class LongToIntMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Long, Int)]
  with Map[Long, Int]
  with immutable.MapLike[Long, Int, immutable.Map[Long, Int]]
  with DataTemplate[DataMap] {
  import LongToIntMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): Int = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Integer])

  }

  private[this] def coerceKeyInput(key: String): Long = {

    def coerceKeyDataInput(any: AnyRef): Long = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.Long])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Long): Option[Int] = map.get(key)

  override def iterator: Iterator[(Long, Int)] = map.iterator

  override def +[F >: Int](kv: (Long, F)): Map[Long, F] = {
    val (key, value) = kv
    value match {
      case v: Int =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new LongToIntMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Long): LongToIntMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new LongToIntMap(copy)
  }

  override def schema(): DataSchema = LongToIntMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object LongToIntMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"int","keys":"long"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""long"""")

  val empty = LongToIntMap()

  def apply(elems: (Long, Int)*): LongToIntMap = {
    LongToIntMap(elems.toMap)
  }

  def apply(map: Map[Long, Int]): LongToIntMap = {
    new LongToIntMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): LongToIntMap = {
    new LongToIntMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[LongToIntMap, (Long, Int), LongToIntMap] {
    def apply(from: LongToIntMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: LongToIntMap) extends mutable.Builder[(Long, Int), LongToIntMap] {
    def this() = this(new LongToIntMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Long, Int)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new LongToIntMap(entries)
    }
  }

  private def coerceOutput(value: Int): AnyRef = {

    DataTemplateUtil.coerceInput(Int.box(value), classOf[java.lang.Integer], classOf[java.lang.Integer])

  }

  private def coerceKeyOutput(key: Long): String = {

    def coerceKeyDataOutput(value: Long): AnyRef = {

      DataTemplateUtil.coerceInput(Long.box(value), classOf[java.lang.Long], classOf[java.lang.Long])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[Long, Int]): LongToIntMap = {
    LongToIntMap(map)
  }
}
