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

import sun.misc.URLClassPath

class TestBridge {
  def value = TestCompanion.VALUE.toString
  def withName = TestCompanion.withName("VALUE").toString
}

/**
  * This test shows that the deadlock tested for in [[EnumTemplateRaceTest]]
  * can be reproduced with relatively simple scala only, with no dependence
  * on courier or courscala libraries. This deadlock uses two lazy values and
  * a companion class initialization lock.
  */
class CompanionRaceTest() {

  /**
    * Make a separate class loader for a test, and load the [[TestBridge]] class.
    * The bug occurs while loading classes, hence the need for a class loader per test.
    * @return The TestBridge class in the new class loader.
    */
  def createForeignClazz(): Class[_] = {
    val path = System.getProperty("java.class.path")
    val urls: Array[URL] = path
      .split(":")
      .map { x: String =>
        new java.io.File(x).toURI.toURL
      }
    val urlClassPath = new URLClassPath(urls)
    val systemClassLoader = ClassLoader.getSystemClassLoader
    val extensionClassLoader = systemClassLoader.getParent

    val classLoader = new ClassLoader(extensionClassLoader) {
      override def findClass(name: String): Class[_] = {

        val path = name.replace('.', '/').concat(".class")
        val resOpt = urlClassPath.getResource(path)
        Option(resOpt) match {
          case Some(res) =>
            try {
              Option(res.getByteBuffer) match {
                case Some(bb) => defineClass(name, bb, null)
                case None =>
                  val b = res.getBytes
                  defineClass(name, b, 0, b.length)
              }
            } catch {
              case e: IOException => throw new ClassNotFoundException(name, e)
            }

          case None => null
        }
      }
      override def loadClass(name: String, resolve: Boolean) =
        super.loadClass(name, true)
    }
    classLoader.loadClass(new TestBridge().getClass.getName)
  }

  class TestSetup() extends Race {

    private val bridgeClazz = createForeignClazz()
    def method(name: String) = bridgeClazz.getMethod(name)
    private var foreignObject = bridgeClazz.getConstructor().newInstance()

    def changeInstance(): Unit = {
      foreignObject = bridgeClazz.getConstructor().newInstance()
    }

    def value(): String = s"""${method("value").invoke(foreignObject)}"""
    def withName(): String = s"""${method("withName").invoke(foreignObject)}"""

  }
  @Test
  def valueBeforeRace(): Unit = {
    val testSetup = new TestSetup()
    testSetup.value()
    testSetup.changeInstance()
    assert(testSetup.race(testSetup.value(), testSetup.withName()))
  }
  @Test
  def raceToDeadlock(): Unit = {
    val testSetup = new TestSetup()
    assert(testSetup.race(testSetup.value(), testSetup.withName()))
  }

  /**
    * This is [[CompanionRaceTest.raceToDeadlock]] modified to avoid the race
    * by running one of the halves first, which initializes all the classes,
    * we then change the instance in `testSetup` and run the race, which does
    * not deadlock - showing that the class initialization lock is involved in
    * the deadlock.
    */
  @Test
  def withNameBeforeRace(): Unit = {
    val testSetup = new TestSetup()
    testSetup.withName()
    testSetup.changeInstance()
    assert(testSetup.race(testSetup.value(), testSetup.withName()))
  }

}

abstract class TestCompanion(properties: Option[String]) {}

object TestCompanion {
  // Set this to false to exhibit the deadlock.
  private val WORKAROUND = true
  lazy val symbols: Set[TestCompanion] = {
    Thread.sleep(10)
    findSymbols
  }
  def findSymbols: Set[TestCompanion] = Set(VALUE)
  case object VALUE extends TestCompanion(properties)
  val SCHEMA = Set.empty[String]
  def withName(s: String): TestCompanion = {
    symbols.find(_.toString == s).get
  }

  def properties: Option[String] =
    // Implementation note: using a lazy field can result in deadlock.
    if (WORKAROUND) {
      // compute lazy field without lock.
      optionProperties match {
        case Some(lazilyComputed) => lazilyComputed
        case None                 =>
          // This can be entered by multiple racing threads.
          val lazilyComputed = SCHEMA.headOption
          // The last thread wins, but the result is always the same.
          optionProperties = Some(lazilyComputed)
          lazilyComputed
      }
    } else
      lazyProperties

  lazy val lazyProperties = {
    Thread.sleep(10)
    SCHEMA.headOption
  }

  /**
    * The value of [[TestCompanion.properties]]
    */
  private var optionProperties: Option[Option[String]] = None

  protected def properties(symbolName: String): Option[String] = {
    properties
  }
}
