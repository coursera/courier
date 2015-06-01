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

import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.data.template.BooleanArray
import com.linkedin.data.template.BooleanMap
import com.linkedin.data.template.BytesArray
import com.linkedin.data.template.BytesMap
import com.linkedin.data.template.DoubleArray
import com.linkedin.data.template.DoubleMap
import com.linkedin.data.template.FloatArray
import com.linkedin.data.template.FloatMap
import com.linkedin.data.template.IntegerArray
import com.linkedin.data.template.IntegerMap
import com.linkedin.data.template.LongArray
import com.linkedin.data.template.LongMap
import com.linkedin.data.template.StringArray
import com.linkedin.data.template.StringMap
import com.linkedin.pegasus.generator.spec.PrimitiveTemplateSpec
import org.coursera.courier.generator.specs.ArrayDefinition
import org.coursera.courier.generator.specs.Definition
import org.coursera.courier.generator.specs.MapDefinition
import org.coursera.courier.generator.specs.PrimitiveDefinition

/**
 * Courier generates data bindings for a select set of schemas and provides those bindings in
 * the runtime artifact.
 *
 * Any schemas deemed sufficiently general purpose should be included here so that they are
 * only be generated once.
 */
object CourierPredef {
  val dataNamespace = "org.coursera.courier.data"
  val intArraySchema = new IntegerArray().schema()
  val bySchema = Map[DataSchema, Definition](
    arrayPredef("IntArray", DataSchema.Type.INT),
    arrayPredef("LongArray", DataSchema.Type.LONG),
    arrayPredef("FloatArray", DataSchema.Type.FLOAT),
    arrayPredef("DoubleArray", DataSchema.Type.DOUBLE),
    arrayPredef("BooleanArray", DataSchema.Type.BOOLEAN),
    arrayPredef("StringArray", DataSchema.Type.STRING),
    arrayPredef("BytesArray", DataSchema.Type.BYTES),

    mapPredef("IntMap", DataSchema.Type.INT),
    mapPredef("LongMap", DataSchema.Type.LONG),
    mapPredef("FloatMap", DataSchema.Type.FLOAT),
    mapPredef("DoubleMap", DataSchema.Type.DOUBLE),
    mapPredef("BooleanMap", DataSchema.Type.BOOLEAN),
    mapPredef("StringMap", DataSchema.Type.STRING),
    mapPredef("BytesMap", DataSchema.Type.BYTES)
  )

  def arrayPredef(
      className: String, dataType: DataSchema.Type): (ArrayDataSchema, ArrayDefinition) = {
    val definition = PrimitiveDefinition(PrimitiveTemplateSpec.getInstance(dataType))
    val arraySchema = getArraySchema(dataType)

    arraySchema -> ArrayDefinition.forPredef(className, dataNamespace, definition, arraySchema)
  }

  def mapPredef(
      className: String, dataType: DataSchema.Type): (MapDataSchema, MapDefinition) = {
    val definition = PrimitiveDefinition(PrimitiveTemplateSpec.getInstance(dataType))
    val mapSchema = getMapSchema(dataType)

    mapSchema -> MapDefinition.forPredef(className, dataNamespace, definition, mapSchema)
  }

  def getArraySchema(dataType: DataSchema.Type): ArrayDataSchema = {
    import com.linkedin.data.schema.DataSchema.Type._
    dataType match {
      case INT => new IntegerArray().schema()
      case LONG => new LongArray().schema()
      case FLOAT => new FloatArray().schema()
      case DOUBLE => new DoubleArray().schema()
      case BOOLEAN => new BooleanArray().schema()
      case STRING => new StringArray().schema()
      case BYTES => new BytesArray().schema()
      case _ => throw new IllegalArgumentException(s"Unsupported DataSchema.Type: $dataType")
    }
  }

  def getMapSchema(dataType: DataSchema.Type): MapDataSchema = {
    import com.linkedin.data.schema.DataSchema.Type._
    dataType match {
      case INT => new IntegerMap().schema()
      case LONG => new LongMap().schema()
      case FLOAT => new FloatMap().schema()
      case DOUBLE => new DoubleMap().schema()
      case BOOLEAN => new BooleanMap().schema()
      case STRING => new StringMap().schema()
      case BYTES => new BytesMap().schema()
      case _ => throw new IllegalArgumentException(s"Unsupported DataSchema.Type: $dataType")
    }
  }
}
