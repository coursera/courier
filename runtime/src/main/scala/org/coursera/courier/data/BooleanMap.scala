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

@Generated(value = Array("BooleanMap"), comments = "Courier Data Template.", date = "Sat May 30 13:09:01 PDT 2015")
final class BooleanMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, Boolean)]
  with Map[String, Boolean]
  with immutable.MapLike[String, Boolean, immutable.Map[String, Boolean]]
  with DataTemplate[DataMap] {

  import org.coursera.courier.data.BooleanMap._

  // TODO(jbetz): Decide on caching policy for data template types. We should not be creating a
  // new instance here on each lookup.
  private[this] def lookup(key: String): Option[Boolean] = {

    Option(dataMap.get(key).asInstanceOf[java.lang.Boolean])

  }

  override def get(key: String): Option[Boolean] = lookup(key)

  override def iterator: Iterator[(String, Boolean)] = new Iterator[(String, Boolean)] {
    val underlying = dataMap.keySet().iterator()

    override def hasNext: Boolean = underlying.hasNext

    override def next(): (String, Boolean) = {
      val key = underlying.next()
      key -> lookup(key).get
    }
  }

  override def +[F >: Boolean](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case v: Boolean =>
        val copy = dataMap.copy()
        copy.put(key, coerceOutput(v))
        copy.setReadOnly()
        new BooleanMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: String): BooleanMap = {
    val copy = dataMap.copy()
    copy.remove(key)
    copy.setReadOnly()
    new BooleanMap(copy)
  }

  override def schema(): DataSchema = BooleanMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = {
    val copy = dataMap.copy()
    copy.setReadOnly()
    new BooleanMap(copy)
  }
}

object BooleanMap {
  val SCHEMA = DataTemplateUtil.parseSchema( """{"type":"map","values":"boolean"}""").asInstanceOf[MapDataSchema]

  val empty = BooleanMap()

  def apply(elems: (String, Boolean)*): BooleanMap = {
    BooleanMap(elems.toMap)
  }

  def apply(map: Map[String, Boolean]): BooleanMap = {
    new BooleanMap(new DataMap(map.mapValues(coerceOutput).asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): BooleanMap = {
    new BooleanMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[BooleanMap, (String, Boolean), BooleanMap] {
    def apply(from: BooleanMap) = new DataBuilder(from)

    def apply() = newBuilder
  }

  class DataBuilder(initial: BooleanMap) extends mutable.Builder[(String, Boolean), BooleanMap] {
    def this() = this(new BooleanMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (String, Boolean)): this.type = {
      val (key, value) = kv
      entries.put(key, coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.setReadOnly()
      new BooleanMap(entries)
    }
  }

  private def coerceOutput(value: Boolean): AnyRef = {

    Boolean.box(value)

  }
}

