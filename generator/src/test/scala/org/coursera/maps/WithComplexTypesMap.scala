

package org.coursera.maps

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




@Generated(value = Array("WithComplexTypesMap"), comments = "Courier Data Template.", date = "Sat May 30 10:07:51 PDT 2015")
final class WithComplexTypesMap private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithComplexTypesMap.SCHEMA) with Product {
  import WithComplexTypesMap._

  
  
    

    
    
        /* TODO(jbetz): support map fields */
        lazy val empties: org.coursera.records.test.EmptyMap = ???
      
  

  
  private def setFields(empties: org.coursera.records.test.EmptyMap): Unit = {
    
      
      
          /* TODO(jbetz): support map fields */
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => empties
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithComplexTypesMap"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithComplexTypesMap]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(empties: org.coursera.records.test.EmptyMap = this.empties): WithComplexTypesMap = {
      val dataMap = new DataMap
      val result = new WithComplexTypesMap(dataMap)
      result.setFields(empties)
      dataMap.setReadOnly()
      result
    }
  
}

object WithComplexTypesMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithComplexTypesMap","namespace":"org.coursera.maps","fields":[{"name":"empties","type":{"type":"map","values":{"type":"record","name":"Empty","namespace":"org.coursera.records.test","fields":[]}}}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
  

  private object Fields {
    
    val empties = WithComplexTypesMap.SCHEMA.getField("empties")
  }

  def apply(empties: org.coursera.records.test.EmptyMap): WithComplexTypesMap = {
    val dataMap = new DataMap
    val result = new WithComplexTypesMap(dataMap)
    result.setFields(empties)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithComplexTypesMap = {
    new WithComplexTypesMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithComplexTypesMap): Option[(org.coursera.records.test.EmptyMap)] = {
        try {
          Some((record.empties))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


