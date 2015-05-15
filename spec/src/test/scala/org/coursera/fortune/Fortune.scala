/*
 Copyright 2015 Coursera Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.coursera.fortune

import com.linkedin.data.DataMap
import com.linkedin.data.schema.UnionDataSchema
import com.linkedin.data.template.Custom
import com.linkedin.data.template.GetMode
import com.linkedin.data.template.RecordTemplate
import com.linkedin.data.template.RequiredFieldNotPresentException
import com.linkedin.data.template.TemplateOutputCastException
import com.linkedin.data.template.UnionTemplate
import org.coursera.data.DataTemplates
import org.coursera.data.DataTemplates.DataConversion
import org.coursera.data.DataTemplates.DataConversion
import org.coursera.data.generated
import org.coursera.models.common.DateTimeCoercer
import org.joda.time.DateTime
import scala.runtime.ScalaRunTime
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.schema.RecordDataSchema

/**
 * A fortune.
 */
@generated(source="/org/coursera/fortune/Fortune.pdsc")
final class Fortune private (
    private val dataMap: DataMap)
  extends RecordTemplate(dataMap, Fortune.SCHEMA) with Product {

  /**
   * The fortune telling.
   */
  lazy val telling: Fortune.Telling = Fortune.Telling(dataMap)
  lazy val createdAt: DateTime = obtainDirect(
    Fortune.Fields.createdAt, classOf[DateTime], GetMode.STRICT)

  override val productArity: Int = 2

  override def productElement(n: Int): Any =
    n match {
      case 0 => telling
      case 1 => createdAt
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "Fortune"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[Fortune]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  def copy(telling: Fortune.Telling = this.telling, createdAt: DateTime = this.createdAt): Fortune = {
    val dataMap = data().copy()
    Fortune.setFields(dataMap, telling, createdAt)
    dataMap.setReadOnly()
    new Fortune(dataMap)
  }
}

object Fortune {
  private val SCHEMA = DataTemplateUtil.parseSchema(
    """
      |{
      |  "type": "record",
      |  "name": "Fortune",
      |  "namespace": "org.coursera.fortune",
      |  "doc": "A fortune.",
      |  "fields": [
      |    {
      |      "name": "telling",
      |      "doc": "The fortune telling.",
      |      "type": [
      |        {
      |          "type": "record",
      |          "name": "FortuneCookie",
      |          "namespace": "org.coursera.fortune",
      |          "doc": "A fortune cookie.",
      |          "fields": [
      |            {
      |              "name": "message",
      |              "type": "string",
      |              "doc": "A fortune cookie message."
      |            },
      |            {
      |              "name": "certainty",
      |              "type": "float",
      |              "optional": true
      |            }
      |          ]
      |        },
      |        {
      |          "type": "record",
      |          "name": "MagicEightBall",
      |          "namespace": "org.coursera.fortune",
      |          "fields": [
      |            {
      |              "name": "question",
      |              "type": "string"
      |            },
      |            {
      |              "name": "answer",
      |              "type": {
      |                "name": "MagicEightBallAnswer",
      |                "namespace": "org.coursera.fortune",
      |                "doc": "Magic eight ball answers.",
      |                "type": "enum",
      |                "symbols": [
      |                  "IT_IS_CERTAIN",
      |                  "ASK_AGAIN_LATER",
      |                  "OUTLOOK_NOT_SO_GOOD"
      |                ],
      |                "symbolDocs": {
      |                  "ASK_AGAIN_LATER": "Where later is at least 10 ms from now."
      |                }
      |              }
      |            }
      |          ]
      |        },
      |        "string"
      |      ]
      |    },
      |    {
      |      "name": "createdAt",
      |      "type": {
      |        "name": "DateTime",
      |        "namespace": "org.coursera.models.common",
      |        "type": "typeref",
      |        "ref": "string",
      |        "doc": "ISO 8601 date-time.",
      |        "scala": {
      |          "class": "DateTime",
      |          "coercerClass": "org.coursera.models.common.DateTimeCoercer"
      |        }
      |      }
      |    }
      |  ]
      |}
      |""".stripMargin).asInstanceOf[RecordDataSchema]

  // register custom types and coercers
  Custom.initializeCustomClass(classOf[DateTime])
  DateTimeCoercer.registerCoercer()

  private object Fields {
    val telling = Fortune.SCHEMA.getField("telling")
    val createdAt = Fortune.SCHEMA.getField("createdAt")
  }

  private def setFields(dataMap: DataMap, telling: Telling, createdAt: DateTime): Unit = {
    dataMap.put("telling", telling.data())
    dataMap.put("createdAt",
      DataTemplateUtil.coerceInput(createdAt, classOf[DateTime], classOf[String]))
  }

  def apply(telling: Telling, createdAt: DateTime): Fortune = {
    val dataMap = new DataMap
    setFields(dataMap, telling, createdAt)
    dataMap.setReadOnly()
    new Fortune(dataMap)
  }

  def apply(dataMap: DataMap, conversion: DataConversion): Fortune = {
    new Fortune(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def unapply(record: Fortune): Option[(Telling, DateTime)] = {
    try {
      Some((record.telling, record.createdAt))
    } catch {
      case cast: TemplateOutputCastException => None
      case notPresent: RequiredFieldNotPresentException => None
    }
  }

  @generated(source="/org/coursera/fortune/Fortune.pdsc")
  sealed abstract class Telling protected(
      private val value: DataMap)
    extends UnionTemplate(value, Telling.SCHEMA) with Product {

    // reset UnionTemplate overrides
    override def canEqual(that: Any): Boolean = that.isInstanceOf[this.type]

    override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

    override def toString: String = ScalaRunTime._toString(this)

    override def hashCode: Int = ScalaRunTime._hashCode(this)
  }

  object Telling {
    private val SCHEMA = DataTemplateUtil.parseSchema(
      """
        |[{
        |  "type": "record",
        |  "name": "FortuneCookie",
        |  "namespace": "org.coursera.fortune",
        |  "doc": "A fortune cookie.",
        |  "fields": [
        |    {
        |      "name": "message",
        |      "type": "string",
        |      "doc": "A fortune cookie message."
        |    },
        |    {
        |      "name": "certainty",
        |      "type": "float",
        |      "optional": true
        |    }
        |  ]
        |},
        |{
        |  "type": "record",
        |  "name": "MagicEightBall",
        |  "namespace": "org.coursera.fortune",
        |  "fields": [
        |    {
        |      "name": "question",
        |      "type": "string"
        |    },
        |    {
        |      "name": "answer",
        |      "type": {
        |        "name": "MagicEightBallAnswer",
        |        "namespace": "org.coursera.fortune",
        |        "doc": "Magic eight ball answers.",
        |        "type": "enum",
        |        "symbols": [
        |          "IT_IS_CERTAIN",
        |          "ASK_AGAIN_LATER",
        |          "OUTLOOK_NOT_SO_GOOD"
        |        ],
        |        "symbolDocs": {
        |          "ASK_AGAIN_LATER": "Where later is at least 10 ms from now."
        |        }
        |      }
        |    }
        |  ]
        |},
        |"string"]
      """.stripMargin).asInstanceOf[UnionDataSchema]

    def apply(dataMap: DataMap): Telling = {
      val union = dataMap.getDataMap(Fortune.Fields.telling.getName)
      require(
        union.size == 1,
        "Malformed union DataMap, exactly one field must be present and must be a member key")

      val unionTag = union.keySet().iterator().next()
      unionTag match {
        case FortuneCookieMember.memberKey =>
          Fortune.Telling.FortuneCookieMember(
            FortuneCookie(
              union.getDataMap(FortuneCookieMember.memberKey),
              DataConversion.SetReadOnly))
        case MagicEightBallMember.memberKey =>
          Fortune.Telling.MagicEightBallMember(
            MagicEightBall(
              union.getDataMap(MagicEightBallMember.memberKey),
              DataConversion.SetReadOnly))
        case StringMember.memberKey =>
          Fortune.Telling.StringMember(union.getString(StringMember.memberKey))
        case _: Any =>
          Fortune.Telling.$UnknownMember(union)
      }
    }

    case class FortuneCookieMember(fortuneCookie: FortuneCookie)
      extends Telling(DataTemplates.toUnionMap(fortuneCookie.data(), FortuneCookieMember.memberKey))

    object FortuneCookieMember {
      private[Telling] val memberKey = "org.coursera.fortune.FortuneCookie"
    }

    case class MagicEightBallMember(magicEightBall: MagicEightBall)
      extends Telling(DataTemplates.toUnionMap(magicEightBall.data(), MagicEightBallMember.memberKey))

    object MagicEightBallMember {
      private[Telling] val memberKey = "org.coursera.fortune.MagicEightBall"
    }

    case class StringMember(string: String)
      extends Telling(DataTemplates.toUnionMap(string, StringMember.memberKey))

    object StringMember {
      private[Telling] val memberKey = "string"
    }

    case class $UnknownMember private[Telling](
        private[Telling] val dataMap: DataMap)
      extends Telling(dataMap)
  }
}
