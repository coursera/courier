



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

   @Generated(value = Array("LongMap"), comments="Courier Data Template.", date = "Sat Jun 20 09:28:24 PDT 2015")
    final class LongMap(private val dataMap: DataMap)
     extends immutable.Iterable[(String, Long)]
     with Map[String, Long]
     with immutable.MapLike[String, Long, immutable.Map[String, Long]]
     with DataTemplate[DataMap] {
     import org.coursera.courier.data.LongMap._


     private[this] lazy val map = dataMap.asScala.map { case (k, v) => k -> coerceInput(v) }.toMap

     private[this] def coerceInput(any: AnyRef): Long = {

           DataTemplateUtil.coerceOutput(any, classOf[java.lang.Long])

     }

     override def get(key: String): Option[Long] = map.get(key)

     override def iterator: Iterator[(String, Long)] = map.iterator

     override def +[F >: Long](kv: (String, F)): Map[String, F] = {
       val (key, value) = kv
       value match {
         case v: Long =>
           val copy = dataMap.copy()
           copy.put(key, coerceOutput(v))
           copy.setReadOnly()
           new LongMap(copy)
         case _: Any =>
           (iterator ++ Iterator.single(kv)).toMap
       }
     }

     override def -(key: String): LongMap = {
       val copy = dataMap.copy()
       copy.remove(key)
       copy.setReadOnly()
       new LongMap(copy)
     }

     override def schema(): DataSchema = LongMap.SCHEMA

     override def data(): DataMap = dataMap

     override def copy(): DataTemplate[DataMap] = this
   }

   object LongMap {
     val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"long"}""").asInstanceOf[MapDataSchema]











     val empty = LongMap()

     def apply(elems: (String, Long)*): LongMap = {
       LongMap(elems.toMap)
     }

     def apply(map: Map[String, Long]): LongMap = {
       new LongMap(new DataMap(map.mapValues(coerceOutput).asJava))
     }

     def apply(dataMap: DataMap, conversion: DataConversion): LongMap = {
       new LongMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
     }

     def newBuilder = new DataBuilder()

     implicit val canBuildFrom = new CanBuildFrom[LongMap, (String, Long), LongMap] {
       def apply(from: LongMap) = new DataBuilder(from)
       def apply() = newBuilder
     }

     class DataBuilder(initial: LongMap) extends mutable.Builder[(String, Long), LongMap] {
       def this() = this(new LongMap(new DataMap()))

       val entries = new DataMap(initial.data())

       def +=(kv: (String, Long)): this.type = {
         val (key, value) = kv
         entries.put(key, coerceOutput(value))
         this
       }

       def clear() = {
         entries.clear()
       }

       def result() = {
         entries.setReadOnly()
         new LongMap(entries)
       }
     }

     private def coerceOutput(value: Long): AnyRef = {

           DataTemplateUtil.coerceInput(Long.box(value), classOf[java.lang.Long], classOf[java.lang.Long])

     }
   }
