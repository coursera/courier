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

import org.coursera.courier.generator.specs.Definition

/**
 * Identifies a particular scala file.
 */
case class CompilationUnit(name: String, namespace: String)

/**
 * Code that has been generated.
 */
case class GeneratedCode(code: String, compilationUnit: CompilationUnit)

/**
 * A simple pegasus code generator.
 */
trait TemplateGenerator {

  /**
   * Generates code for the given spec.
   *
   * Because Definitions can currently contain nested type declarations that should be
   * generated into top level class files, a single call to generate can produce multiple files.
   */
  def generate(spec: Definition): Option[GeneratedCode]

  /**
   * Generate all predefined types.
   *
   * We only generate schemas for pre defined types when re-generating types in courier-runtime.
   */
  def generatePredef(): Seq[GeneratedCode]

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
  def findTopLevelTypes(definition: Definition): Set[Definition]
}
