







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







 
@Generated(value = Array("LongToDoubleMap"), comments="Courier Data Template.", date = "Fri Jul 10 10:23:12 PDT 2015")
 final class LongToDoubleMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Long, Double)]
  with Map[Long, Double]
  with immutable.MapLike[Long, Double, immutable.Map[Long, Double]]
  with DataTemplate[DataMap] {
  import LongToDoubleMap._

  
  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] 
  def coerceInput(any: AnyRef): Double = {
    
        DataTemplateUtil.coerceOutput(any, classOf[java.lang.Double])
      
  }


  private[this] def coerceKeyInput(key: String): Long = {
    
  def coerceKeyDataInput(any: AnyRef): Long = {
    
        DataTemplateUtil.coerceOutput(any, classOf[java.lang.Long])
      
  }


    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Long): Option[Double] = map.get(key)

  override def iterator: Iterator[(Long, Double)] = map.iterator

  override def +[F >: Double](kv: (Long, F)): Map[Long, F] = {
    val (key, value) = kv
    value match {
      case v: Double =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.setReadOnly()
        new LongToDoubleMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Long): LongToDoubleMap = {
    val copy = dataMap.copy()
    copy.remove(key)
    copy.setReadOnly()
    new LongToDoubleMap(copy)
  }

  override def schema(): DataSchema = LongToDoubleMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object LongToDoubleMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"double","keys":"long"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""long"""")

  
  

  
  





  val empty = LongToDoubleMap()

  def apply(elems: (Long, Double)*): LongToDoubleMap = {
    LongToDoubleMap(elems.toMap)
  }

  def apply(map: Map[Long, Double]): LongToDoubleMap = {
    new LongToDoubleMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): LongToDoubleMap = {
    new LongToDoubleMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[LongToDoubleMap, (Long, Double), LongToDoubleMap] {
    def apply(from: LongToDoubleMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: LongToDoubleMap) extends mutable.Builder[(Long, Double), LongToDoubleMap] {
    def this() = this(new LongToDoubleMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Long, Double)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.setReadOnly()
      new LongToDoubleMap(entries)
    }
  }

  private 
  def coerceOutput(value: Double): AnyRef = {
    
        DataTemplateUtil.coerceInput(Double.box(value), classOf[java.lang.Double], classOf[java.lang.Double])
      
  }


  private def coerceKeyOutput(key: Long): String = {
    
  def coerceKeyDataOutput(value: Long): AnyRef = {
    
        DataTemplateUtil.coerceInput(Long.box(value), classOf[java.lang.Long], classOf[java.lang.Long])
      
  }


    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }
}
