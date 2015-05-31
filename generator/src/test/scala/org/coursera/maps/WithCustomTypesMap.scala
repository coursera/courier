

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




@Generated(value = Array("WithCustomTypesMap"), comments = "Courier Data Template.", date = "Sun May 31 11:22:32 PDT 2015")
final class WithCustomTypesMap private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithCustomTypesMap.SCHEMA) with Product {
  import WithCustomTypesMap._

  
  
    

    
    
        lazy val ints: org.coursera.customtypes.CustomIntMap = obtainWrapped(WithCustomTypesMap.Fields.ints, classOf[org.coursera.customtypes.CustomIntMap])
      
  

  
  private def setFields(ints: org.coursera.customtypes.CustomIntMap): Unit = {
    
      
      
           putWrapped(WithCustomTypesMap.Fields.ints, classOf[org.coursera.customtypes.CustomIntMap], ints)
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => ints
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithCustomTypesMap"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithCustomTypesMap]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(ints: org.coursera.customtypes.CustomIntMap = this.ints): WithCustomTypesMap = {
      val dataMap = new DataMap
      val result = new WithCustomTypesMap(dataMap)
      result.setFields(ints)
      dataMap.setReadOnly()
      result
    }
  
}

object WithCustomTypesMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithCustomTypesMap","namespace":"org.coursera.maps","fields":[{"name":"ints","type":{"type":"map","values":{"type":"typeref","name":"CustomInt","namespace":"org.coursera.customtypes","ref":"int","java":{"coercerClass":"org.coursera.courier.generator.customtypes.CustomIntCoercer","class":"org.coursera.courier.generator.customtypes.CustomInt"}}}}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
  

  private object Fields {
    
    val ints = WithCustomTypesMap.SCHEMA.getField("ints")
  }

  def apply(ints: org.coursera.customtypes.CustomIntMap): WithCustomTypesMap = {
    val dataMap = new DataMap
    val result = new WithCustomTypesMap(dataMap)
    result.setFields(ints)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithCustomTypesMap = {
    new WithCustomTypesMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithCustomTypesMap): Option[(org.coursera.customtypes.CustomIntMap)] = {
        try {
          Some((record.ints))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


