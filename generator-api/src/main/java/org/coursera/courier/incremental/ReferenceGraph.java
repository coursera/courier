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

package org.coursera.courier.incremental;

import com.linkedin.data.DataList;
import com.linkedin.data.DataMap;
import com.linkedin.data.codec.PrettyPrinterJacksonDataCodec;
import com.linkedin.data.codec.TextDataCodec;
import com.linkedin.data.schema.NamedDataSchema;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import org.coursera.courier.api.ClassTemplateSpecs;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class ReferenceGraph {
  private Set<String> nodes;
  private Map<String, Set<String>> references;
  private Map<String, Set<String>> referencedBy;

  public ReferenceGraph() {
    this.nodes = new HashSet<>();
    this.references = new HashMap<>();
    this.referencedBy = new HashMap<>();
  }

  public Set<String> getNodes() {
    return nodes;
  }

  public void putReference(String referencer, String referencee) {
    nodes.add(referencer);
    nodes.add(referencee);
    Set<String> referenceeSet = references.get(referencer);
    if (referenceeSet == null) {
      referenceeSet = new HashSet<>();
      references.put(referencer, referenceeSet);
    }
    referenceeSet.add(referencee);

    Set<String> referencedBySet = referencedBy.get(referencee);
    if (referencedBySet == null) {
      referencedBySet = new HashSet<>();
      referencedBy.put(referencee, referencedBySet);
    }
    referencedBySet.add(referencer);
  }

  public void removeReferencesFrom(String referencer) {
    for (String referencee: references.get(referencer)) {
      Set<String> referencedBySet = referencedBy.get(referencee);
      if (referencedBySet != null) {
        referencedBySet.remove(referencer);
      }
    }
    references.remove(referencer);
  }

  public Set<String> getDirectlyReferences(String referencer) {
    Set<String> set = references.get(referencer);
    if (set == null) {
      return Collections.emptySet();
    } else {
      return set;
    }
  }

  public Set<String> getDirectlyReferencedBy(String referencee) {
    Set<String> set = referencedBy.get(referencee);
    if (set == null) {
      return Collections.emptySet();
    } else {
      return set;
    }
  }

  public Set<String> getTransitivelyReferencedBy(Set<String> referencees) {
    Set<String> results = new HashSet<>(referencees);

    Deque<String> toVisit = new LinkedList<>(referencees);
    while(!toVisit.isEmpty()) {
      String current = toVisit.removeFirst();
      Set<String> refs = getDirectlyReferencedBy(current);
      for (String ref: refs) {
        if (!results.contains(ref)) {
          results.add(ref);
          toVisit.add(ref);
        }
      }
    }
    return results;
  }

  public void applyDelta(ReferenceGraph delta) {
    for (Map.Entry<String, Set<String>> edge: delta.references.entrySet()) {
      String referencer = edge.getKey();
      removeReferencesFrom(referencer);
      for (String referencee: edge.getValue()) {
        putReference(referencer, referencee);
      }
    }
  }

  private static final String SCHEMA = "schema";
  private static final String REFERENCED_BY = "referencedBy";

  public static ReferenceGraph load(File file) throws IOException {
    ReferenceGraph graph = new ReferenceGraph();
    try (FileReader reader = new FileReader(file)) {
      DataList rawReferencesList = codec.readList(reader);
      for (Object entryObj : rawReferencesList) {
        DataMap entry = (DataMap) entryObj;
        String referencee = entry.getString(SCHEMA);
        for (Object referenceeObj : entry.getDataList(REFERENCED_BY)) {
          String referencer = (String) referenceeObj;
          graph.putReference(referencer, referencee);
        }
      }
    }
    return graph;
  }

  public void save(File file) throws IOException {
    DataList rawReferencesList = new DataList();
    for (Map.Entry<String, Set<String>> referencesEntry: referencedBy.entrySet()) {
      DataMap rawReferencesEntry = new DataMap();
      rawReferencesEntry.put(SCHEMA, referencesEntry.getKey());
      DataList rawReferencedBySet = new DataList();
      for (String referencedByEntry: referencesEntry.getValue()) {
        rawReferencedBySet.add(referencedByEntry);
      }
      rawReferencesEntry.put(REFERENCED_BY, rawReferencedBySet);
      rawReferencesList.add(rawReferencesEntry);
    }
    try (FileWriter fileWriter = new FileWriter(file)) {
      codec.writeList(rawReferencesList, fileWriter);
    }
  }

  public interface SpecToGraphLabelTransformer {
    String toLabel(ClassTemplateSpec spec);
  }

  public static ReferenceGraph buildReferenceGraph(
      Set<ClassTemplateSpec> specs,
      SpecToGraphLabelTransformer transformer) {
    ReferenceGraph graph = new ReferenceGraph();

    for(ClassTemplateSpec referencerSpec: specs) {
      Set<ClassTemplateSpec> referencedSet =
        ClassTemplateSpecs.directReferencedTypes(referencerSpec);
      for (ClassTemplateSpec referenceeSpec: referencedSet) {
        if (referenceeSpec != referencerSpec &&
          referenceeSpec.getSchema() instanceof NamedDataSchema) {
          graph.putReference(
            transformer.toLabel(referencerSpec),
            transformer.toLabel(referenceeSpec));
        }
      }
    }

    return graph;
  }

  @Override
  public String toString() {
    return references.toString();
  }

  private static TextDataCodec codec = new PrettyPrinterJacksonDataCodec();
}
