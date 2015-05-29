

package org.coursera.unions

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




@Generated(value = Array("WithComplexTypesUnion"), comments = "Courier Data Template.", date = "Fri May 29 19:37:17 PDT 2015")
final class WithComplexTypesUnion private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithComplexTypesUnion.SCHEMA) with Product {
  import WithComplexTypesUnion._

  
  
    

    
    
        lazy val union: Union = Union(dataMap.getDataMap(WithComplexTypesUnion.Fields.union.getName))
      
  

  
  private def setFields(union: Union): Unit = {
    
      
      
           dataMap.put(WithComplexTypesUnion.Fields.union.getName, union.data())
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => union
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithComplexTypesUnion"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithComplexTypesUnion]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(union: Union = this.union): WithComplexTypesUnion = {
      val dataMap = new DataMap
      val result = new WithComplexTypesUnion(dataMap)
      result.setFields(union)
      dataMap.setReadOnly()
      result
    }
  
}

object WithComplexTypesUnion {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithComplexTypesUnion","namespace":"org.coursera.unions","fields":[{"name":"union","type":[{"type":"record","name":"Empty","namespace":"org.coursera.records.test","fields":[]},{"type":"enum","name":"Fruits","namespace":"org.coursera.enums","symbols":["APPLE","BANANA","ORANGE","PINEAPPLE"],"symbolDocs":{"APPLE":"An Apple."}}]}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
         

  @Generated(value = Array("Union"), comments="Courier Data Template.", date = "Fri May 29 19:37:17 PDT 2015")
  sealed abstract class Union protected(private val value: DataMap)
    extends UnionTemplate(value, Union.SCHEMA) with Product {
    import Union._

    // reset UnionTemplate overrides
    override def canEqual(that: Any): Boolean = that.isInstanceOf[this.type]

    override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

    override def toString: String = ScalaRunTime._toString(this)

    override def hashCode: Int = ScalaRunTime._hashCode(this)
  }

  object Union {
    val SCHEMA = DataTemplateUtil.parseSchema("""[{"type":"record","name":"Empty","namespace":"org.coursera.records.test","fields":[]},{"type":"enum","name":"Fruits","namespace":"org.coursera.enums","symbols":["APPLE","BANANA","ORANGE","PINEAPPLE"],"symbolDocs":{"APPLE":"An Apple."}}]""").asInstanceOf[UnionDataSchema]

    
      
          protected val EmptyMemberSchema = SCHEMA.getType("org.coursera.records.test.Empty")
        
    
      
          protected val FruitsMemberSchema = SCHEMA.getType("org.coursera.enums.Fruits.Fruits")
        
    

    def apply(union: DataMap): Union = {
      require(
        union.size == 1,
        "Malformed union DataMap, exactly one field must be present and must be a member key")

      val unionTag = union.keySet().iterator().next()
      unionTag match {
        
          case EmptyMember.memberKey =>
            EmptyMember(union)
        
          case FruitsMember.memberKey =>
            FruitsMember(union)
        
        case _: Any =>
          Union.$UnknownMember(union)
      }
    }

    
      class EmptyMember(private val dataMap: DataMap)
        extends Union(dataMap) with Product1[org.coursera.records.test.Empty] {
        import EmptyMember._

        lazy val _1, value = 
            obtainWrapped(EmptyMemberSchema, classOf[org.coursera.records.test.Empty], memberKey)
          
        /* TODO(jbetz): support map fields */
        /* TODO(jbetz): support array fields */

        private def setFields(value: org.coursera.records.test.Empty): Unit = {
          
              selectWrapped(EmptyMemberSchema, classOf[org.coursera.records.test.Empty], memberKey, value)
            
          /* TODO(jbetz): support map fields */
          /* TODO(jbetz): support array fields */
          /* TODO(jbetz): support enum fields */
        }
      }

      object EmptyMember {
        def apply(value: org.coursera.records.test.Empty): EmptyMember = {
          val dataMap = new DataMap
          val result = new EmptyMember(dataMap)
          result.setFields(value)
          dataMap.setReadOnly()
          result
        }

        def apply(dataMap: DataMap): EmptyMember = {
          dataMap.setReadOnly()
          new EmptyMember(dataMap)
        }

        private[Union] val memberKey = "org.coursera.records.test.Empty"
      }
    
      class FruitsMember(private val dataMap: DataMap)
        extends Union(dataMap) with Product1[org.coursera.enums.Fruits.Fruits] {
        import FruitsMember._

        lazy val _1, value = 
            org.coursera.enums.Fruits.fromString(
              obtainDirect(FruitsMemberSchema, classOf[String], memberKey))
          
        /* TODO(jbetz): support map fields */
        /* TODO(jbetz): support array fields */

        private def setFields(value: org.coursera.enums.Fruits.Fruits): Unit = {
          
              selectDirect(FruitsMemberSchema, classOf[String], memberKey, value.toString)
            
          /* TODO(jbetz): support map fields */
          /* TODO(jbetz): support array fields */
          /* TODO(jbetz): support enum fields */
        }
      }

      object FruitsMember {
        def apply(value: org.coursera.enums.Fruits.Fruits): FruitsMember = {
          val dataMap = new DataMap
          val result = new FruitsMember(dataMap)
          result.setFields(value)
          dataMap.setReadOnly()
          result
        }

        def apply(dataMap: DataMap): FruitsMember = {
          dataMap.setReadOnly()
          new FruitsMember(dataMap)
        }

        private[Union] val memberKey = "org.coursera.enums.Fruits"
      }
    

    case class $UnknownMember private[Union](
        private[Union] val dataMap: DataMap)
      extends Union(dataMap)
  }
 
      
  

  private object Fields {
    
    val union = WithComplexTypesUnion.SCHEMA.getField("union")
  }

  def apply(union: Union): WithComplexTypesUnion = {
    val dataMap = new DataMap
    val result = new WithComplexTypesUnion(dataMap)
    result.setFields(union)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithComplexTypesUnion = {
    new WithComplexTypesUnion(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithComplexTypesUnion): Option[(Union)] = {
        try {
          Some((record.union))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


