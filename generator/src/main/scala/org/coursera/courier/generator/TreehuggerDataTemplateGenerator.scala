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

