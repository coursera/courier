

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

@Generated(value = Array("IntToByteStringMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class IntToByteStringMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Int, ByteString)]
  with Map[Int, ByteString]
  with immutable.MapLike[Int, ByteString, immutable.Map[Int, ByteString]]
  with DataTemplate[DataMap] {
  import IntToByteStringMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): ByteString = {

    DataTemplateUtil.coerceOutput(any, classOf[com.linkedin.data.ByteString])

  }

  private[this] def coerceKeyInput(key: String): Int = {

    def coerceKeyDataInput(any: AnyRef): Int = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.Integer])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Int): Option[ByteString] = map.get(key)

  override def iterator: Iterator[(Int, ByteString)] = map.iterator

  override def +[F >: ByteString](kv: (Int, F)): Map[Int, F] = {
    val (key, value) = kv
    value match {
      case v: ByteString =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new IntToByteStringMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Int): IntToByteStringMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new IntToByteStringMap(copy)
  }

  override def schema(): DataSchema = IntToByteStringMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object IntToByteStringMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"bytes","keys":"int"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""int"""")

  val empty = IntToByteStringMap()

  def apply(elems: (Int, ByteString)*): IntToByteStringMap = {
    IntToByteStringMap(elems.toMap)
  }

  def apply(map: Map[Int, ByteString]): IntToByteStringMap = {
    new IntToByteStringMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): IntToByteStringMap = {
    new IntToByteStringMap(DataTemplates.makeImmutable(dataMap, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[IntToByteStringMap, (Int, ByteString), IntToByteStringMap] {
    def apply(from: IntToByteStringMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: IntToByteStringMap) extends mutable.Builder[(Int, ByteString), IntToByteStringMap] {
    def this() = this(new IntToByteStringMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Int, ByteString)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new IntToByteStringMap(entries)
    }
  }

  private def coerceOutput(value: ByteString): AnyRef = {

    DataTemplateUtil.coerceInput(value, classOf[com.linkedin.data.ByteString], classOf[com.linkedin.data.ByteString])

  }

  private def coerceKeyOutput(key: Int): String = {

    def coerceKeyDataOutput(value: Int): AnyRef = {

      DataTemplateUtil.coerceInput(Int.box(value), classOf[java.lang.Integer], classOf[java.lang.Integer])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[Int, ByteString]): IntToByteStringMap = {
    IntToByteStringMap(map)
  }
}
