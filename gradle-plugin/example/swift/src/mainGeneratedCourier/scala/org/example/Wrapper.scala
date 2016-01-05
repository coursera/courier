



   package org.example 

  import javax.annotation.Generated

  import com.linkedin.data.DataMap
  import com.linkedin.data.schema.DataSchema
  import com.linkedin.data.schema.UnionDataSchema
  import com.linkedin.data.template.Custom
  import com.linkedin.data.template.DataTemplate
  import com.linkedin.data.template.RecordTemplate
  import com.linkedin.data.template.RequiredFieldNotPresentException
  import com.linkedin.data.template.TemplateOutputCastException
  import com.linkedin.data.template.UnionTemplate
  import org.coursera.courier.templates.DataTemplates
  import org.coursera.courier.templates.DataTemplates.DataConversion
  import org.coursera.courier.templates.ScalaRecordTemplate
  import org.coursera.courier.templates.ScalaUnionTemplate
  import scala.runtime.ScalaRunTime
  import com.linkedin.data.template.DataTemplateUtil
  import com.linkedin.data.schema.RecordDataSchema
  import com.linkedin.data.schema.DataSchemaConstants
  import com.linkedin.data.ByteString
  import com.linkedin.data.DataList
  import scala.collection.JavaConverters._
  import scala.collection.immutable
  import scala.collection.mutable
  import scala.collection.generic.CanBuildFrom
  import com.linkedin.data.schema.MapDataSchema
  import com.linkedin.data.schema.ArrayDataSchema
  import scala.collection.GenTraversable
  import org.coursera.courier.codecs.InlineStringCodec







 
@Generated(value = Array("Wrapper"), comments="Courier Data Template.", date = "Tue Aug 11 12:03:03 PDT 2015")
 final class Wrapper private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, Wrapper.SCHEMA) with Product {
  import Wrapper._

  
  
    
    
        

 
 lazy val fortune: org.example.Fortune = obtainWrapped(Wrapper.Fields.fortune, classOf[org.example.Fortune])
      
  

  
  private def setFields(fortune: org.example.Fortune): Unit = {
    
      
      
           putWrapped(Wrapper.Fields.fortune, classOf[org.example.Fortune], fortune)
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => fortune
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "Wrapper"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[Wrapper]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  override def copy(): Wrapper = this

  
    def copy(fortune: org.example.Fortune = this.fortune): Wrapper = {
      val dataMap = new DataMap
      val result = new Wrapper(dataMap)
      result.setFields(fortune)
      dataMap.makeReadOnly()
      result
    }
  
}

object Wrapper {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"Wrapper","namespace":"org.example","fields":[{"name":"fortune","type":{"type":"record","name":"Fortune","fields":[{"name":"message","type":"string"}]}}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  





  private object Fields {
    
    val fortune = Wrapper.SCHEMA.getField("fortune")
  }

  
  def apply(fortune: org.example.Fortune): Wrapper = {
    val dataMap = new DataMap
    val result = new Wrapper(dataMap)
    result.setFields(fortune)
    dataMap.makeReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): Wrapper = {
    new Wrapper(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
   
      def unapply(record: Wrapper): Option[(org.example.Fortune)] = {
        try {
          Some((record.fortune))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}

