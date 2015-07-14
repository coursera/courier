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

package org.coursera.courier.generator.twirl

import com.typesafe.scalalogging.slf4j.StrictLogging
import org.coursera.courier.generator.CompilationUnit
import org.coursera.courier.generator.CourierPredef
import org.coursera.courier.generator.GeneratedCode
import org.coursera.courier.generator.TemplateGenerator
import org.coursera.courier.generator.specs.ArrayDefinition
import org.coursera.courier.generator.specs.Definition
import org.coursera.courier.generator.specs.EnumDefinition
import org.coursera.courier.generator.specs.FixedDefinition
import org.coursera.courier.generator.specs.MapDefinition
import org.coursera.courier.generator.specs.PrimitiveDefinition
import org.coursera.courier.generator.specs.RecordDefinition
import org.coursera.courier.generator.specs.TyperefDefinition
import org.coursera.courier.generator.specs.UnionDefinition
import org.coursera.courier.templates.txt.ArrayClass
import org.coursera.courier.templates.txt.EnumClass
import org.coursera.courier.templates.txt.FixedClass
import org.coursera.courier.templates.txt.MapClass
import org.coursera.courier.templates.txt.RecordClass
import org.coursera.courier.templates.txt.TyperefClass
import org.coursera.courier.templates.txt.UnionClass

/**
 * Generates Scala files using the Twirl string template engine.
 */
class TwirlDataTemplateGenerator(generateTyperefs: Boolean)
  extends TemplateGenerator
  with StrictLogging {

  def generate(topLevelSpec: Definition): Option[GeneratedCode] = {
    val maybeCode = topLevelSpec match {
      case predef: Definition if CourierPredef.definitions.contains(predef) =>
        None // Predefined types should already exist, so we don't generate them
      case record: RecordDefinition =>
        Some(RecordClass(record).body)
      case union: UnionDefinition =>
        Some(UnionClass(union).body)
      case enum: EnumDefinition =>
        Some(EnumClass(enum).body)
      case array: ArrayDefinition =>
        Some(ArrayClass(array).body)
      case map: MapDefinition =>
        Some(MapClass(map).body)
      case typeref: TyperefDefinition =>
        if (generateTyperefs) {
          Some(TyperefClass(typeref).body)
        } else {
          None
        }
      case fixed: FixedDefinition =>
        Some(FixedClass(fixed).body)
      case primitive: PrimitiveDefinition =>
        None // nothing to generate for primitives
      case _ =>
        throw new IllegalArgumentException(s"Unsupported schema type: ${topLevelSpec.getClass}")
    }
    maybeCode.map { code =>
      val namespace = topLevelSpec.namespace.getOrElse("")
      GeneratedCode(code, CompilationUnit(topLevelSpec.scalaType, namespace))
    }
  }

  /**
   * Generate predefined types.
   *
   * We only generate schemas for pre defined types when re-generating types in courier-runtime.
   */
  def generatePredef(): Seq[GeneratedCode] = {
    CourierPredef.bySchema.flatMap { case (schema, definition) =>
      val code = definition match {
        case array: ArrayDefinition =>
          ArrayClass(array).body
        case map: MapDefinition =>
          MapClass(map).body
        case _: Any =>
          throw new IllegalArgumentException(s"Unsupported schema type: ${schema.getClass}")
      }
      val namespace = definition.namespace.getOrElse("")
      Some(GeneratedCode(code, CompilationUnit(definition.scalaType, namespace)))
    }.toSeq
  }

  override def findTopLevelTypes(definition: Definition): Set[Definition] = {
    (definition.allReferencedTypes + definition).filter(_.isTopLevel)
  }
}
