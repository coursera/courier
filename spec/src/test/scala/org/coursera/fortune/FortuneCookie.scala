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
import org.coursera.data.IntArray
import org.coursera.data.generated
import scala.runtime.ScalaRunTime
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.schema.RecordDataSchema

/**
 * A fortune cookie.
 */
@generated(source="/org/coursera/fortune/FortuneCookie.pdsc")
final class FortuneCookie private (private val dataMap: DataMap) extends RecordTemplate(dataMap, FortuneCookie.SCHEMA) with Product {

  /**
   * A fortune cookie message.
   */
  lazy val message: String = {
    obtainDirect(FortuneCookie.Fields.message, classOf[String], GetMode.STRICT)
  }

  lazy val certainty: Option[Float] = {
    Option(Float.unbox(obtainDirect(FortuneCookie.Fields.certainty, classOf[java.lang.Float], GetMode.STRICT)))
  }

  lazy val luckyNumbers: IntArray = {
    IntArray(dataMap.getDataList(FortuneCookie.Fields.luckyNumbers.getName), DataConversion.SetReadOnly)
  }

  override val productArity: Int = 3

  override def productElement(n: Int): Any =
    n match {
      case 0 => message
      case 1 => certainty
      case 2 => luckyNumbers
      case _ => throw new IndexOutOfBoundsException(n.toString)
    }

  override val productPrefix: String = "Fortune"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[FortuneCookie]

  override def hashCode: Int = ScalaRunTime._hashCode(this)

  override def equals(that: Any): Boolean = ScalaRunTime._equals(this, that)

  override def toString: String = ScalaRunTime._toString(this)

  def copy(message: String = this.message, certainty: Option[Float] = this.certainty, luckyNumbers: IntArray): FortuneCookie = {
    val dataMap = data().copy()
    FortuneCookie.setFields(dataMap, message, certainty, luckyNumbers)
    dataMap.setReadOnly()
    new FortuneCookie(dataMap)
  }
}

object FortuneCookie {
  val SCHEMA = DataTemplateUtil.parseSchema(
    """
      |{
      |  "type":"record",
      |  "name":"FortuneCookie",
      |  "namespace": "org.coursera.fortune",
      |  "fields":[
      |    {
      |      "name":"message",
      |      "type":"string"
      |    },
      |    {
      |      "name": "certainty",
      |      "type": "float",
      |      "optional": true
      |    },
      |    {
      |      "name": "luckyNumbers",
      |      "type": { "type": "array", "items": "int" }
      |    }
      |  ]
      |}
      |""".stripMargin).asInstanceOf[RecordDataSchema]

  private object Fields {
    val message = FortuneCookie.SCHEMA.getField("message")
    val certainty = FortuneCookie.SCHEMA.getField("certainty")
    val luckyNumbers = FortuneCookie.SCHEMA.getField("luckyNumbers")
  }

  private def setFields(dataMap: DataMap, message: String, certainty: Option[Float], luckyNumbers: IntArray): Unit = {
    dataMap.put("message", DataTemplateUtil.coerceOutput(message, classOf[String]))
    certainty.foreach(value => dataMap.put("certainty", DataTemplateUtil.coerceOutput(Float.box(value), classOf[java.lang.Float])))
    dataMap.put("luckyNumbers", luckyNumbers.data())
  }

  def apply(message: String, certainty: Option[Float], luckyNumbers: IntArray): FortuneCookie = {
    val dataMap = new DataMap
    setFields(dataMap, message, certainty, luckyNumbers)
    dataMap.setReadOnly()
    new FortuneCookie(dataMap)
  }

  def apply(dataMap: DataMap, conversion: DataConversion): FortuneCookie = {
    new FortuneCookie(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def unapply(record: FortuneCookie): Option[(String, Option[Float], IntArray)] = {
    try {
      Some((record.message, record.certainty, record.luckyNumbers))
    } catch {
      case cast: TemplateOutputCastException => None
      case notPresent: RequiredFieldNotPresentException => None
    }
  }
}
