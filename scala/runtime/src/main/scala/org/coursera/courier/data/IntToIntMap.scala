

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

@Generated(value = Array("IntToIntMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class IntToIntMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Int, Int)]
  with Map[Int, Int]
  with immutable.MapLike[Int, Int, immutable.Map[Int, Int]]
  with DataTemplate[DataMap] {
  import IntToIntMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): Int = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Integer])

  }

  private[this] def coerceKeyInput(key: String): Int = {

    def coerceKeyDataInput(any: AnyRef): Int = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.Integer])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Int): Option[Int] = map.get(key)

  override def iterator: Iterator[(Int, Int)] = map.iterator

  override def +[F >: Int](kv: (Int, F)): Map[Int, F] = {
    val (key, value) = kv
    value match {
      case v: Int =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new IntToIntMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Int): IntToIntMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new IntToIntMap(copy)
  }

  override def schema(): DataSchema = IntToIntMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object IntToIntMap extends MapCompanion[IntToIntMap] {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"int","keys":"int"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""int"""")

  val empty = IntToIntMap()

  def apply(elems: (Int, Int)*): IntToIntMap = {
    IntToIntMap(elems.toMap)
  }

  def apply(map: Map[Int, Int]): IntToIntMap = {
    new IntToIntMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def build(dataMap: DataMap, conversion: DataConversion): IntToIntMap = {
    new IntToIntMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[IntToIntMap, (Int, Int), IntToIntMap] {
    def apply(from: IntToIntMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: IntToIntMap) extends mutable.Builder[(Int, Int), IntToIntMap] {
    def this() = this(new IntToIntMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Int, Int)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new IntToIntMap(entries)
    }
  }

  private def coerceOutput(value: Int): AnyRef = {

    DataTemplateUtil.coerceInput(Int.box(value), classOf[java.lang.Integer], classOf[java.lang.Integer])

  }

  private def coerceKeyOutput(key: Int): String = {

    def coerceKeyDataOutput(value: Int): AnyRef = {

      DataTemplateUtil.coerceInput(Int.box(value), classOf[java.lang.Integer], classOf[java.lang.Integer])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[Int, Int]): IntToIntMap = {
    IntToIntMap(map)
  }
}
