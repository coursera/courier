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

import com.linkedin.pegasus.generator.spec.ClassTemplateSpec

/**
 * Identifies a particular scala file.
 */
case class CompilationUnit(name: String, namespace: String)

/**
 * Code that has been generated.
 */
// TODO(jbetz): can we stream straight to disk and avoid holding the generated code in a string?
case class GeneratedCode(code: String, compilationUnit: CompilationUnit)

/**
 * A simple pegasus code generator.
 */
trait TemplateGenerator {

  /**
   * Generates code for the given spec.
   *
   * Because ClassTemplateSpecs can currently contain nested type declarations that should be
   * generated into top level class files, a single call to generate can produce multiple files.
   */
  def generate(spec: ClassTemplateSpec): Seq[GeneratedCode]
}
