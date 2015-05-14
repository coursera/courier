package org.coursera.fortune

import com.linkedin.data.DataMap
import com.linkedin.data.template.GetMode
import com.linkedin.data.template.RecordTemplate
import com.linkedin.data.template.RequiredFieldNotPresentException
import com.linkedin.data.template.TemplateOutputCastException
import org.coursera.data.DataTemplates
import org.coursera.data.DataTemplates.DataConversion
import org.coursera.data.generated
import org.coursera.models.common.UserId
import scala.runtime.ScalaRunTime
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.schema.RecordDataSchema

@generated(source="/org/coursera/fortune/TyperefExample.pdsc")
final class TyperefExample private (private val dataMap: DataMap) extends RecordTemplate(dataMap, TyperefExample.SCHEMA) with Product {

  lazy val timestamp: Long = {
    obtainDirect(TyperefExample.Fields.timestamp, classOf[Long], GetMode.STRICT)
  }

  lazy val userId: UserId = {
    UserId(obtainDirect(TyperefExample.Fields.userId, classOf[Int], GetMode.STRICT))
  }

  override val productArity: Int = 2

  override def productElement(n: Int): Any =
    n match {
      case 0 => timestamp
      case 1 => userId
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "TyperefExample"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[TyperefExample]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  def copy(timestamp: Long = this.timestamp, userId: UserId = this.userId): TyperefExample = {
    val dataMap = data().copy()
    TyperefExample.setFields(dataMap, timestamp, userId)
    dataMap.setReadOnly()
    new TyperefExample(dataMap)
  }
}

object TyperefExample {
  private val SCHEMA = DataTemplateUtil.parseSchema(
    """
      |{
      |  "type": "record",
      |  "name": "TyperefExample",
      |  "namespace": "org.coursera.fortune",
      |  "fields": [
      |    {
      |      "name": "timestamp",
      |      "type": {
      |        "name": "Timestamp",
      |        "namespace": "org.coursera.models.common",
      |        "type": "typeref",
      |        "ref": "long",
      |        "doc": "A unix timestamp."
      |      },
      |      "default": 1430849546000
      |    },
      |    {
      |      "name": "userId",
      |      "type": {
      |        "name": "UserId",
      |        "namespace": "org.coursera.models.common",
      |        "type": "typeref",
      |        "ref": "int",
      |        "scala": {
      |          "class": "org.coursera.models.common.UserId"
      |        }
      |      }
      |    }
      |  ]
      |}
      |""".stripMargin).asInstanceOf[RecordDataSchema]

  private object Fields {
    val timestamp = TyperefExample.SCHEMA.getField("timestamp")
    val userId = TyperefExample.SCHEMA.getField("userId")
  }

  private def setFields(dataMap: DataMap, timestamp: Long, userId: UserId): Unit = {
    dataMap.put("timestamp", Long.box(timestamp))
    dataMap.put("userId", Int.box(userId match { case UserId(value) => value }))
  }

  def apply(timestamp: Long = 1430849546000L, userId: UserId): TyperefExample = {
    val dataMap = new DataMap
    setFields(dataMap, timestamp, userId)
    dataMap.setReadOnly()
    new TyperefExample(dataMap)
  }

  def apply(dataMap: DataMap, conversion: DataConversion): TyperefExample = {
    new TyperefExample(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def unapply(record: TyperefExample): Option[(Long, UserId)] = {
    try {
      Some((record.timestamp, record.userId))
    } catch {
      case cast: TemplateOutputCastException => None
      case notPresent: RequiredFieldNotPresentException => None
    }
  }
}
