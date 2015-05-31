

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




@Generated(value = Array("WithPrimitiveTyperefsUnion"), comments = "Courier Data Template.", date = "Sun May 31 11:16:33 PDT 2015")
final class WithPrimitiveTyperefsUnion private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithPrimitiveTyperefsUnion.SCHEMA) with Product {
  import WithPrimitiveTyperefsUnion._

  
  
    

    
    
        lazy val union: Union = Union(dataMap.getDataMap(WithPrimitiveTyperefsUnion.Fields.union.getName))
      
  

  
  private def setFields(union: Union): Unit = {
    
      
      
           dataMap.put(WithPrimitiveTyperefsUnion.Fields.union.getName, union.data())
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => union
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithPrimitiveTyperefsUnion"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithPrimitiveTyperefsUnion]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(union: Union = this.union): WithPrimitiveTyperefsUnion = {
      val dataMap = new DataMap
      val result = new WithPrimitiveTyperefsUnion(dataMap)
      result.setFields(union)
      dataMap.setReadOnly()
      result
    }
  
}

object WithPrimitiveTyperefsUnion {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithPrimitiveTyperefsUnion","namespace":"org.coursera.unions","fields":[{"name":"union","type":[{"type":"typeref","name":"IntTyperef","ref":"int"}]}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
         

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
    val SCHEMA = DataTemplateUtil.parseSchema("""[{"type":"typeref","name":"IntTyperef","namespace":"org.coursera.unions","ref":"int"}]""").asInstanceOf[UnionDataSchema]

    
      
          protected val IntMemberSchema = SCHEMA.getType("int")
        
    

    def apply(union: DataMap): Union = {
      require(
        union.size == 1,
        "Malformed union DataMap, exactly one field must be present and must be a member key")

      val unionTag = union.keySet().iterator().next()
      unionTag match {
        
          case IntMember.memberKey =>
            IntMember(union)
        
        case _: Any =>
          Union.$UnknownMember(union)
      }
    }

    
      class IntMember(private val dataMap: DataMap)
        extends Union(dataMap) with Product1[Int] {
        import IntMember._

        lazy val _1, value = 
            obtainDirect(IntMemberSchema, classOf[Int], memberKey)
          
        /* TODO(jbetz): support map fields */
        /* TODO(jbetz): support array fields */
        /* TODO(jbetz): support union fields */

        private def setFields(value: Int): Unit = {
          
              selectDirect(IntMemberSchema, classOf[java.lang.Integer], memberKey, Int.box(value))
            
          /* TODO(jbetz): support map fields */
          /* TODO(jbetz): support array fields */
          /* TODO(jbetz): support union fields */
        }
      }

      object IntMember {
        def apply(value: Int): IntMember = {
          val dataMap = new DataMap
          val result = new IntMember(dataMap)
          result.setFields(value)
          dataMap.setReadOnly()
          result
        }

        def apply(dataMap: DataMap): IntMember = {
          dataMap.setReadOnly()
          new IntMember(dataMap)
        }

        private[Union] val memberKey = "int"
      }
    

    case class $UnknownMember private[Union](
        private[Union] val dataMap: DataMap)
      extends Union(dataMap)
  }
 
      
  

  private object Fields {
    
    val union = WithPrimitiveTyperefsUnion.SCHEMA.getField("union")
  }

  def apply(union: Union): WithPrimitiveTyperefsUnion = {
    val dataMap = new DataMap
    val result = new WithPrimitiveTyperefsUnion(dataMap)
    result.setFields(union)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithPrimitiveTyperefsUnion = {
    new WithPrimitiveTyperefsUnion(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithPrimitiveTyperefsUnion): Option[(Union)] = {
        try {
          Some((record.union))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


