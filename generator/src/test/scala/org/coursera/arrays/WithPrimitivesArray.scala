

package org.coursera.arrays

import javax.annotation.Generated

import com.linkedin.data.DataMap
import com.linkedin.data.schema.UnionDataSchema
import com.linkedin.data.template.Custom
import com.linkedin.data.template.RecordTemplate
import com.linkedin.data.template.RequiredFieldNotPresentException
import com.linkedin.data.template.TemplateOutputCastException
import com.linkedin.data.template.UnionTemplate
import org.coursera.courier.data.DataTemplates
import org.coursera.courier.data.DataTemplates.DataConversion
import org.coursera.courier.data.ScalaRecordTemplate
import scala.runtime.ScalaRunTime
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.schema.RecordDataSchema
import javax.annotation.Generated
import com.linkedin.data.schema.DataSchemaConstants
import com.linkedin.data.ByteString




@Generated(value = Array("WithPrimitivesArray"), comments = "Courier Data Template.", date = "Fri May 29 21:44:23 PDT 2015")
final class WithPrimitivesArray private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithPrimitivesArray.SCHEMA) with Product {
  import WithPrimitivesArray._

  
  
    

    
    
        lazy val ints: com.linkedin.data.template.IntegerArray = obtainWrapped(WithPrimitivesArray.Fields.ints, classOf[com.linkedin.data.template.IntegerArray])
      
  

  
  private def setFields(ints: com.linkedin.data.template.IntegerArray): Unit = {
    
      
      
           putWrapped(WithPrimitivesArray.Fields.ints, classOf[com.linkedin.data.template.IntegerArray], ints)
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => ints
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithPrimitivesArray"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithPrimitivesArray]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(ints: com.linkedin.data.template.IntegerArray = this.ints): WithPrimitivesArray = {
      val dataMap = new DataMap
      val result = new WithPrimitivesArray(dataMap)
      result.setFields(ints)
      dataMap.setReadOnly()
      result
    }
  
}

object WithPrimitivesArray {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithPrimitivesArray","namespace":"org.coursera.arrays","fields":[{"name":"ints","type":{"type":"array","items":"int"}}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
  

  private object Fields {
    
    val ints = WithPrimitivesArray.SCHEMA.getField("ints")
  }

  def apply(ints: com.linkedin.data.template.IntegerArray): WithPrimitivesArray = {
    val dataMap = new DataMap
    val result = new WithPrimitivesArray(dataMap)
    result.setFields(ints)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithPrimitivesArray = {
    new WithPrimitivesArray(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithPrimitivesArray): Option[(com.linkedin.data.template.IntegerArray)] = {
        try {
          Some((record.ints))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


