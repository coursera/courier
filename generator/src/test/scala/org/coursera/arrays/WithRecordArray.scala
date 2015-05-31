

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




@Generated(value = Array("WithRecordArray"), comments = "Courier Data Template.", date = "Sat May 30 22:17:04 PDT 2015")
final class WithRecordArray private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithRecordArray.SCHEMA) with Product {
  import WithRecordArray._

  
  
    

    
    
        lazy val empties: org.coursera.records.test.EmptyArray = obtainWrapped(WithRecordArray.Fields.empties, classOf[org.coursera.records.test.EmptyArray])
      
  
    

    
    
        lazy val fruits: org.coursera.enums.FruitsArray = obtainWrapped(WithRecordArray.Fields.fruits, classOf[org.coursera.enums.FruitsArray])
      
  

  
  private def setFields(empties: org.coursera.records.test.EmptyArray, fruits: org.coursera.enums.FruitsArray): Unit = {
    
      
      
           putWrapped(WithRecordArray.Fields.empties, classOf[org.coursera.records.test.EmptyArray], empties)
        
    
      
      
           putWrapped(WithRecordArray.Fields.fruits, classOf[org.coursera.enums.FruitsArray], fruits)
        
    
  }

  override val productArity: Int = 2

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => empties
      case 1 => fruits
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithRecordArray"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithRecordArray]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(empties: org.coursera.records.test.EmptyArray = this.empties, fruits: org.coursera.enums.FruitsArray = this.fruits): WithRecordArray = {
      val dataMap = new DataMap
      val result = new WithRecordArray(dataMap)
      result.setFields(empties, fruits)
      dataMap.setReadOnly()
      result
    }
  
}

object WithRecordArray {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithRecordArray","namespace":"org.coursera.arrays","fields":[{"name":"empties","type":{"type":"array","items":{"type":"record","name":"Empty","namespace":"org.coursera.records.test","fields":[]}}},{"name":"fruits","type":{"type":"array","items":{"type":"enum","name":"Fruits","namespace":"org.coursera.enums","symbols":["APPLE","BANANA","ORANGE","PINEAPPLE"],"symbolDocs":{"APPLE":"An Apple."}}}}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
  
    
  

  private object Fields {
    
    val empties = WithRecordArray.SCHEMA.getField("empties")
    val fruits = WithRecordArray.SCHEMA.getField("fruits")
  }

  def apply(empties: org.coursera.records.test.EmptyArray, fruits: org.coursera.enums.FruitsArray): WithRecordArray = {
    val dataMap = new DataMap
    val result = new WithRecordArray(dataMap)
    result.setFields(empties, fruits)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithRecordArray = {
    new WithRecordArray(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithRecordArray): Option[(org.coursera.records.test.EmptyArray, org.coursera.enums.FruitsArray)] = {
        try {
          Some((record.empties, record.fruits))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


