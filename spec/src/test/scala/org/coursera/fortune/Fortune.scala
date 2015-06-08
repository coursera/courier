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

import javax.annotation.Generated

import com.linkedin.data.DataMap
import com.linkedin.data.schema.DataSchemaConstants
import com.linkedin.data.schema.IntegerDataSchema
import com.linkedin.data.schema.UnionDataSchema
import com.linkedin.data.template.Custom
import com.linkedin.data.template.GetMode
import com.linkedin.data.template.RecordTemplate
import com.linkedin.data.template.RequiredFieldNotPresentException
import com.linkedin.data.template.SetMode
import com.linkedin.data.template.TemplateOutputCastException
import com.linkedin.data.template.UnionTemplate
import org.coursera.data.DataTemplates
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
@Generated(
  value = Array("Fortune"),
  comments="Courier Data Template. Generated from /org/coursera/fortune/Fortune.pdsc",
  date = "2015-1-1")
final class Fortune private (
    private val dataMap: DataMap)
  extends RecordTemplate(dataMap, Fortune.SCHEMA) with Product {

  /**
   * The fortune telling.
   */
  def telling: Fortune.Telling = Fortune.Telling(dataMap.getDataMap(Fortune.Fields.telling.getName))
  def createdAt: DateTime = obtainDirect(
    Fortune.Fields.createdAt, classOf[DateTime], GetMode.STRICT)

  private def setFields(telling: Fortune.Telling, createdAt: DateTime): Unit = {
    putDirect(Fortune.Fields.createdAt, classOf[DateTime], classOf[String], createdAt, SetMode.DISALLOW_NULL)
    data.put(Fortune.Fields.telling.getName, telling.data()) // TODO: figure out how to use putWrapped
  }

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
    Fortune(data().copy(), DataConversion.SetReadOnly)
  }
}

object Fortune {
  val SCHEMA = DataTemplateUtil.parseSchema(
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



  def apply(telling: Telling, createdAt: DateTime): Fortune = {
    val dataMap = new DataMap
    val record = new Fortune(dataMap)
    record.setFields(telling, createdAt)
    dataMap.setReadOnly()
    record
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

  @Generated(
    value = Array("Telling"),
    comments="Courier Data Template. Generated from /org/coursera/fortune/Fortune.pdsc",
    date = "2015-1-1")
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

    def apply(union: DataMap): Telling = {
      require(
        union.size == 1,
        "Malformed union DataMap, exactly one field must be present and must be a member key")

      val unionTag = union.keySet().iterator().next()
      unionTag match {
        case FortuneCookieMember.memberKey =>
          Fortune.Telling.FortuneCookieMember(union)
        case MagicEightBallMember.memberKey =>
          Fortune.Telling.MagicEightBallMember(union)
        case StringMember.memberKey =>
          Fortune.Telling.StringMember(union)
        case _: Any =>
          Fortune.Telling.$UnknownMember(union)
      }
    }

    class FortuneCookieMember(private val dataMap: DataMap)
      extends Telling(dataMap) with Product1[FortuneCookie] {
      import FortuneCookieMember._

      lazy val _1, value = obtainWrapped(FortuneCookie.SCHEMA, classOf[FortuneCookie], memberKey)

      private def setFields(value: FortuneCookie): Unit = {
        selectWrapped(FortuneCookie.SCHEMA, classOf[FortuneCookie], memberKey, value)
      }
    }

    object FortuneCookieMember {
      def apply(value: FortuneCookie): FortuneCookieMember = {
        val dataMap = new DataMap
        val result = new FortuneCookieMember(dataMap)
        result.setFields(value)
        dataMap.setReadOnly()
        result
      }

      def apply(dataMap: DataMap): FortuneCookieMember = {
        dataMap.setReadOnly()
        new FortuneCookieMember(dataMap)
      }

      private[Telling] val memberKey = "org.coursera.fortune.FortuneCookie"
    }

    class MagicEightBallMember(private val dataMap: DataMap)
      extends Telling(dataMap) with Product1[MagicEightBall] {
      import MagicEightBallMember._

      lazy val _1, value = obtainWrapped(MagicEightBall.SCHEMA, classOf[MagicEightBall], memberKey)

      private def setFields(value: MagicEightBall): Unit = {
        selectWrapped(MagicEightBall.SCHEMA, classOf[MagicEightBall], memberKey, value)
      }
    }

    object MagicEightBallMember {

      def apply(value: MagicEightBall): MagicEightBallMember = {
        val dataMap = new DataMap
        val result = new MagicEightBallMember(dataMap)
        result.setFields(value)
        dataMap.setReadOnly()
        result
      }

      def apply(dataMap: DataMap): MagicEightBallMember = {
        dataMap.setReadOnly()
        new MagicEightBallMember(dataMap)
      }

      private[Telling] val memberKey = "org.coursera.fortune.MagicEightBall"
    }

    class StringMember(private val dataMap: DataMap)
      extends Telling(dataMap) with Product1[String] {
      import StringMember._

      lazy val _1, value = obtainDirect(DataSchemaConstants.STRING_DATA_SCHEMA, classOf[String], memberKey)

      private def setFields(string: String): Unit = {
        selectDirect(DataSchemaConstants.STRING_DATA_SCHEMA, classOf[String], memberKey, string)
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

      private[Telling] val memberKey = "string"
    }

    case class $UnknownMember private[Telling](
        private[Telling] val dataMap: DataMap)
      extends Telling(dataMap)
  }
}
