

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




@Generated(value = Array("`class`"), comments = "Courier Data Template.", date = "Sat May 30 22:16:44 PDT 2015")
final class `class` private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, `class`.SCHEMA) with Product {
  import `class`._

  
  

  
  private def setFields(): Unit = {
    
  }

  override val productArity: Int = 0

  override def productElement(n: Int): Any =
    n match {
      
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "`class`"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[`class`]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
}

object `class` {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"class","namespace":"org.coursera.escaping","fields":[]}""").asInstanceOf[RecordDataSchema]

  
  

  
  

  private object Fields {
    
  }

  def apply(): `class` = {
    val dataMap = new DataMap
    val result = new `class`(dataMap)
    result.setFields()
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): `class` = {
    new `class`(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: `class`): Boolean = true
    
}


