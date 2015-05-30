

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




@Generated(value = Array("WithCustomTypesArray"), comments = "Courier Data Template.", date = "Sat May 30 17:14:08 PDT 2015")
final class WithCustomTypesArray private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithCustomTypesArray.SCHEMA) with Product {
  import WithCustomTypesArray._

  
  
    

    
    
        lazy val ints: org.coursera.customtypes.CustomIntArray = obtainWrapped(WithCustomTypesArray.Fields.ints, classOf[org.coursera.customtypes.CustomIntArray])
      
  

  
  private def setFields(ints: org.coursera.customtypes.CustomIntArray): Unit = {
    
      
      
           putWrapped(WithCustomTypesArray.Fields.ints, classOf[org.coursera.customtypes.CustomIntArray], ints)
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => ints
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithCustomTypesArray"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithCustomTypesArray]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(ints: org.coursera.customtypes.CustomIntArray = this.ints): WithCustomTypesArray = {
      val dataMap = new DataMap
      val result = new WithCustomTypesArray(dataMap)
      result.setFields(ints)
      dataMap.setReadOnly()
      result
    }
  
}

object WithCustomTypesArray {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithCustomTypesArray","namespace":"org.coursera.arrays","fields":[{"name":"ints","type":{"type":"array","items":{"type":"typeref","name":"CustomInt","namespace":"org.coursera.customtypes","ref":"int","java":{"coercerClass":"org.coursera.courier.generator.customtypes.CustomIntCoercer","class":"org.coursera.courier.generator.customtypes.CustomInt"}}}}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
  

  private object Fields {
    
    val ints = WithCustomTypesArray.SCHEMA.getField("ints")
  }

  def apply(ints: org.coursera.customtypes.CustomIntArray): WithCustomTypesArray = {
    val dataMap = new DataMap
    val result = new WithCustomTypesArray(dataMap)
    result.setFields(ints)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithCustomTypesArray = {
    new WithCustomTypesArray(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithCustomTypesArray): Option[(org.coursera.customtypes.CustomIntArray)] = {
        try {
          Some((record.ints))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


