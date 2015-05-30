

package org.coursera.enums

import javax.annotation.Generated

import com.linkedin.data.ByteString
import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.template.DataTemplate
import org.coursera.courier.data.DataTemplates
import org.coursera.courier.data.DataTemplates.DataConversion

import scala.collection.GenTraversable
import scala.collection.JavaConverters._
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable
import com.linkedin.data.template.Custom



@Generated(value = Array("FruitsArray"), comments="Courier Data Template.", date = "Sat May 30 17:14:08 PDT 2015")
final class FruitsArray(private val dataList: DataList)
  extends IndexedSeq[org.coursera.enums.Fruits.Fruits]
  with Product
  with GenTraversable[org.coursera.enums.Fruits.Fruits]
  with DataTemplate[DataList] {

  override def length: Int = dataList.size()

  
  private[this] def lookup(idx: Int) = {
    
        Fruits.fromString(dataList.get(idx).asInstanceOf[String])
      
    
    
    
  }

  override def apply(idx: Int): org.coursera.enums.Fruits.Fruits = lookup(idx)

  override def productElement(n: Int): Any = dataList.get(n)
  override def productArity: Int = dataList.size()

  override def schema(): DataSchema = FruitsArray.SCHEMA

  override def data(): DataList = dataList
  override def copy(): DataTemplate[DataList] = this
}

object FruitsArray {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"array","items":{"type":"enum","name":"Fruits","namespace":"org.coursera.enums","symbols":["APPLE","BANANA","ORANGE","PINEAPPLE"],"symbolDocs":{"APPLE":"An Apple."}}}""").asInstanceOf[ArrayDataSchema]

  
  

  val empty = FruitsArray()

  def apply(elems: org.coursera.enums.Fruits.Fruits*): FruitsArray = {
    new FruitsArray(new DataList(elems.map(coerceOutput).toList.asJava))
  }

  def apply(collection: Traversable[org.coursera.enums.Fruits.Fruits]): FruitsArray = {
    new FruitsArray(new DataList(collection.map(coerceOutput).toList.asJava))
  }

  def apply(dataList: DataList, conversion: DataConversion): FruitsArray = {
    new FruitsArray(DataTemplates.makeImmutable(dataList, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[FruitsArray, org.coursera.enums.Fruits.Fruits, FruitsArray] {
    def apply(from: FruitsArray) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: FruitsArray) extends mutable.Builder[org.coursera.enums.Fruits.Fruits, FruitsArray] {
    def this() = this(new FruitsArray(new DataList()))

    val elems = new DataList(initial.data())

    def +=(x: org.coursera.enums.Fruits.Fruits): this.type = {
      elems.add(coerceOutput(x))
      this
    }

    def clear() = {
      elems.clear()
    }

    def result() = {
      elems.setReadOnly()
      new FruitsArray(elems)
    }
  }

  private def coerceOutput(value: org.coursera.enums.Fruits.Fruits): AnyRef = {
    
        value.toString
      
    
    
    
  }
}


