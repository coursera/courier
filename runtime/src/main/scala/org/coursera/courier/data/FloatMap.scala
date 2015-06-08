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

@Generated(value = Array("FloatMap"), comments = "Courier Data Template.", date = "Sat May 30 13:09:01 PDT 2015")
final class FloatMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, Float)]
  with Map[String, Float]
  with immutable.MapLike[String, Float, immutable.Map[String, Float]]
  with DataTemplate[DataMap] {

  import org.coursera.courier.data.FloatMap._

  // TODO(jbetz): Decide on caching policy for data template types. We should not be creating a
  // new instance here on each lookup.
  private[this] def lookup(key: String): Option[Float] = {

    Option(dataMap.get(key).asInstanceOf[java.lang.Float])

  }

  override def get(key: String): Option[Float] = lookup(key)

  override def iterator: Iterator[(String, Float)] = new Iterator[(String, Float)] {
    val underlying = dataMap.keySet().iterator()

    override def hasNext: Boolean = underlying.hasNext

    override def next(): (String, Float) = {
      val key = underlying.next()
      key -> lookup(key).get
    }
  }

  override def +[F >: Float](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case v: Float =>
        val copy = dataMap.copy()
        copy.put(key, coerceOutput(v))
        copy.setReadOnly()
        new FloatMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: String): FloatMap = {
    val copy = dataMap.copy()
    copy.remove(key)
    copy.setReadOnly()
    new FloatMap(copy)
  }

  override def schema(): DataSchema = FloatMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = {
    val copy = dataMap.copy()
    copy.setReadOnly()
    new FloatMap(copy)
  }
}

object FloatMap {
  val SCHEMA = DataTemplateUtil.parseSchema( """{"type":"map","values":"float"}""").asInstanceOf[MapDataSchema]

  val empty = FloatMap()

  def apply(elems: (String, Float)*): FloatMap = {
    FloatMap(elems.toMap)
  }

  def apply(map: Map[String, Float]): FloatMap = {
    new FloatMap(new DataMap(map.mapValues(coerceOutput).asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): FloatMap = {
    new FloatMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[FloatMap, (String, Float), FloatMap] {
    def apply(from: FloatMap) = new DataBuilder(from)

    def apply() = newBuilder
  }

  class DataBuilder(initial: FloatMap) extends mutable.Builder[(String, Float), FloatMap] {
    def this() = this(new FloatMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (String, Float)): this.type = {
      val (key, value) = kv
      entries.put(key, coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.setReadOnly()
      new FloatMap(entries)
    }
  }

  private def coerceOutput(value: Float): AnyRef = {

    Float.box(value)

  }
}

