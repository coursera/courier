

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
import com.linkedin.data.schema.DataSchemaConstants
import com.linkedin.data.ByteString




@Generated(value = Array("WithPrimitiveTyperefs"), comments = "Courier Data Template.", date = "Sat May 30 19:52:42 PDT 2015")
final class WithPrimitiveTyperefs private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithPrimitiveTyperefs.SCHEMA) with Product {
  import WithPrimitiveTyperefs._

  
  
    

    
    
        lazy val intField: Int = obtainDirect(WithPrimitiveTyperefs.Fields.intField, classOf[java.lang.Integer])
      
  
    

    
    
        lazy val longField: Long = obtainDirect(WithPrimitiveTyperefs.Fields.longField, classOf[java.lang.Long])
      
  
    

    
    
        lazy val floatField: Float = obtainDirect(WithPrimitiveTyperefs.Fields.floatField, classOf[java.lang.Float])
      
  
    

    
    
        lazy val doubleField: Double = obtainDirect(WithPrimitiveTyperefs.Fields.doubleField, classOf[java.lang.Double])
      
  
    

    
    
        lazy val booleanField: Boolean = obtainDirect(WithPrimitiveTyperefs.Fields.booleanField, classOf[java.lang.Boolean])
      
  
    

    
    
        lazy val stringField: String = obtainDirect(WithPrimitiveTyperefs.Fields.stringField, classOf[java.lang.String])
      
  
    

    
    
        lazy val bytesField: ByteString = obtainDirect(WithPrimitiveTyperefs.Fields.bytesField, classOf[com.linkedin.data.ByteString])
      
  

  
  private def setFields(intField: Int, longField: Long, floatField: Float, doubleField: Double, booleanField: Boolean, stringField: String, bytesField: ByteString): Unit = {
    
      
      
           putDirect(WithPrimitiveTyperefs.Fields.intField, classOf[java.lang.Integer], Int.box(intField))
        
    
      
      
           putDirect(WithPrimitiveTyperefs.Fields.longField, classOf[java.lang.Long], Long.box(longField))
        
    
      
      
           putDirect(WithPrimitiveTyperefs.Fields.floatField, classOf[java.lang.Float], Float.box(floatField))
        
    
      
      
           putDirect(WithPrimitiveTyperefs.Fields.doubleField, classOf[java.lang.Double], Double.box(doubleField))
        
    
      
      
           putDirect(WithPrimitiveTyperefs.Fields.booleanField, classOf[java.lang.Boolean], Boolean.box(booleanField))
        
    
      
      
           putDirect(WithPrimitiveTyperefs.Fields.stringField, classOf[java.lang.String], stringField)
        
    
      
      
           putDirect(WithPrimitiveTyperefs.Fields.bytesField, classOf[com.linkedin.data.ByteString], bytesField)
        
    
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

  override val productPrefix: String = "WithPrimitiveTyperefs"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithPrimitiveTyperefs]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(intField: Int = this.intField, longField: Long = this.longField, floatField: Float = this.floatField, doubleField: Double = this.doubleField, booleanField: Boolean = this.booleanField, stringField: String = this.stringField, bytesField: ByteString = this.bytesField): WithPrimitiveTyperefs = {
      val dataMap = new DataMap
      val result = new WithPrimitiveTyperefs(dataMap)
      result.setFields(intField, longField, floatField, doubleField, booleanField, stringField, bytesField)
      dataMap.setReadOnly()
      result
    }
  
}

object WithPrimitiveTyperefs {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithPrimitiveTyperefs","namespace":"org.coursera.records.test","fields":[{"name":"intField","type":{"type":"typeref","name":"IntTyperef","ref":"int"}},{"name":"longField","type":{"type":"typeref","name":"LongTyperef","ref":"long"}},{"name":"floatField","type":{"type":"typeref","name":"FloatTyperef","ref":"float"}},{"name":"doubleField","type":{"type":"typeref","name":"DoubleTyperef","ref":"double"}},{"name":"booleanField","type":{"type":"typeref","name":"BooleanTyperef","ref":"boolean"}},{"name":"stringField","type":{"type":"typeref","name":"StringTyperef","ref":"string"}},{"name":"bytesField","type":{"type":"typeref","name":"BytesTyperef","ref":"bytes"}}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
  
    
  
    
  
    
  
    
  
    
  
    
  

  private object Fields {
    
    val intField = WithPrimitiveTyperefs.SCHEMA.getField("intField")
    val longField = WithPrimitiveTyperefs.SCHEMA.getField("longField")
    val floatField = WithPrimitiveTyperefs.SCHEMA.getField("floatField")
    val doubleField = WithPrimitiveTyperefs.SCHEMA.getField("doubleField")
    val booleanField = WithPrimitiveTyperefs.SCHEMA.getField("booleanField")
    val stringField = WithPrimitiveTyperefs.SCHEMA.getField("stringField")
    val bytesField = WithPrimitiveTyperefs.SCHEMA.getField("bytesField")
  }

  def apply(intField: Int, longField: Long, floatField: Float, doubleField: Double, booleanField: Boolean, stringField: String, bytesField: ByteString): WithPrimitiveTyperefs = {
    val dataMap = new DataMap
    val result = new WithPrimitiveTyperefs(dataMap)
    result.setFields(intField, longField, floatField, doubleField, booleanField, stringField, bytesField)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithPrimitiveTyperefs = {
    new WithPrimitiveTyperefs(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithPrimitiveTyperefs): Option[(Int, Long, Float, Double, Boolean, String, ByteString)] = {
        try {
          Some((record.intField, record.longField, record.floatField, record.doubleField, record.booleanField, record.stringField, record.bytesField))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


