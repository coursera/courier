







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







 
@Generated(value = Array("IntToLongMap"), comments="Courier Data Template.", date = "Fri Jul 10 10:23:12 PDT 2015")
 final class IntToLongMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Int, Long)]
  with Map[Int, Long]
  with immutable.MapLike[Int, Long, immutable.Map[Int, Long]]
  with DataTemplate[DataMap] {
  import IntToLongMap._

  
  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] 
  def coerceInput(any: AnyRef): Long = {
    
        DataTemplateUtil.coerceOutput(any, classOf[java.lang.Long])
      
  }


  private[this] def coerceKeyInput(key: String): Int = {
    
  def coerceKeyDataInput(any: AnyRef): Int = {
    
        DataTemplateUtil.coerceOutput(any, classOf[java.lang.Integer])
      
  }


    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Int): Option[Long] = map.get(key)

  override def iterator: Iterator[(Int, Long)] = map.iterator

  override def +[F >: Long](kv: (Int, F)): Map[Int, F] = {
    val (key, value) = kv
    value match {
      case v: Long =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.setReadOnly()
        new IntToLongMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Int): IntToLongMap = {
    val copy = dataMap.copy()
    copy.remove(key)
    copy.setReadOnly()
    new IntToLongMap(copy)
  }

  override def schema(): DataSchema = IntToLongMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object IntToLongMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"long","keys":"int"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""int"""")

  
  

  
  





  val empty = IntToLongMap()

  def apply(elems: (Int, Long)*): IntToLongMap = {
    IntToLongMap(elems.toMap)
  }

  def apply(map: Map[Int, Long]): IntToLongMap = {
    new IntToLongMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): IntToLongMap = {
    new IntToLongMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[IntToLongMap, (Int, Long), IntToLongMap] {
    def apply(from: IntToLongMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: IntToLongMap) extends mutable.Builder[(Int, Long), IntToLongMap] {
    def this() = this(new IntToLongMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Int, Long)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.setReadOnly()
      new IntToLongMap(entries)
    }
  }

  private 
  def coerceOutput(value: Long): AnyRef = {
    
        DataTemplateUtil.coerceInput(Long.box(value), classOf[java.lang.Long], classOf[java.lang.Long])
      
  }


  private def coerceKeyOutput(key: Int): String = {
    
  def coerceKeyDataOutput(value: Int): AnyRef = {
    
        DataTemplateUtil.coerceInput(Int.box(value), classOf[java.lang.Integer], classOf[java.lang.Integer])
      
  }


    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }
}
