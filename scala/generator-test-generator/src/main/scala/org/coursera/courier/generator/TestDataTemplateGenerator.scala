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

import scala.collection.immutable

/**
 * Adds a type-dependent method to each class and companion object:
 *
 * class Foo {
 *   ...
 *   def classMixinDef: Option[Foo] = None
 * }
 *
 * object Foo {
 *    ...
 *    def companionMixinDef: Option[Foo] = None
 * }
 * 
 * Templates with these methods will compile, and mixin method presence
 * can be verified in generator unit tests.
 */
object TestGeneratorMixin extends GeneratorMixin {
  override def extraClassExpressions(
      definition: Definition): immutable.Seq[String] =
    List(s"def classMixinDef: Option[${definition.dataType}] = None")
  override def extraCompanionExpressions(
      definition: Definition): immutable.Seq[String] =
    List(s"def companionMixinDef: Option[${definition.dataType}] = None")
}

object TestScalaDataTemplateGenerator extends AbstractScalaDataTemplateGenerator(TestGeneratorMixin)