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

package org.coursera.courier.data

import javax.annotation.Generated

import com.linkedin.data.DataMap
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.DataTemplateUtil
import org.coursera.courier.templates.DataTemplates
import org.coursera.courier.templates.DataTemplates.DataConversion

import scala.collection.JavaConverters._
import scala.collection.generic.CanBuildFrom
import scala.collection.immutable
import scala.collection.mutable

@Generated(value = Array("StringMap"), comments = "Courier Data Template.", date = "Sat May 30 13:09:01 PDT 2015")
final class StringMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, String)]
  with Map[String, String]
  with immutable.MapLike[String, String, immutable.Map[String, String]]
  with DataTemplate[DataMap] {

  import org.coursera.courier.data.StringMap._

  // TODO(jbetz): Decide on caching policy for data template types. We should not be creating a
  // new instance here on each lookup.
  private[this] def lookup(key: String): Option[String] = {

    Option(dataMap.get(key).asInstanceOf[java.lang.String])

  }

  override def get(key: String): Option[String] = lookup(key)

  override def iterator: Iterator[(String, String)] = new Iterator[(String, String)] {
    val underlying = dataMap.keySet().iterator()

    override def hasNext: Boolean = underlying.hasNext

    override def next(): (String, String) = {
      val key = underlying.next()
      key -> lookup(key).get
    }
  }

  override def +[F >: String](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case v: String =>
        val copy = dataMap.copy()
        copy.put(key, coerceOutput(v))
        copy.setReadOnly()
        new StringMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: String): StringMap = {
    val copy = dataMap.copy()
    copy.remove(key)
    copy.setReadOnly()
    new StringMap(copy)
  }

  override def schema(): DataSchema = StringMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = {
    val copy = dataMap.copy()
    copy.setReadOnly()
    new StringMap(copy)
  }
}

object StringMap {
  val SCHEMA = DataTemplateUtil.parseSchema( """{"type":"map","values":"string"}""").asInstanceOf[MapDataSchema]

  val empty = StringMap()

  def apply(elems: (String, String)*): StringMap = {
    StringMap(elems.toMap)
  }

  def apply(map: Map[String, String]): StringMap = {
    new StringMap(new DataMap(map.mapValues(coerceOutput).asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): StringMap = {
    new StringMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[StringMap, (String, String), StringMap] {
    def apply(from: StringMap) = new DataBuilder(from)

    def apply() = newBuilder
  }

  class DataBuilder(initial: StringMap) extends mutable.Builder[(String, String), StringMap] {
    def this() = this(new StringMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (String, String)): this.type = {
      val (key, value) = kv
      entries.put(key, coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.setReadOnly()
      new StringMap(entries)
    }
  }

  private def coerceOutput(value: String): AnyRef = {

    value

  }
}

