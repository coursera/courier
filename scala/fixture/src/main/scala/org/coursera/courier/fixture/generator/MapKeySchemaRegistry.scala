package org.coursera.courier.fixture.generator

import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.BooleanDataSchema
import com.linkedin.data.schema.BytesDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.DoubleDataSchema
import com.linkedin.data.schema.FloatDataSchema
import com.linkedin.data.schema.IntegerDataSchema
import com.linkedin.data.schema.LongDataSchema
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.data.schema.StringDataSchema
import com.linkedin.data.schema.TyperefDataSchema
import org.coursera.courier.companions.MapCompanion

import scala.reflect.runtime._
import scala.collection.mutable

object MapKeySchemaRegistry {

  private[this] val schemasByName: mutable.Map[String, DataSchema] =
    mutable.Map.empty

  private[this] val rootMirror = universe.runtimeMirror(getClass.getClassLoader)
  private[this] val runtimeMirror =
    universe.runtimeMirror(getClass.getClassLoader)

  def getKeySchema(keyTypeName: String, valueSchema: DataSchema): DataSchema = {
    schemasByName.getOrElse(
      keyTypeName, {
        val schema = inferKeySchema(keyTypeName, valueSchema)
        schemasByName + (keyTypeName -> schema)
        schema
      }
    )
  }

  /**
   * Gets the companion object associated with the map type wrapper via reflection.
   */
  private[this] def inferKeySchema(keyTypeName: String,
                                   valueSchema: DataSchema): DataSchema = {
    val companionKlass =
      Class.forName(inferMapClassName(keyTypeName, valueSchema) + "$")
    var classSymbol =
      rootMirror.classSymbol(companionKlass).companionSymbol.asModule
    val companion = rootMirror
      .reflectModule(classSymbol)
      .instance
      .asInstanceOf[MapCompanion[_]]
    companion.KEY_SCHEMA
  }

  /**
   * Courier generates typed map data wrappers for custom map types. This method infers the class
   * method.
   *
   * When the value is a custom type, the classpath is:
   *  <value package>.<key name>To<value name>Map
   *
   * When the value is a primitive type, the classpath is:
   *  <key package>.<key name>To<value name>Map
   */
  private[this] def inferMapClassName(keyTypeName: String, valueSchema: DataSchema): String = {
    val keyName = keyTypeName.split('.').last
    val (valuePackageName, valueName) = getNamespaceAndName(valueSchema)
    val packageName =
      valuePackageName.getOrElse(keyTypeName.dropRight(keyName.length + 1))
    s"$packageName.${keyName}To${valueName.capitalize}Map"
  }

  private[this] def getNamespaceAndName(
      dataSchema: DataSchema): (Option[String], String) = {
    dataSchema match {
      case typeref: TyperefDataSchema => Some(typeref.getNamespace) -> typeref.getName
      case record: RecordDataSchema => Some(record.getNamespace) -> record.getName
      case array: ArrayDataSchema => getNamespaceAndName(array.getItems)
      case map: MapDataSchema     => getNamespaceAndName(map.getValues)
      case _: IntegerDataSchema   => None -> Int.getClass.getName
      case _: LongDataSchema      => None -> Long.getClass.getName
      case _: FloatDataSchema     => None -> Float.getClass.getName
      case _: DoubleDataSchema    => None -> Double.getClass.getName
      case _: BooleanDataSchema   => None -> Boolean.getClass.getName
      case _: StringDataSchema    => None -> "string"
      case _: BytesDataSchema     => None -> "bytes"
    }
  }

}
