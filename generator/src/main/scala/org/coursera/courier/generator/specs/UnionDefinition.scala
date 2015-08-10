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

package org.coursera.courier.generator.specs

import com.linkedin.data.schema.UnionDataSchema
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec
import scala.collection.JavaConverters._

/**
 * Union names are inferred from the containing type or typeref.
 *
 * Note that all unions are defined anonymously, so pegasus makes a best effort to give
 * them a reasonable name.
 *
 * E.g. A union defined as the type of a field of a record will be named after that field.
 */
case class UnionDefinition(override val spec: UnionTemplateSpec) extends Definition(spec) {
  def unionSchema: UnionDataSchema = spec.getSchema
  def schema: Option[UnionDataSchema] = Option(unionSchema)

  def scalaDoc: Option[String] = None

  /**
   * The union member types.
   */
  def members: Seq[UnionMemberDefinition] = spec.getMembers.asScala.map(UnionMemberDefinition)

  def directReferencedTypes: Set[Definition] = members.map(_.classDefinition).toSet
}
