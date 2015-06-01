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
import org.coursera.courier.templates.txt.ArrayClassFile
import org.coursera.courier.templates.txt.EnumClassFile
import org.coursera.courier.templates.txt.MapClassFile
import org.coursera.courier.templates.txt.RecordClassFile
import org.coursera.courier.templates.txt.TyperefClassFile
import org.coursera.courier.templates.txt.UnionClassFile

/**
 * Generates Scala files using the Twirl string template engine.
 */
class TwirlDataTemplateGenerator()
  extends TemplateGenerator
  with StrictLogging {

  def generate(spec: Definition): Seq[GeneratedCode] = {
    findTopLevelSpecs(List(spec), List(spec)).flatMap { topLevelSpec =>
      val maybeCode = topLevelSpec match {
        case predef if CourierPredef.bySchema.contains(predef.schema) =>
          None // Predefined types should already exist, so we don't generate them
        case record: RecordDefinition =>
          Some(RecordClassFile(record).body)
        case union: UnionDefinition =>
          Some(UnionClassFile(union).body)
        case enum: EnumDefinition =>
          Some(EnumClassFile(enum).body)
        case array: ArrayDefinition =>
          Some(ArrayClassFile(array).body)
        case map: MapDefinition =>
          Some(MapClassFile(map).body)
        case typeref: TyperefDefinition =>
          Some(TyperefClassFile(typeref).body)
        case fixed: FixedDefinition =>
          ??? // TODO(jbetz): Add generator support
        case primitive: PrimitiveDefinition =>
          None // nothing to generate for primitives
        case _ =>
          throw new IllegalArgumentException(s"Unsupported schema type: ${topLevelSpec.getClass}")
      }
      maybeCode.map { code =>
        val namespace = topLevelSpec.namespace.getOrElse("")
        GeneratedCode(code, CompilationUnit(topLevelSpec.scalaType, namespace))
      }
    }.toSeq
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
          ArrayClassFile(array).body
        case map: MapDefinition =>
          MapClassFile(map).body
        case _: Any =>
          throw new IllegalArgumentException(s"Unsupported schema type: ${schema.getClass}")
      }
      val namespace = definition.namespace.getOrElse("")
      Some(GeneratedCode(code, CompilationUnit(definition.scalaType, namespace)))
    }.toSeq
  }

  /**
   * Currently, one ClassDefinition is provided per .pdsc file. But some of those .pdsc contain
   * inline schema definitions that should be generated into top level classes.
   *
   * This method traverses the spec hierarchy, finding all specs that should be generated as top
   * level classes.
   *
   * I've asked the rest.li team to consider restructuring the generator utilities so that one
   * ClassDefinition per top level class is provided. If they restructure the utilities, this
   * method should no longer be needed.
   */
  // TODO(jbetz): Make sure we don't attempt to generate the same schema multiple times. Because
  // schemas can have circular dependencies, we need to keep a set somewhere? (write a test for
  // this case)
  private def findTopLevelSpecs(
      specsToSearch: List[Definition],
      acc: List[Definition]): List[Definition] = {
    val nestedTypes = specsToSearch.flatMap {
      case record: RecordDefinition =>
        record.fields.map(_.typ)
      case union: UnionDefinition =>
        union.members.map(_.classDefinition)
      case array: ArrayDefinition =>
        List(array.itemClass)
      case map: MapDefinition =>
        List(map.valueClass)
      case other: Any =>
        List()
    }
    val foundTopLevelTypes = nestedTypes.filter(isTopLevel)
    if (foundTopLevelTypes.nonEmpty) {
      findTopLevelSpecs(nestedTypes, acc ++ foundTopLevelTypes)
    } else {
      acc
    }
  }

  private def isTopLevel(spec: Definition): Boolean = {

    val isContained = spec.enclosingDefinition.isDefined
    val isComplex = Option(spec.schema) match {
      case Some(schema) => schema.isComplex // TODO: make spec.schema optional
      case None => false
    }

    !isContained && isComplex
  }
}
