

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

@Generated(value = Array("IntToFloatMap"), comments = "Courier Data Template.", date = "Fri Aug 14 14:51:38 PDT 2015")
final class IntToFloatMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Int, Float)]
  with Map[Int, Float]
  with immutable.MapLike[Int, Float, immutable.Map[Int, Float]]
  with DataTemplate[DataMap] {
  import IntToFloatMap._

  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] def coerceInput(any: AnyRef): Float = {

    DataTemplateUtil.coerceOutput(any, classOf[java.lang.Float])

  }

  private[this] def coerceKeyInput(key: String): Int = {

    def coerceKeyDataInput(any: AnyRef): Int = {

      DataTemplateUtil.coerceOutput(any, classOf[java.lang.Integer])

    }

    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Int): Option[Float] = map.get(key)

  override def iterator: Iterator[(Int, Float)] = map.iterator

  override def +[F >: Float](kv: (Int, F)): Map[Int, F] = {
    val (key, value) = kv
    value match {
      case v: Float =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.makeReadOnly()
        new IntToFloatMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Int): IntToFloatMap = {
    val copy = dataMap.copy()
    copy.remove(coerceKeyOutput(key))
    copy.makeReadOnly()
    new IntToFloatMap(copy)
  }

  override def schema(): DataSchema = IntToFloatMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object IntToFloatMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"float","keys":"int"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""int"""")

  val empty = IntToFloatMap()

  def apply(elems: (Int, Float)*): IntToFloatMap = {
    IntToFloatMap(elems.toMap)
  }

  def apply(map: Map[Int, Float]): IntToFloatMap = {
    new IntToFloatMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): IntToFloatMap = {
    new IntToFloatMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[IntToFloatMap, (Int, Float), IntToFloatMap] {
    def apply(from: IntToFloatMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: IntToFloatMap) extends mutable.Builder[(Int, Float), IntToFloatMap] {
    def this() = this(new IntToFloatMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Int, Float)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.makeReadOnly()
      new IntToFloatMap(entries)
    }
  }

  private def coerceOutput(value: Float): AnyRef = {

    DataTemplateUtil.coerceInput(Float.box(value), classOf[java.lang.Float], classOf[java.lang.Float])

  }

  private def coerceKeyOutput(key: Int): String = {

    def coerceKeyDataOutput(value: Int): AnyRef = {

      DataTemplateUtil.coerceInput(Int.box(value), classOf[java.lang.Integer], classOf[java.lang.Integer])

    }

    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }

  implicit def wrap(map: Map[Int, Float]): IntToFloatMap = {
    IntToFloatMap(map)
  }
}
