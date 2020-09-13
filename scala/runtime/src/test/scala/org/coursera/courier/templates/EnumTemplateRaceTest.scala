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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean

import java.io.IOException

import com.linkedin.data.DataMap
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.schema.EnumDataSchema

import sun.misc.URLClassPath

class EnumTestBridge  {
  def value = EnumTemplateTest.VALUE.name
  def withName = EnumTemplateTest.withName("VALUE").name
}

class EnumTemplateRaceTest() {
  import java.lang.ClassLoader.registerAsParallelCapable
  /**
   * Make a separate class loader for a test, and load [[EnumTestBridge]].
   * The bug occurs while loading classes, hence the need for a class loader per test.
   * @return The [[EnumTestBridge]] class in the new class loader.
   */
  def createForeignClazz():Class[_] = {
    val path = System.getProperty("java.class.path")
    val urls:Array[URL] = path.split(":").map{x:String => new java.io.File(x).toURI.toURL}.toArray
    val urlClassPath = new URLClassPath(urls)
    val systemClassLoader = ClassLoader.getSystemClassLoader
    val extensionClassLoader = systemClassLoader.getParent

    val classLoader = new ClassLoader(extensionClassLoader){
      registerAsParallelCapable()
      override def   findClass(name:String):Class[_] = {

        val path = name.replace('.', '/').concat(".class")
        val resOpt = urlClassPath.getResource(path)
        Option(resOpt) match {
          case Some (res) =>
            try {
              Option(res.getByteBuffer) match {
                case Some(bb) => defineClass(name, bb, null)
                case None =>
                  val b = res.getBytes
                  defineClass(name, b, 0, b.length)
              }
            }
            catch {
              case e:IOException => throw new ClassNotFoundException(name, e)
            }

          case None => null
        }
      }
      override def loadClass(name:String, resolve:Boolean):Class[_] = super.loadClass(name, true)
    }
    classLoader.loadClass(new EnumTestBridge().getClass.getName)
  }

  class TestSetup() {

    private val startSignal = new CountDownLatch(1)
    private val logging = new AtomicBoolean(false)
    private val bridgeClazz  = createForeignClazz()
    def method (name:String) = bridgeClazz.getMethod(name)
    private val foreignObject = bridgeClazz.getConstructor().newInstance()


    def value():String = s"""${method("value").invoke(foreignObject)}"""
    def withName():String = s"""${method("withName").invoke(foreignObject)}"""

    def race( a : => Unit, b : => Unit):Boolean = {
      logging.set(true)
      val aThread = new Thread("A") {
        override def run() {
          a
        }
      }
      val bThread = new Thread("B") {
        override def run() {
          b
        }
      }
      aThread.start()
      bThread.start()
      aThread.join(1000)
      bThread.join(1000)
      val aAlive = aThread.isAlive
      val bAlive = bThread.isAlive
      if (aAlive) aThread.interrupt()
      if (bAlive) bThread.interrupt()
      ! (aAlive || bAlive)
    }
  }
  @Test
  def raceValueValue():Unit = {
    val testSetup = new TestSetup()
    assert(testSetup.race(testSetup.value(), testSetup.value()))
  }
  @Test
  def raceValueWithName():Unit = {
    val testSetup = new TestSetup()
    assert(testSetup.race(testSetup.value(), testSetup.withName()))
  }
  @Test
  def raceWithNameWithName():Unit = {
    val testSetup = new TestSetup()
    assert(testSetup.race(testSetup.withName(), testSetup.withName()))
  }

}

/* The remaining test classes and objects resemble those created
   by Courier.
 */
sealed abstract class EnumTemplateTest(name: String, properties: Option[DataMap])
  extends ScalaEnumTemplateSymbol(name, properties) {
}

object EnumTemplateTest extends ScalaEnumTemplate[EnumTemplateTest] {
  case object VALUE extends EnumTemplateTest("VALUE", properties("VALUE"))
  val SCHEMA = DataTemplateUtil.parseSchema(
    """
      { "type": "enum",
        "name": "EnumTypeB",
        "namespace": "org.coursera.courier.templates",
        "symbols": ["VALUE"]
      }""").asInstanceOf[EnumDataSchema]
  override def withName(s: String): EnumTemplateTest = {
    symbols.find(_.toString == s).get
  }
}
