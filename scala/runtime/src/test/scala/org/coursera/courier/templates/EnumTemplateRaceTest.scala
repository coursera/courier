package org.coursera.courier.templates

import org.junit.Test
import java.net.URL
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean

import scala.language.existentials
import java.io.IOException

import com.linkedin.data.DataMap
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.schema.EnumDataSchema

import scala.language.reflectiveCalls
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import sun.misc.URLClassPath

trait TestTrait {
  def value:String
  def withName:String

}
class TestClassA extends TestTrait {
  def value = EnumTypeA.VALUE1.name
  def withName = EnumTypeA.withName("VALUE2").name
}
class TestClassB extends TestTrait {
  def value = EnumTypeB.VALUE3.name
  def withName = EnumTypeB.withName("VALUE4").name
}

@RunWith(classOf[Parameterized])
class EnumTemplateRaceTest(name:String) {
  import java.lang.ClassLoader.registerAsParallelCapable

  def createForeignClazz(startSignalEnabled:ThreadLocal[Int], startSignal:CountDownLatch):(
    Class[_], Class[_], Class[_]) = {
    val path = System.getProperty("java.class.path")
    val urls:Array[URL] = path.split(":").map{x:String => new java.io.File(x).toURI.toURL}.toArray
    val urlClassPath = new URLClassPath(urls)
    val systemClassLoader = ClassLoader.getSystemClassLoader
    val extensionClassLoader = systemClassLoader.getParent

    val classLoader = new ClassLoader(extensionClassLoader){
      registerAsParallelCapable()
      override def   findClass(name:String):Class[_] = {
        if (startSignalEnabled.get() == 0) {
          startSignal.await()
        }
        startSignalEnabled.set(startSignalEnabled.get()-1)

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
      override def loadClass(name:String, resolve:Boolean) = super.loadClass(name, true)
    }
    (classLoader.loadClass(new TestClassA().getClass.getName),
      classLoader.loadClass(new TestClassB().getClass.getName),
      classLoader.loadClass(classOf[TestTrait].getName))
  }

  class TestSetup(parallel:Boolean = true) {

    private val startSignal = new CountDownLatch(1)
    private val logging = new AtomicBoolean(false)
    private val waitForStartSignal = new ThreadLocal[Int]()
    waitForStartSignal.set(-1)
    private val (clazzA, clazzB, testTrait) = createForeignClazz(waitForStartSignal, startSignal)
    def method (name:String) = testTrait.getMethod(name)
    private val foreignObjectA = clazzA.getConstructor().newInstance()
    private val foreignObjectB = clazzB.getConstructor().newInstance()
    private val foreignObjects = List(foreignObjectA, foreignObjectB)


    def value(i:Int):String = s"""${method("value").invoke(foreignObjects(i))}"""
    def withName(i:Int):String = s"""${method("withName").invoke(foreignObjects(i))}"""

    def race( a : => Unit, b : => Unit, useStartSignal:(Int,Int)=(-1,-1)):Boolean = {
      logging.set(true)
      val aThread = new Thread("A") {
        waitForStartSignal.set(useStartSignal._1)
        override def run() {
          a
        }
      }
      val bThread = new Thread("B") {
        waitForStartSignal.set(useStartSignal._2)
        override def run() {
          b
        }
      }
      aThread.start()
      bThread.start()
      Thread.sleep(1)
      startSignal.countDown()
      aThread.join(10000)
      bThread.join(10000)
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
    testSetup.value(0)
    testSetup.withName(0)
    assert(testSetup.race(testSetup.value(1), testSetup.value(1)))
  }
  @Test
  def raceValueWithName():Unit = {
    val testSetup = new TestSetup()
    testSetup.value(0)
    testSetup.withName(0)
    assert(testSetup.race(testSetup.value(1), testSetup.withName(1)))
  }
  @Test
  def raceWithNameWithName():Unit = {
    val testSetup = new TestSetup()
    testSetup.value(0)
    testSetup.withName(0)
    assert(testSetup.race(testSetup.value(1), testSetup.withName(1)))
  }

}

object EnumTemplateRaceTest {
  // This test is for a heisenbug. If it recurs, please increase this value.
  val NUMBER_OF_RUNS = 10
  @Parameters(name = "{0}")
  def parameters: java.util.Collection[Array[AnyRef]] = {
    val testCases = new java.util.ArrayList[Array[AnyRef]]
    (1 to NUMBER_OF_RUNS).foreach { i =>
      testCases.add(Array[AnyRef](i.toString))
    }
    testCases
  }
}

sealed abstract class EnumTypeA(name: String, properties: Option[DataMap])
  extends ScalaEnumTemplateSymbol(name, properties) {
}

object EnumTypeA extends ScalaEnumTemplate[EnumTypeA] {
  case object VALUE1 extends EnumTypeA("VALUE1", properties("VALUE1"))
  case object VALUE2 extends EnumTypeA("VALUE2", properties("VALUE2"))
  val SCHEMA = DataTemplateUtil.parseSchema(
    """
      { "type": "enum",
        "name": "EnumTypeA",
        "namespace": "org.coursera.courier.templates",
        "symbols": ["VALUE1", "VALUE2"]
      }""").asInstanceOf[EnumDataSchema]
  override def withName(s: String): EnumTypeA = {
    symbols.find(_.toString == s).get
  }
}

sealed abstract class EnumTypeB(name: String, properties: Option[DataMap])
  extends ScalaEnumTemplateSymbol(name, properties) {
}

object EnumTypeB extends ScalaEnumTemplate[EnumTypeB] {
  case object VALUE3 extends EnumTypeB("VALUE3", properties("VALUE3"))
  case object VALUE4 extends EnumTypeB("VALUE4", properties("VALUE4"))
  //case object $UNKNOWN extends EnumTypeB("$UNKNOWN", None)
  val SCHEMA = DataTemplateUtil.parseSchema(
    """
      { "type": "enum",
        "name": "EnumTypeB",
        "namespace": "org.coursera.courier.templates",
        "symbols": ["VALUE3","VALUE4"]
      }""").asInstanceOf[EnumDataSchema]
  override def withName(s: String): EnumTypeB = {
    symbols.find(_.toString == s).get
  }
}
