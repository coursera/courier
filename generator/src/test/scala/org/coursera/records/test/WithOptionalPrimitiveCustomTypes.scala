

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




@Generated(value = Array("WithOptionalPrimitiveCustomTypes"), comments="Courier Data Template. Generated from TODO", date = "Wed May 27 17:11:07 PDT 2015")
final class WithOptionalPrimitiveCustomTypes private (private val dataMap: DataMap)
  extends RecordTemplate(dataMap, WithOptionalPrimitiveCustomTypes.SCHEMA) with Product {
  import WithOptionalPrimitiveCustomTypes._

  
  
    

    /* TODO(jbetz): Decide on order of fields and decide how to handle optional fields and defaults. Note that decisions here will impact source backward compatibility! */
    
        lazy val intField: Option[org.coursera.courier.generator.customtypes.CustomInt] = Option(obtainCustomType(WithOptionalPrimitiveCustomTypes.Fields.intField, classOf[org.coursera.courier.generator.customtypes.CustomInt], GetMode.STRICT))
      
  

  
  private def setFields(intField: Option[org.coursera.courier.generator.customtypes.CustomInt]): Unit = {
    
      
      
          
          intField.foreach(value =>  putCustomType(WithOptionalPrimitiveCustomTypes.Fields.intField, classOf[org.coursera.courier.generator.customtypes.CustomInt], classOf[java.lang.Integer], value, SetMode.DISALLOW_NULL))
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => intField
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithOptionalPrimitiveCustomTypes"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithOptionalPrimitiveCustomTypes]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  def copy(intField: Option[org.coursera.courier.generator.customtypes.CustomInt] = this.intField): WithOptionalPrimitiveCustomTypes = {
    val dataMap = new DataMap
    val result = new WithOptionalPrimitiveCustomTypes(dataMap)
    result.setFields(intField)
    dataMap.setReadOnly()
    result
  }
}

object WithOptionalPrimitiveCustomTypes {
  private val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithOptionalPrimitiveCustomTypes","namespace":"org.coursera.records.test","fields":[{"name":"intField","type":{"type":"typeref","name":"OptionalIntCustomType","ref":"int","java":{"coercerClass":"org.coursera.courier.generator.customtypes.CustomIntCoercer","class":"org.coursera.courier.generator.customtypes.CustomInt"}},"optional":true}]}""").asInstanceOf[RecordDataSchema]

  
  
    
      Custom.initializeCustomClass(classOf[org.coursera.courier.generator.customtypes.CustomInt])
      
        org.coursera.courier.generator.customtypes.CustomIntCoercer.registerCoercer()
      
    
  

  
  
    
  

  private object Fields {
    
    val intField = WithOptionalPrimitiveCustomTypes.SCHEMA.getField("intField")
  }

  def apply(intField: Option[org.coursera.courier.generator.customtypes.CustomInt]): WithOptionalPrimitiveCustomTypes = {
    val dataMap = new DataMap
    val result = new WithOptionalPrimitiveCustomTypes(dataMap)
    result.setFields(intField)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithOptionalPrimitiveCustomTypes = {
    new WithOptionalPrimitiveCustomTypes(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def unapply(record: WithOptionalPrimitiveCustomTypes): Option[(Option[org.coursera.courier.generator.customtypes.CustomInt])] = {
    try {
      Some((record.intField))
    } catch {
      case cast: TemplateOutputCastException => None
      case notPresent: RequiredFieldNotPresentException => None
    }
  }
}


