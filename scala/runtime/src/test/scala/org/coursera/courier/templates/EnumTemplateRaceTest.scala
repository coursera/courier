/*
 Copyright 2020 Coursera Inc.

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

import org.junit.Test
import java.net.URL
import java.io.IOException
import java.nio.ByteBuffer

import com.linkedin.data.DataMap
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.schema.EnumDataSchema
import sun.misc.URLClassPath

class EnumTestBridge {
  def value = EnumTemplateTest.VALUE.name
  def withName = EnumTemplateTest.withName("VALUE").name
}

class EnumTemplateRaceTest() {

  /**
    * Make a separate class loader for a test, and load [[EnumTestBridge]].
    * The bug occurs while loading classes, hence the need for a class loader per test.
    * @return The [[EnumTestBridge]] class in the new class loader.
    */
  def createForeignClazz(): Class[_] = {
    val systemClassLoader = this.getClass.getClassLoader()

    val classLoader = new ClassLoader(systemClassLoader.getParent) {
      override def findClass(name: String): Class[_] = {
        val path = name.replace('.', '/').concat(".class")
        val resOpt = systemClassLoader.getResourceAsStream(path)
        Option(resOpt) match {
          case Some(res) =>
            try {
              val bytes = Stream.continually(res.read).takeWhile(_ != -1).map(_.toByte).toArray
              defineClass(name, ByteBuffer.wrap(bytes), null)
            } catch {
              case e: IOException => throw new ClassNotFoundException(name, e)
            }
          case None => throw new ClassNotFoundException(name)
        }
      }
      override def loadClass(name: String, resolve: Boolean) =
        super.loadClass(name, true)
    }
    classLoader.loadClass(new EnumTestBridge().getClass.getName)
  }

  class TestSetup() extends Race {
    private val bridgeClazz = createForeignClazz()
    def method(name: String) = bridgeClazz.getMethod(name)
    private val foreignObject = bridgeClazz.getConstructor().newInstance()

    def value(): String = s"""${method("value").invoke(foreignObject)}"""
    def withName(): String = s"""${method("withName").invoke(foreignObject)}"""
  }

  @Test
  def raceValueValue(): Unit = {
    val testSetup = new TestSetup()
    assert(testSetup.race(testSetup.value(), testSetup.value()))
  }
  @Test
  def raceValueWithName(): Unit = {
    val testSetup = new TestSetup()
    assert(testSetup.race(testSetup.value(), testSetup.withName()))
  }
  @Test
  def raceWithNameWithName(): Unit = {
    val testSetup = new TestSetup()
    assert(testSetup.race(testSetup.withName(), testSetup.withName()))
  }

}

/* The remaining test classes and objects resemble those created
   by Courier.
 */
sealed abstract class EnumTemplateTest(name: String,
                                       properties: Option[DataMap])
    extends ScalaEnumTemplateSymbol(name, properties) {}

object EnumTemplateTest extends ScalaEnumTemplate[EnumTemplateTest] {
  case object VALUE extends EnumTemplateTest("VALUE", properties("VALUE"))
  val SCHEMA =
    DataTemplateUtil.parseSchema("""
      { "type": "enum",
        "name": "EnumTypeB",
        "namespace": "org.coursera.courier.templates",
        "symbols": ["VALUE"]
      }""").asInstanceOf[EnumDataSchema]
  override def withName(s: String): EnumTemplateTest = {
    symbols.find(_.toString == s).get
  }
}
