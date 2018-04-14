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

package org.coursera.courier

import org.coursera.courier.generator.CourierPredef
import org.coursera.courier.generator.GeneratorMixin
import org.coursera.courier.generator.ScalaCompilationUnit
import org.coursera.courier.generator.ScalaGeneratedCode
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
import org.coursera.courier.lang.DocCommentStyle
import org.coursera.courier.lang.PoorMansCStyleSourceFormatter
import org.coursera.courier.templates.txt.ArrayClass
import org.coursera.courier.templates.txt.EnumClass
import org.coursera.courier.templates.txt.FixedClass
import org.coursera.courier.templates.txt.MapClass
import org.coursera.courier.templates.txt.RecordClass
import org.coursera.courier.templates.txt.TyperefClass
import org.coursera.courier.templates.txt.UnionClass

import scalariform.formatter.ScalaFormatter
import scalariform.formatter.preferences.CompactControlReadability
import scalariform.formatter.preferences.DoubleIndentClassDeclaration
import scalariform.formatter.preferences.FormattingPreferences
import scalariform.parser.ScalaParserException

/**
 * Generates Scala files using the Twirl string template engine.
 */
class ConfigurableScalaGenerator(mixin: GeneratorMixin)
  extends TemplateGenerator {

  // TODO(jbetz): Make configurable
  val generateTyperefs = false
  val formatter = new PoorMansCStyleSourceFormatter(2, DocCommentStyle.ASTRISK_MARGIN)

  override def generate(topLevelSpec: Definition): Option[ScalaGeneratedCode] = {
    val maybeCode = topLevelSpec match {
      case predef: Definition if CourierPredef.definitions.contains(predef) =>
        None // Predefined types should already exist, so we don't generate them
      case record: RecordDefinition =>
        Some(RecordClass(record, mixin).body)
      case union: UnionDefinition =>
        Some(UnionClass(union, mixin).body)
      case enum: EnumDefinition =>
        Some(EnumClass(enum, mixin).body)
      case array: ArrayDefinition =>
        Some(ArrayClass(array, mixin).body)
      case map: MapDefinition =>
        Some(MapClass(map, mixin).body)
      case typeref: TyperefDefinition =>
        if (generateTyperefs) {
          Some(TyperefClass(typeref, mixin).body)
        } else {
          None
        }
      case fixed: FixedDefinition =>
        Some(FixedClass(fixed).body)
      case primitive: PrimitiveDefinition =>
        None // nothing to generate for primitives
      case _ =>
        None
        //throw new IllegalArgumentException(s"Unsupported schema type: ${topLevelSpec.getClass} " +
        //  s"for ${topLevelSpec.scalaTypeFullname}")
    }
    maybeCode.map { code =>
      val formattedCode = try {
        formatter.format(code)
      } catch {
        case _: ScalaParserException => code
      }
      val namespace = topLevelSpec.namespace.getOrElse("")
      ScalaGeneratedCode(formattedCode, ScalaCompilationUnit(topLevelSpec.scalaType, namespace))
    }
  }

  private[this] val formatterPrefs = {
    new FormattingPreferences(
      Map(
        DoubleIndentClassDeclaration -> true,
        CompactControlReadability -> true))
  }

  /**
   * Generate predefined types.
   *
   * We only generate schemas for pre defined types when re-generating types in courier-runtime.
   */
  override def generatePredefinedTypes(): Seq[ScalaGeneratedCode] = {
    CourierPredef.bySchema.flatMap { case (schema, definition) =>
      val code = definition match {
        case array: ArrayDefinition =>
          ArrayClass(array, mixin).body
        case map: MapDefinition =>
          MapClass(map, mixin).body
        case _: Any =>
          throw new IllegalArgumentException(s"Unsupported schema type: ${schema.getClass}")
      }
      val namespace = definition.namespace.getOrElse("")
      val formattedCode = ScalaFormatter.format(code)
      Some(ScalaGeneratedCode(formattedCode, ScalaCompilationUnit(definition.scalaType, namespace)))
    }.toSeq
  }

  override def buildLanguage() = "scala"

  override def customTypeLanguage() = "scala"
}
