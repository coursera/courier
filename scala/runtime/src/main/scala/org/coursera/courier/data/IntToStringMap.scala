

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

@Generated(value = Array("IntToStringMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class IntToStringMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Int, String)]
  with Map[Int, String]
  with immutable.MapLike[Int, String, immutable.Map[Int, String]]
  with DataTemplate[DataMap] {
  import IntToStringMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): String = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.String])

  }

  private[this] def coerceKeyInput(key: String): Int = {

    def coerceKeyDataInput(any: AnyRef): Int = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.Integer])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Int): Option[String] = map.get(key)

  override def iterator: Iterator[(Int, String)] = map.iterator

  override def +[F >: String](kv: (Int, F)): Map[Int, F] = {
    val (key, value) = kv
    value match {
      case v: String =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new IntToStringMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Int): IntToStringMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new IntToStringMap(copy)
  }

  override def schema(): DataSchema = IntToStringMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object IntToStringMap extends MapCompanion[IntToStringMap] {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"string","keys":"int"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""int"""")

  val empty = IntToStringMap()

  def apply(elems: (Int, String)*): IntToStringMap = {
    IntToStringMap(elems.toMap)
  }

  def apply(map: Map[Int, String]): IntToStringMap = {
    new IntToStringMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def build(dataMap: DataMap, conversion: DataConversion): IntToStringMap = {
    new IntToStringMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[IntToStringMap, (Int, String), IntToStringMap] {
    def apply(from: IntToStringMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: IntToStringMap) extends mutable.Builder[(Int, String), IntToStringMap] {
    def this() = this(new IntToStringMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Int, String)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new IntToStringMap(entries)
    }
  }

  private def coerceOutput(value: String): AnyRef = {

    DataTemplateUtil.coerceInput(value, classOf[java.lang.String], classOf[java.lang.String])

  }

  private def coerceKeyOutput(key: Int): String = {

    def coerceKeyDataOutput(value: Int): AnyRef = {

      DataTemplateUtil.coerceInput(Int.box(value), classOf[java.lang.Integer], classOf[java.lang.Integer])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[Int, String]): IntToStringMap = {
    IntToStringMap(map)
  }
}
