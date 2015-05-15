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
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.DataTemplateUtil
import org.coursera.data.DataTemplates
import org.coursera.data.DataTemplates.DataConversion
import org.coursera.data.generated
import scala.collection.generic.CanBuildFrom
import scala.collection.immutable
import scala.collection.mutable
import scala.collection.JavaConverters._

@generated(source = "/org/coursera/fortune/FortuneMapExample.pdsc")
final class FortuneMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, Fortune)]
  with Map[String, Fortune]
  with immutable.MapLike[String, Fortune, immutable.Map[String, Fortune]]
  with DataTemplate[DataMap] {

  // TODO(jbetz): Decide on caching policy for data template types. We should not be creating a
  // new instance here on each lookup.
  private[this] def lookup(key: String) = {
    Option(dataMap.getDataMap(key)).map(dataMap => Fortune(dataMap, DataConversion.SetReadOnly))
  }

  override def get(key: String): Option[Fortune] = lookup(key)

  override def iterator: Iterator[(String, Fortune)] = new Iterator[(String, Fortune)] {
    val underlying = dataMap.keySet().iterator()
    override def hasNext: Boolean = underlying.hasNext
    override def next(): (String, Fortune) = {
      val key = underlying.next()
      key -> lookup(key).get
    }
  }

  override def +[F >: Fortune](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case fortune: Fortune =>
        val copy = dataMap.copy()
        copy.put(key, fortune.data())
        copy.setReadOnly()
        new FortuneMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: String): FortuneMap = {
    val copy = dataMap.copy()
    copy.remove(key)
    copy.setReadOnly()
    new FortuneMap(copy)
  }

  override def schema(): DataSchema = FortuneMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = {
    val copy = dataMap.copy()
    copy.setReadOnly()
    new FortuneMap(copy)
  }
}

object FortuneMap {
  private val SCHEMA = DataTemplateUtil.parseSchema(
    """
      |{
      |  "type" : "map",
      |  "values" : {
      |    "type": "record",
      |    "name": "Fortune",
      |    "namespace": "org.coursera.fortune",
      |    "doc": "A fortune.",
      |    "fields": [
      |      {
      |        "name": "telling",
      |        "doc": "The fortune telling.",
      |        "type": [
      |          {
      |            "type": "record",
      |            "name": "FortuneCookie",
      |            "namespace": "org.coursera.fortune",
      |            "doc": "A fortune cookie.",
      |            "fields": [
      |              {
      |                "name": "message",
      |                "type": "string",
      |                "doc": "A fortune cookie message."
      |              },
      |              {
      |                "name": "certainty",
      |                "type": "float",
      |                "optional": true
      |              }
      |            ]
      |          },
      |          {
      |            "type": "record",
      |            "name": "MagicEightBall",
      |            "namespace": "org.coursera.fortune",
      |            "fields": [
      |              {
      |                "name": "question",
      |                "type": "string"
      |              },
      |              {
      |                "name": "answer",
      |                "type": {
      |                  "name": "MagicEightBallAnswer",
      |                  "namespace": "org.coursera.fortune",
      |                  "doc": "Magic eight ball answers.",
      |                  "type": "enum",
      |                  "symbols": [
      |                    "IT_IS_CERTAIN",
      |                    "ASK_AGAIN_LATER",
      |                    "OUTLOOK_NOT_SO_GOOD"
      |                  ],
      |                  "symbolDocs": {
      |                    "ASK_AGAIN_LATER": "Where later is at least 10 ms from now."
      |                  }
      |                }
      |              }
      |            ]
      |          },
      |          "string"
      |        ]
      |      }
      |    ]
      |  }
      |}
      |""".stripMargin
  ).asInstanceOf[MapDataSchema]

  val empty = FortuneMap()

  def apply(elems: (String, Fortune)*): FortuneMap = {
    FortuneMap(elems.toMap)
  }

  def apply(map: Map[String, Fortune]): FortuneMap = {
    new FortuneMap(new DataMap(map.mapValues(_.data()).asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): FortuneMap = {
    new FortuneMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[FortuneMap, (String, Fortune), FortuneMap] {
    def apply(from: FortuneMap) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: FortuneMap) extends mutable.Builder[(String, Fortune), FortuneMap] {
    def this() = this(new FortuneMap(new DataMap()))

    val entries = new DataMap(initial.dataMap)

    def +=(kv: (String, Fortune)): this.type = {
      val (key, value) = kv
      entries.put(key, value.data())
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.setReadOnly()
      new FortuneMap(entries)
    }
  }
}
