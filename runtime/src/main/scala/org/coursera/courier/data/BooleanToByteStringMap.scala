







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







 
@Generated(value = Array("BooleanToByteStringMap"), comments="Courier Data Template.", date = "Fri Jul 10 10:23:12 PDT 2015")
 final class BooleanToByteStringMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Boolean, ByteString)]
  with Map[Boolean, ByteString]
  with immutable.MapLike[Boolean, ByteString, immutable.Map[Boolean, ByteString]]
  with DataTemplate[DataMap] {
  import BooleanToByteStringMap._

  
  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] 
  def coerceInput(any: AnyRef): ByteString = {
    
        DataTemplateUtil.coerceOutput(any, classOf[com.linkedin.data.ByteString])
      
  }


  private[this] def coerceKeyInput(key: String): Boolean = {
    
  def coerceKeyDataInput(any: AnyRef): Boolean = {
    
        DataTemplateUtil.coerceOutput(any, classOf[java.lang.Boolean])
      
  }


    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Boolean): Option[ByteString] = map.get(key)

  override def iterator: Iterator[(Boolean, ByteString)] = map.iterator

  override def +[F >: ByteString](kv: (Boolean, F)): Map[Boolean, F] = {
    val (key, value) = kv
    value match {
      case v: ByteString =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.setReadOnly()
        new BooleanToByteStringMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Boolean): BooleanToByteStringMap = {
    val copy = dataMap.copy()
    copy.remove(key)
    copy.setReadOnly()
    new BooleanToByteStringMap(copy)
  }

  override def schema(): DataSchema = BooleanToByteStringMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object BooleanToByteStringMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"bytes","keys":"boolean"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""boolean"""")

  
  

  
  





  val empty = BooleanToByteStringMap()

  def apply(elems: (Boolean, ByteString)*): BooleanToByteStringMap = {
    BooleanToByteStringMap(elems.toMap)
  }

  def apply(map: Map[Boolean, ByteString]): BooleanToByteStringMap = {
    new BooleanToByteStringMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): BooleanToByteStringMap = {
    new BooleanToByteStringMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[BooleanToByteStringMap, (Boolean, ByteString), BooleanToByteStringMap] {
    def apply(from: BooleanToByteStringMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: BooleanToByteStringMap) extends mutable.Builder[(Boolean, ByteString), BooleanToByteStringMap] {
    def this() = this(new BooleanToByteStringMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Boolean, ByteString)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.setReadOnly()
      new BooleanToByteStringMap(entries)
    }
  }

  private 
  def coerceOutput(value: ByteString): AnyRef = {
    
        DataTemplateUtil.coerceInput(value, classOf[com.linkedin.data.ByteString], classOf[com.linkedin.data.ByteString])
      
  }


  private def coerceKeyOutput(key: Boolean): String = {
    
  def coerceKeyDataOutput(value: Boolean): AnyRef = {
    
        DataTemplateUtil.coerceInput(Boolean.box(value), classOf[java.lang.Boolean], classOf[java.lang.Boolean])
      
  }


    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }
}
