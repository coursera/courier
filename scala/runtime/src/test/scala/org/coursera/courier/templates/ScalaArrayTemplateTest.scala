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

package org.coursera.courier.templates

import com.linkedin.data.DataList
import com.linkedin.data.schema.ArrayDataSchema
import com.linkedin.data.schema.DataSchema
import com.linkedin.data.template.DataTemplate
import com.linkedin.data.template.DataTemplateUtil
import org.coursera.courier.data.IntArray
import org.coursera.courier.templates.DataTemplates.DataConversion
import org.junit.Test
import org.scalatestplus.junit.AssertionsForJUnit

/**
 * Tests for [[ScalaArrayTemplate]], specifically the `clone()` default implementation added
 * in the Scala 2.13 migration, which delegates to `copy()`.
 */
class ScalaArrayTemplateTest extends AssertionsForJUnit {

  /**
   * Calling `clone()` via a `ScalaArrayTemplate`-typed reference exercises the default
   * trait implementation (`override def clone() = copy()`), which was added during the
   * 2.13 migration and previously had 0% scoverage.
   */
  @Test
  def clone_viaTrait_delegatesToCopy(): Unit = {
    val arr: ScalaArrayTemplate = IntArray(1, 2, 3)
    val cloned: DataTemplate[DataList] = arr.clone()
    assert(cloned ne null)
    // The clone is the same immutable instance (IntArray.copy() returns `this`)
    assert(cloned.data() === arr.data())
    assert(cloned.data().size() === 3)
  }

  @Test
  def clone_result_isEqualToOriginal(): Unit = {
    val arr: ScalaArrayTemplate = IntArray(10, 20)
    val cloned: DataTemplate[DataList] = arr.clone()
    assert(cloned.data() === arr.data())
  }

  @Test
  def clone_onEmptyArray_returnsEmptyArray(): Unit = {
    val arr: ScalaArrayTemplate = IntArray()
    val cloned = arr.clone()
    assert(cloned.data().size() === 0)
  }
}
