/*
 * Copyright 2016 Coursera Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.coursera.courier.generator;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class ReferenceGraphTest {

  @Test
  public void delta() throws Throwable {
    ReferenceGraph graph1 = new ReferenceGraph();
    graph1.putReference("A", "B");
    graph1.putReference("A", "C");
    graph1.putReference("A", "D");

    graph1.putReference("B", "C");
    graph1.putReference("B", "D");

    ReferenceGraph delta = new ReferenceGraph();
    delta.putReference("B", "C");
    delta.putReference("B", "E");

    graph1.applyDelta(delta);

    Set<String> referencedByB = graph1.getDirectlyReferences("B");
    Assert.assertTrue(referencedByB.size() == 2);
    Assert.assertTrue(referencedByB.contains("C"));
    Assert.assertTrue(referencedByB.contains("E"));

    Set<String> referencesC = graph1.getDirectlyReferencedBy("C");
    Assert.assertTrue(referencesC.size() == 2);
    Assert.assertTrue(referencesC.contains("A"));
    Assert.assertTrue(referencesC.contains("B"));

    Set<String> referencesE = graph1.getDirectlyReferencedBy("E");
    Assert.assertTrue(referencesE.size() == 1);
    Assert.assertTrue(referencesE.contains("B"));

    Set<String> referencesD = graph1.getDirectlyReferencedBy("D");
    Assert.assertTrue(referencesD.size() == 1);
    Assert.assertTrue(referencesD.contains("A"));
  }

  @Test
  public void build() throws Throwable {
    // TODO(jbetz): Add a good unit test suite for reference graph construction
    //ReferenceGraph.buildReferenceGraph();
  }
}
