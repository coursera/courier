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
import org.coursera.fixed.Fixed8
import org.coursera.fixed.WithFixed8
import org.junit.Test

class FixedGeneratorTest extends GeneratorTest with SchemaFixtures {

  @Test
  def testFixed(): Unit = {
    val original = WithFixed8(Fixed8(bytesFixed8))
    val roundTripped = WithFixed8(roundTrip(original.data()), DataConversion.SetReadOnly)

    assert(original === roundTripped)

    Seq(original, roundTripped) foreach { withFixed =>
      assert(withFixed.fixed.bytes() === bytesFixed8)
    }
  }
}
