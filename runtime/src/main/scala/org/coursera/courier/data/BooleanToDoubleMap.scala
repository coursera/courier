







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







 
@Generated(value = Array("BooleanToDoubleMap"), comments="Courier Data Template.", date = "Fri Jul 10 10:23:12 PDT 2015")
 final class BooleanToDoubleMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Boolean, Double)]
  with Map[Boolean, Double]
  with immutable.MapLike[Boolean, Double, immutable.Map[Boolean, Double]]
  with DataTemplate[DataMap] {
  import BooleanToDoubleMap._

  
  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] 
  def coerceInput(any: AnyRef): Double = {
    
        DataTemplateUtil.coerceOutput(any, classOf[java.lang.Double])
      
  }


  private[this] def coerceKeyInput(key: String): Boolean = {
    
  def coerceKeyDataInput(any: AnyRef): Boolean = {
    
        DataTemplateUtil.coerceOutput(any, classOf[java.lang.Boolean])
      
  }


    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Boolean): Option[Double] = map.get(key)

  override def iterator: Iterator[(Boolean, Double)] = map.iterator

  override def +[F >: Double](kv: (Boolean, F)): Map[Boolean, F] = {
    val (key, value) = kv
    value match {
      case v: Double =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.setReadOnly()
        new BooleanToDoubleMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Boolean): BooleanToDoubleMap = {
    val copy = dataMap.copy()
    copy.remove(key)
    copy.setReadOnly()
    new BooleanToDoubleMap(copy)
  }

  override def schema(): DataSchema = BooleanToDoubleMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object BooleanToDoubleMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"double","keys":"boolean"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""boolean"""")

  
  

  
  





  val empty = BooleanToDoubleMap()

  def apply(elems: (Boolean, Double)*): BooleanToDoubleMap = {
    BooleanToDoubleMap(elems.toMap)
  }

  def apply(map: Map[Boolean, Double]): BooleanToDoubleMap = {
    new BooleanToDoubleMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): BooleanToDoubleMap = {
    new BooleanToDoubleMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[BooleanToDoubleMap, (Boolean, Double), BooleanToDoubleMap] {
    def apply(from: BooleanToDoubleMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: BooleanToDoubleMap) extends mutable.Builder[(Boolean, Double), BooleanToDoubleMap] {
    def this() = this(new BooleanToDoubleMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Boolean, Double)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.setReadOnly()
      new BooleanToDoubleMap(entries)
    }
  }

  private 
  def coerceOutput(value: Double): AnyRef = {
    
        DataTemplateUtil.coerceInput(Double.box(value), classOf[java.lang.Double], classOf[java.lang.Double])
      
  }


  private def coerceKeyOutput(key: Boolean): String = {
    
  def coerceKeyDataOutput(value: Boolean): AnyRef = {
    
        DataTemplateUtil.coerceInput(Boolean.box(value), classOf[java.lang.Boolean], classOf[java.lang.Boolean])
      
  }


    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }
}
