



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

import com.linkedin.data.ByteString
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

   @Generated(value = Array("BytesMap"), comments="Courier Data Template.", date = "Sat Jun 20 09:28:24 PDT 2015")
    final class BytesMap(private val dataMap: DataMap)
     extends immutable.Iterable[(String, ByteString)]
     with Map[String, ByteString]
     with immutable.MapLike[String, ByteString, immutable.Map[String, ByteString]]
     with DataTemplate[DataMap] {
     import org.coursera.courier.data.BytesMap._


     private[this] lazy val map = dataMap.asScala.map { case (k, v) => k -> coerceInput(v) }.toMap

     private[this] def coerceInput(any: AnyRef): ByteString = {

           DataTemplateUtil.coerceOutput(any, classOf[com.linkedin.data.ByteString])

     }

     override def get(key: String): Option[ByteString] = map.get(key)

     override def iterator: Iterator[(String, ByteString)] = map.iterator

     override def +[F >: ByteString](kv: (String, F)): Map[String, F] = {
       val (key, value) = kv
       value match {
         case v: ByteString =>
           val copy = dataMap.copy()
           copy.put(key, coerceOutput(v))
           copy.setReadOnly()
           new BytesMap(copy)
         case _: Any =>
           (iterator ++ Iterator.single(kv)).toMap
       }
     }

     override def -(key: String): BytesMap = {
       val copy = dataMap.copy()
       copy.remove(key)
       copy.setReadOnly()
       new BytesMap(copy)
     }

     override def schema(): DataSchema = BytesMap.SCHEMA

     override def data(): DataMap = dataMap

     override def copy(): DataTemplate[DataMap] = this
   }

   object BytesMap {
     val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"bytes"}""").asInstanceOf[MapDataSchema]











     val empty = BytesMap()

     def apply(elems: (String, ByteString)*): BytesMap = {
       BytesMap(elems.toMap)
     }

     def apply(map: Map[String, ByteString]): BytesMap = {
       new BytesMap(new DataMap(map.mapValues(coerceOutput).asJava))
     }

     def apply(dataMap: DataMap, conversion: DataConversion): BytesMap = {
       new BytesMap(DataTemplates.makeImmutable(dataMap, SCHEMA, conversion))
     }

     def newBuilder = new DataBuilder()

     implicit val canBuildFrom = new CanBuildFrom[BytesMap, (String, ByteString), BytesMap] {
       def apply(from: BytesMap) = new DataBuilder(from)
       def apply() = newBuilder
     }

     class DataBuilder(initial: BytesMap) extends mutable.Builder[(String, ByteString), BytesMap] {
       def this() = this(new BytesMap(new DataMap()))

       val entries = new DataMap(initial.data())

       def +=(kv: (String, ByteString)): this.type = {
         val (key, value) = kv
         entries.put(key, coerceOutput(value))
         this
       }

       def clear() = {
         entries.clear()
       }

       def result() = {
         entries.setReadOnly()
         new BytesMap(entries)
       }
     }

     private def coerceOutput(value: ByteString): AnyRef = {

           DataTemplateUtil.coerceInput(value, classOf[com.linkedin.data.ByteString], classOf[com.linkedin.data.ByteString])

     }
   }
