

package org.coursera.records.test

import javax.annotation.Generated

import com.linkedin.data.DataMap
import com.linkedin.data.schema.UnionDataSchema
import com.linkedin.data.template.Custom
import com.linkedin.data.template.GetMode
import com.linkedin.data.template.SetMode
import com.linkedin.data.template.RecordTemplate
import com.linkedin.data.template.RequiredFieldNotPresentException
import com.linkedin.data.template.TemplateOutputCastException
import com.linkedin.data.template.UnionTemplate
import org.coursera.courier.data.DataTemplates
import org.coursera.courier.data.DataTemplates.DataConversion
import scala.runtime.ScalaRunTime
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.schema.RecordDataSchema
import javax.annotation.Generated




@Generated(value = Array("InlineOptionalRecord"), comments = "Courier Data Template.", date = "Wed May 27 20:52:11 PDT 2015")
final class InlineOptionalRecord private (private val dataMap: DataMap)
  extends RecordTemplate(dataMap, InlineOptionalRecord.SCHEMA) with Product {
  import InlineOptionalRecord._

  
  
    

    
    
        lazy val value: String = obtainDirect(InlineOptionalRecord.Fields.value, classOf[java.lang.String], GetMode.STRICT)
      
  

  
  private def setFields(value: String): Unit = {
    
      
      
           putDirect(InlineOptionalRecord.Fields.value, classOf[java.lang.String], value)
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => value
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "InlineOptionalRecord"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[InlineOptionalRecord]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  def copy(value: String = this.value): InlineOptionalRecord = {
    val dataMap = new DataMap
    val result = new InlineOptionalRecord(dataMap)
    result.setFields(value)
    dataMap.setReadOnly()
    result
  }
}

object InlineOptionalRecord {
  private val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"InlineOptionalRecord","namespace":"org.coursera.records.test","fields":[{"name":"value","type":"string"}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
  

  private object Fields {
    
    val value = InlineOptionalRecord.SCHEMA.getField("value")
  }

  def apply(value: String): InlineOptionalRecord = {
    val dataMap = new DataMap
    val result = new InlineOptionalRecord(dataMap)
    result.setFields(value)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): InlineOptionalRecord = {
    new InlineOptionalRecord(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def unapply(record: InlineOptionalRecord): Option[(String)] = {
    try {
      Some((record.value))
    } catch {
      case cast: TemplateOutputCastException => None
      case notPresent: RequiredFieldNotPresentException => None
    }
  }
}


