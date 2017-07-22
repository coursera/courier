package org.coursera.records.test

import javax.annotation.Generated

import com.linkedin.data.DataMap
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.UnionDataSchema
import com.linkedin.data.schema.TyperefDataSchema
import com.linkedin.data.template.Custom
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.RecordTemplate
import com.linkedin.data.template.RequiredFieldNotPresentException
import com.linkedin.data.template.TemplateOutputCastException
import com.linkedin.data.template.UnionTemplate
import org.coursera.courier.templates.DataTemplates
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.courier.templates.ScalaRecordTemplate
import org.coursera.courier.templates.ScalaArrayTemplate
import org.coursera.courier.templates.ScalaUnionTemplate
import org.coursera.courier.templates.ScalaEnumTemplate
import org.coursera.courier.templates.ScalaEnumTemplateSymbol
import org.coursera.courier.companions.UnionCompanion
import org.coursera.courier.companions.UnionMemberCompanion
import org.coursera.courier.companions.UnionWithTyperefCompanion
import org.coursera.courier.companions.RecordCompanion
import org.coursera.courier.companions.ArrayCompanion
import org.coursera.courier.companions.MapCompanion
import scala.runtime.ScalaRunTime
import com.linkedin.data.template.DataTemplateUtil
import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.data.schema.UnionDataSchema
import com.linkedin.data.schema.TyperefDataSchema
import com.linkedin.data.schema.DataSchemaConstants
import com.linkedin.data.ByteString
import com.linkedin.data.DataList
import scala.collection.JavaConverters._
import scala.collection.immutable.Iterable
import scala.collection.immutable.MapLike
import scala.collection.mutable.Builder
import scala.collection.generic.CanBuildFrom
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.data.schema.ArrayDataSchema
import scala.collection.GenTraversable
import org.coursera.courier.codecs.InlineStringCodec
import org.coursera.courier.coercers.SingleElementCaseClassCoercer
import scala.language.implicitConversions

@Generated(value = Array("WithPrimitives"), comments = "Courier Data Template.")
final class WithPrimitives private (private val dataMap: DataMap)
  extends ScalaRecordTemplate(dataMap, WithPrimitives.SCHEMA) with Product {
  WithPrimitives // force static initialization
  import WithPrimitives._
  
  lazy val intField: Int = obtainDirect(WithPrimitives.Fields.intField, classOf[java.lang.Integer])
  
  lazy val longField: Long = obtainDirect(WithPrimitives.Fields.longField, classOf[java.lang.Long])
  
  lazy val floatField: Float = obtainDirect(WithPrimitives.Fields.floatField, classOf[java.lang.Float])
  
  lazy val doubleField: Double = obtainDirect(WithPrimitives.Fields.doubleField, classOf[java.lang.Double])
  
  lazy val booleanField: Boolean = obtainDirect(WithPrimitives.Fields.booleanField, classOf[java.lang.Boolean])
  
  lazy val stringField: String = obtainDirect(WithPrimitives.Fields.stringField, classOf[java.lang.String])
  
  lazy val bytesField: ByteString = obtainDirect(WithPrimitives.Fields.bytesField, classOf[com.linkedin.data.ByteString])
  
  private def setFields(intField: Int, longField: Long, floatField: Float, doubleField: Double, booleanField: Boolean, stringField: String, bytesField: ByteString): Unit = {
    putDirect(WithPrimitives.Fields.intField, classOf[java.lang.Integer], Int.box(intField))
    putDirect(WithPrimitives.Fields.longField, classOf[java.lang.Long], Long.box(longField))
    putDirect(WithPrimitives.Fields.floatField, classOf[java.lang.Float], Float.box(floatField))
    putDirect(WithPrimitives.Fields.doubleField, classOf[java.lang.Double], Double.box(doubleField))
    putDirect(WithPrimitives.Fields.booleanField, classOf[java.lang.Boolean], Boolean.box(booleanField))
    putDirect(WithPrimitives.Fields.stringField, classOf[java.lang.String], stringField)
    putDirect(WithPrimitives.Fields.bytesField, classOf[com.linkedin.data.ByteString], bytesField)
  }
  
  override val productArity: Int = 7
  
  override def productElement(n: Int): Any =
  n match {
    case 0 => intField
    case 1 => longField
    case 2 => floatField
    case 3 => doubleField
    case 4 => booleanField
    case 5 => stringField
    case 6 => bytesField
    case _ => throw new IndexOutOfBoundsException(n.toString)
  }
  
  override val productPrefix: String = "WithPrimitives"
  
  override def canEqual(that: Any): Boolean = that.isInstanceOf[WithPrimitives]
  
  override def hashCode: Int = ScalaRunTime._hashCode(this)
  
  override def equals(that: Any): Boolean = canEqual(that) && ScalaRunTime._equals(this, that)
  
  override def toString: String = ScalaRunTime._toString(this)
  
  override def copy(): WithPrimitives = this
  
  def copy(intField: Int = this.intField, longField: Long = this.longField, floatField: Float = this.floatField, doubleField: Double = this.doubleField, booleanField: Boolean = this.booleanField, stringField: String = this.stringField, bytesField: ByteString = this.bytesField): WithPrimitives = {
    val $dataMap = new DataMap
    val $result = new WithPrimitives($dataMap)
    $result.setFields(intField, longField, floatField, doubleField, booleanField, stringField, bytesField)
    $dataMap.makeReadOnly()
    $result
  }
  
  override def copy(dataMap: DataMap, conversion: DataConversion): WithPrimitives = {
    new WithPrimitives(DataTemplates.makeImmutable(dataMap, conversion))
  }
  
  override def clone(): WithPrimitives = this
}

