

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




@Generated(value = Array("WithInlineRecord"), comments = "Courier Data Template.", date = "Fri May 29 11:12:12 PDT 2015")
final class WithInlineRecord private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithInlineRecord.SCHEMA) with Product {
  import WithInlineRecord._

  
  
    

    
    
        lazy val inline: org.coursera.records.test.InlineRecord = obtainWrapped(WithInlineRecord.Fields.inline, classOf[org.coursera.records.test.InlineRecord])
      
  
    

    
    
        lazy val inlineOptional: Option[org.coursera.records.test.InlineOptionalRecord] = Option(obtainWrapped(WithInlineRecord.Fields.inlineOptional, classOf[org.coursera.records.test.InlineOptionalRecord]))
      
  

  
  private def setFields(inline: org.coursera.records.test.InlineRecord, inlineOptional: Option[org.coursera.records.test.InlineOptionalRecord]): Unit = {
    
      
      
           putWrapped(WithInlineRecord.Fields.inline, classOf[org.coursera.records.test.InlineRecord], inline)
        
    
      
      
          inlineOptional.foreach(value =>  putWrapped(WithInlineRecord.Fields.inlineOptional, classOf[org.coursera.records.test.InlineOptionalRecord], value))
        
    
  }

  override val productArity: Int = 2

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => inline
      case 1 => inlineOptional
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithInlineRecord"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithInlineRecord]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(inline: org.coursera.records.test.InlineRecord = this.inline, inlineOptional: Option[org.coursera.records.test.InlineOptionalRecord] = this.inlineOptional): WithInlineRecord = {
      val dataMap = new DataMap
      val result = new WithInlineRecord(dataMap)
      result.setFields(inline, inlineOptional)
      dataMap.setReadOnly()
      result
    }
  
}

object WithInlineRecord {
  private val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithInlineRecord","namespace":"org.coursera.records.test","fields":[{"name":"inline","type":{"type":"record","name":"InlineRecord","fields":[{"name":"value","type":"int"}]}},{"name":"inlineOptional","type":{"type":"record","name":"InlineOptionalRecord","fields":[{"name":"value","type":"string"}]},"optional":true}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
  
    
  

  private object Fields {
    
    val inline = WithInlineRecord.SCHEMA.getField("inline")
    val inlineOptional = WithInlineRecord.SCHEMA.getField("inlineOptional")
  }

  def apply(inline: org.coursera.records.test.InlineRecord, inlineOptional: Option[org.coursera.records.test.InlineOptionalRecord]): WithInlineRecord = {
    val dataMap = new DataMap
    val result = new WithInlineRecord(dataMap)
    result.setFields(inline, inlineOptional)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithInlineRecord = {
    new WithInlineRecord(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithInlineRecord): Option[(org.coursera.records.test.InlineRecord, Option[org.coursera.records.test.InlineOptionalRecord])] = {
        try {
          Some((record.inline, record.inlineOptional))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


