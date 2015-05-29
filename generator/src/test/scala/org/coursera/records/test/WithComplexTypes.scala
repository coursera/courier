

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




@Generated(value = Array("WithComplexTypes"), comments = "Courier Data Template.", date = "Fri May 29 11:12:12 PDT 2015")
final class WithComplexTypes private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithComplexTypes.SCHEMA) with Product {
  import WithComplexTypes._

  
  
    

    
    
        lazy val fruits: org.coursera.enums.Fruits.Fruits =  org.coursera.enums.Fruits.fromString(
            obtainDirect(WithComplexTypes.Fields.fruits, classOf[String])
          ) 
      
  
    

    
    
        lazy val optionalFruits: Option[org.coursera.enums.Fruits.Fruits] = Option(
            obtainDirect(WithComplexTypes.Fields.optionalFruits, classOf[String])
          ).map(value =>  org.coursera.enums.Fruits.fromString(value) )
      
  

  
  private def setFields(fruits: org.coursera.enums.Fruits.Fruits, optionalFruits: Option[org.coursera.enums.Fruits.Fruits]): Unit = {
    
      
      
           putDirect(WithComplexTypes.Fields.fruits, classOf[String], fruits.toString)
        
    
      
      
          optionalFruits.foreach(value =>  putDirect(WithComplexTypes.Fields.optionalFruits, classOf[String], value.toString))
        
    
  }

  override val productArity: Int = 2

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => fruits
      case 1 => optionalFruits
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithComplexTypes"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithComplexTypes]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(fruits: org.coursera.enums.Fruits.Fruits = this.fruits, optionalFruits: Option[org.coursera.enums.Fruits.Fruits] = this.optionalFruits): WithComplexTypes = {
      val dataMap = new DataMap
      val result = new WithComplexTypes(dataMap)
      result.setFields(fruits, optionalFruits)
      dataMap.setReadOnly()
      result
    }
  
}

object WithComplexTypes {
  private val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithComplexTypes","namespace":"org.coursera.records.test","fields":[{"name":"fruits","type":{"type":"enum","name":"Fruits","namespace":"org.coursera.enums","symbols":["APPLE","BANANA","ORANGE","PINEAPPLE"],"symbolDocs":{"APPLE":"An Apple."}}},{"name":"optionalFruits","type":"org.coursera.enums.Fruits","optional":true}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
  
    
  

  private object Fields {
    
    val fruits = WithComplexTypes.SCHEMA.getField("fruits")
    val optionalFruits = WithComplexTypes.SCHEMA.getField("optionalFruits")
  }

  def apply(fruits: org.coursera.enums.Fruits.Fruits, optionalFruits: Option[org.coursera.enums.Fruits.Fruits]): WithComplexTypes = {
    val dataMap = new DataMap
    val result = new WithComplexTypes(dataMap)
    result.setFields(fruits, optionalFruits)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithComplexTypes = {
    new WithComplexTypes(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithComplexTypes): Option[(org.coursera.enums.Fruits.Fruits, Option[org.coursera.enums.Fruits.Fruits])] = {
        try {
          Some((record.fruits, record.optionalFruits))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


