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
import com.linkedin.data.schema.EnumDataSchema
import com.linkedin.data.schema.FixedDataSchema
import com.linkedin.data.schema.MapDataSchema
import com.linkedin.data.schema.RecordDataSchema
import com.linkedin.data.schema.TyperefDataSchema
import com.linkedin.data.schema.UnionDataSchema
import com.typesafe.scalalogging.slf4j.StrictLogging
import treehugger.forest._

case class CompilationUnit(name: String, namespace: String)
case class GeneratedCode(tree: Tree, compilationUnit: CompilationUnit)

trait SchemaHandler {
  def handleSchema(schema: DataSchema): Unit
}

class TreehuggerDataTemplateGenerator(val handler: SchemaHandler) extends StrictLogging {
  def generate(schema: DataSchema): GeneratedCode = {
    schema match {
      case record: RecordDataSchema => ???
      case union: UnionDataSchema => ???
      case enum: EnumDataSchema => ???
      case array: ArrayDataSchema => ???
      case map: MapDataSchema => ???
      case typeref: TyperefDataSchema => ???
      case fixed: FixedDataSchema => ???
      case _ =>
        throw new IllegalArgumentException(s"Unsupported schema type: $schema")
    }
  }
}

