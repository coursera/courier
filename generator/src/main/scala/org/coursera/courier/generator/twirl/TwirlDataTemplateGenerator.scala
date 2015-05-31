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

import com.linkedin.data.schema.DataSchema
import com.linkedin.data.schema.DataSchemaConstants
import com.linkedin.pegasus.generator.spec.ArrayTemplateSpec
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec
import com.linkedin.pegasus.generator.spec.EnumTemplateSpec
import com.linkedin.pegasus.generator.spec.FixedTemplateSpec
import com.linkedin.pegasus.generator.spec.MapTemplateSpec
import com.linkedin.pegasus.generator.spec.PrimitiveTemplateSpec
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec
import com.linkedin.pegasus.generator.spec.TyperefTemplateSpec
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec
import com.typesafe.scalalogging.slf4j.StrictLogging
import org.coursera.courier.data.IntArray
import org.coursera.courier.generator.CompilationUnit
import org.coursera.courier.generator.GeneratedCode
import org.coursera.courier.generator.TemplateGenerator
import org.coursera.courier.generator.twirl.defs.ArrayDefinition
import org.coursera.courier.generator.twirl.defs.EnumDefinition
import org.coursera.courier.generator.twirl.defs.MapDefinition
import org.coursera.courier.generator.twirl.defs.RecordDefinition
import org.coursera.courier.generator.twirl.defs.ScalaTypes
import org.coursera.courier.generator.twirl.defs.TyperefDefinition
import org.coursera.courier.generator.twirl.defs.UnionDefinition
import org.coursera.courier.templates.txt.ArrayClassFile
import org.coursera.courier.templates.txt.EnumClassFile
import org.coursera.courier.templates.txt.MapClassFile
import org.coursera.courier.templates.txt.RecordClassFile
import org.coursera.courier.templates.txt.TyperefClassFile
import org.coursera.courier.templates.txt.UnionClassFile
import scala.collection.JavaConverters._

class TwirlDataTemplateGenerator()
  extends TemplateGenerator
  with StrictLogging {

  /**
   * Generates Scala files using the Twirl string template engine.
   */
  def generate(spec: ClassTemplateSpec): Seq[GeneratedCode] = {
    findTopLevelSpecs(List(spec), List(spec)).flatMap { topLevelSpec =>
      val schema = topLevelSpec.getSchema
      topLevelSpec match {
        case predef if ScalaTypes.predef.contains(predef.getSchema) =>

          // We only generate schemas for pre defined types when building courier-runtime.
          ScalaTypes.predef(predef.getSchema) match { // TODO: clean up, we should generate these automatically, but only for courier-runtime
            case array: ArrayDefinition =>
              //val code = ArrayClassFile(array).body
              //Seq(GeneratedCode(code, CompilationUnit(array.scalaType, array.namespace.get)))
              Seq()
            case map: MapDefinition =>
              //val code = MapClassFile(map).body
              //Seq(GeneratedCode(code, CompilationUnit(map.scalaType, map.namespace.get)))
              Seq()
            case _: Any =>
              Seq()
          }
        case record: RecordTemplateSpec =>
          val code = RecordClassFile(RecordDefinition(record)).body
          Seq(GeneratedCode(code, CompilationUnit(record.getClassName, record.getNamespace)))
        case union: UnionTemplateSpec =>
          val code = UnionClassFile(UnionDefinition(union)).body
          Seq(GeneratedCode(code, CompilationUnit(union.getClassName, union.getNamespace)))
        case enum: EnumTemplateSpec =>
          val code = EnumClassFile(EnumDefinition(enum)).body
          Seq(GeneratedCode(code, CompilationUnit(enum.getClassName, enum.getNamespace)))
        case array: ArrayTemplateSpec =>
          val code = ArrayClassFile(ArrayDefinition(array)).body
          Seq(GeneratedCode(code, CompilationUnit(array.getClassName, array.getNamespace)))
        case map: MapTemplateSpec =>
          val code = MapClassFile(MapDefinition(map)).body
          Seq(GeneratedCode(code, CompilationUnit(map.getClassName, map.getNamespace)))
        case typeref: TyperefTemplateSpec =>
          val code = TyperefClassFile(TyperefDefinition(typeref)).body
          Seq(GeneratedCode(code, CompilationUnit(typeref.getClassName, typeref.getNamespace)))
        case fixed: FixedTemplateSpec => ??? // TODO(jbetz): Add generator support
        case primitive: PrimitiveTemplateSpec => Seq() // nothing to generate for primitives
        case _ =>
          throw new IllegalArgumentException(s"Unsupported schema type: ${topLevelSpec.getClass}")
      }
    }.toSeq
  }

  /**
   * Currently, one ClassTemplateSpec is provided per .pdsc file. But some of those .pdsc contain
   * inline schema definitions that should be generated into top level classes.
   *
   * This method traverses the spec hierarchy, finding all specs that should be generated as top
   * level classes.
   *
   * I've asked the rest.li team to consider restructuring the generator utilities so that one
   * ClassTemplateSpec per top level class is provided. If they restructure the utilities, this
   * method should no longer be needed.
   */
  // TODO(jbetz): Make sure we don't attempt to generate the same schema multiple times. Because schemas can have circular dependencies, we need to keep a set somewhere? (write a test for this case)
  private def findTopLevelSpecs(specsToSearch: List[ClassTemplateSpec], acc: List[ClassTemplateSpec]): List[ClassTemplateSpec] = {
    val nestedTypes = specsToSearch.flatMap {
      case recordSpec: RecordTemplateSpec =>
        recordSpec.getFields.asScala.map(_.getType)
      case unionSpec: UnionTemplateSpec =>
        unionSpec.getMembers.asScala.map(_.getClassTemplateSpec)
      case arraySpec: ArrayTemplateSpec =>
        List(arraySpec.getItemClass)
      case mapSpec: MapTemplateSpec =>
        List(mapSpec.getValueClass)
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

  private def isTopLevel(spec: ClassTemplateSpec): Boolean = {
    val isContained = spec.getEnclosingClass != null
    val isComplex = Option(spec.getSchema) match {
      case Some(schema) if schema.isComplex => true
      case _: Any => false
    }

    !isContained && isComplex
  }
}
