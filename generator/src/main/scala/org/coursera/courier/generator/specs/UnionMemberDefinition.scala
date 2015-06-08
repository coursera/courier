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

import com.linkedin.data.schema.DataSchema
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec

case class UnionMemberDefinition(spec: UnionTemplateSpec.Member) {

  def schema: DataSchema = spec.getSchema

  /**
   * The member type definition, may be any pegasus type
   * (record, primitive, enum, union, ...).
   */
  def classDefinition: Definition = Definition(spec.getClassTemplateSpec)

  /**
   * The pegasus data type of the member.
   *
   * For the select and obtain methods in UnionTemplate (be it direct, wrapped or customType),
   * this is the type that UnionTemplate expects for the "dataClass".
   */
  def dataClass: Definition = Definition(spec.getDataClass)

  def unionMemberKey: String = schema.getUnionMemberKey
}
