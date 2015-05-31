

package org.coursera.typerefs

import javax.annotation.Generated

import com.linkedin.data.DataMap
import com.linkedin.data.schema.DataSchemaConstants
import com.linkedin.data.ByteString
import com.linkedin.data.schema.UnionDataSchema
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.template.UnionTemplate

import scala.runtime.ScalaRunTime



  @Generated(value = Array("UnionTyperef"), comments="Courier Data Template.", date = "Sun May 31 11:20:37 PDT 2015")
  sealed abstract class UnionTyperef protected(private val value: DataMap)
    extends UnionTemplate(value, UnionTyperef.SCHEMA) with Product {
    import UnionTyperef._

    // reset UnionTemplate overrides
    override def canEqual(that: Any): Boolean = that.isInstanceOf[this.type]

    override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

    override def toString: String = ScalaRunTime._toString(this)

    override def hashCode: Int = ScalaRunTime._hashCode(this)
  }

  object UnionTyperef {
    val SCHEMA = DataTemplateUtil.parseSchema("""["string","int"]""").asInstanceOf[UnionDataSchema]

    
      
          protected val StringMemberSchema = SCHEMA.getType("string")
        
    
      
          protected val IntMemberSchema = SCHEMA.getType("int")
        
    

    def apply(union: DataMap): UnionTyperef = {
      require(
        union.size == 1,
        "Malformed union DataMap, exactly one field must be present and must be a member key")

      val unionTag = union.keySet().iterator().next()
      unionTag match {
        
          case StringMember.memberKey =>
            StringMember(union)
        
          case IntMember.memberKey =>
            IntMember(union)
        
        case _: Any =>
          UnionTyperef.$UnknownMember(union)
      }
    }

    
      class StringMember(private val dataMap: DataMap)
        extends UnionTyperef(dataMap) with Product1[String] {
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

        private[UnionTyperef] val memberKey = "string"
      }
    
      class IntMember(private val dataMap: DataMap)
        extends UnionTyperef(dataMap) with Product1[Int] {
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

        private[UnionTyperef] val memberKey = "int"
      }
    

    case class $UnknownMember private[UnionTyperef](
        private[UnionTyperef] val dataMap: DataMap)
      extends UnionTyperef(dataMap)
  }

