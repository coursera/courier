

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




@Generated(value = Array("WithPrimitiveCustomTypesUnion"), comments = "Courier Data Template.", date = "Sun May 31 11:16:33 PDT 2015")
final class WithPrimitiveCustomTypesUnion private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithPrimitiveCustomTypesUnion.SCHEMA) with Product {
  import WithPrimitiveCustomTypesUnion._

  
  
    

    
    
        lazy val union: Union = Union(dataMap.getDataMap(WithPrimitiveCustomTypesUnion.Fields.union.getName))
      
  

  
  private def setFields(union: Union): Unit = {
    
      
      
           dataMap.put(WithPrimitiveCustomTypesUnion.Fields.union.getName, union.data())
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => union
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithPrimitiveCustomTypesUnion"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithPrimitiveCustomTypesUnion]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(union: Union = this.union): WithPrimitiveCustomTypesUnion = {
      val dataMap = new DataMap
      val result = new WithPrimitiveCustomTypesUnion(dataMap)
      result.setFields(union)
      dataMap.setReadOnly()
      result
    }
  
}

object WithPrimitiveCustomTypesUnion {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithPrimitiveCustomTypesUnion","namespace":"org.coursera.unions","fields":[{"name":"union","type":[{"type":"typeref","name":"IntCustomType","ref":"int","java":{"coercerClass":"org.coursera.courier.generator.customtypes.CustomIntCoercer","class":"org.coursera.courier.generator.customtypes.CustomInt"}}]}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
         

  @Generated(value = Array("Union"), comments="Courier Data Template.", date = "Sun May 31 11:16:33 PDT 2015")
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
    val SCHEMA = DataTemplateUtil.parseSchema("""[{"type":"typeref","name":"IntCustomType","namespace":"org.coursera.unions","ref":"int","java":{"coercerClass":"org.coursera.courier.generator.customtypes.CustomIntCoercer","class":"org.coursera.courier.generator.customtypes.CustomInt"}}]""").asInstanceOf[UnionDataSchema]

    
      
          protected val CustomIntMemberSchema = SCHEMA.getType("org.coursera.courier.generator.customtypes.CustomInt")
        
    

    def apply(union: DataMap): Union = {
      require(
        union.size == 1,
        "Malformed union DataMap, exactly one field must be present and must be a member key")

      val unionTag = union.keySet().iterator().next()
      unionTag match {
        
          case CustomIntMember.memberKey =>
            CustomIntMember(union)
        
        case _: Any =>
          Union.$UnknownMember(union)
      }
    }

    
      class CustomIntMember(private val dataMap: DataMap)
        extends Union(dataMap) with Product1[org.coursera.courier.generator.customtypes.CustomInt] {
        import CustomIntMember._

        lazy val _1, value = 
            obtainDirect(CustomIntMemberSchema, classOf[org.coursera.courier.generator.customtypes.CustomInt], memberKey)
          
        /* TODO(jbetz): support map fields */
        /* TODO(jbetz): support array fields */
        /* TODO(jbetz): support union fields */

        private def setFields(value: org.coursera.courier.generator.customtypes.CustomInt): Unit = {
          
              selectDirect(CustomIntMemberSchema, classOf[org.coursera.courier.generator.customtypes.CustomInt], classOf[java.lang.Integer], memberKey, value)
            
          /* TODO(jbetz): support map fields */
          /* TODO(jbetz): support array fields */
          /* TODO(jbetz): support union fields */
        }
      }

      object CustomIntMember {
        def apply(value: org.coursera.courier.generator.customtypes.CustomInt): CustomIntMember = {
          val dataMap = new DataMap
          val result = new CustomIntMember(dataMap)
          result.setFields(value)
          dataMap.setReadOnly()
          result
        }

        def apply(dataMap: DataMap): CustomIntMember = {
          dataMap.setReadOnly()
          new CustomIntMember(dataMap)
        }

        private[Union] val memberKey = "int"
      }
    

    case class $UnknownMember private[Union](
        private[Union] val dataMap: DataMap)
      extends Union(dataMap)
  }
 
      
  

  private object Fields {
    
    val union = WithPrimitiveCustomTypesUnion.SCHEMA.getField("union")
  }

  def apply(union: Union): WithPrimitiveCustomTypesUnion = {
    val dataMap = new DataMap
    val result = new WithPrimitiveCustomTypesUnion(dataMap)
    result.setFields(union)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithPrimitiveCustomTypesUnion = {
    new WithPrimitiveCustomTypesUnion(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithPrimitiveCustomTypesUnion): Option[(Union)] = {
        try {
          Some((record.union))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


