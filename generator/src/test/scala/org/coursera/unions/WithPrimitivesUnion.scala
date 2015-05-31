

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




@Generated(value = Array("WithPrimitivesUnion"), comments = "Courier Data Template.", date = "Sun May 31 11:16:33 PDT 2015")
final class WithPrimitivesUnion private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithPrimitivesUnion.SCHEMA) with Product {
  import WithPrimitivesUnion._

  
  
    

    
    
        lazy val union: Union = Union(dataMap.getDataMap(WithPrimitivesUnion.Fields.union.getName))
      
  

  
  private def setFields(union: Union): Unit = {
    
      
      
           dataMap.put(WithPrimitivesUnion.Fields.union.getName, union.data())
        
    
  }

  override val productArity: Int = 1

  override def productElement(n: Int): Any =
    n match {
      
      case 0 => union
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "WithPrimitivesUnion"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithPrimitivesUnion]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  
  
    def copy(union: Union = this.union): WithPrimitivesUnion = {
      val dataMap = new DataMap
      val result = new WithPrimitivesUnion(dataMap)
      result.setFields(union)
      dataMap.setReadOnly()
      result
    }
  
}

object WithPrimitivesUnion {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithPrimitivesUnion","namespace":"org.coursera.unions","fields":[{"name":"union","type":["int","long","float","double","boolean","string","bytes"]}]}""").asInstanceOf[RecordDataSchema]

  
  

  
  
    
         

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
    val SCHEMA = DataTemplateUtil.parseSchema("""["int","long","float","double","boolean","string","bytes"]""").asInstanceOf[UnionDataSchema]

    
      
          protected val IntMemberSchema = SCHEMA.getType("int")
        
    
      
          protected val LongMemberSchema = SCHEMA.getType("long")
        
    
      
          protected val FloatMemberSchema = SCHEMA.getType("float")
        
    
      
          protected val DoubleMemberSchema = SCHEMA.getType("double")
        
    
      
          protected val BooleanMemberSchema = SCHEMA.getType("boolean")
        
    
      
          protected val StringMemberSchema = SCHEMA.getType("string")
        
    
      
          protected val ByteStringMemberSchema = SCHEMA.getType("bytes")
        
    

    def apply(union: DataMap): Union = {
      require(
        union.size == 1,
        "Malformed union DataMap, exactly one field must be present and must be a member key")

      val unionTag = union.keySet().iterator().next()
      unionTag match {
        
          case IntMember.memberKey =>
            IntMember(union)
        
          case LongMember.memberKey =>
            LongMember(union)
        
          case FloatMember.memberKey =>
            FloatMember(union)
        
          case DoubleMember.memberKey =>
            DoubleMember(union)
        
          case BooleanMember.memberKey =>
            BooleanMember(union)
        
          case StringMember.memberKey =>
            StringMember(union)
        
          case ByteStringMember.memberKey =>
            ByteStringMember(union)
        
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
    
      class LongMember(private val dataMap: DataMap)
        extends Union(dataMap) with Product1[Long] {
        import LongMember._

        lazy val _1, value = 
            obtainDirect(LongMemberSchema, classOf[Long], memberKey)
          
        /* TODO(jbetz): support map fields */
        /* TODO(jbetz): support array fields */
        /* TODO(jbetz): support union fields */

        private def setFields(value: Long): Unit = {
          
              selectDirect(LongMemberSchema, classOf[java.lang.Long], memberKey, Long.box(value))
            
          /* TODO(jbetz): support map fields */
          /* TODO(jbetz): support array fields */
          /* TODO(jbetz): support union fields */
        }
      }

      object LongMember {
        def apply(value: Long): LongMember = {
          val dataMap = new DataMap
          val result = new LongMember(dataMap)
          result.setFields(value)
          dataMap.setReadOnly()
          result
        }

        def apply(dataMap: DataMap): LongMember = {
          dataMap.setReadOnly()
          new LongMember(dataMap)
        }

        private[Union] val memberKey = "long"
      }
    
      class FloatMember(private val dataMap: DataMap)
        extends Union(dataMap) with Product1[Float] {
        import FloatMember._

        lazy val _1, value = 
            obtainDirect(FloatMemberSchema, classOf[Float], memberKey)
          
        /* TODO(jbetz): support map fields */
        /* TODO(jbetz): support array fields */
        /* TODO(jbetz): support union fields */

        private def setFields(value: Float): Unit = {
          
              selectDirect(FloatMemberSchema, classOf[java.lang.Float], memberKey, Float.box(value))
            
          /* TODO(jbetz): support map fields */
          /* TODO(jbetz): support array fields */
          /* TODO(jbetz): support union fields */
        }
      }

      object FloatMember {
        def apply(value: Float): FloatMember = {
          val dataMap = new DataMap
          val result = new FloatMember(dataMap)
          result.setFields(value)
          dataMap.setReadOnly()
          result
        }

        def apply(dataMap: DataMap): FloatMember = {
          dataMap.setReadOnly()
          new FloatMember(dataMap)
        }

        private[Union] val memberKey = "float"
      }
    
      class DoubleMember(private val dataMap: DataMap)
        extends Union(dataMap) with Product1[Double] {
        import DoubleMember._

        lazy val _1, value = 
            obtainDirect(DoubleMemberSchema, classOf[Double], memberKey)
          
        /* TODO(jbetz): support map fields */
        /* TODO(jbetz): support array fields */
        /* TODO(jbetz): support union fields */

        private def setFields(value: Double): Unit = {
          
              selectDirect(DoubleMemberSchema, classOf[java.lang.Double], memberKey, Double.box(value))
            
          /* TODO(jbetz): support map fields */
          /* TODO(jbetz): support array fields */
          /* TODO(jbetz): support union fields */
        }
      }

      object DoubleMember {
        def apply(value: Double): DoubleMember = {
          val dataMap = new DataMap
          val result = new DoubleMember(dataMap)
          result.setFields(value)
          dataMap.setReadOnly()
          result
        }

        def apply(dataMap: DataMap): DoubleMember = {
          dataMap.setReadOnly()
          new DoubleMember(dataMap)
        }

        private[Union] val memberKey = "double"
      }
    
      class BooleanMember(private val dataMap: DataMap)
        extends Union(dataMap) with Product1[Boolean] {
        import BooleanMember._

        lazy val _1, value = 
            obtainDirect(BooleanMemberSchema, classOf[Boolean], memberKey)
          
        /* TODO(jbetz): support map fields */
        /* TODO(jbetz): support array fields */
        /* TODO(jbetz): support union fields */

        private def setFields(value: Boolean): Unit = {
          
              selectDirect(BooleanMemberSchema, classOf[java.lang.Boolean], memberKey, Boolean.box(value))
            
          /* TODO(jbetz): support map fields */
          /* TODO(jbetz): support array fields */
          /* TODO(jbetz): support union fields */
        }
      }

      object BooleanMember {
        def apply(value: Boolean): BooleanMember = {
          val dataMap = new DataMap
          val result = new BooleanMember(dataMap)
          result.setFields(value)
          dataMap.setReadOnly()
          result
        }

        def apply(dataMap: DataMap): BooleanMember = {
          dataMap.setReadOnly()
          new BooleanMember(dataMap)
        }

        private[Union] val memberKey = "boolean"
      }
    
      class StringMember(private val dataMap: DataMap)
        extends Union(dataMap) with Product1[String] {
        import StringMember._

        lazy val _1, value = 
            obtainDirect(StringMemberSchema, classOf[String], memberKey)
          
        /* TODO(jbetz): support map fields */
        /* TODO(jbetz): support array fields */
        /* TODO(jbetz): support union fields */

        private def setFields(value: String): Unit = {
          
              selectDirect(StringMemberSchema, classOf[java.lang.String], memberKey, value)
            
          /* TODO(jbetz): support map fields */
          /* TODO(jbetz): support array fields */
          /* TODO(jbetz): support union fields */
        }
      }

      object StringMember {
        def apply(value: String): StringMember = {
          val dataMap = new DataMap
          val result = new StringMember(dataMap)
          result.setFields(value)
          dataMap.setReadOnly()
          result
        }

        def apply(dataMap: DataMap): StringMember = {
          dataMap.setReadOnly()
          new StringMember(dataMap)
        }

        private[Union] val memberKey = "string"
      }
    
      class ByteStringMember(private val dataMap: DataMap)
        extends Union(dataMap) with Product1[ByteString] {
        import ByteStringMember._

        lazy val _1, value = 
            obtainDirect(ByteStringMemberSchema, classOf[ByteString], memberKey)
          
        /* TODO(jbetz): support map fields */
        /* TODO(jbetz): support array fields */
        /* TODO(jbetz): support union fields */

        private def setFields(value: ByteString): Unit = {
          
              selectDirect(ByteStringMemberSchema, classOf[com.linkedin.data.ByteString], memberKey, value)
            
          /* TODO(jbetz): support map fields */
          /* TODO(jbetz): support array fields */
          /* TODO(jbetz): support union fields */
        }
      }

      object ByteStringMember {
        def apply(value: ByteString): ByteStringMember = {
          val dataMap = new DataMap
          val result = new ByteStringMember(dataMap)
          result.setFields(value)
          dataMap.setReadOnly()
          result
        }

        def apply(dataMap: DataMap): ByteStringMember = {
          dataMap.setReadOnly()
          new ByteStringMember(dataMap)
        }

        private[Union] val memberKey = "bytes"
      }
    

    case class $UnknownMember private[Union](
        private[Union] val dataMap: DataMap)
      extends Union(dataMap)
  }
 
      
  

  private object Fields {
    
    val union = WithPrimitivesUnion.SCHEMA.getField("union")
  }

  def apply(union: Union): WithPrimitivesUnion = {
    val dataMap = new DataMap
    val result = new WithPrimitivesUnion(dataMap)
    result.setFields(union)
    dataMap.setReadOnly()
    result
  }

  def apply(dataMap: DataMap, conversion: DataConversion): WithPrimitivesUnion = {
    new WithPrimitivesUnion(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  
  
      def unapply(record: WithPrimitivesUnion): Option[(Union)] = {
        try {
          Some((record.union))
        } catch {
          case cast: TemplateOutputCastException => None
          case notPresent: RequiredFieldNotPresentException => None
        }
      }
    
}


