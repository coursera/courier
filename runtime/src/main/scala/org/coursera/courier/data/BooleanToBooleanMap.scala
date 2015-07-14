







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







 
@Generated(value = Array("BooleanToBooleanMap"), comments="Courier Data Template.", date = "Fri Jul 10 10:23:12 PDT 2015")
 final class BooleanToBooleanMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Boolean, Boolean)]
  with Map[Boolean, Boolean]
  with immutable.MapLike[Boolean, Boolean, immutable.Map[Boolean, Boolean]]
  with DataTemplate[DataMap] {
  import BooleanToBooleanMap._

  
  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] 
  def coerceInput(any: AnyRef): Boolean = {
    
        DataTemplateUtil.coerceOutput(any, classOf[java.lang.Boolean])
      
  }


  private[this] def coerceKeyInput(key: String): Boolean = {
    
  def coerceKeyDataInput(any: AnyRef): Boolean = {
    
        DataTemplateUtil.coerceOutput(any, classOf[java.lang.Boolean])
      
  }


    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Boolean): Option[Boolean] = map.get(key)

  override def iterator: Iterator[(Boolean, Boolean)] = map.iterator

  override def +[F >: Boolean](kv: (Boolean, F)): Map[Boolean, F] = {
    val (key, value) = kv
    value match {
      case v: Boolean =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.setReadOnly()
        new BooleanToBooleanMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Boolean): BooleanToBooleanMap = {
    val copy = dataMap.copy()
    copy.remove(key)
    copy.setReadOnly()
    new BooleanToBooleanMap(copy)
  }

  override def schema(): DataSchema = BooleanToBooleanMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object BooleanToBooleanMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"boolean","keys":"boolean"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""boolean"""")

  
  

  
  





  val empty = BooleanToBooleanMap()

  def apply(elems: (Boolean, Boolean)*): BooleanToBooleanMap = {
    BooleanToBooleanMap(elems.toMap)
  }

  def apply(map: Map[Boolean, Boolean]): BooleanToBooleanMap = {
    new BooleanToBooleanMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): BooleanToBooleanMap = {
    new BooleanToBooleanMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[BooleanToBooleanMap, (Boolean, Boolean), BooleanToBooleanMap] {
    def apply(from: BooleanToBooleanMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: BooleanToBooleanMap) extends mutable.Builder[(Boolean, Boolean), BooleanToBooleanMap] {
    def this() = this(new BooleanToBooleanMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Boolean, Boolean)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.setReadOnly()
      new BooleanToBooleanMap(entries)
    }
  }

  private 
  def coerceOutput(value: Boolean): AnyRef = {
    
        DataTemplateUtil.coerceInput(Boolean.box(value), classOf[java.lang.Boolean], classOf[java.lang.Boolean])
      
  }


  private def coerceKeyOutput(key: Boolean): String = {
    
  def coerceKeyDataOutput(value: Boolean): AnyRef = {
    
        DataTemplateUtil.coerceInput(Boolean.box(value), classOf[java.lang.Boolean], classOf[java.lang.Boolean])
      
  }


    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }
}