object WithPrimitives extends RecordCompanion[WithPrimitives] {
  val SCHEMA = DataTemplateUtil.parseSchema("""{"type":"record","name":"WithPrimitives","namespace":"org.coursera.records.test","fields":[{"name":"intField","type":"int"},{"name":"longField","type":"long"},{"name":"floatField","type":"float"},{"name":"doubleField","type":"double"},{"name":"booleanField","type":"boolean"},{"name":"stringField","type":"string"},{"name":"bytesField","type":"bytes"}]}""").asInstanceOf[RecordDataSchema]
  
  implicit val withPrimitivesCompanion: RecordCompanion[WithPrimitives] = this
  
  private object Fields {
    val intField = WithPrimitives.SCHEMA.getField("intField")
    val longField = WithPrimitives.SCHEMA.getField("longField")
    val floatField = WithPrimitives.SCHEMA.getField("floatField")
    val doubleField = WithPrimitives.SCHEMA.getField("doubleField")
    val booleanField = WithPrimitives.SCHEMA.getField("booleanField")
    val stringField = WithPrimitives.SCHEMA.getField("stringField")
    val bytesField = WithPrimitives.SCHEMA.getField("bytesField")
  }
  
  def apply(intField: Int, longField: Long, floatField: Float, doubleField: Double, booleanField: Boolean, stringField: String, bytesField: ByteString): WithPrimitives = {
    val $dataMap = new DataMap
    val $result = new WithPrimitives($dataMap)
    $result.setFields(intField, longField, floatField, doubleField, booleanField, stringField, bytesField)
    $dataMap.makeReadOnly()
    $result
  }
  
  override def build(dataMap: DataMap, conversion: DataConversion): WithPrimitives = {
    new WithPrimitives(DataTemplates.makeImmutable(dataMap, conversion))
  }
  
  def unapply(record: WithPrimitives): Option[(Int, Long, Float, Double, Boolean, String, ByteString)] = {
    try {
      Some((record.intField, record.longField, record.floatField, record.doubleField, record.booleanField, record.stringField, record.bytesField))
    } catch {
      case cast: TemplateOutputCastException => None
      case notPresent: RequiredFieldNotPresentException => None
    }
  }
  
}
