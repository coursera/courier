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
import com.linkedin.data.template.GetMode
import com.linkedin.data.template.RecordTemplate
import com.linkedin.data.template.RequiredFieldNotPresentException
import com.linkedin.data.template.TemplateOutputCastException
import org.coursera.data.DataTemplates
import org.coursera.data.DataTemplates.DataConversion
import org.coursera.data.generated
import scala.runtime.ScalaRunTime
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.schema.RecordDataSchema

@generated(source="/org/coursera/fortune/MagicEightBall.pdsc")
final class MagicEightBall private (private val dataMap: DataMap) extends RecordTemplate(dataMap, MagicEightBall.SCHEMA) with Product {

  lazy val question: String = {
    obtainDirect(MagicEightBall.Fields.question, classOf[String], GetMode.STRICT)
  }

  lazy val answer: MagicEightBallAnswer.MagicEightBallAnswer = {
    MagicEightBallAnswer.fromString(obtainDirect(MagicEightBall.Fields.answer, classOf[String], GetMode.STRICT))
  }

  override val productArity: Int = 2

  override def productElement(n: Int): Any =
    n match {
      case 0 => question
      case 1 => answer
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "MagicEightBall"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[MagicEightBall]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  def copy(answer: MagicEightBallAnswer.MagicEightBallAnswer = this.answer): MagicEightBall = {
    val dataMap = data().copy()
    MagicEightBall.setFields(dataMap, question, answer)
    dataMap.setReadOnly()
    new MagicEightBall(dataMap)
  }
}

object MagicEightBall {
  val SCHEMA = DataTemplateUtil.parseSchema(
    """
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
      |}
      |""".stripMargin).asInstanceOf[RecordDataSchema]

  private object Fields {
    val question = MagicEightBall.SCHEMA.getField("question")
    val answer = MagicEightBall.SCHEMA.getField("answer")
  }

  private def setFields(dataMap: DataMap, question: String, answer: MagicEightBallAnswer.MagicEightBallAnswer): Unit = {
    dataMap.put("question", DataTemplateUtil.coerceOutput(question, classOf[String]))
    dataMap.put("answer", DataTemplateUtil.coerceOutput(answer.toString, classOf[String]))
  }

  def apply(question: String, answer: MagicEightBallAnswer.MagicEightBallAnswer): MagicEightBall = {
    val dataMap = new DataMap
    setFields(dataMap, question, answer)
    dataMap.setReadOnly()
    new MagicEightBall(dataMap)
  }

  def apply(dataMap: DataMap, conversion: DataConversion): MagicEightBall = {
    new MagicEightBall(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def unapply(record: MagicEightBall): Option[(String, MagicEightBallAnswer.MagicEightBallAnswer)] = {
    try {
      Some((record.question, record.answer))
    } catch {
      case cast: TemplateOutputCastException => None
      case notPresent: RequiredFieldNotPresentException => None
    }
  }
}
