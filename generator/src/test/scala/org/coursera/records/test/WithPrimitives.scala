

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




@Generated(value = Array("WithPrimitives"), comments = "Courier Data Template.", date = "Wed May 27 20:52:10 PDT 2015")
final class WithPrimitives private (private val dataMap: DataMap)
  extends RecordTemplate(dataMap, WithPrimitives.SCHEMA) with Product {
  import WithPrimitives._

  
  
    

    
    
        lazy val intField: Int = obtainDirect(WithPrimitives.Fields.intField, classOf[java.lang.Integer], GetMode.STRICT)
      
  
    

    
    
        lazy val longField: Long = obtainDirect(WithPrimitives.Fields.longField, classOf[java.lang.Long], GetMode.STRICT)
      
  
    

    
    
        lazy val floatField: Float = obtainDirect(WithPrimitives.Fields.floatField, classOf[java.lang.Float], GetMode.STRICT)
      
  
    

    
    
        lazy val doubleField: Double = obtainDirect(WithPrimitives.Fields.doubleField, classOf[java.lang.Double], GetMode.STRICT)
      
  
    

    
    
        lazy val booleanField: Boolean = obtainDirect(WithPrimitives.Fields.booleanField, classOf[java.lang.Boolean], GetMode.STRICT)
      
  
    

    
    
        lazy val stringField: String = obtainDirect(WithPrimitives.Fields.stringField, classOf[java.lang.String], GetMode.STRICT)
      
  
    

    
    
        lazy val bytesField: com.linkedin.data.ByteString = obtainDirect(WithPrimitives.Fields.bytesField, classOf[com.linkedin.data.ByteString], GetMode.STRICT)
      
  

  
  private def setFields(intField: Int, longField: Long, floatField: Float, doubleField: Double, booleanField: Boolean, stringField: String, bytesField: com.linkedin.data.ByteString): Unit = {
    
      
      
           putDirect(WithPrimitives.Fields.intField, classOf[java.lang.Integer], Int.box(intField))
        
    
      
      
           putDirect(WithPrimitives.Fields.longField, classOf[java.lang.Long], Long.box(longField))
        
    
      
      
           putDirect(WithPrimitives.Fields.floatField, classOf[java.lang.Float], Float.box(floatField))
        
    
      
      
           putDirect(WithPrimitives.Fields.doubleField, classOf[java.lang.Double], Double.box(doubleField))
        
    
      
      
           putDirect(WithPrimitives.Fields.booleanField, classOf[java.lang.Boolean], Boolean.box(booleanField))
        
    
      
      
           putDirect(WithPrimitives.Fields.stringField, classOf[java.lang.String], stringField)
        
    
      
      
           putDirect(WithPrimitives.Fields.bytesField, classOf[com.linkedin.data.ByteString], bytesField)
        
    
  }

  override val productArity: Int = 7

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => intField
      case 1 => longField
      case 2 => floatField
      case 3 => doubleField
      case 4 => booleanField
      case 5 => stringField
      case 6 => bytesField
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithPrimitives"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithPrimitives]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  def copy(intField: Int = this.intField, longField: Long = this.longField, floatField: Float = this.floatField, doubleField: Double = this.doubleField, booleanField: Boolean = this.booleanField, stringField: String = this.stringField, bytesField: com.linkedin.data.ByteString = this.bytesField): WithPrimitives = {
    val dataMap = new DataMap
    val result = new WithPrimitives(dataMap)
    result.setFields(intField, longField, floatField, doubleField, booleanField, stringField, bytesField)
    dataMap.setReadOnly()
    result
  }
}

object WithPrimitives {
  private val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithPrimitives","namespace":"org.coursera.records.test","fields":[{"name":"intField","type":"int"},{"name":"longField","type":"long"},{"name":"floatField","type":"float"},{"name":"doubleField","type":"double"},{"name":"booleanField","type":"boolean"},{"name":"stringField","type":"string"},{"name":"bytesField","type":"bytes"}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
  
    
  
    
  
    
  
    
  
    
  
    
  

  private object Fields {
    
    val intField = WithPrimitives.SCHEMA.getField("intField")
    val longField = WithPrimitives.SCHEMA.getField("longField")
    val floatField = WithPrimitives.SCHEMA.getField("floatField")
    val doubleField = WithPrimitives.SCHEMA.getField("doubleField")
    val booleanField = WithPrimitives.SCHEMA.getField("booleanField")
    val stringField = WithPrimitives.SCHEMA.getField("stringField")
    val bytesField = WithPrimitives.SCHEMA.getField("bytesField")
  }

  def apply(intField: Int, longField: Long, floatField: Float, doubleField: Double, booleanField: Boolean, stringField: String, bytesField: com.linkedin.data.ByteString): WithPrimitives = {
    val dataMap = new DataMap
    val result = new WithPrimitives(dataMap)
    result.setFields(intField, longField, floatField, doubleField, booleanField, stringField, bytesField)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithPrimitives = {
    new WithPrimitives(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def unapply(record: WithPrimitives): Option[(Int, Long, Float, Double, Boolean, String, com.linkedin.data.ByteString)] = {
    try {
      Some((record.intField, record.longField, record.floatField, record.doubleField, record.booleanField, record.stringField, record.bytesField))
    } catch {
      case cast: TemplateOutputCastException => None
      case notPresent: RequiredFieldNotPresentException => None
    }
  }
}


