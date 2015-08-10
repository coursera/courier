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

import com.linkedin.data.schema.DataSchema
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec
import org.coursera.courier.api.GeneratedCode
import org.coursera.courier.api.GeneratedCodeTargetFile
import org.coursera.courier.api.PegasusCodeGenerator
import org.coursera.courier.generator.specs.Definition

import scala.annotation.meta.beanGetter
import scala.collection.JavaConverters._

/**
 * Identifies a particular scala file.
 */
case class ScalaCompilationUnit(name: String, namespace: String)
  extends GeneratedCodeTargetFile(name, namespace, "scala")

/**
 * Code that has been generated.
 */
case class ScalaGeneratedCode(
    @beanGetter code: String,
    @beanGetter target: ScalaCompilationUnit)
  extends GeneratedCode(target, code)

/**
 * A simple pegasus code generator.
 */
trait TemplateGenerator extends PegasusCodeGenerator {

  /**
   * Generates code for the given spec.
   *
   * Because Definitions can currently contain nested type declarations that should be
   * generated into top level class files, a single call to generate can produce multiple files.
   */
  def generate(definition: Definition): Option[ScalaGeneratedCode]

  /**
   * Generate all predefined types.
   *
   * We only generate schemas for pre defined types when re-generating types in courier-runtime.
   */
  def generatePredefinedTypes(): Seq[ScalaGeneratedCode]

  // Implement the Java API
  override def generate(spec: ClassTemplateSpec): GeneratedCode = {
    generate(Definition(spec)).orNull
  }

  override def generatePredef: java.util.Collection[GeneratedCode] = {
    generatePredefinedTypes().map(_.asInstanceOf[GeneratedCode]).asJavaCollection
  }

  override def definedSchemas(): java.util.Collection[DataSchema] = {
    TypeConversions.primitiveSchemas.asJavaCollection
  }
}
