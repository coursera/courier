package org.coursera.courier.generator

import org.coursera.courier.generator.specs.Definition

import scala.collection.immutable

trait GeneratorMixin {
  def extraClassExpressions(definition: Definition): immutable.Seq[String] =
    List.empty
  def extraCompanionExpressions(definition: Definition): immutable.Seq[String] =
    List.empty
}

object NilGeneratorMixin extends GeneratorMixin {
  override def extraClassExpressions(definition: Definition): immutable.Seq[String] =
    List.empty
  override def extraCompanionExpressions(definition: Definition): immutable.Seq[String] =
    List.empty
}
