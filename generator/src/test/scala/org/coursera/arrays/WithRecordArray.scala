

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




@Generated(value = Array("WithRecordArray"), comments = "Courier Data Template.", date = "Fri May 29 21:44:23 PDT 2015")
final class WithRecordArray private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithRecordArray.SCHEMA) with Product {
  import WithRecordArray._

  
  
    

    
    
        lazy val empties: org.coursera.records.test.EmptyArray = obtainWrapped(WithRecordArray.Fields.empties, classOf[org.coursera.records.test.EmptyArray])
      
  

  
  private def setFields(empties: org.coursera.records.test.EmptyArray): Unit = {
    
      
      
           putWrapped(WithRecordArray.Fields.empties, classOf[org.coursera.records.test.EmptyArray], empties)
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => empties
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithRecordArray"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithRecordArray]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(empties: org.coursera.records.test.EmptyArray = this.empties): WithRecordArray = {
      val dataMap = new DataMap
      val result = new WithRecordArray(dataMap)
      result.setFields(empties)
      dataMap.setReadOnly()
      result
    }
  
}

object WithRecordArray {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithRecordArray","namespace":"org.coursera.arrays","fields":[{"name":"empties","type":{"type":"array","items":{"type":"record","name":"Empty","namespace":"org.coursera.records.test","fields":[]}}}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
  

  private object Fields {
    
    val empties = WithRecordArray.SCHEMA.getField("empties")
  }

  def apply(empties: org.coursera.records.test.EmptyArray): WithRecordArray = {
    val dataMap = new DataMap
    val result = new WithRecordArray(dataMap)
    result.setFields(empties)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithRecordArray = {
    new WithRecordArray(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithRecordArray): Option[(org.coursera.records.test.EmptyArray)] = {
        try {
          Some((record.empties))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


