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

@Generated(value = Array("DoubleMap"), comments = "Courier Data Template.", date = "Sat May 30 13:09:01 PDT 2015")
final class DoubleMap(private val dataMap: DataMap)
  extends immutable.Iterable[(String, Double)]
  with Map[String, Double]
  with immutable.MapLike[String, Double, immutable.Map[String, Double]]
  with DataTemplate[DataMap] {

  import org.coursera.courier.data.DoubleMap._

  // TODO(jbetz): Decide on caching policy for data template types. We should not be creating a
  // new instance here on each lookup.
  private[this] def lookup(key: String): Option[Double] = {

    Option(dataMap.get(key).asInstanceOf[java.lang.Double])

  }

  override def get(key: String): Option[Double] = lookup(key)

  override def iterator: Iterator[(String, Double)] = new Iterator[(String, Double)] {
    val underlying = dataMap.keySet().iterator()

    override def hasNext: Boolean = underlying.hasNext

    override def next(): (String, Double) = {
      val key = underlying.next()
      key -> lookup(key).get
    }
  }

  override def +[F >: Double](kv: (String, F)): Map[String, F] = {
    val (key, value) = kv
    value match {
      case v: Double =>
        val copy = dataMap.copy()
        copy.put(key, coerceOutput(v))
        copy.setReadOnly()
        new DoubleMap(copy)
      case _: Any =>
        (iterator ++ Iterator.single(kv)).toMap
    }
  }

  override def -(key: String): DoubleMap = {
    val copy = dataMap.copy()
    copy.remove(key)
    copy.setReadOnly()
    new DoubleMap(copy)
  }

  override def schema(): DataSchema = DoubleMap.SCHEMA

  override def data(): DataMap = dataMap

  override def copy(): DataTemplate[DataMap] = {
    val copy = dataMap.copy()
    copy.setReadOnly()
    new DoubleMap(copy)
  }
}

object DoubleMap {
  val SCHEMA = DataTemplateUtil.parseSchema( """{"type":"map","values":"double"}""").asInstanceOf[MapDataSchema]

  val empty = DoubleMap()

  def apply(elems: (String, Double)*): DoubleMap = {
    DoubleMap(elems.toMap)
  }

  def apply(map: Map[String, Double]): DoubleMap = {
    new DoubleMap(new DataMap(map.mapValues(coerceOutput).asJava))
  }

  def apply(dataMap: DataMap, conversion: DataConversion): DoubleMap = {
    new DoubleMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[DoubleMap, (String, Double), DoubleMap] {
    def apply(from: DoubleMap) = new DataBuilder(from)

    def apply() = newBuilder
  }

  class DataBuilder(initial: DoubleMap) extends mutable.Builder[(String, Double), DoubleMap] {
    def this() = this(new DoubleMap(new DataMap()))

    val entries = new DataMap(initial.data())

    def +=(kv: (String, Double)): this.type = {
      val (key, value) = kv
      entries.put(key, coerceOutput(value))
      this
    }

    def clear() = {
      entries.clear()
    }

    def result() = {
      entries.setReadOnly()
      new DoubleMap(entries)
    }
  }

  private def coerceOutput(value: Double): AnyRef = {

    Double.box(value)

  }
}

