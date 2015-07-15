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

package org.coursera.courier.benchmark

import play.api.libs.json._

case class PayloadPlayJson(title: String, offset: Int, elements: Seq[ElementPlayJson])

object PayloadPlayJson {
  implicit val format: Format[PayloadPlayJson] = Json.format[PayloadPlayJson]
}

case class ElementPlayJson(title: String, version: Long, weight: Option[Double], active: Boolean, body: BodyPlayJson)

object ElementPlayJson {
  implicit val format: Format[ElementPlayJson] = Json.format[ElementPlayJson]
}

case class BodyPlayJson(termWeights: Map[String, Float], text: String, enum: ContentTypePlayJson.ContentTypePlayJson)

object BodyPlayJson {
  implicit val enumAFormat: Format[ContentTypePlayJson.ContentTypePlayJson] = EnumUtils.enumFormat(ContentTypePlayJson)
  implicit val format: Format[BodyPlayJson] = Json.format[BodyPlayJson]
}

object ContentTypePlayJson extends Enumeration {
  type ContentTypePlayJson = Value
  val AUDIO = Value("AUDIO")
  val VIDEO = Value("VIDEO")
  val TEXT = Value("TEXT")
}

object EnumUtils {
  def enumReads[E <: Enumeration](enum: E): Reads[E#Value] = new Reads[E#Value] {
    def reads(json: JsValue): JsResult[E#Value] = json match {
      case JsString(s) => {
        try {
          JsSuccess(enum.withName(s))
        } catch {
          case _: NoSuchElementException => JsError(s"Enumeration expected of type: '${enum.getClass}', but it does not appear to contain the value: '$s'")
        }
      }
      case _ => JsError("String value expected")
    }
  }

  implicit def enumWrites[E <: Enumeration]: Writes[E#Value] = new Writes[E#Value] {
    def writes(v: E#Value): JsValue = JsString(v.toString)
  }

  implicit def enumFormat[E <: Enumeration](enum: E): Format[E#Value] = {
    Format(EnumUtils.enumReads(enum), EnumUtils.enumWrites)
  }
}
