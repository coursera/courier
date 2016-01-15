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

package org.coursera.courier.templates

import com.linkedin.data.DataComplex
import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.codec.JacksonDataCodec
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.TyperefDataSchema
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.RecordTemplate
import com.linkedin.data.template.UnionTemplate

import scala.reflect.ClassTag

object DataTemplates {

  object DataConversion extends Enumeration {
    val SetReadOnly = Value("SetReadOnly")
    val DeepCopy = Value("DeepCopy")
  }
  type DataConversion = DataConversion.Value

  def makeImmutable[T <: DataComplex](
      data: T, conversion: DataConversion): T = {
    conversion match {
      case DataConversion.DeepCopy =>
        val copy = data.copy()
        copy.makeReadOnly()
        copy.asInstanceOf[T]
      case DataConversion.SetReadOnly =>
        data.makeReadOnly()
        data
    }
  }

  private val dataCodec = new JacksonDataCodec

  def readDataMap(json: String): DataMap = {
    dataCodec.stringToMap(json)
  }

  def readDataList(json: String): DataList = {
    dataCodec.stringToList(json)
  }

  def writeDataMap(dataMap: DataMap): String = {
    dataCodec.mapToString(dataMap)
  }

  def writeDataList(dataList: DataList): String = {
    dataCodec.listToString(dataList)
  }

  /**
   * Dynamically instantiates a record template type from json.
   */
  def readRecord[T <: RecordTemplate](json: String)(implicit tag: ClassTag[T]): T = {
    val clazz = tag.runtimeClass.asInstanceOf[Class[T]]
    val dataMap = readDataMap(json)
    newDataMapTemplate(dataMap, clazz)
  }

  /**
   * Writes a record template to json.
   */
  def writeRecord[T <: RecordTemplate](template: T)(implicit tag: ClassTag[T]): String = {
    writeDataMap(template.data())
  }

  /**
   * Dynamically instantiates an array template type from json.
   */
  def readArray[T <: DataTemplate[DataList]](json: String)(implicit tag: ClassTag[T]): T = {
    val clazz = tag.runtimeClass.asInstanceOf[Class[T]]
    val dataList = readDataList(json)
    newDataListTemplate(dataList, clazz)
  }

  /**
   * Writes an array template to json.
   */
  def writeArray[T <: DataTemplate[DataList]](template: T)(implicit tag: ClassTag[T]): String = {
    writeDataList(template.data())
  }

  /**
   * Dynamically instantiates a map template type from json.
   */
  def readMap[T <: DataTemplate[DataMap]](json: String)(implicit tag: ClassTag[T]): T = {
    val clazz = tag.runtimeClass.asInstanceOf[Class[T]]
    val dataMap = readDataMap(json)
    newDataMapTemplate(dataMap, clazz)
  }

  /**
   * Writes a map template to json.
   */
  def writeMap[T <: DataTemplate[DataMap]](template: T)(implicit tag: ClassTag[T]): String = {
    writeDataMap(template.data())
  }

  /**
   * Dynamically instantiates a union template type from json.
   */
  def readUnion[T <: UnionTemplate](json: String)(implicit tag: ClassTag[T]): T = {
    val clazz = tag.runtimeClass.asInstanceOf[Class[T]]
    val dataMap = readDataMap(json)
    newUnionTemplate(dataMap, clazz)
  }

  /**
    * Writes a union template to json.
    */
  def writeUnion[T <: UnionTemplate](template: T)(implicit tag: ClassTag[T]): String = {
    template.data() match {
      case dataMap: DataMap =>
        writeDataMap(dataMap)
      case _: AnyRef =>
        throw new IllegalArgumentException("Null union values not supported by CourierSerializers")
    }
  }

  /**
    * For types declared within typeref, gets the schema of the typeref.
    *
    * For example, the typeref declaration:
    *
    * ```
    * typeref MyUnion = union[Alpha, Beta]
    * ```
    *
    * declares a union that will be generated as the class MyUnion.  This generated union class
    * will contain both a `SCHEMA` field to access the union schema (`union[Alpha, Beta]`) and a
    * `TYPEREF_SCHEMA` field to access the declaring typeref schema
    * (`typeref MyUnion = union[Alpha, Beta]`).
    *
    * @param clazz provides a Scala generated data binding class.
    * @return a typeref data schema.
    */
  def getDeclaringTyperefSchema(clazz: Class[_]): TyperefDataSchema = {
    getSchema(clazz, "TYPEREF_SCHEMA").asInstanceOf[TyperefDataSchema]
  }

  /**
    * Gets the schema of the Scala generated data binding class.
    *
    * @param clazz provides a Scala generated data binding class.
    * @return a data schema.
    */
  def getSchema(clazz: Class[_]): DataSchema = {
    getSchema(clazz, "SCHEMA")
  }

  /**
    * Gets the schema from companion object of the given Scala generated data binding class for
    * the given field of the companion object.
    *
    * @param clazz provides a Scala generated data binding class.
    * @param fieldName provides the name of the field of the companion object to get the schema
    *                  from.
    * @return a data schema.
    */
  private[this] def getSchema(clazz: Class[_], fieldName: String): DataSchema = {
    val companionInstance = companion(clazz)
    val companionClass = companionInstance.getClass
    companionClass.getDeclaredMethod(fieldName).invoke(companionInstance).asInstanceOf[DataSchema]
  }

  /**
    * Finds the companion object instance for a given Scala class.
    *
    * @param clazz provides the Scala class to get the companion object instance for.
    * @return a companion object instance.
    */
  private[this] def companion(clazz: Class[_]): AnyRef = {
    val companion = Class.forName(clazz.getName + "$")
    companion.getField("MODULE$").get(null)
  }

  private[this] def newDataMapTemplate[T <: DataTemplate[DataMap]](
      data: DataMap, clazz: Class[T]): T = {
    val companionInstance = companion(clazz)
    val applyMethod =
      companionInstance.getClass.getDeclaredMethod(
        "apply",
        classOf[DataMap],
        classOf[DataConversion])
    applyMethod.invoke(companionInstance, data, DataConversion.SetReadOnly).asInstanceOf[T]
  }

  private[this] def newDataListTemplate[T <: DataTemplate[DataList]](
      data: DataList, clazz: Class[T]): T = {
    val companionInstance = companion(clazz)
    val applyMethod =
      companionInstance.getClass.getDeclaredMethod(
        "apply",
        classOf[DataList],
        classOf[DataConversion])
    applyMethod.invoke(companionInstance, data, DataConversion.SetReadOnly).asInstanceOf[T]
  }

  private[this] def newUnionTemplate[T <: UnionTemplate](data: DataMap, clazz: Class[T]): T = {
    val companionInstance = companion(clazz)
    val applyMethod =
      companionInstance.getClass.getDeclaredMethod(
        "apply",
        classOf[DataMap],
        classOf[DataConversion])
    applyMethod.invoke(companionInstance, data, DataConversion.SetReadOnly).asInstanceOf[T]
  }
}
