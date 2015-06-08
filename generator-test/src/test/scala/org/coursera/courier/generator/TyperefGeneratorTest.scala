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

import org.coursera.courier.templates.DataTemplates.DataConversion
import org.coursera.enums.Fruits
import org.coursera.records.test.Empty
import org.coursera.records.test.EmptyArray
import org.coursera.records.test.EmptyMap
import org.coursera.records.test.WithComplexTyperefs
import org.coursera.typerefs.UnionTyperef
import org.junit.Test

class TyperefGeneratorTest extends GeneratorTest with SchemaFixtures {

  @Test
  def testTyperefs(): Unit = {
    // TODO: We're mainly interested in making sure the referenced type exist, but we should test
    // them further
    UnionTyperef.IntMember(1).data()
    EmptyArray(Empty()).data()
    EmptyMap("a" -> Empty()).data()
    Fruits.BANANA.toString
  }

  @Test
  def testWithComplexTyperefs(): Unit = {
    val original = WithComplexTyperefs(
      Fruits.APPLE,
      Empty(),
      EmptyMap("a" -> Empty()),
      EmptyArray(Empty()),
      UnionTyperef.IntMember(1))
    val roundTripped = WithComplexTyperefs(roundTrip(original.data()), DataConversion.SetReadOnly)
    assert(original === roundTripped)
  }
}
