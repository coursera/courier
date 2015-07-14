







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







 
@Generated(value = Array("BooleanToIntMap"), comments="Courier Data Template.", date = "Fri Jul 10 10:23:12 PDT 2015")
 final class BooleanToIntMap(private val dataMap: DataMap)
  extends immutable.Iterable[(Boolean, Int)]
  with Map[Boolean, Int]
  with immutable.MapLike[Boolean, Int, immutable.Map[Boolean, Int]]
  with DataTemplate[DataMap] {
  import BooleanToIntMap._

  
  private[this] lazy val map = dataMap.asScala.map { case (k, v) => coerceKeyInput(k) -> coerceInput(v) }.toMap

  private[this] 
  def coerceInput(any: AnyRef): Int = {
    
        DataTemplateUtil.coerceOutput(any, classOf[java.lang.Integer])
      
  }


  private[this] def coerceKeyInput(key: String): Boolean = {
    
  def coerceKeyDataInput(any: AnyRef): Boolean = {
    
        DataTemplateUtil.coerceOutput(any, classOf[java.lang.Boolean])
      
  }


    coerceKeyDataInput(InlineStringCodec.stringToData(key, KEY_SCHEMA))
  }

  override def get(key: Boolean): Option[Int] = map.get(key)

  override def iterator: Iterator[(Boolean, Int)] = map.iterator

  override def +[F >: Int](kv: (Boolean, F)): Map[Boolean, F] = {
    val (key, value) = kv
    value match {
      case v: Int =>
        val copy = dataMap.copy()
        copy.put(coerceKeyOutput(key), coerceOutput(v))
        copy.setReadOnly()
        new BooleanToIntMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: Boolean): BooleanToIntMap = {
    val copy = dataMap.copy()
    copy.remove(key)
    copy.setReadOnly()
    new BooleanToIntMap(copy)
  }

  override def schema(): DataSchema = BooleanToIntMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = this
}

object BooleanToIntMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"int","keys":"boolean"}""").asInstanceOf[MapDataSchema]
  val KEY_SCHEMA = DataTemplateUtil.parseSchema(""""boolean"""")

  
  

  
  





  val empty = BooleanToIntMap()

  def apply(elems: (Boolean, Int)*): BooleanToIntMap = {
    BooleanToIntMap(elems.toMap)
  }

  def apply(map: Map[Boolean, Int]): BooleanToIntMap = {
    new BooleanToIntMap(new DataMap(map.map { case (k, v) => coerceKeyOutput(k) -> coerceOutput(v) }.asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): BooleanToIntMap = {
    new BooleanToIntMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[BooleanToIntMap, (Boolean, Int), BooleanToIntMap] {
    def apply(from: BooleanToIntMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: BooleanToIntMap) extends mutable.Builder[(Boolean, Int), BooleanToIntMap] {
    def this() = this(new BooleanToIntMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (Boolean, Int)): this.type = {
      val (key, value) = kv
      entries.put(coerceKeyOutput(key), coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.setReadOnly()
      new BooleanToIntMap(entries)
    }
  }

  private 
  def coerceOutput(value: Int): AnyRef = {
    
        DataTemplateUtil.coerceInput(Int.box(value), classOf[java.lang.Integer], classOf[java.lang.Integer])
      
  }


  private def coerceKeyOutput(key: Boolean): String = {
    
  def coerceKeyDataOutput(value: Boolean): AnyRef = {
    
        DataTemplateUtil.coerceInput(Boolean.box(value), classOf[java.lang.Boolean], classOf[java.lang.Boolean])
      
  }


    InlineStringCodec.dataToString(coerceKeyDataOutput(key))
  }
}
