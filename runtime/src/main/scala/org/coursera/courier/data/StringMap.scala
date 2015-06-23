



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

   @Generated(value = Array("StringMap"), comments="Courier Data Template.", date = "Sat Jun 20 09:28:24 PDT 2015")
    final class StringMap(private val dataMap: DataMap)
     extends immutable.Iterable[(String, String)]
     with Map[String, String]
     with immutable.MapLike[String, String, immutable.Map[String, String]]
     with DataTemplate[DataMap] {
     import org.coursera.courier.data.StringMap._


     private[this] lazy val map = dataMap.asScala.map { case (k, v) => k -> coerceInput(v) }.toMap

     private[this] def coerceInput(any: AnyRef): String = {

           DataTemplateUtil.coerceOutput(any, classOf[java.lang.String])

     }

     override def get(key: String): Option[String] = map.get(key)

     override def iterator: Iterator[(String, String)] = map.iterator

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

     override def copy(): DataTemplate[DataMap] = this
   }

   object StringMap {
     val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"map","values":"string"}""").asInstanceOf[MapDataSchema]











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

           DataTemplateUtil.coerceInput(value, classOf[java.lang.String], classOf[java.lang.String])

     }
   }
