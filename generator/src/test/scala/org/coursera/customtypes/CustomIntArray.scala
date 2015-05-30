

package org.coursera.customtypes

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



@Generated(value = Array("CustomIntArray"), comments="Courier Data Template.", date = "Sat May 30 14:21:22 PDT 2015")
final class CustomIntArray(private val dataList: DataList)
  extends IndexedSeq[org.coursera.courier.generator.customtypes.CustomInt]
  with Product
  with GenTraversable[org.coursera.courier.generator.customtypes.CustomInt]
  with DataTemplate[DataList] {

  override def length: Int = dataList.size()

  
  private[this] def lookup(idx: Int) = {
    
        DataTemplateUtil.coerceOutput(dataList.get(idx), classOf[org.coursera.courier.generator.customtypes.CustomInt])
      
    
    
    
  }

  override def apply(idx: Int): org.coursera.courier.generator.customtypes.CustomInt = lookup(idx)

  override def productElement(n: Int): Any = dataList.get(n)
  override def productArity: Int = dataList.size()

  override def schema(): DataSchema = CustomIntArray.SCHEMA

  override def data(): DataList = dataList
  override def copy(): DataTemplate[DataList] = this
}

object CustomIntArray {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"array","items":{"type":"typeref","name":"CustomInt","namespace":"org.coursera.customtypes","ref":"int","java":{"coercerClass":"org.coursera.courier.generator.customtypes.CustomIntCoercer","class":"org.coursera.courier.generator.customtypes.CustomInt"}}}""").asInstanceOf[ArrayDataSchema]

  val empty = CustomIntArray()

  def apply(elems: org.coursera.courier.generator.customtypes.CustomInt*): CustomIntArray = {
    new CustomIntArray(new DataList(elems.map(coerceOutput).toList.asJava))
  }

  def apply(collection: Traversable[org.coursera.courier.generator.customtypes.CustomInt]): CustomIntArray = {
    new CustomIntArray(new DataList(collection.map(coerceOutput).toList.asJava))
  }

  def apply(dataList: DataList, conversion: DataConversion): CustomIntArray = {
    new CustomIntArray(DataTemplates.makeImmutable(dataList, SCHEMA, conversion))
  }

  def newBuilder = new DataBuilder()

  implicit val canBuildFrom = new CanBuildFrom[CustomIntArray, org.coursera.courier.generator.customtypes.CustomInt, CustomIntArray] {
    def apply(from: CustomIntArray) = new DataBuilder(from)
    def apply() = newBuilder
  }

  class DataBuilder(initial: CustomIntArray) extends mutable.Builder[org.coursera.courier.generator.customtypes.CustomInt, CustomIntArray] {
    def this() = this(new CustomIntArray(new DataList()))

    val elems = new DataList(initial.data())

    def +=(x: org.coursera.courier.generator.customtypes.CustomInt): this.type = {
      elems.add(coerceOutput(x))
      this
    }

    def clear() = {
      elems.clear()
    }

    def result() = {
      elems.setReadOnly()
      new CustomIntArray(elems)
    }
  }

  private def coerceOutput(value: org.coursera.courier.generator.customtypes.CustomInt): AnyRef = {
    
        DataTemplateUtil.coerceInput(value, classOf[org.coursera.courier.generator.customtypes.CustomInt], classOf[Int])
      
    
    
    
  }
}


