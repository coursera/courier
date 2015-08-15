

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

@Generated(value = Array("IntMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class IntMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, Int)]
  with Map[String, Int]
  with immutable.MapLike[String, Int, immutable.Map[String, Int]]
  with DataTemplate[DataMap] {
  import IntMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): Int = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Integer])

  }

  private[this] def coerceKeyInput(key: String): String = {

    def coerceKeyDataInput(any: AnyRef): String = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.String])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: String): Option[Int] = map.get(key)

  override def iterator: Iterator[(String, Int)] = map.iterator

  override def +[F >: Int](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case v: Int =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new IntMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: String): IntMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new IntMap(copy)
  }

  override def schema(): DataSchema = IntMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object IntMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"int"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""string"""")

  val empty = IntMap()

  def apply(elems: (String, Int)*): IntMap = {
    IntMap(elems.toMap)
  }

  def apply(map: Map[String, Int]): IntMap = {
    new IntMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): IntMap = {
    new IntMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[IntMap, (String, Int), IntMap] {
    def apply(from: IntMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: IntMap) extends mutable.Builder[(String, Int), IntMap] {
    def this() = this(new IntMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (String, Int)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new IntMap(entries)
    }
  }

  private def coerceOutput(value: Int): AnyRef = {

    DataTemplateUtil.coerceInput(Int.box(value), classOf[java.lang.Integer], classOf[java.lang.Integer])

  }

  private def coerceKeyOutput(key: String): String = {

    def coerceKeyDataOutput(value: String): AnyRef = {

      DataTemplateUtil.coerceInput(value, classOf[java.lang.String], classOf[java.lang.String])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[String, Int]): IntMap = {
    IntMap(map)
  }
}
