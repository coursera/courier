

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




@Generated(value = Array("WithPrimitiveCustomTypes"), comments="Courier Data Template. Generated from TODO", date = "Wed May 27 17:11:07 PDT 2015")
final class WithPrimitiveCustomTypes private (private val dataMap: DataMap)
  extends RecordTemplate(dataMap, WithPrimitiveCustomTypes.SCHEMA) with Product {
  import WithPrimitiveCustomTypes._

  
  
    

    /* TODO(jbetz): Decide on order of fields and decide how to handle optional fields and defaults. Note that decisions here will impact source backward compatibility! */
    
        lazy val intField: org.coursera.courier.generator.customtypes.CustomInt = obtainCustomType(WithPrimitiveCustomTypes.Fields.intField, classOf[org.coursera.courier.generator.customtypes.CustomInt], GetMode.STRICT)
      
  

  
  private def setFields(intField: org.coursera.courier.generator.customtypes.CustomInt): Unit = {
    
      
      
          
           putCustomType(WithPrimitiveCustomTypes.Fields.intField, classOf[org.coursera.courier.generator.customtypes.CustomInt], classOf[java.lang.Integer], intField, SetMode.DISALLOW_NULL)
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => intField
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithPrimitiveCustomTypes"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithPrimitiveCustomTypes]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  def copy(intField: org.coursera.courier.generator.customtypes.CustomInt = this.intField): WithPrimitiveCustomTypes = {
    val dataMap = new DataMap
    val result = new WithPrimitiveCustomTypes(dataMap)
    result.setFields(intField)
    dataMap.setReadOnly()
    result
  }
}

object WithPrimitiveCustomTypes {
  private val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithPrimitiveCustomTypes","namespace":"org.coursera.records.test","fields":[{"name":"intField","type":{"type":"typeref","name":"IntCustomType","ref":"int","java":{"coercerClass":"org.coursera.courier.generator.customtypes.CustomIntCoercer","class":"org.coursera.courier.generator.customtypes.CustomInt"}}}]}""").asInstanceOf[RecordDataSchema]

  
  
    
      Custom.initializeCustomClass(classOf[org.coursera.courier.generator.customtypes.CustomInt])
      
        org.coursera.courier.generator.customtypes.CustomIntCoercer.registerCoercer()
      
    
  

  
  
    
  

  private object Fields {
    
    val intField = WithPrimitiveCustomTypes.SCHEMA.getField("intField")
  }

  def apply(intField: org.coursera.courier.generator.customtypes.CustomInt): WithPrimitiveCustomTypes = {
    val dataMap = new DataMap
    val result = new WithPrimitiveCustomTypes(dataMap)
    result.setFields(intField)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithPrimitiveCustomTypes = {
    new WithPrimitiveCustomTypes(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def unapply(record: WithPrimitiveCustomTypes): Option[(org.coursera.courier.generator.customtypes.CustomInt)] = {
    try {
      Some((record.intField))
    } catch {
      case cast: TemplateOutputCastException => None
      case notPresent: RequiredFieldNotPresentException => None
    }
  }
}


