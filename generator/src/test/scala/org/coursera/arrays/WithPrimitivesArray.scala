

package org.coursera.arrays

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




@Generated(value = Array("WithPrimitivesArray"), comments = "Courier Data Template.", date = "Sat May 30 13:31:01 PDT 2015")
final class WithPrimitivesArray private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithPrimitivesArray.SCHEMA) with Product {
  import WithPrimitivesArray._

  
  
    

    
    
        lazy val ints: org.coursera.courier.data.IntArray = obtainWrapped(WithPrimitivesArray.Fields.ints, classOf[org.coursera.courier.data.IntArray])
      
  
    

    
    
        lazy val longs: org.coursera.courier.data.LongArray = obtainWrapped(WithPrimitivesArray.Fields.longs, classOf[org.coursera.courier.data.LongArray])
      
  
    

    
    
        lazy val floats: org.coursera.courier.data.FloatArray = obtainWrapped(WithPrimitivesArray.Fields.floats, classOf[org.coursera.courier.data.FloatArray])
      
  
    

    
    
        lazy val doubles: org.coursera.courier.data.DoubleArray = obtainWrapped(WithPrimitivesArray.Fields.doubles, classOf[org.coursera.courier.data.DoubleArray])
      
  
    

    
    
        lazy val booleans: org.coursera.courier.data.BooleanArray = obtainWrapped(WithPrimitivesArray.Fields.booleans, classOf[org.coursera.courier.data.BooleanArray])
      
  
    

    
    
        lazy val strings: org.coursera.courier.data.StringArray = obtainWrapped(WithPrimitivesArray.Fields.strings, classOf[org.coursera.courier.data.StringArray])
      
  
    

    
    
        lazy val bytes: org.coursera.courier.data.BytesArray = obtainWrapped(WithPrimitivesArray.Fields.bytes, classOf[org.coursera.courier.data.BytesArray])
      
  

  
  private def setFields(ints: org.coursera.courier.data.IntArray, longs: org.coursera.courier.data.LongArray, floats: org.coursera.courier.data.FloatArray, doubles: org.coursera.courier.data.DoubleArray, booleans: org.coursera.courier.data.BooleanArray, strings: org.coursera.courier.data.StringArray, bytes: org.coursera.courier.data.BytesArray): Unit = {
    
      
      
           putWrapped(WithPrimitivesArray.Fields.ints, classOf[org.coursera.courier.data.IntArray], ints)
        
    
      
      
           putWrapped(WithPrimitivesArray.Fields.longs, classOf[org.coursera.courier.data.LongArray], longs)
        
    
      
      
           putWrapped(WithPrimitivesArray.Fields.floats, classOf[org.coursera.courier.data.FloatArray], floats)
        
    
      
      
           putWrapped(WithPrimitivesArray.Fields.doubles, classOf[org.coursera.courier.data.DoubleArray], doubles)
        
    
      
      
           putWrapped(WithPrimitivesArray.Fields.booleans, classOf[org.coursera.courier.data.BooleanArray], booleans)
        
    
      
      
           putWrapped(WithPrimitivesArray.Fields.strings, classOf[org.coursera.courier.data.StringArray], strings)
        
    
      
      
           putWrapped(WithPrimitivesArray.Fields.bytes, classOf[org.coursera.courier.data.BytesArray], bytes)
        
    
  }

  override val productArity: Int = 7

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => ints
      case 1 => longs
      case 2 => floats
      case 3 => doubles
      case 4 => booleans
      case 5 => strings
      case 6 => bytes
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithPrimitivesArray"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithPrimitivesArray]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(ints: org.coursera.courier.data.IntArray = this.ints, longs: org.coursera.courier.data.LongArray = this.longs, floats: org.coursera.courier.data.FloatArray = this.floats, doubles: org.coursera.courier.data.DoubleArray = this.doubles, booleans: org.coursera.courier.data.BooleanArray = this.booleans, strings: org.coursera.courier.data.StringArray = this.strings, bytes: org.coursera.courier.data.BytesArray = this.bytes): WithPrimitivesArray = {
      val dataMap = new DataMap
      val result = new WithPrimitivesArray(dataMap)
      result.setFields(ints, longs, floats, doubles, booleans, strings, bytes)
      dataMap.setReadOnly()
      result
    }
  
}

object WithPrimitivesArray {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithPrimitivesArray","namespace":"org.coursera.arrays","fields":[{"name":"ints","type":{"type":"array","items":"int"}},{"name":"longs","type":{"type":"array","items":"long"}},{"name":"floats","type":{"type":"array","items":"float"}},{"name":"doubles","type":{"type":"array","items":"double"}},{"name":"booleans","type":{"type":"array","items":"boolean"}},{"name":"strings","type":{"type":"array","items":"string"}},{"name":"bytes","type":{"type":"array","items":"bytes"}}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
  
    
  
    
  
    
  
    
  
    
  
    
  

  private object Fields {
    
    val ints = WithPrimitivesArray.SCHEMA.getField("ints")
    val longs = WithPrimitivesArray.SCHEMA.getField("longs")
    val floats = WithPrimitivesArray.SCHEMA.getField("floats")
    val doubles = WithPrimitivesArray.SCHEMA.getField("doubles")
    val booleans = WithPrimitivesArray.SCHEMA.getField("booleans")
    val strings = WithPrimitivesArray.SCHEMA.getField("strings")
    val bytes = WithPrimitivesArray.SCHEMA.getField("bytes")
  }

  def apply(ints: org.coursera.courier.data.IntArray, longs: org.coursera.courier.data.LongArray, floats: org.coursera.courier.data.FloatArray, doubles: org.coursera.courier.data.DoubleArray, booleans: org.coursera.courier.data.BooleanArray, strings: org.coursera.courier.data.StringArray, bytes: org.coursera.courier.data.BytesArray): WithPrimitivesArray = {
    val dataMap = new DataMap
    val result = new WithPrimitivesArray(dataMap)
    result.setFields(ints, longs, floats, doubles, booleans, strings, bytes)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithPrimitivesArray = {
    new WithPrimitivesArray(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithPrimitivesArray): Option[(org.coursera.courier.data.IntArray, org.coursera.courier.data.LongArray, org.coursera.courier.data.FloatArray, org.coursera.courier.data.DoubleArray, org.coursera.courier.data.BooleanArray, org.coursera.courier.data.StringArray, org.coursera.courier.data.BytesArray)] = {
        try {
          Some((record.ints, record.longs, record.floats, record.doubles, record.booleans, record.strings, record.bytes))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


