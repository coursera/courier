package org.coursera.courier.fixture.generator

import com.linkedin.data.DataList
import com.linkedin.data.DataMap
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.RecordDataSchema.Field
import com.linkedin.data.schema.TyperefDataSchema
import com.linkedin.data.schema.UnionDataSchema
import com.linkedin.data.template.DataTemplate
import org.coursera.courier.templates.ScalaEnumTemplateSymbol
import org.coursera.courier.templates.ScalaUnionTemplate

import scala.collection.JavaConverters._
import scala.reflect.ClassTag

object GeneratorUtil {

  def anyToData(input: Any): AnyRef = {
    input match {
      case Some(value) => anyToData(value)
      case None => None
      case data: DataMap => data
      case list: DataList => list
      case template: DataTemplate[_] => anyToData(template.data())
      case enumValue: ScalaEnumTemplateSymbol => enumValue.toString()
      case int: Int => Int.box(int)
      case long: Long => Long.box(long)
      case float: Float => Float.box(float)
      case double: Double => Double.box(double)
      case boolean: Boolean => Boolean.box(boolean)
      case _ => throw new IllegalArgumentException(s"Non-data type for value $input.")
    }
  }

  def wrapUnionMember(obj: Any): AnyRef = {
    obj match {
      case unionMember: ScalaUnionTemplate => unionMember.data()
      case _ =>
        val dataMap = new DataMap()
        dataMap.put(obj.getClass.getName, anyToData(obj))
        dataMap
    }
  }

  def getUnionSchema(schema: DataSchema): Option[UnionDataSchema] = {
    schema match {
      case typeref: TyperefDataSchema => getUnionSchema(typeref.getRef)
      case union: UnionDataSchema => Some(union)
      case _ => None
    }
  }
}
