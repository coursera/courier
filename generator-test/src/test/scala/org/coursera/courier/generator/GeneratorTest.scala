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

package org.coursera.courier.generator

import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream

import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.codec.JacksonDataCodec
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.JacksonDataTemplateCodec
import com.linkedin.data.template.PrettyPrinterJacksonDataTemplateCodec
import com.linkedin.pegasus.generator.TemplateSpecGenerator
import org.coursera.courier.generator.specs.Definition
import org.coursera.courier.generator.twirl.TwirlDataTemplateGenerator
import org.scalatest.FunSuite
import org.scalatest.junit.AssertionsForJUnit
import org.scalatest.junit.JUnitSuite

abstract class GeneratorTest extends JUnitSuite with AssertionsForJUnit {

  def printJson(dataTemplate: DataTemplate[DataMap]): Unit = printJson(dataTemplate.data)

  def printJson(dataMap: DataMap): Unit = println(mapToJson(dataMap))

  def assertJson(left: DataTemplate[DataMap], right: String): Unit = {
    val leftMap = readJsonToMap(mapToJson(left.data()))
    val rightMap = readJsonToMap(right)
    assert(leftMap === rightMap)
  }

  def roundTrip(complex: DataMap): DataMap = {
    readJsonToMap(mapToJson(complex))
  }

  def roundTrip(complex: DataList): DataList = {
    readJsonToList(listToJson(complex))
  }

  private val prettyPrinter = new PrettyPrinterJacksonDataTemplateCodec
  private val codec = new JacksonDataTemplateCodec
  private val dataCodec = new JacksonDataCodec

  private def mapToJson(dataTemplate: DataTemplate[DataMap]): String = mapToJson(dataTemplate.data)

  private def listToJson(dataTemplate: DataTemplate[DataList]): String = {
    listToJson(dataTemplate.data)
  }

  private def mapToJson(dataMap: DataMap): String = prettyPrinter.mapToString(dataMap)

  private def listToJson(dataList: DataList): String = prettyPrinter.listToString(dataList)

  private def readJsonToMap(string: String): DataMap = dataCodec.stringToMap(string)

  private def readJsonToList(string: String): DataList = dataCodec.stringToList(string)
}
