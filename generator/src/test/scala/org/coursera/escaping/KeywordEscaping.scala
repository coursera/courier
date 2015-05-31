

package org.coursera.escaping

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




@Generated(value = Array("KeywordEscaping"), comments = "Courier Data Template.", date = "Sat May 30 22:16:44 PDT 2015")
final class KeywordEscaping private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, KeywordEscaping.SCHEMA) with Product {
  import KeywordEscaping._

  
  
    

    
    
        lazy val `type`: String = obtainDirect(KeywordEscaping.Fields.`type`, classOf[java.lang.String])
      
  

  
  private def setFields(`type`: String): Unit = {
    
      
      
           putDirect(KeywordEscaping.Fields.`type`, classOf[java.lang.String], `type`)
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => `type`
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "KeywordEscaping"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[KeywordEscaping]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(`type`: String = this.`type`): KeywordEscaping = {
      val dataMap = new DataMap
      val result = new KeywordEscaping(dataMap)
      result.setFields(`type`)
      dataMap.setReadOnly()
      result
    }
  
}

object KeywordEscaping {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"KeywordEscaping","namespace":"org.coursera.escaping","fields":[{"name":"type","type":"string"}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
  

  private object Fields {
    
    val `type` = KeywordEscaping.SCHEMA.getField("type")
  }

  def apply(`type`: String): KeywordEscaping = {
    val dataMap = new DataMap
    val result = new KeywordEscaping(dataMap)
    result.setFields(`type`)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): KeywordEscaping = {
    new KeywordEscaping(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: KeywordEscaping): Option[(String)] = {
        try {
          Some((record.`type`))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


