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

import com.linkedin.data.DataList
import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.DataTemplateUtil
import org.coursera.courier.templates.DataTemplates
import org.coursera.courier.templates.DataTemplates.DataConversion

import scala.collection.GenTraversable
import scala.collection.JavaConverters._
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable

@Generated(value = Array("BooleanArray"), comments = "Courier Data Template.", date = "Sat May 30 12:21:42 PDT 2015")
final class BooleanArray(private val dataList: DataList)
  extends IndexedSeq[Boolean]
  with Product
  with GenTraversable[Boolean]
  with DataTemplate[DataList] {

  override def length: Int = dataList.size()

  private[this] def lookup(idx: Int) = {

    dataList.get(idx).asInstanceOf[java.lang.Boolean]

  }

  override def apply(idx: Int): Boolean = lookup(idx)

  override def productElement(n: Int): Any = dataList.get(n)

  override def productArity: Int = dataList.size()

  override def schema(): DataSchema = BooleanArray.SCHEMA

  override def data(): DataList = dataList

  override def copy(): DataTemplate[DataList] = this
}

object BooleanArray {
  val SCHEMA = DataTemplateUtil.parseSchema( """{"type":"array","items":"boolean"}""").asInstanceOf[ArrayDataSchema]

  val empty = BooleanArray()

  def apply(elems: Boolean*): BooleanArray = {
    new BooleanArray(new DataList(elems.map(coerceOutput).toList.asJava))
  }

  def apply(collection: Traversable[Boolean]): BooleanArray = {
    new BooleanArray(new DataList(collection.map(coerceOutput).toList.asJava))
  }

  def apply(dataList: DataList, conversion: DataConversion): BooleanArray = {
    new BooleanArray(DataTemplates.makeImmutable(dataList, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[BooleanArray, Boolean, BooleanArray] {
    def apply(from: BooleanArray) = new DataBuilder(from)

    def apply() = newBuilder
  }

  class DataBuilder(initial: BooleanArray) extends mutable.Builder[Boolean, BooleanArray] {
    def this() = this(new BooleanArray(new DataList()))

    val elems = new DataList(initial.data())

    def +=(x: Boolean): this.type = {
      elems.add(coerceOutput(x))
      this
    }

    def clear() = {
      elems.clear()
    }

    def result() = {
      elems.setReadOnly()
      new BooleanArray(elems)
    }
  }

  private def coerceOutput(value: Boolean): AnyRef = {

    Boolean.box(value)

  }
}


