

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




@Generated(value = Array("WithPrimitivesMap"), comments = "Courier Data Template.", date = "Sat May 30 14:26:52 PDT 2015")
final class WithPrimitivesMap private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithPrimitivesMap.SCHEMA) with Product {
  import WithPrimitivesMap._

  
  
    

    
    
        lazy val ints: org.coursera.courier.data.IntMap = obtainWrapped(WithPrimitivesMap.Fields.ints, classOf[org.coursera.courier.data.IntMap])
      
  
    

    
    
        lazy val longs: org.coursera.courier.data.LongMap = obtainWrapped(WithPrimitivesMap.Fields.longs, classOf[org.coursera.courier.data.LongMap])
      
  
    

    
    
        lazy val floats: org.coursera.courier.data.FloatMap = obtainWrapped(WithPrimitivesMap.Fields.floats, classOf[org.coursera.courier.data.FloatMap])
      
  
    

    
    
        lazy val doubles: org.coursera.courier.data.DoubleMap = obtainWrapped(WithPrimitivesMap.Fields.doubles, classOf[org.coursera.courier.data.DoubleMap])
      
  
    

    
    
        lazy val booleans: org.coursera.courier.data.BooleanMap = obtainWrapped(WithPrimitivesMap.Fields.booleans, classOf[org.coursera.courier.data.BooleanMap])
      
  
    

    
    
        lazy val strings: org.coursera.courier.data.StringMap = obtainWrapped(WithPrimitivesMap.Fields.strings, classOf[org.coursera.courier.data.StringMap])
      
  
    

    
    
        lazy val bytes: org.coursera.courier.data.BytesMap = obtainWrapped(WithPrimitivesMap.Fields.bytes, classOf[org.coursera.courier.data.BytesMap])
      
  

  
  private def setFields(ints: org.coursera.courier.data.IntMap, longs: org.coursera.courier.data.LongMap, floats: org.coursera.courier.data.FloatMap, doubles: org.coursera.courier.data.DoubleMap, booleans: org.coursera.courier.data.BooleanMap, strings: org.coursera.courier.data.StringMap, bytes: org.coursera.courier.data.BytesMap): Unit = {
    
      
      
           putWrapped(WithPrimitivesMap.Fields.ints, classOf[org.coursera.courier.data.IntMap], ints)
        
    
      
      
           putWrapped(WithPrimitivesMap.Fields.longs, classOf[org.coursera.courier.data.LongMap], longs)
        
    
      
      
           putWrapped(WithPrimitivesMap.Fields.floats, classOf[org.coursera.courier.data.FloatMap], floats)
        
    
      
      
           putWrapped(WithPrimitivesMap.Fields.doubles, classOf[org.coursera.courier.data.DoubleMap], doubles)
        
    
      
      
           putWrapped(WithPrimitivesMap.Fields.booleans, classOf[org.coursera.courier.data.BooleanMap], booleans)
        
    
      
      
           putWrapped(WithPrimitivesMap.Fields.strings, classOf[org.coursera.courier.data.StringMap], strings)
        
    
      
      
           putWrapped(WithPrimitivesMap.Fields.bytes, classOf[org.coursera.courier.data.BytesMap], bytes)
        
    
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

  override val productPrefix: String = "WithPrimitivesMap"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithPrimitivesMap]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(ints: org.coursera.courier.data.IntMap = this.ints, longs: org.coursera.courier.data.LongMap = this.longs, floats: org.coursera.courier.data.FloatMap = this.floats, doubles: org.coursera.courier.data.DoubleMap = this.doubles, booleans: org.coursera.courier.data.BooleanMap = this.booleans, strings: org.coursera.courier.data.StringMap = this.strings, bytes: org.coursera.courier.data.BytesMap = this.bytes): WithPrimitivesMap = {
      val dataMap = new DataMap
      val result = new WithPrimitivesMap(dataMap)
      result.setFields(ints, longs, floats, doubles, booleans, strings, bytes)
      dataMap.setReadOnly()
      result
    }
  
}

object WithPrimitivesMap {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithPrimitivesMap","namespace":"org.coursera.maps","fields":[{"name":"ints","type":{"type":"map","values":"int"}},{"name":"longs","type":{"type":"map","values":"long"}},{"name":"floats","type":{"type":"map","values":"float"}},{"name":"doubles","type":{"type":"map","values":"double"}},{"name":"booleans","type":{"type":"map","values":"boolean"}},{"name":"strings","type":{"type":"map","values":"string"}},{"name":"bytes","type":{"type":"map","values":"bytes"}}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
  
    
  
    
  
    
  
    
  
    
  
    
  

  private object Fields {
    
    val ints = WithPrimitivesMap.SCHEMA.getField("ints")
    val longs = WithPrimitivesMap.SCHEMA.getField("longs")
    val floats = WithPrimitivesMap.SCHEMA.getField("floats")
    val doubles = WithPrimitivesMap.SCHEMA.getField("doubles")
    val booleans = WithPrimitivesMap.SCHEMA.getField("booleans")
    val strings = WithPrimitivesMap.SCHEMA.getField("strings")
    val bytes = WithPrimitivesMap.SCHEMA.getField("bytes")
  }

  def apply(ints: org.coursera.courier.data.IntMap, longs: org.coursera.courier.data.LongMap, floats: org.coursera.courier.data.FloatMap, doubles: org.coursera.courier.data.DoubleMap, booleans: org.coursera.courier.data.BooleanMap, strings: org.coursera.courier.data.StringMap, bytes: org.coursera.courier.data.BytesMap): WithPrimitivesMap = {
    val dataMap = new DataMap
    val result = new WithPrimitivesMap(dataMap)
    result.setFields(ints, longs, floats, doubles, booleans, strings, bytes)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithPrimitivesMap = {
    new WithPrimitivesMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithPrimitivesMap): Option[(org.coursera.courier.data.IntMap, org.coursera.courier.data.LongMap, org.coursera.courier.data.FloatMap, org.coursera.courier.data.DoubleMap, org.coursera.courier.data.BooleanMap, org.coursera.courier.data.StringMap, org.coursera.courier.data.BytesMap)] = {
        try {
          Some((record.ints, record.longs, record.floats, record.doubles, record.booleans, record.strings, record.bytes))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


