/*
 * Copyright 2015 Coursera Inc.
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

package org.coursera.courier.api;

import com.linkedin.pegasus.generator.spec.ArrayTemplateSpec;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.MapTemplateSpec;
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec;
import com.linkedin.pegasus.generator.spec.TyperefTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Utilities for ClassTemplateSpec.
 */
public class ClassTemplateSpecs {

  public static Set<ClassTemplateSpec> directReferencedTypes(ClassTemplateSpec spec) {
    Set<ClassTemplateSpec> results = new HashSet<ClassTemplateSpec>();
    if (spec instanceof RecordTemplateSpec) {
      RecordTemplateSpec recordSpec = (RecordTemplateSpec)spec;
      for (RecordTemplateSpec.Field field: recordSpec.getFields()) {
        results.add(field.getType());
      }
    } else if (spec instanceof UnionTemplateSpec) {
      UnionTemplateSpec unionSpec = (UnionTemplateSpec)spec;
      for (UnionTemplateSpec.Member member: unionSpec.getMembers()) {
        results.add(member.getClassTemplateSpec());
      }
    } else if (spec instanceof CourierMapTemplateSpec) {
      CourierMapTemplateSpec mapSpec = (CourierMapTemplateSpec)spec;
      if (mapSpec.getKeyClass() != null) {
        results.add(mapSpec.getKeyClass());
      }
      results.add(mapSpec.getValueClass());
    } else if (spec instanceof ArrayTemplateSpec) {
      ArrayTemplateSpec arraySpec = (ArrayTemplateSpec)spec;
      results.add(arraySpec.getItemClass());
    }
    return results;
  }

  public static Set<ClassTemplateSpec> directContainedTypes(ClassTemplateSpec spec) {
    Set<ClassTemplateSpec> results = new HashSet<ClassTemplateSpec>();
    for (ClassTemplateSpec nested: directReferencedTypes(spec)) {
      if (nested.getEnclosingClass() == spec) {
        results.add(nested);
      }
    }
    return results;
  }

  /**
   * Return all types directly or transitively referenced by this type.
   */
  public static Set<ClassTemplateSpec> allReferencedTypes(ClassTemplateSpec spec) {
    return findAllReferencedTypes(
        directReferencedTypes(spec),
        Collections.<ClassTemplateSpec>emptySet(),
        Collections.<ClassTemplateSpec>emptySet());
  }

  // traverses the directReferencedTypes graph, keeping track of already visited ClassTemplateSpecs
  private static Set<ClassTemplateSpec> findAllReferencedTypes(
      Set<ClassTemplateSpec> current,
      Set<ClassTemplateSpec> visited,
      Set<ClassTemplateSpec> acc) {

    //val nextUnvisited = current.flatMap(_.directReferencedTypes).filterNot(visited.contains);
    Set<ClassTemplateSpec> nextUnvisited = new HashSet<ClassTemplateSpec>();
    for (ClassTemplateSpec currentSpec: current) {
      for (ClassTemplateSpec maybeNext: directReferencedTypes(currentSpec)) {
        if (!visited.contains(maybeNext)) {
          nextUnvisited.add(maybeNext);
        }
      }
    }

    Set<ClassTemplateSpec> accAndCurrent = new HashSet<ClassTemplateSpec>(acc);
    accAndCurrent.addAll(current);

    if (nextUnvisited.size() > 0) {
      Set<ClassTemplateSpec> currentAndVisited = new HashSet<ClassTemplateSpec>(current);
      currentAndVisited.addAll(visited);

      return findAllReferencedTypes(nextUnvisited, currentAndVisited, accAndCurrent);
    } else {
      return accAndCurrent;
    }
  }
}
