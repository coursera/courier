

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

@Generated(value = Array("IntToBooleanMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class IntToBooleanMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Int, Boolean)]
  with Map[Int, Boolean]
  with immutable.MapLike[Int, Boolean, immutable.Map[Int, Boolean]]
  with DataTemplate[DataMap] {
  import IntToBooleanMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): Boolean = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Boolean])

  }

  private[this] def coerceKeyInput(key: String): Int = {

    def coerceKeyDataInput(any: AnyRef): Int = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.Integer])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Int): Option[Boolean] = map.get(key)

  override def iterator: Iterator[(Int, Boolean)] = map.iterator

  override def +[F >: Boolean](kv: (Int, F)): Map[Int, F] = {
    val (key, value) = kv
    value match {
      case v: Boolean =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new IntToBooleanMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Int): IntToBooleanMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new IntToBooleanMap(copy)
  }

  override def schema(): DataSchema = IntToBooleanMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object IntToBooleanMap extends MapCompanion[IntToBooleanMap] {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"boolean","keys":"int"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""int"""")

  val empty = IntToBooleanMap()

  def apply(elems: (Int, Boolean)*): IntToBooleanMap = {
    IntToBooleanMap(elems.toMap)
  }

  def apply(map: Map[Int, Boolean]): IntToBooleanMap = {
    new IntToBooleanMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def build(dataMap: DataMap, conversion: DataConversion): IntToBooleanMap = {
    new IntToBooleanMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[IntToBooleanMap, (Int, Boolean), IntToBooleanMap] {
    def apply(from: IntToBooleanMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: IntToBooleanMap) extends mutable.Builder[(Int, Boolean), IntToBooleanMap] {
    def this() = this(new IntToBooleanMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Int, Boolean)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new IntToBooleanMap(entries)
    }
  }

  private def coerceOutput(value: Boolean): AnyRef = {

    DataTemplateUtil.coerceInput(Boolean.box(value), classOf[java.lang.Boolean], classOf[java.lang.Boolean])

  }

  private def coerceKeyOutput(key: Int): String = {

    def coerceKeyDataOutput(value: Int): AnyRef = {

      DataTemplateUtil.coerceInput(Int.box(value), classOf[java.lang.Integer], classOf[java.lang.Integer])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[Int, Boolean]): IntToBooleanMap = {
    IntToBooleanMap(map)
  }
}
