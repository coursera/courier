

package org.coursera.records.test

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




@Generated(value = Array("InlineRecord"), comments = "Courier Data Template.", date = "Fri May 29 11:12:12 PDT 2015")
final class InlineRecord private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, InlineRecord.SCHEMA) with Product {
  import InlineRecord._

  
  
    

    
    
        lazy val value: Int = obtainDirect(InlineRecord.Fields.value, classOf[java.lang.Integer])
      
  

  
  private def setFields(value: Int): Unit = {
    
      
      
           putDirect(InlineRecord.Fields.value, classOf[java.lang.Integer], Int.box(value))
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => value
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "InlineRecord"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[InlineRecord]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(value: Int = this.value): InlineRecord = {
      val dataMap = new DataMap
      val result = new InlineRecord(dataMap)
      result.setFields(value)
      dataMap.setReadOnly()
      result
    }
  
}

object InlineRecord {
  private val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"InlineRecord","namespace":"org.coursera.records.test","fields":[{"name":"value","type":"int"}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
  

  private object Fields {
    
    val value = InlineRecord.SCHEMA.getField("value")
  }

  def apply(value: Int): InlineRecord = {
    val dataMap = new DataMap
    val result = new InlineRecord(dataMap)
    result.setFields(value)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): InlineRecord = {
    new InlineRecord(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: InlineRecord): Option[(Int)] = {
        try {
          Some((record.value))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


