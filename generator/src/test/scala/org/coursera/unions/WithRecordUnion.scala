

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




@Generated(value = Array("WithRecordUnion"), comments = "Courier Data Template.", date = "Fri May 29 18:57:57 PDT 2015")
final class WithRecordUnion private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithRecordUnion.SCHEMA) with Product {
  import WithRecordUnion._

  
  
    

    
    
        lazy val union: Union = Union(dataMap.getDataMap(WithRecordUnion.Fields.union.getName))
      
  

  
  private def setFields(union: Union): Unit = {
    
      
      
           dataMap.put(WithRecordUnion.Fields.union.getName, union.data())
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => union
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithRecordUnion"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithRecordUnion]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(union: Union = this.union): WithRecordUnion = {
      val dataMap = new DataMap
      val result = new WithRecordUnion(dataMap)
      result.setFields(union)
      dataMap.setReadOnly()
      result
    }
  
}

object WithRecordUnion {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithRecordUnion","namespace":"org.coursera.unions","fields":[{"name":"union","type":[{"type":"record","name":"Empty","namespace":"org.coursera.records.test","fields":[]}]}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
         

  @Generated(value = Array("Union"), comments="Courier Data Template.", date = "Fri May 29 18:57:57 PDT 2015")
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
    val SCHEMA = DataTemplateUtil.parseSchema("""[{"type":"record","name":"Empty","namespace":"org.coursera.records.test","fields":[]}]""").asInstanceOf[UnionDataSchema]

    
      
          protected val MEMBER_Empty = SCHEMA.getType("org.coursera.records.test.Empty")
        
    

    def apply(union: DataMap): Union = {
      require(
        union.size == 1,
        "Malformed union DataMap, exactly one field must be present and must be a member key")

      val unionTag = union.keySet().iterator().next()
      unionTag match {
        
          case EmptyMember.memberKey =>
            EmptyMember(union)
        
        case _: Any =>
          Union.$UnknownMember(union)
      }
    }

    
      class EmptyMember(private val dataMap: DataMap)
        extends Union(dataMap) with Product1[org.coursera.records.test.Empty] {
        import EmptyMember._

        lazy val _1, value = 
            obtainWrapped(MEMBER_Empty, classOf[org.coursera.records.test.Empty], memberKey)
          
        /* TODO(jbetz): support map fields */
        /* TODO(jbetz): support array fields */
        /* TODO(jbetz): support enum fields */

        private def setFields(value: org.coursera.records.test.Empty): Unit = {
          
              selectWrapped(MEMBER_Empty, classOf[org.coursera.records.test.Empty], memberKey, value)
            
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
    

    case class $UnknownMember private[Union](
        private[Union] val dataMap: DataMap)
      extends Union(dataMap)
  }
 
      
  

  private object Fields {
    
    val union = WithRecordUnion.SCHEMA.getField("union")
  }

  def apply(union: Union): WithRecordUnion = {
    val dataMap = new DataMap
    val result = new WithRecordUnion(dataMap)
    result.setFields(union)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithRecordUnion = {
    new WithRecordUnion(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithRecordUnion): Option[(Union)] = {
        try {
          Some((record.union))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


